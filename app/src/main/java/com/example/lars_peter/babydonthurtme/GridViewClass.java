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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


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
    int lifeCount;
    int direction = 0;
    int oldPos;
    int oldDirection;
    int oldLenght;
    boolean firstShipPlaced = false;

    GridView gridview;
    Enemy enemy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String  _string = intent.getStringExtra("EnemyDif");

        enemy = new Enemy(difficutly, this);



        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setVisibility(View.VISIBLE);



        final GridView enemyGrid = (GridView)findViewById(R.id.enemygrid);
        enemyGrid.setAdapter(new ImageAdapter(this));
        enemyGrid.setVisibility(View.GONE);


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
                                gameStatus++;
                                break;
                        }

                    break;

                    case 1:
                        if(playerTurn) {
                            playerTurn = TakeShot(position, gridview);
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
    public void PlaceShip(int _direction, int _length, int _pos, GridView _gridView)
    {
        int tempLeng = _length;
        boolean flick = false;
        _direction = direction;
        ImageView temp;
        if(CheckShipSpace(_direction, _length,_pos,_gridView)) {
            if (_direction == 0) {
                //Make sure it's in the GRID
                for (int i = 0; i < _length; i++) {
                    // Check if it's in the first Line
                    // 1
                    // 10
                    // 20
                    if (_pos == 10 || _pos == 20 || _pos == 30 || _pos == 40 || _pos == 50 ||
                            _pos == 60 || _pos == 70 || _pos == 80 || _pos == 90) {
                        temp = (ImageView) _gridView.getChildAt(_pos + i);
                    } else {
                        //Since it's a 1 Dimensional Array it makes sure that it doesn't start on a new line
                        if (_pos + i == 10 || _pos + i == 20 || _pos + i == 30 || _pos + i == 40
                                || _pos + i == 50 || _pos + i == 60 || _pos + i == 70 || _pos + i == 80 ||
                                _pos + i == 90 || _pos + i == 100 || flick) {
                            temp = (ImageView) _gridView.getChildAt(_pos - tempLeng);
                            flick = true;
                            tempLeng--;
                        }
                        // Nothing is wrong, so it just draws the ship
                        else {
                            tempLeng--;
                            temp = (ImageView) _gridView.getChildAt(_pos + i);
                        }
                    }
                    temp.setImageResource(R.drawable.d1);
                    temp.setTag(R.drawable.d1);
                }
            }
            // Same as Above just for Vertical
            if (_direction == 1) {
                for (int i = 0; i < _length; i++) {
                    if (_pos + (10 * i) > 100) {
                        temp = (ImageView) _gridView.getChildAt(_pos - (10 * tempLeng));
                        tempLeng--;
                    } else {
                        tempLeng--;
                        temp = (ImageView) _gridView.getChildAt(_pos + (10 * i));
                    }
                    temp.setImageResource(R.drawable.d1);
                    temp.setTag(R.drawable.d1);
                }
            }
            lifeCount+= _length;
        }
        if(!firstShipPlaced)
        {
            firstShipPlaced = true;
            shipCount++;
        }
        else
        {
            Removeship(oldDirection, oldLenght, oldPos, _gridView);
        }
        oldPos = _pos;
        oldLenght = _length;
        oldDirection = _direction;
    }

    public void Removeship(int _direction, int _length, int _pos, GridView _gridView)
    {
        int tempLeng = _length;
        boolean flick = false;
        ImageView temp;
        if(!CheckShipSpace(_direction, _length,_pos,_gridView)) {
            if (_direction == 0) {
                //Make sure it's in the GRID
                for (int i = 0; i < _length; i++) {
                    // Check if it's in the first Line
                    // 1
                    // 10
                    // 20
                    if (_pos == 10 || _pos == 20 || _pos == 30 || _pos == 40 || _pos == 50 ||
                            _pos == 60 || _pos == 70 || _pos == 80 || _pos == 90) {
                        temp = (ImageView) _gridView.getChildAt(_pos + i);
                    } else {
                        //Since it's a 1 Dimensional Array it makes sure that it doesn't start on a new line
                        if (_pos + i == 10 || _pos + i == 20 || _pos + i == 30 || _pos + i == 40
                                || _pos + i == 50 || _pos + i == 60 || _pos + i == 70 || _pos + i == 80 ||
                                _pos + i == 90 || _pos + i == 100 || flick) {
                            temp = (ImageView) _gridView.getChildAt(_pos - tempLeng);
                            flick = true;
                            tempLeng--;
                        }
                        // Nothing is wrong, so it just draws the ship
                        else {
                            tempLeng--;
                            temp = (ImageView) _gridView.getChildAt(_pos + i);
                        }
                    }
                    temp.setImageResource(R.drawable.a1);
                    temp.setTag(R.drawable.a1);
                }
            }
            // Same as Above just for Vertical
            if (_direction == 1) {
                for (int i = 0; i < _length; i++) {
                    if (_pos + (10 * i) > 100) {
                        temp = (ImageView) _gridView.getChildAt(_pos - (10 * tempLeng));
                        tempLeng--;
                    } else {
                        tempLeng--;
                        temp = (ImageView) _gridView.getChildAt(_pos + (10 * i));
                    }
                    temp.setImageResource(R.drawable.a1);
                    temp.setTag(R.drawable.a1);
                }
            }
            shipCount++;
            lifeCount+= _length;
        }
    }

    //Copy of PlaceShip Just Doesn't change the image
    public void EnemyShip(int _direction, int _length, int _pos, GridView _gridView)
    {
        int tempLeng = _length;
        boolean flick = false;
        ImageView temp;
        if(CheckShipSpace(_direction, _length,_pos,_gridView)) {
            if (_direction == 0) {
                //Make sure it's in the GRID
                for (int i = 0; i < _length; i++) {
                    // Check if it's in the first Line
                    // 1
                    // 10
                    // 20
                    if (_pos == 10 || _pos == 20 || _pos == 30 || _pos == 40 || _pos == 50 ||
                            _pos == 60 || _pos == 70 || _pos == 80 || _pos == 90) {
                        temp = (ImageView) _gridView.getChildAt(_pos + i);
                    } else {
                        //Since it's a 1 Dimensional Array it makes sure that it doesn't start on a new line
                        if (_pos + i == 10 || _pos + i == 20 || _pos + i == 30 || _pos + i == 40
                                || _pos + i == 50 || _pos + i == 60 || _pos + i == 70 || _pos + i == 80 ||
                                _pos + i == 90 || _pos + i == 100 || flick) {
                            temp = (ImageView) _gridView.getChildAt(_pos - tempLeng);
                            flick = true;
                            tempLeng--;
                        }
                        // Nothing is wrong, so it just draws the ship
                        else {
                            tempLeng--;
                            temp = (ImageView) _gridView.getChildAt(_pos + i);
                        }
                    }
                    temp.setTag(R.drawable.d1);
                }
            }
            // Same as Above just for Vertical
            if (_direction == 1) {
                for (int i = 0; i < _length; i++) {
                    if (_pos + (10 * i) > 100) {
                        temp = (ImageView) _gridView.getChildAt(_pos - (10 * tempLeng));
                        tempLeng--;
                    } else {
                        tempLeng--;
                        temp = (ImageView) _gridView.getChildAt(_pos + (10 * i));
                    }
                    temp.setTag(R.drawable.d1);
                }
            }
            shipCount++;
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

    public void placeVertical(View view)
    {
        direction = 1;
    }

    public void placeHorizontal(View view)
    {
        direction = 0;
    }

    private void passToast(Context context, String message)
    {
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }



}

