package com.example.remus.androidtcpmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Settings extends ActionBarActivity {

    private static String destinationIp;
    private static Integer localPort,remotePort;
    private static String username;
    private EditText editDestIp,editRemotePort;
    private TextView usernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editDestIp = (EditText) findViewById(R.id.editTextDestIp);
        editRemotePort = (EditText) findViewById(R.id.editTextRemotePort);
        usernameView = (TextView) findViewById(R.id.textViewUsername);

        usernameView.setText(getUsername());

        CharSequence cs2 = getDestinationIp();
        CharSequence cs3 = getRemotePort().toString();
        try
        {
            editDestIp.setText(cs2);
            editRemotePort.setText(cs3);
        }
        catch (Exception e)
        {
            Log.i("On Create Settings",e.getMessage());
        }

        final Button saveButton =(Button)findViewById(R.id.saveSettingsButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setSettings();
            }
        });

        final Button logoutButton = (Button)findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startFresh();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
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


    private void setSettings()
    {
        Integer remotePort = null;
        String destIp=null;

        editDestIp.setError(null);
        editRemotePort.setError(null);

        boolean cancel = false;
        View focusView = null;

        try
        {
            destIp = editDestIp.getText().toString();
        }
        catch (Exception e)
        {
            Log.i("Set Settings",e.getMessage());
        }
        try
        {
            remotePort = Integer.parseInt(editRemotePort.getText().toString());
        }
        catch (Exception e)
        {
            Log.i("Set Settings",e.getMessage());
        }

        if(TextUtils.isEmpty(destIp))
        {
            editDestIp.setError("Please write an IP address");
            cancel = true;
            focusView = editDestIp;

        }
        Log.i("Set connections", "The ip address to be set is :" + destIp);

        String remotePortString = null;
        try
        {
            remotePortString = remotePort.toString();

        }
        catch (Exception e)
        {

        }
        if(TextUtils.isEmpty(remotePortString))
        {
            editRemotePort.setError("Please write a remote port");
            cancel = true;
            focusView = editRemotePort;

        }
        Log.i("Set connections", "The remote port to be set is :" + remotePort);

        if(cancel)
        {
            // There was an error; don't attempt settting the connections and focus the
            // form fields with an error.
            focusView.requestFocus();
        }
        else
        {
            Settings.setDestinationIp(destIp);
            Settings.setRemotePort(remotePort);
            Log.i("Set connections","All conditions fulfilled");

            Intent i = new Intent(this, ChatWindow.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }




    }

    public void startFresh()
    {
        Intent i = new Intent(this,login_page.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public static String getDestinationIp()
    {
        return destinationIp;
    }

    public static void setDestinationIp(String destinationIp) {
        Settings.destinationIp = destinationIp;
    }

    public static Integer getRemotePort() {
        return remotePort;
    }

    public static void setRemotePort(Integer remotePort) {
        Settings.remotePort = remotePort;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Settings.username = username;
    }


}
