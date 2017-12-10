package com.example.khaledelsayed.bluetalk;

import android.app.ProgressDialog;
import android.content.Context;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import java.net.URLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentcInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HomeFragment extends Fragment {


 // TODO: Customize parameter argument names
 private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnFragmentcInteractionListener mListener;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView;
    private MychannelAdapter mychannelAdapter;


    /**

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameter
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {

        //String param1, String param2
        HomeFragment fragment = new HomeFragment();
      /*  Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  mError = (TextView) getActivity().findViewById(R.id.error);
      /*   if (getArguments() != null) {
           mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_home, container, false);
     new AsyncFetch().execute();
        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentcInteractionListener) {
            mListener = (OnFragmentcInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentcInteractionListener {
        // TODO: Update argument type and name
        void onFragmentcInteraction(DataChannel dataChannel);
        void onCreatePiconet( int ChannelId);
        void onJoinPiconet(int PiconetId);

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(HomeFragment.this.getActivity());
        HttpURLConnection conn;
        URL url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                String myUrl = "https://bluetalk.herokuapp.com/channels.json";
                url = new URL(myUrl);
                //https://bluetalk.herokuapp.com/channels.json

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
               conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
               // conn.connect();
                // setDoOutput to true as we recieve data from json file


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {

                int response_code = conn.getResponseCode();
//if (response_code==200){ return ("5ara");}

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK ||response_code ==422) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
            finally {
                conn.disconnect();
            }

        }
        @Override
            protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataChannel> data=new ArrayList<>();

            pdLoading.dismiss();

            try {



                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataChannel channelData = new DataChannel();
                    channelData.channelid = json_data.getInt("id");
                    channelData.channel_name = json_data.getString("name");

                   // channelData.number_of_users = json_data.getInt("number_of_users");
                    channelData.piconetid= json_data.getInt("piconet_id");

                    /*************************************/
                    if(channelData.piconetid==0){channelData.timer="";}
                    else {


                    String tempTime= json_data.getJSONObject("piconet").getString("created_at");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                    Date date = null;

                    try {
                        date = format.parse(tempTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    String convertedDate = format1.format(date);
                    channelData.timer= convertedDate; }
                    /********************************/
                    data.add(channelData);

                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView)HomeFragment.this.getActivity().findViewById(R.id.lv);
                mychannelAdapter = new MychannelAdapter(mListener,HomeFragment.this.getActivity(),data);
                recyclerView.setAdapter(mychannelAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeFragment.this.getActivity()));
                recyclerView.addItemDecoration(new DividerItemDecoration(HomeFragment.this.getActivity()));

            } catch (JSONException e) {
                Toast.makeText(HomeFragment.this.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }






}
