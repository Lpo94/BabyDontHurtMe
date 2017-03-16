package com.example.lars_peter.babydonthurtme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    public void Easy(View view)
    {
        Intent i = new Intent(this, GridViewClass.class);
        i.putExtra("EnemyDif", "0");
        startActivity(i);
    }

    public void Hard(View view)
    {
        Intent i = new Intent(this, GridViewClass.class);
        i.putExtra("EnemyDif", "1");
        startActivity(i);
    }
}
