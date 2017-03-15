package com.example.lars_peter.babydonthurtme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    DatabaseManager dbManager = new DatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void FinishClicked(View view)
    {
        EditText name = (EditText)findViewById(R.id.RegisterName);
        EditText password = (EditText)findViewById(R.id.RegisterPassword);
        EditText passwordConfirm = (EditText)findViewById(R.id.RegisterConfirmPassword);

        String nameString = name.getText().toString();
        String passwordString = password.getText().toString();
        String passwordConfirmString = passwordConfirm.getText().toString();

        if(!passwordString.equals(passwordConfirmString))
        {
            Toast nono = Toast.makeText(RegisterActivity.this, "Passwords does not match!", Toast.LENGTH_SHORT);
            nono.show();
        }
        else
        {
            Users newUser = new Users();
            newUser.setName(nameString);
            newUser.setPassword(passwordString);

            dbManager.AddUser(newUser);

            Toast nono = Toast.makeText(RegisterActivity.this, "Succesfull!", Toast.LENGTH_SHORT);
            nono.show();
        }
    }

    public void BackClicked(View view)
    {
        Intent loginGo = new Intent(this, LoginActivity.class);
        startActivity(loginGo);
    }
}