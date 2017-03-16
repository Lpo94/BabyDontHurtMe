package com.example.lars_peter.babydonthurtme;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Random;


public class GridViewClass extends AppCompatActivity {

    MediaPlayer mySound;

    // 0 = GameSetup, 1 = Game In progress, 2 = Game Done
    int gameStatus = 0;
    // 0 = 5, 1 = 4, 2 = 3, 3 = 3, 4 = 2, 5 = 2, 6 Done
    int shipCount = 0;
    boolean playerTurn = true;
    String message = "default message";
    Toast toast;
    int difficutly;
    int lifeCount = 19;
    int direction = 0;
    int enemyLifeCount = 19;
    int enemyShipCount;
    int[] lastShip = new int[5];
    boolean firstShipPlaced = false;

    GridView gridview;
    GridView enemyGrid;
    Enemy enemy;

    private SharedPreferences pref;



    @Override
    protected void onStop() {
        super.onStop();
        pref = this.getSharedPreferences("Grids",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        for(int i = 0; i < 100; i++) {
            int tag = (int)enemyGrid.getChildAt(i).getTag();

            switch (tag)
            {
                case R.drawable.a1:
                    editor.putInt("Enemy"+i, 0);
                    break;
                case R.drawable.b1:
                    editor.putInt("Enemy"+i, 1);
                    break;
                case R.drawable.c1:
                    editor.putInt("Enemy"+i, 2);
                    break;
                case R.drawable.d1:
                    editor.putInt("Enemy"+i, 3);
                    break;
            }

            tag = (int)gridview.getChildAt(i).getTag();

            switch (tag)
            {
                case R.drawable.a1:
                    editor.putInt("Player"+i, 0);
                    break;
                case R.drawable.b1:
                    editor.putInt("Player"+i, 1);
                    break;
                case R.drawable.c1:
                    editor.putInt("Player"+i, 2);
                    break;
                case R.drawable.d1:
                    editor.putInt("Player"+i, 3);
                    break;
            }


        }

        passToast(this, String.valueOf(pref.getInt("Player1",-1)));
        editor.commit();
    }

    private void readSharedPref()
    {
        pref = this.getSharedPreferences("Grids",MODE_PRIVATE);
        int check = pref.getInt("Enemy0",-1);
        if(check != 0)
        {
            for(int i = 0; i < 100; i++)
            {
                gridview.getChildAt(i).setTag(pref.getInt("Player"+i,R.drawable.a1));
                ImageView img = (ImageView)gridview.getChildAt(i);
                img.setImageResource(pref.getInt("Player"+i,R.drawable.a1));
            }
            passToast(this, "Loaded");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = this.getSharedPreferences("Grids",MODE_PRIVATE);



        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String _string = intent.getStringExtra("EnemyDif");
        difficutly = Integer.valueOf(_string);

        enemy = new Enemy(difficutly, this);



        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setVisibility(View.VISIBLE);



        enemyGrid = (GridView)findViewById(R.id.enemygrid);
        enemyGrid.setAdapter(new ImageAdapter(this));
        enemyGrid.setVisibility(View.VISIBLE);






        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Game Setup
                switch(gameStatus)
                {
                    case 0:
                            switch (shipCount) {
                                case 0:
                                    PlaceShip(0, 5, position, gridview);
                                    break;
                                case 1:
                                    PlaceShip(0, 4, position, gridview);
                                    break;
                                case 2:
                                    PlaceShip(0, 3, position, gridview);
                                    break;
                                case 3:
                                    PlaceShip(0, 3, position, gridview);
                                    break;
                                case 4:
                                    PlaceShip(0, 2, position, gridview);
                                    break;
                                case 5:
                                    PlaceShip(0, 2, position, gridview);
                                    break;
                            }

                    break;

                    case 1:
                        break;
                }

            }
        });

        enemyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                switch(gameStatus)
                {
                    case 0:

                        break;
                    case 1:
                        if(playerTurn) {
                            playerTurn = TakeShot(position, enemyGrid);
                        }
                        while(!playerTurn){
                            playerTurn = enemy.EnemyTurn(gridview);
                        }

                        break;

                }
            }
        });
    }


    // 0 = Vertical          1 = Horizontal
    public boolean PlaceShip(int _direction, int _length, int _pos, GridView _gridView)
    {
        int tempLeng = _length;
        int fix;
        boolean flick = false;
        _direction = direction;
        ImageView temp;
        if(CheckShipSpace(_direction, _length,_pos,_gridView)) {
            if(!firstShipPlaced)
            {
                firstShipPlaced = true;
            }
            else
            {if(shipCount != 6) {
                Removeship(_gridView);
            }
            }
            if (_direction == 0) {
                //Make sure it's in the GRID
                for (int i = 0; i < _length; i++) {
                    // Check if it's in the first Line
                    // 1
                    // 10
                    // 20
                    if (_pos == 10 || _pos == 20 || _pos == 30 || _pos == 40 || _pos == 50 ||
                            _pos == 60 || _pos == 70 || _pos == 80 || _pos == 90) {
                        fix = _pos + i;
                        temp = (ImageView) _gridView.getChildAt(fix);
                    } else {
                        //Since it's a 1 Dimensional Array it makes sure that it doesn't start on a new line
                        if (_pos + i == 10 || _pos + i == 20 || _pos + i == 30 || _pos + i == 40
                                || _pos + i == 50 || _pos + i == 60 || _pos + i == 70 || _pos + i == 80 ||
                                _pos + i == 90 || _pos + i == 100 || flick) {
                            fix = _pos-tempLeng;
                            temp = (ImageView) _gridView.getChildAt(fix);
                            flick = true;
                            tempLeng--;
                        }
                        // Nothing is wrong, so it just draws the ship
                        else {
                            tempLeng--;
                            fix = _pos + i;
                            temp = (ImageView) _gridView.getChildAt(fix);
                        }
                    }
                    lastShip[i] = fix;
                    if(shipCount != 6) {
                        temp.setImageResource(R.drawable.d1);
                    }
                    temp.setTag(R.drawable.d1);
                }
            }
            // Same as Above just for Vertical
            if (_direction == 1) {
                for (int i = 0; i < _length; i++) {
                    if (_pos + (10 * i) > 100) {
                        fix = _pos - (10* tempLeng);
                        temp = (ImageView) _gridView.getChildAt(fix);
                        tempLeng--;
                    } else {
                        tempLeng--;
                        fix = _pos + (10*i);
                        temp = (ImageView) _gridView.getChildAt(fix);
                    }
                    lastShip[i] = fix;
                    if(shipCount != 6) {
                        temp.setImageResource(R.drawable.d1);
                    }
                    temp.setTag(R.drawable.d1);
                }
            }
            return true;
        }
        else
        {
            return false;
        }

    }

    public void Removeship(GridView _gridView)
    {
        for(int i = 0; i < lastShip.length; i++)
        {
            ImageView temp = (ImageView)_gridView.getChildAt(lastShip[i]);
            temp.setImageResource(R.drawable.a1);
            temp.setTag(R.drawable.a1);
        }
    }

    //Copy of PlaceShip Just Doesn't change the image
    public void EnemyShip(GridView _gridView)
    {
       while(enemyShipCount < 6)
       {
           Random r = new Random();
           int temp = r.nextInt(99);
           int tempdir = r.nextInt(1);

           switch (enemyShipCount) {
               case 0:
                   if(PlaceShip(tempdir, 5, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 1:
                   if(PlaceShip(tempdir, 4, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 2:
                   if(PlaceShip(tempdir, 3, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 3:
                   if(PlaceShip(tempdir, 3, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 4:
                   if(PlaceShip(tempdir, 2, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 5:
                   if(PlaceShip(tempdir, 2, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
           }
       }
    }

    // 0 = Vertical          1 = Horizontal
    public boolean CheckShipSpace(int _direction, int _length, int _pos, GridView _gridView)
    {
        int tempLeng = _length;
        boolean flick = false;
        ImageView temp;
        // Same as Placing a ship, except it makes sure that it doesn't collide with another ship
        if(_direction == 0) {
            for (int i = 0; i < _length; i++) {
                if (_pos == 10 || _pos == 20 || _pos == 30 || _pos == 40 || _pos == 50 ||
                        _pos == 60 || _pos == 70 || _pos == 80 || _pos == 90) {
                    temp = (ImageView) _gridView.getChildAt(_pos + i);
                } else {
                    if (_pos + i == 10 || _pos + i == 20 || _pos + i == 30 || _pos + i == 40
                            || _pos + i == 50 || _pos + i == 60 || _pos + i == 70 || _pos + i == 80 ||
                            _pos + i == 90 || _pos + i == 100 || flick) {
                        temp = (ImageView) _gridView.getChildAt(_pos - tempLeng);
                        flick = true;
                        tempLeng--;
                    } else {
                        tempLeng--;
                        temp = (ImageView) _gridView.getChildAt(_pos + i);
                    }
                }
                if((int)temp.getTag() != R.drawable.a1)
                {
                    return false;
                }
            }
        }

        if(_direction == 1)
        {
            for(int i = 0; i < _length; i++){
                if(_pos+(10*i) > 100){
                    temp = (ImageView) _gridView.getChildAt(_pos - (10*tempLeng));
                    tempLeng--;
                }
                else
                {
                    tempLeng--;
                    temp = (ImageView) _gridView.getChildAt(_pos + (10*i));
                }
                if((int)temp.getTag() != R.drawable.a1)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean TakeShot(int _pos, GridView _gridView)
    {
      ImageView temp = (ImageView)_gridView.getChildAt(_pos);
        if((int)temp.getTag() == R.drawable.a1)
        {
            temp.setTag(R.drawable.c1);
            temp.setImageResource(R.drawable.c1);
            return false;
        }
        else if((int)temp.getTag() == R.drawable.d1)
        {
            temp.setTag(R.drawable.b1);
            temp.setImageResource(R.drawable.b1);
            return false;
        }

        return true;
    }

    public void SwapDirection(View view)
    {
        Button btn = (Button)findViewById(R.id.switchButton);
        if(direction == 1) {
            direction = 0;
            btn.setText("Horizontal");
        }
        else
        {
            direction = 1;
            btn.setText("Vertical");
        }
    }

    private void passToast(Context context, String message)
    {
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void ConfirmPlacement(View view)
    {
        if(firstShipPlaced) {
            firstShipPlaced = false;
            lastShip = new int[5];
            shipCount++;
        }
        if(shipCount == 6)
        {
            EnemyShip(enemyGrid);
            gameStatus++;
            Button tempBtn = (Button)findViewById(R.id.switchButton);
            tempBtn.setVisibility(View.GONE);
            tempBtn = (Button)findViewById(R.id.placeButton);
            tempBtn.setVisibility(View.GONE);
        }
    }





}

