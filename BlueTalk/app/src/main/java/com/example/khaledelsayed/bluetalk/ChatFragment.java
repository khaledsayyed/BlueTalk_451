package com.example.khaledelsayed.bluetalk;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static String mUser = null;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView;
    public View.OnClickListener mOnAttachPressedListener
            = new View.OnClickListener() {

        public void onClick(View v) {
            new MaterialFilePicker()
                    .withActivity(getActivity())
                    .withRequestCode(10)
                    .start();
        }
    };


    public View.OnClickListener mOnSendPressedListener
            = new View.OnClickListener() {

        public void onClick(View v) {
            DataMessage msj = new DataMessage(((EditText) getActivity().findViewById(R.id.message_to_be_sent)).getText().toString(),((MainActivity) getActivity()).MyName);

            if(msj.message=="")
                return;
            new MessageAsyncSend(msj.message,msj.sender,mUser).execute();
         ((MainActivity) getActivity()).myMessageAdapter.data.add(msj);
            ((MainActivity) getActivity()).myMessageAdapter.notifyItemInserted(((MainActivity) getActivity()).myMessageAdapter.getItemCount()-1);
          //  recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,null,myMessageAdapter.getItemCount()-1);
         /*   RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller((Context) mListener) {
                @Override protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_END;
                }
            };
            smoothScroller.setTargetPosition(myMessageAdapter.getItemCount()-1);
            recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);*/
            ((LinearLayoutManager) recyclerView.getLayoutManager()).setSmoothScrollbarEnabled(true);

           ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(((MainActivity) getActivity()).myMessageAdapter.getItemCount()-1,5);
            /*
         recyclerView.postDelayed(new Runnable() {
             @Override
             public void run() {
                 recyclerView.getLayoutManager().scrollToPosition(myMessageAdapter.getItemCount()-1);
             }
         }, 1000);*/

            ((EditText) getActivity().findViewById(R.id.message_to_be_sent)).setText("");
        }

    };

    private OnFragmentInteractionListener mListener;
    private ImageView send_btn,attach_btn;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String user) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mUser = getArguments().getString(ARG_PARAM1);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        send_btn = (ImageView)view.findViewById(R.id.send_message);
      send_btn.setOnClickListener(mOnSendPressedListener);
        attach_btn = (ImageView)view.findViewById(R.id.upload_file);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return view;
            }
        }

        enable_button();
        attach_btn.setOnClickListener(mOnAttachPressedListener);
        new ChatAsyncFetch().execute();


        return view;

    }
    private void enable_button() {
        attach_btn.setOnClickListener(mOnAttachPressedListener);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

  //           send_btn = (ImageView)getActivity().findViewById(R.id.send_message);
//       send_btn.setOnClickListener(mOnSendPressedListener);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }



    private class ChatAsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ChatFragment.this.getActivity());
        HttpURLConnection conn;
        URL url = null;
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

                url = new URL("https://khaled-sayed.000webhostapp.com/messages.json");//?username1="+MyName+"+username2="+mUser
                //  url = new URL("http://localhost:81/users.json");

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

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
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
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }
        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataMessage> data=new ArrayList<>();

            pdLoading.dismiss();

            try {



                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataMessage messageData = new DataMessage();
                    messageData.message = json_data.getString("message");
                    messageData.sender = json_data.getString("sender");

                    data.add(messageData);
                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView)ChatFragment.this.getActivity().findViewById(R.id.messages_list);
                ((MainActivity) getActivity()).myMessageAdapter = new MyMessageAdapter(((MainActivity)getActivity()).MyName,ChatFragment.this.getActivity(), data);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatFragment.this.getActivity().getBaseContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(((MainActivity) getActivity()).myMessageAdapter);
                new Thread(new Runnable() {
                    public void run() {
                        ChatFragment.this.getActivity().startService(new Intent(getActivity(), UpdateMessagesService.class)); //I call the Service

                    }
                    }).start();



            } catch (JSONException e) {
                Toast.makeText(ChatFragment.this.getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    private class MessageAsyncSend extends AsyncTask<String, String, String> {

        MessageAsyncSend(String message,String sender,String receiver){
            mMesssage = message;
            mSender = sender;
            mReceiver = receiver;
        }
        String mMesssage,mSender,mReceiver;
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("https://khaled-sayed.000webhostapp.com/messages/create.php");//?username1="+MyName+"+username2="+mUser


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

                conn.setRequestMethod("POST");
                conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                conn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");


                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                String urlParameters = "message="+mMesssage+"&sender="+mSender+"&receiver="+mReceiver;
                DataOutputStream outputPost = new DataOutputStream(conn.getOutputStream());
                outputPost.writeBytes(urlParameters);
                outputPost.flush();
                outputPost.close();
//                conn.setFixedLengthStreamingMode(urlParameters.getBytes().length);
 //               conn.setChunkedStreamingMode(0);

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
               conn.disconnect();
            }
            return ("success");
        }


    }


}
