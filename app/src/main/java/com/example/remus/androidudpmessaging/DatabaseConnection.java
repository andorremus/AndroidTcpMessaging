package com.example.remus.androidudpmessaging;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Remus on 02/04/2015.
 */
public class DatabaseConnection
{
    public SQLiteDatabase dbConnection;
    public Cursor cursor;
    private SimpleDateFormat formatter;
    Calendar now;

    public DatabaseConnection()
    {

        dbConnection = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.remus.androidudpmessaging/DatabaseConnection.db", null);
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dbConnection .execSQL("CREATE TABLE IF NOT EXISTS `users` (`username` TEXT NOT NULL, `password` TEXT NOT NULL, PRIMARY KEY(username));");
        dbConnection.execSQL("CREATE TABLE IF NOT EXISTS `conversation_log` (`logId`	INTEGER PRIMARY KEY AUTOINCREMENT,`username`	TEXT);");
        dbConnection.execSQL("CREATE TABLE IF NOT EXISTS `conversation_log_lines`(`lineId` INTEGER PRIMARY KEY AUTOINCREMENT,`datetime` TEXT,`logId` INTEGER NOT NULL,`text` TEXT NOT NULL);");

    }

    public boolean checkLogin(String username,String password)
    {
        boolean truth = false;
        cursor = dbConnection.rawQuery("Select * from users where username="+username+" and password="+password+";" ,null);
        if(cursor.getCount() != 0)
        {
            truth = true;
        }
        cursor.close();
        return truth;
    }
    public boolean registerUser(String username,String password)
    {
        boolean registered = false;
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("password",password);
        if(dbConnection.insert("users", null, values) > 0)
        {
            registered = true;
        }
        return registered;
    }

    public boolean checkUsername(String username)
    {
        boolean truth = false;
        cursor = dbConnection.rawQuery("Select * from users where username='"+username+"';" ,null);
        if(cursor.getCount() != 0)
        {
            truth = true;
        }
        cursor.close();
        return truth;
    }

    public Integer startConversation()
    {
        Integer logId = null;

        dbConnection.beginTransaction();
        try
        {
            dbConnection.execSQL("Insert into conversation_log values(null," + Settings.getUsername() + ");");
            cursor = dbConnection.rawQuery("Select max(logId) from conversation_log", null);
            if(cursor.moveToFirst())
            {
                int x  = cursor.getColumnIndex("max(logId)");
                logId = cursor.getInt(x);
            }
            dbConnection.setTransactionSuccessful();
        }
        finally
        {
            dbConnection.endTransaction();
            cursor.close();
        }

        return logId;
    }
    public void insertLine(String text,Integer logId)
    {
        dbConnection.beginTransaction();
        try
        {
            now = Calendar.getInstance();
            String date = formatter.format(now.getTime());
            Log.i("date =",date);
            dbConnection.execSQL("Insert into conversation_log_lines values(null,'"+date+"',"+logId+",'" + text + "');");
            dbConnection.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.i("Insert Line EXC",e.getMessage());

        }
        finally
        {
            Log.i("Database Connection","Insert Line ");
            dbConnection.endTransaction();
            cursor.close();
        }
    }




}
