package com.example.khaledelsayed.bluetalk;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by User on 13/11/2017.
 */

public class JasonConnector {
    public static Object connect(String JsonURL)
    {
        try
        {
            URL url = new URL(JsonURL);
            HttpURLConnection con =  (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setConnectTimeout(15000);
            con.setDoInput(true);

            return con;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return "Error"+ e.getMessage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error"+ e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error"+ e.getMessage();
        }
    }




}
