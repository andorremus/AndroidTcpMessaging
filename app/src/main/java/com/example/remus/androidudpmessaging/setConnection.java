package com.example.remus.androidudpmessaging;

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
import android.widget.Toast;


public class setConnection extends ActionBarActivity {

    private EditText localPortEdit;
    private EditText ipAddressEdit;
    private EditText remotePortEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_connection);

        localPortEdit =(EditText)findViewById(R.id.editTextLocalPort);
        ipAddressEdit =(EditText)findViewById(R.id.editTextIpAddress);
        remotePortEdit = (EditText)findViewById(R.id.editTextRemotePort);

        final Button setButton =(Button)findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setConnections();
                // startChat();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent i = new Intent(this,Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setConnections()   // This method validates the inputted values and uses them to
    {
        localPortEdit.setError(null);
        ipAddressEdit.setError(null);
        remotePortEdit.setError(null);

        Integer localPort = null;
        Integer remotePort = null;

        boolean cancel = false;
        View focusView = null;

        try
        {
            localPort = Integer.parseInt(localPortEdit.getText().toString());
            remotePort = Integer.parseInt(remotePortEdit.getText().toString());
        }
        catch (NumberFormatException e)
        {
            Log.i("Set Connections",e.getMessage());
        }
        catch (Exception e)
        {
            Log.i("Set Connections",e.getMessage());
        }

        try
        {
            remotePort = Integer.parseInt(remotePortEdit.getText().toString());
        }
        catch (NumberFormatException e)
        {
            Log.i("Set Connections",e.getMessage());
        }
        catch (Exception e)
        {
            Log.i("Set Connections",e.getMessage());
        }


        String localPortString = null;
        try
        {
            localPortString = localPort.toString();

        }
        catch (Exception e)
        {

        }
        if(TextUtils.isEmpty(localPortString))
        {
            localPortEdit.setError("Please write a local port number");
            cancel = true;
            focusView = localPortEdit;
        }
        Log.i("Set connections", "The local port to be set is :" + localPort);

        String ipAddress = ipAddressEdit.getText().toString();
        if(TextUtils.isEmpty(ipAddress))
        {
            ipAddressEdit.setError("Please write an IP address");
            cancel = true;
            focusView = ipAddressEdit;

        }
        Log.i("Set connections", "The ip address to be set is :" + ipAddress);

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
            remotePortEdit.setError("Please write a remote port");
            cancel = true;
            focusView = remotePortEdit;

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
            Settings.setLocalPort(localPort);
            Settings.setDestinationIp(ipAddress);
            Settings.setRemotePort(remotePort);
            Log.i("Set connections","All conditions fulfilled");
            Toast toast = Toast.makeText(getApplicationContext(),"Connection set successfully",Toast.LENGTH_LONG);
            toast.show();
            startChat();
        }




    }

    private void startChat()
    {
        Intent i = new Intent(this,ChatWindow.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}
