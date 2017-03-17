package com.example.lars_peter.babydonthurtme;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void Start(View view)
    {
        Intent i = new Intent(this, DifficultyActivity.class);
        startActivity(i);
    }

    public void Back(View view)
    {
        finish();
    }

    @Override
    protected void onStart() {
        if(music==null) {
            super.onStart();
            if (music == null) {
                music = MediaPlayer.create(this, R.raw.music);
                music.setLooping(true);
                music.start();
            }
        }
    }
}
