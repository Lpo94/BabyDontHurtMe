package com.example.lars_peter.babydonthurtme;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class GridViewClass extends AppCompatActivity {


    // 0 = GameSetup, 1 = Game In progress, 2 = Game Done
    int gameStatus;
    // 0 = 5, 1 = 4, 2 = 3, 3 = 3, 4 = 2, 5 = 2, 6 Done
    int shipCount;
    boolean playerTurn = true;
    Toast toast;
    int difficutly;
    int lifeCount = 19;
    int direction = 0;
    Random r;
    int enemyLifeCount = 19;
    int enemyShipCount;
    int[] lastShip = new int[5];
    boolean firstShipPlaced = false;
    boolean player1Turn = true;
    boolean PvP_NextTurn;
    boolean PvP = false;
    TextView playerTxt;
    MediaPlayer shots;

    Button nextBtn;

    TextView enemyTxt;

    GridView gridview;
    GridView enemyGrid;
    Enemy enemy;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button doneBtn = (Button)findViewById(R.id.doneBtn);
        doneBtn.setVisibility(View.GONE);

        nextBtn = (Button)findViewById(R.id.NextPlayer);
        nextBtn.setVisibility(View.GONE);
        //Resetting Values
        shipCount = 0;
        gameStatus = 0;
        enemyLifeCount = 19;
        firstShipPlaced = false;
        playerTurn = true;

        playerTxt =(TextView)findViewById(R.id.PlayerTextView);
        enemyTxt =(TextView)findViewById(R.id.enemyTextView);

        playerTxt.setText(LoginActivity.playerName);


        // Getting the difficulty of the Enemy & Creating it
        Intent intent = getIntent();
        String _string = intent.getStringExtra("EnemyDif");
        difficutly = Integer.valueOf(_string);

        if(difficutly < 10) {
            enemy = new Enemy(difficutly, this, lifeCount);
        }
        else
        {
            PvP = true;
        }


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
                        if(player1Turn) {
                            // Allows u to place ships in the Setup
                            switch (shipCount) {
                                case 0:
                                    PlaceShip(direction, 5, position, gridview);
                                    break;
                                case 1:
                                    PlaceShip(direction, 4, position, gridview);
                                    break;
                                case 2:
                                    PlaceShip(direction, 3, position, gridview);
                                    break;
                                case 3:
                                    PlaceShip(direction, 3, position, gridview);
                                    break;
                                case 4:
                                    PlaceShip(direction, 2, position, gridview);
                                    break;
                                case 5:
                                    PlaceShip(direction, 2, position, gridview);
                                    break;
                            }
                        }


                    break;

                    case 1:
                        if(PvP && !player1Turn)
                        {
                            if(!TakeShot(position,gridview)) {
                                player1Turn = true;
                                nextBtn.setVisibility(View.VISIBLE);
                                SwapPlayerGrid(enemyGrid, true);
                                enemyTxt.setText("Player2:" + String.valueOf(enemyLifeCount));
                                playerTxt.setText(LoginActivity.playerName + ": " + lifeCount);

                                if (lifeCount == 0) {
                                    gameStatus++;
                                    GameReview(enemyGrid);
                                    doneBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
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
                        if(!player1Turn)
                        {
                            switch (shipCount) {
                                case 0:
                                    PlaceShip(direction, 5, position, enemyGrid);
                                    break;
                                case 1:
                                    PlaceShip(direction, 4, position, enemyGrid);
                                    break;
                                case 2:
                                    PlaceShip(direction, 3, position, enemyGrid);
                                    break;
                                case 3:
                                    PlaceShip(direction, 3, position, enemyGrid);
                                    break;
                                case 4:
                                    PlaceShip(direction, 2, position, enemyGrid);
                                    break;
                                case 5:
                                    PlaceShip(direction, 2, position, enemyGrid);
                                    break;
                            }
                        }

                        break;
                    case 1:
                        if(!PvP) {
                            if (playerTurn) {
                                playerTurn = TakeShot(position, enemyGrid);
                            }
                            while (!playerTurn) {
                                playerTurn = enemy.EnemyTurn(gridview);
                            }
                            if (enemy.playerLife == 0 || enemyLifeCount == 0) {
                                gameStatus++;
                                GameReview(enemyGrid);
                                doneBtn.setVisibility(View.VISIBLE);
                            }

                            enemyTxt.setText("Enemy AI:" + String.valueOf(enemyLifeCount));
                            playerTxt.setText(LoginActivity.playerName + ": " + String.valueOf(enemy.playerLife));
                        }

                        if(PvP && player1Turn)
                        {
                            if(!TakeShot(position,enemyGrid)) {
                                player1Turn = false;
                                nextBtn.setVisibility(View.VISIBLE);
                                SwapPlayerGrid(gridview, true);

                                enemyTxt.setText("Player2:" + String.valueOf(enemyLifeCount));
                                playerTxt.setText(LoginActivity.playerName + ": " + lifeCount);

                                if (enemyLifeCount == 0) {
                                    gameStatus++;
                                    GameReview(enemyGrid);
                                    doneBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
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
                RemoveShip(_gridView,_length);
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
    public void RemoveShip(GridView _gridView, int _length)
    {
        for(int i = 0; i < _length; i++)
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
           r = new Random();
           int temp = r.nextInt(99);
           int tempDirection = r.nextInt(10);
           // The Random wasn't working with a number between 0,1 so we mixed it up
           switch (tempDirection)
           {
               case 0:
                   tempDirection = 0;
                   break;
               case 1:
                   tempDirection = 0;
                   break;
               case 2:
                   tempDirection = 0;
                   break;
               case 3:
                   tempDirection = 0;
                   break;
               case 4:
                   tempDirection = 0;
                   break;
               case 5:
                   tempDirection = 1;
                   break;
               case 6:
                   tempDirection = 1;
                   break;
               case 7:
                   tempDirection = 1;
                   break;
               case 8:
                   tempDirection = 1;
                   break;
               case 9:
                   tempDirection = 1;
                   break;

               case 10:
                   tempDirection = 1;
                   break;
           }

           switch (enemyShipCount) {
               case 0:
                   if(PlaceShip(tempDirection, 5, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 1:
                   if(PlaceShip(tempDirection, 4, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 2:
                   if(PlaceShip(tempDirection, 3, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 3:
                   if(PlaceShip(tempDirection, 3, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 4:
                   if(PlaceShip(tempDirection, 2, temp, _gridView)) {
                       enemyShipCount++;
                   }
                   break;
               case 5:
                   if(PlaceShip(tempDirection, 2, temp, _gridView)) {
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
            shots.setVolume(0.5f, 0.5f);
            shots.start();
            temp.setTag(R.drawable.c1);
            temp.setImageResource(R.drawable.c1);
            return false;
        }
        //Hit
        else if((int)temp.getTag() == R.drawable.d1)
        {
            shots = MediaPlayer.create(this, R.raw.hit);
            shots.setVolume(0.5f, 0.5f);
            shots.start();
            temp.setTag(R.drawable.b1);
            temp.setImageResource(R.drawable.b1);
            if(!PvP) {
                enemyLifeCount--;
            }
            else if(PvP && player1Turn)
            {
                enemyLifeCount--;
            }
            else if(PvP && !player1Turn){
                lifeCount--;
            }
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

        }
        //Makes the game procced after last ship Placed
        if(shipCount == 6 && !PvP)
        {
            EnemyShip(enemyGrid);
            gameStatus++;
            Button tempBtn = (Button)findViewById(R.id.switchButton);
            tempBtn.setVisibility(View.GONE);
            tempBtn = (Button)findViewById(R.id.placeButton);
            tempBtn.setVisibility(View.GONE);
        }
        else if(PvP && shipCount == 6 && player1Turn)
        {
            shipCount = 0;
            player1Turn = false;
            passToast(this, "Player 2!");
            SwapPlayerGrid(gridview,true);
        }
        else if(PvP && shipCount == 6 && !player1Turn)
        {
            gameStatus++;
            player1Turn = true;
            SwapPlayerGrid(enemyGrid,true);
            Button tempBtn = (Button)findViewById(R.id.switchButton);
            tempBtn.setVisibility(View.GONE);
            tempBtn = (Button)findViewById(R.id.placeButton);
            tempBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
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

    public void NextTurn(View view)
    {
        if(player1Turn)
        {
            SwapPlayerGrid(gridview,false);
        }
        else if(!player1Turn)
        {
            SwapPlayerGrid(enemyGrid,false);
        }

        nextBtn.setVisibility(View.GONE);
    }

    public void GameReview(GridView _gridView)
    {
        if(!PvP) {
            if (enemy.playerLife == 0) {
                passToast(this, "YOU LOST!");
            } else if (enemyLifeCount == 0) {
                passToast(this, "YOU'VE WON!");
            }

            SwapPlayerGrid(_gridView, false);
        }
        else if(PvP)
        {
            if (lifeCount == 0) {
                passToast(this, "PLAYER 2 WON!");
            } else if (enemyLifeCount == 0) {
                passToast(this, "PLAYER 1 WON!");
            }

            SwapPlayerGrid(gridview, false);
            SwapPlayerGrid(enemyGrid, false);
        }
    }

    public void SwapPlayerGrid(GridView _gridView, Boolean _hide)
    {
        if(_hide) {

            for (int i = 0; i < 100; i++) {
                ImageView img = (ImageView) _gridView.getChildAt(i);
                if ((int) img.getTag() == R.drawable.d1) {
                    img.setImageResource(R.drawable.a1);
                }
            }
        }
        else if(!_hide)
        {
            for (int i = 0; i < 100; i++) {
                ImageView img = (ImageView) _gridView.getChildAt(i);
                if ((int) img.getTag() == R.drawable.d1) {
                    img.setImageResource(R.drawable.d1);
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!Menu.musicPaused) {
            Music(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Menu.musicPaused) {
            Music(null);
        }
    }
}

