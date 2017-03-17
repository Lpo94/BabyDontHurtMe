package com.example.lars_peter.babydonthurtme;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    static MediaPlayer music;
    static int paused;
    Button musicButton;
    static boolean musicPaused;
    static int maxVolume = 50;


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
            super.onStart();
            if (music == null) {
                music = MediaPlayer.create(this, R.raw.music);
                music.setLooping(true);
                music.setVolume(0.1f, 0.1f);
                music.start();
            }
    }

    public void Music(View view)
    {
        musicButton = (Button) findViewById(R.id.musicButton);
        if(musicPaused == false) {
            musicButton.setText("Unpause music");
            musicPaused = true;
            music.pause();
            paused=music.getCurrentPosition();
        }
        else if(musicPaused == true)
        {
            music.start();
            musicButton.setText("Pause music");
            musicPaused = false;
        }
    }
}
