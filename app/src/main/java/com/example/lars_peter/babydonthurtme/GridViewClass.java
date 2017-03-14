package com.example.lars_peter.babydonthurtme;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;


public class GridViewClass extends AppCompatActivity {

    // 0 = GameSetup, 1 = Game In progress, 2 = Game Done
    int gameStatus = 0;
    // 0 = 5, 1 = 4, 2 = 3, 3 = 3, 4 = 2, 5 = 2, 6 Done
    int shipCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //Game Setup
                if (gameStatus == 0) {
                    ImageView imageView = (ImageView) v;
                    switch (shipCount)
                    {
                        case 0:
                            PlaceShip(0,5,position,gridview);
                            break;
                        case 1:
                            PlaceShip(0,4,position,gridview);
                            break;
                        case 2:
                            PlaceShip(0,3,position,gridview);
                            break;
                        case 3:
                            PlaceShip(0,3,position,gridview);
                            break;
                        case 4:
                            PlaceShip(0,2,position,gridview);
                            break;
                        case 5:
                            PlaceShip(0,2,position,gridview);
                            break;
                        case 6:
                            gameStatus ++;
                            break;
                    }

                    imageView.setTag(R.drawable.b1);

                    if((int)imageView.getTag() == R.drawable.b1)
                    {
                        imageView.setImageResource(R.drawable.c1);
                    }
                    shipCount++;

                }
            }
        });
    }
    // 0 = Vertical          1 = Horizontal
    public void PlaceShip(int _direction, int _length, int _pos, GridView _gridView)
    {
        int tempLeng = _length;
        boolean flick = false;

        if(_direction == 0) {
            for (int i = 0; i < _length; i++) {
                if (_pos == 10 || _pos == 20 || _pos == 30 || _pos == 40 || _pos == 50 ||
                        _pos == 60 || _pos == 70 || _pos == 80 || _pos == 90) {
                    ImageView temp = (ImageView) _gridView.getChildAt(_pos + i);
                    temp.setImageResource(R.drawable.d1);
                    temp = null;
                } else {
                    if (_pos + i == 10 || _pos + i == 20 || _pos + i == 30 || _pos + i == 40
                            || _pos + i == 50 || _pos + i == 60 || _pos + i == 70 || _pos + i == 80 ||
                            _pos + i == 90 || _pos + i == 100 || flick) {
                        ImageView temp = (ImageView) _gridView.getChildAt(_pos - tempLeng);
                        temp.setImageResource(R.drawable.d1);
                        temp = null;
                        flick = true;
                        tempLeng--;
                    } else {
                        tempLeng--;
                        ImageView temp = (ImageView) _gridView.getChildAt(_pos + i);
                        temp.setImageResource(R.drawable.d1);
                        temp = null;
                    }
                }
            }
        }

        if(_direction == 1)
        {
            for(int i = 0; i < _length; i++){
                if(_pos+(10*i) > 100){
                    ImageView temp = (ImageView) _gridView.getChildAt(_pos - (10*tempLeng));
                    temp.setImageResource(R.drawable.c1);
                    temp = null;
                    tempLeng--;
                }
                else
                {
                    tempLeng--;
                    ImageView temp = (ImageView) _gridView.getChildAt(_pos + (10*i));
                    temp.setImageResource(R.drawable.c1);
                }
            }

        }
    }
}

