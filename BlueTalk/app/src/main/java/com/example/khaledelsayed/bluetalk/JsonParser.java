package com.example.khaledelsayed.bluetalk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User on 13/11/2017.
 */

public class JsonParser extends AsyncTask<Void,Void,Boolean> {
    Context c;
    String jsonData;
    ListView lv;
    ProgressDialog pd;
    ArrayList<String> channels= new ArrayList<>();

    public JsonParser(Context c, String jsonData, ListView lv) {
        this.c = c;
        this.jsonData = jsonData;
        this.lv = lv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Parse JSON");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        return parse();
    }
    @Override
    protected void onPostExecute(Boolean isParsed)
    {
        super.onPostExecute(isParsed);
        pd.dismiss();

        if(isParsed){
            //bind
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, channels);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(c,channels.get(i),Toast.LENGTH_SHORT).show();
                }
            });
        }else
            {
                Toast.makeText(c,"Unable to parse. Check four log output", Toast.LENGTH_SHORT).show();

        }
    }
    private Boolean parse()
    {
        try
        {
            JSONArray ja= new JSONObject(jsonData).getJSONArray("channels");
            JSONObject jo;
            channels.clear();
            for (int i=0; i<ja.length();i++)
            {
                jo= ja.getJSONObject(i);
                String name= jo.getString("name");
                channels.add(name);
            }
            return  true;
        } catch (JSONException e) {
            e.printStackTrace();
            return  false;
        }
    }
}
