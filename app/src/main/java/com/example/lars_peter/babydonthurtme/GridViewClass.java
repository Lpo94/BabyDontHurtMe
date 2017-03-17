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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class GridViewClass extends AppCompatActivity {

    MediaPlayer mySound;

    // 0 = GameSetup, 1 = Game In progress, 2 = Game Done
    int gameStatus;
    // 0 = 5, 1 = 4, 2 = 3, 3 = 3, 4 = 2, 5 = 2, 6 Done
    int shipCount;
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
    TextView playerTxt;
    MediaPlayer shots;

    TextView enemyTxt;

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
        setContentView(R.layout.activity_main);

        final Button doneBtn = (Button)findViewById(R.id.doneBtn);
        doneBtn.setVisibility(View.GONE);
        //Reseting Values
        shipCount = 0;
        gameStatus = 0;
        enemyLifeCount = 19;
        firstShipPlaced = false;
        playerTurn = true;

        playerTxt =(TextView)findViewById(R.id.PlayerTextView);
        enemyTxt =(TextView)findViewById(R.id.enemyTextView);

        // Set GetSharedpreference so we can get values
        pref = this.getSharedPreferences("Grids",MODE_PRIVATE);

        // Getting the difficulty of the Enemy & Creating it
        Intent intent = getIntent();
        String _string = intent.getStringExtra("EnemyDif");
        difficutly = Integer.valueOf(_string);
        enemy = new Enemy(difficutly, this, lifeCount);

        //Setting Up both Grids
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        enemyGrid = (GridView)findViewById(R.id.enemygrid);
        enemyGrid.setAdapter(new ImageAdapter(this));

        //Method being called Whenever u click an item in given Grid
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Game Setup
                switch(gameStatus)
                {
                    case 0:
                        // Allows u to place ships in the Setup
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

        //Same as above Just for enemyGrid Instead
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
                        if(enemy.playerLife == 0 || enemyLifeCount == 0)
                        {
                            gameStatus++;
                            GameReview(enemyGrid);
                            doneBtn.setVisibility(View.VISIBLE);
                        }

                        enemyTxt.setText("Enemy:" + String.valueOf(enemyLifeCount));
                        playerTxt.setText("Player: " + String.valueOf(enemy.playerLife));
                        break;

                }
            }
        });
    }


    // 0 = Horizontal          1 = Vertical
    public boolean PlaceShip(int _direction, int _length, int _pos, GridView _gridView)
    {
        // Reseting values per ship
        int tempLeng = _length;
        int fix;
        boolean flick = false;
        _direction = direction;
        ImageView temp;

        //Checks if Ship can be placed
        if(CheckShipSpace(_direction, _length,_pos,_gridView)) {
            //Makes sure the first ship doesn't get removed or cause a null reference
            if(!firstShipPlaced)
            {
                firstShipPlaced = true;
            }
            else
            //Removes the last placed ship, unless the ship have been confirmed
            {if(shipCount != 6) {
                Removeship(_gridView);
            }
            }
            //Places Ship Horizontal
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
                    // Keeps track of the last ship placed
                    lastShip[i] = fix;

                    //If all player ships have been placed the enemy method is called, and since
                    // we don't want players to see where the enemy have placed their ships
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

    //Removes Last ship place
    public void Removeship(GridView _gridView)
    {
        for(int i = 0; i < lastShip.length; i++)
        {
            ImageView temp = (ImageView)_gridView.getChildAt(lastShip[i]);
            temp.setImageResource(R.drawable.a1);
            temp.setTag(R.drawable.a1);
        }
    }

    //Places all enemy ships
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

    // 0 = Horizontal          1 = Vertical
    // Makes sure a ship can be placed from given position
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

    //Handles SHooting
    public boolean TakeShot(int _pos, GridView _gridView)
    {
      ImageView temp = (ImageView)_gridView.getChildAt(_pos);
        //Miss
        if((int)temp.getTag() == R.drawable.a1)
        {
            shots = MediaPlayer.create(this, R.raw.miss);
            shots.start();
            temp.setTag(R.drawable.c1);
            temp.setImageResource(R.drawable.c1);
            return false;
        }
        //Hit
        else if((int)temp.getTag() == R.drawable.d1)
        {
            shots = MediaPlayer.create(this, R.raw.hit);
            shots.start();
            temp.setTag(R.drawable.b1);
            temp.setImageResource(R.drawable.b1);
            enemyLifeCount--;
            return false;
        }

        return true;
    }

    // So The Player can place Vertical and Horizontal
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

    //Makes a simpe text Toast
    private void passToast(Context context, String message)
    {
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    //Locks in the ship
    public void ConfirmPlacement(View view)
    {
        //Prepare for next ship getting placed
        if(firstShipPlaced) {
            firstShipPlaced = false;
            lastShip = new int[5];
            shipCount++;
            passToast(this, "Ship Placement Confirmed");
        }
        //Makes the game procced after last ship Placed
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

    public void GameEnd(View view)
    {
        Intent intent = new Intent(this,Menu.class);
        startActivity(intent);
        finish();
    }

    public void Music(View view)
    {
        Button musicButton = (Button)findViewById(R.id.muteButton);
        if(!Menu.musicPaused) {
            musicButton.setText("Unmute");
            Menu.music.setVolume(0, 0);
            Menu.musicPaused = true;
        }

        else
        {
            musicButton.setText("Mute");
            Menu.music.setVolume(1,1);
            Menu.musicPaused = false;
        }
    }

    public void GameReview(GridView _gridView)
    {
        if(enemy.playerLife == 0)
        {
            passToast(this, "YOU LOST!");
        }
        else if(enemyLifeCount == 0)
        {
            passToast(this, "YOU'VE WON!");
        }

        for(int i = 0; i < 100; i++)
        {
            ImageView view = (ImageView)_gridView.getChildAt(i);
            if((int)view.getTag() == R.drawable.d1)
            {
                view.setImageResource(R.drawable.d1);
            }
        }
    }







}

