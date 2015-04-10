package com.example.remus.androidtcpmessaging;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class login_page extends ActionBarActivity
{
    DatabaseConnection myDB;
    Cursor cursor;
    private EditText usernameTextEdit,passwordTextEdit,userRegTextEdit,passReg1TextEdit,passReg2TextEdit;



    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        myDB = new DatabaseConnection();

        usernameTextEdit = (EditText)findViewById(R.id.editTextUsername);
        passwordTextEdit = (EditText)findViewById(R.id.editTextPassword);
        userRegTextEdit =  (EditText)findViewById(R.id.editTextRegUser);
        passReg1TextEdit =  (EditText)findViewById(R.id.editTextRegPass1);
        passReg2TextEdit =  (EditText)findViewById(R.id.editTextRegPass2);

        final Button loginButton = (Button)findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Log.i("Login Button","Button Clicked");
                checkCredentials();


            }
        });

        final Button registerButton = (Button)findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.i("Register Button","Button clicked");
                registerUser();
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
            username = DatabaseUtils.sqlEscapeString(username);
            password = DatabaseUtils.sqlEscapeString(password);

            Log.i("Check credentials", username+" - u");
            Log.i("Check credentials",password+" - p");
            Log.i("Check credentials",myDB.checkLogin(username,password)+" value");
            if(myDB.checkLogin(username,password))
            {
                Toast toast = Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_LONG);
                toast.show();
                Settings.setUsername(username);
                Intent i;
                i = new Intent(this, setConnection.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            else
            {
                passwordTextEdit.setError("Credentials do not exist or are incorrect");
                usernameTextEdit.setError("Credentials do not exist or are incorrect");

                if (focusView != null) {
                    focusView.requestFocus();
                }
            }



        }


    }
    private void registerUser()
    {
        boolean cancel = false;
        View focusView = null;


        String username = userRegTextEdit.getText().toString();
        Log.i("Register User", "The username is" + username);

        String pass1 = passReg1TextEdit.getText().toString();
        Log.i("Register user","The password is"+pass1);

        String pass2 = passReg2TextEdit.getText().toString();
        Log.i("Register user","The password is"+pass2);


        if (TextUtils.isEmpty(username))
        {
            usernameTextEdit.setError("You need to type an username");
            focusView = userRegTextEdit;
            cancel = true;
        }

        if (TextUtils.isEmpty(pass1))
        {
            passReg1TextEdit.setError("You need to type a password");
            focusView = passReg1TextEdit;
            cancel = true;
        }
        if (TextUtils.isEmpty(pass2))
        {
            passReg2TextEdit.setError("You need to type a password");
            focusView = passReg2TextEdit;
            cancel = true;
        }


        if(cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            if (!pass1.equals(pass2))
            {
                passReg1TextEdit.setError("The passwords do not match");
                passReg2TextEdit.setError("The passwords do not match");
                passReg1TextEdit.setText("");
                passReg2TextEdit.setText("");
                if (focusView != null)
                {
                    focusView.requestFocus();
                }
            }
            else if(myDB.checkUsername(username))
            {
                userRegTextEdit.setError("Username already exists, Please choose another one.");

                passReg1TextEdit.setText("");
                passReg2TextEdit.setText("");
                focusView = userRegTextEdit;
                if (focusView != null)
                {
                    focusView.requestFocus();
                }
            }
            else
            {
                //username = DatabaseUtils.sqlEscapeString(username);
                //pass1 = DatabaseUtils.sqlEscapeString(pass2);

                Log.i("Register user", username + " - u");
                Log.i("Register user", pass1 + " - p");

                //username = DatabaseUtils.sqlEscapeString(username);
                //pass1 = DatabaseUtils.sqlEscapeString(pass1);

                if(myDB.registerUser(username,pass1))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"You have been registered succesfully",Toast.LENGTH_LONG);
                    toast.show();
                    Settings.setUsername("'"+username+"'");
                    Intent i;
                    i = new Intent(this, setConnection.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"You have not been registered. There was an error. Try again",Toast.LENGTH_LONG);
                }

            }
        }
    }
}





