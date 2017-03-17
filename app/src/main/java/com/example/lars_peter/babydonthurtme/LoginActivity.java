package com.example.lars_peter.babydonthurtme;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    MediaPlayer mySound;
    DatabaseManager dbManager = new DatabaseManager(this);
    static String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void RegisterClicked(View view)
    {
        Intent registerGo = new Intent(this, RegisterActivity.class);
        startActivity(registerGo);
    }

    public void LoginClicked(View view)
    {

        if(view.getId() == R.id.LoginBtn)
        {
            EditText name = (EditText)findViewById(R.id.LoginInputName);
            String nameString = name.getText().toString();
            EditText password = (EditText)findViewById(R.id.LoginInputPassword);
            String passwordString = password.getText().toString();

            String pw = dbManager.SearchPassword(nameString);

            if(passwordString.equals(pw))
            {
                Intent gameMenuGo = new Intent(this, Menu.class);
                startActivity(gameMenuGo);
                playerName = nameString;
            }

            else
            {
                Toast nono = Toast.makeText(LoginActivity.this, "Name and password does not match!", Toast.LENGTH_SHORT);
                nono.show();
            }
        }
    }


}
