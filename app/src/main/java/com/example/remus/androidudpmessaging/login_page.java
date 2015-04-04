package com.example.remus.androidudpmessaging;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class login_page extends ActionBarActivity
{
    DatabaseConnection myDB;
    Cursor cursor;
    private EditText usernameTextEdit;
    private EditText passwordTextEdit;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        usernameTextEdit = (EditText)findViewById(R.id.editTextUsername);
        passwordTextEdit = (EditText)findViewById(R.id.editTextPassword);

        final Button loginButton = (Button)findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Log.i("Button Action","Button Clicked");
                checkCredentials();


            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void checkCredentials() // Function to verify the username and password agains the database
    {
        boolean cancel = false;
        View focusView = null;


        String username = usernameTextEdit.getText().toString();
        Log.i("Check Credentials", "The username is" + username);

        String password = passwordTextEdit.getText().toString();
        Log.i("Check Credentials","The password is"+password);


        //DatabaseConnection db = new DatabaseConnection();
        //myDB.dbConnection.rawQuery("Select * from addressbook",null);
        //myDB.dbConnection.rawQuery("Select username,password from addressbook where username=",null);


        if (TextUtils.isEmpty(username))
        {
            usernameTextEdit.setError("You need to type an username");
            focusView = usernameTextEdit;
            cancel = true;
        }

        if (TextUtils.isEmpty(password))
        {
            passwordTextEdit.setError("You need to type a password");
            focusView = passwordTextEdit;
            cancel = true;
        }

        if(cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            Settings.setUsername(username);
            Intent i;
            i = new Intent(this, setConnection.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }



}

