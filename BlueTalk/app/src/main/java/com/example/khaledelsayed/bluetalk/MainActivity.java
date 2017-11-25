package com.example.khaledelsayed.bluetalk;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
implements HomeFragment.OnFragmentInteractionListener,ConStatusFragment.OnFragmentInteractionListener,ChatFragment.OnFragmentInteractionListener,ChatsFragment.OnListFragmentInteractionListener{

public String MyName="khaled";
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
    }


    @Override
    public void onListFragmentInteraction(DataUser item) {
        Fragment selectedFragment = ChatFragment.newInstance(item.name);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();
    }
}
