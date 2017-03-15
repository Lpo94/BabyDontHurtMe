package com.example.lars_peter.babydonthurtme; /**
 * Created by Lars-Peter on 15/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SharkGaming on 14/03/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "name";
    private static final String COLUMN_PASSWORD = "password";
    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table users(id integer primary key not null , " + "name text not null , password text not null);";

    public DatabaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public void AddUser(Users user)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from users";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_USERNAME, user.getName());
        values.put(COLUMN_PASSWORD, user.getPassword());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String SearchPassword(String name)
    {
        db = this.getReadableDatabase();
        String query ="select name, password from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;  // a = name b = password
        b = "Not found.";

        if(cursor.moveToFirst())
        {
            do
            {
                a = cursor.getString(0); // Column index 0 henter name columns value

                if(a.equals(name))
                {
                    b = cursor.getString(1); // Column index 1 henter password columns value
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return b;
    }
}