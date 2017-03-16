package com.example.lars_peter.babydonthurtme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

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

    public void Sound(View view)
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void Exit(View view)
    {
        finish();
    }
}
