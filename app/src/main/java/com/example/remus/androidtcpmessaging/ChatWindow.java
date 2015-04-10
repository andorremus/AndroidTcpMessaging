package com.example.remus.androidtcpmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ChatWindow extends ActionBarActivity
{
    private static Integer remotePort;
    private static String destIp;
    private Integer conversationLogId;
    private DatabaseConnection myDB;
    private Calendar now;
    private SimpleDateFormat formatter;
    private TextView t;
    private Button buttonWholeHistory;

    private Socket sender;
    private BufferedReader br;
    private PrintStream bw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        myDB = new DatabaseConnection();
        formatter = new SimpleDateFormat("HH:mm:ss");
        t = (TextView) findViewById(R.id.chatTextView);
        t.setMovementMethod(new ScrollingMovementMethod());

        conversationLogId = myDB.startConversation();

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

        buttonWholeHistory = (Button)findViewById(R.id.buttonWholeHistory);
        buttonWholeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loadHistory();

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
            Log.i("Socket Listener ", "Thread running");
            now = Calendar.getInstance();
            String date = formatter.format(now.getTime());

            try
            {

                sender = new Socket(destIp, remotePort);
                br = new BufferedReader (new InputStreamReader(sender.getInputStream()));
                bw = new PrintStream (sender.getOutputStream());

                while (true)
                {
                    Log.i("Socket Listener loop","Received packet");
                    String s = br.readLine();

                    myDB.insertLine(s,conversationLogId);

                    CharSequence cs = t.getText();
                    str = cs + "\r\n" + date + " "+ s;
                    Log.i("Socket Thread", str);

                    t.post(new Runnable()
                           {
                               public void run()
                               {
                                   t.setText(str);
                               }
                           }
                    );
                }
            } catch (Exception e)
            {
                Log.e(getClass().getName(), e.getMessage());
            }

        }
    }

    class SocketSender implements Runnable
    {
        String str;
        String username =Settings.getUsername().replace("'","");

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

            try
            {
                Log.i ("Socket Sender","About to send message"+s);
                bw.println(s);
                Log.i ("Socket Sender","Sent message");

            }
            catch (Exception e)
            {
                System.out.println("Socket Sender Exception" +e.getMessage());
            }


        }

    }

    public void loadHistory()
    {
        String history = myDB.retrieveHistory();
        final String str;

        CharSequence cs = t.getText();
        str = history + "\r\n"+ cs ;
        Log.i("Socket Thread", str);

        t.post(new Runnable()
               {
                   public void run()
                   {
                       t.setText(str);
                   }
               }
        );

        buttonWholeHistory.setClickable(false);


    }
}

