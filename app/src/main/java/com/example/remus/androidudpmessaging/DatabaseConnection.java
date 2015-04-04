package com.example.remus.androidudpmessaging;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Remus on 02/04/2015.
 */
public class DatabaseConnection
{
    Context context;
    public static SQLiteDatabase dbConnection;

    public DatabaseConnection()
    {
        dbConnection = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir()+"/myDB.db", null);
    }

    public void checkLogin(String username,String password)
    {
        dbConnection.rawQuery("Select * from credentialsTable where username='"+username+"' and password='"+password+"';" ,null);
    }


}
