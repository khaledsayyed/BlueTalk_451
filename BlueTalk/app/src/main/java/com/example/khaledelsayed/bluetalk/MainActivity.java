package com.example.khaledelsayed.bluetalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity
implements HomeFragment.OnFragmentInteractionListener,ConStatusFragment.OnFragmentInteractionListener,ChatFragment.OnFragmentInteractionListener,ChatsFragment.OnListFragmentInteractionListener{

public String MyName="khaled";
    public MyMessageAdapter myMessageAdapter;
 public  void onFragmentInteraction(Uri uri){

    }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_status:
                    selectedFragment = ConStatusFragment.newInstance();
                    break;
                case R.id.navigation_chats:
                    selectedFragment = ChatsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, selectedFragment);
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

   FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, HomeFragment.newInstance());
       transaction.addToBackStack(null);
    transaction.commit();
      BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
      navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        EventBus.getDefault().register(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataMessage data){
        if(data!=null&&!data.message.isEmpty()) {
            myMessageAdapter.data.add(data);
            myMessageAdapter.notifyItemInserted(myMessageAdapter.getItemCount() - 1);
            //  findViewById(R.id.messages_list).computeScroll();
            //  findViewById(R.id.messages_list).setScrollY(findViewById(R.id.messages_list).getBottom());
            findViewById(R.id.messages_list).setMinimumHeight(200);
        }
    }

    @Override
    public void onListFragmentInteraction(DataUser item) {
        Fragment selectedFragment = ChatFragment.newInstance(item.name);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();
    }

    ProgressDialog progress;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(MainActivity.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .build();

                    Request request = new Request.Builder()
                            .url("https://khaled-sayed.000webhostapp.com/messages/create.php")
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        myMessageAdapter.data.add(new DataMessage("PIC:"+file_path,MyName));

                        myMessageAdapter.notifyItemInserted(myMessageAdapter.getItemCount() - 1);
                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();




        }
    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
