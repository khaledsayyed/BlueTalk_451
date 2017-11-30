package com.example.khaledelsayed.bluetalk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/**
 * Created by khaled el sayed on 11/26/2017.
 */

public class UpdateMessagesService extends Service
{

    private Timer timer = new Timer();
    HttpURLConnection conn;
    URL url = null;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    url = new URL("https://khaled-sayed.000webhostapp.com/messages.json");//?username1="+MyName+"+username2="+mUser
                    //  url = new URL("http://localhost:81/users.json");

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
                try {

                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("GET");

                    // setDoOutput to true as we recieve data from json file
                    conn.setDoOutput(true);

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return;
                }
                try {

                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        String myresult= "{'message':'yaay',sender:'yasmina'}";//result.toString();
                        JSONObject json_data = new JSONObject(myresult);
                        DataMessage messageData = new DataMessage();
                        messageData.message = json_data.getString("message");
                        messageData.sender = json_data.getString("sender");
                        EventBus.getDefault().post(messageData); //and This
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (JSONException e){
                    e.printStackTrace();
                    return;
                }

            }
        }, 0, 500*1000);//5 secs
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}