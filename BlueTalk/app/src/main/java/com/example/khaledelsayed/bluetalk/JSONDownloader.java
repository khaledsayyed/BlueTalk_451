package com.example.khaledelsayed.bluetalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by User on 13/11/2017.
 */

public class JSONDownloader extends AsyncTask<Void,Void,String> {
    Context c;
    String JsonURL;
    ListView lv;

    ProgressDialog pd;

    public JSONDownloader(Context c, String jsonURL, ListView lv) {
        this.c = c;
        JsonURL = jsonURL;
        this.lv = lv;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Download JSON");
        pd.setMessage("Downloading.. Please wait");
        pd.show();
    }
    @Override
    protected String doInBackground(Void...voids){
        return download();
    }
    @Override
    protected void onPostExecute(String jsonData){
        super.onPostExecute(jsonData);

        pd.dismiss();
        if (jsonData.startsWith("Error"))
        {
            String error= jsonData;
            Toast.makeText(c,error,Toast.LENGTH_SHORT).show();
        }else{
            //parser
            new JsonParser(c, jsonData,lv).execute();
        }
    }
    private String download()
    {
        Object connection= JasonConnector.connect(JsonURL);
        if(connection.toString().startsWith("Error"))
        {
            return connection.toString();
        }
        try
        {
            //establish conn
            HttpURLConnection con= (HttpURLConnection) connection;
            if(con.getResponseCode()== con.HTTP_OK)
            {
                //get input from stream
                InputStream is = new BufferedInputStream(con.getInputStream());
                BufferedReader br= new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer jasonData= new StringBuffer();
                //Read
                while ((line= br.readLine()) != null)
                {
                    jasonData.append(line+"\n");
                }
                //close
                br.close();
                is.close();
                //return json
                return jasonData.toString();
            }else
            {
                return "Error"+ con.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error"+ e.getMessage();
        }
    }

}
