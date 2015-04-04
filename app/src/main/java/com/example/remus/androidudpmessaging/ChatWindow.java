package com.example.remus.androidudpmessaging;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ChatWindow extends ActionBarActivity {

    DatagramSocket socket;
    private static Integer localPort,remotePort;
    private static String destIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        localPort = Settings.getLocalPort();
        remotePort = Settings.getRemotePort();
        destIp = Settings.getDestinationIp();

        Thread receiver = new Thread(new SocketListener());
        receiver.start();

        final Button sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Thread sender = new Thread(new SocketSender());
                sender.start();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_window, menu);
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
        if( id == R.id.action_exit)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SocketListener implements Runnable
    {
        String str;

        public void run()
        {
            DatagramPacket packet;
            byte[] buf = new byte[256];
            Log.i("Socket Thread ", "Thread running");

            try
            {
                socket = new DatagramSocket(localPort);

                while (true)
                {
                    final TextView t = (TextView) findViewById(R.id.chatTextView);


                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    System.out.println("Received packet");
                    String s = new String(packet.getData());

                    CharSequence cs = t.getText();
                    str = cs + "\r\n" + s;
                    Log.i("Socket Thread", str);

                    t.post(new Runnable()
                           {
                               public void run()
                               {
                                   t.setTextColor(Color.BLUE);
                                   t.setText(str);
                               }
                           }
                    );
                }
            } catch (IOException e) {
                Log.e(getClass().getName(), e.getMessage());
            }

        }
    }

    class SocketSender implements Runnable
    {
        String str;
        String username =Settings.getUsername();
        @Override
        public void run()
        {
            String s= null;
            final EditText editTextSender = (EditText)findViewById(R.id.editTextSender);
            try
            {
               s = username +" : " + editTextSender.getText().toString();
            }
            catch (Exception e)
            {
                Log.i("Socket Sender ",e.getMessage());
            }



            //DatagramSocket socket;
            try
            {
                DatagramSocket socket = new DatagramSocket ();

                byte[] buf = new byte[256];

                buf = s.getBytes ();
                InetAddress address = InetAddress.getByName (destIp);
                DatagramPacket packet = new DatagramPacket (buf, buf.length, address, remotePort);
                Log.i ("Socket Sender","About to send message");
                socket.send (packet);
                Log.i ("Socket Sender","Sent message");

                final TextView t = (TextView) findViewById(R.id.chatTextView);
                CharSequence cs = t.getText();
                str = cs + "\r\n" +s;
                Log.i("Socket Thread", str);

                t.post(new Runnable()
                       {
                           public void run()
                           {
                               t.setTextColor(Color.RED);
                               t.setText(str);
                           }
                       }
                );
            }
            catch (SocketException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (UnknownHostException e2)
            {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }


        }

        }
    }

