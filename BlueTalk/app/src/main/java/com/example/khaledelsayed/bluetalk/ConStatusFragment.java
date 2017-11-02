package com.example.khaledelsayed.bluetalk;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConStatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConStatusFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private CheckedTextView bluetooth,wifi,mobile;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            bluetooth = (CheckedTextView) getActivity().findViewById(R.id.checkedBluetooth);
            wifi = (CheckedTextView) getActivity().findViewById(R.id.checkedwifi);
            mobile = (CheckedTextView) getActivity().findViewById(R.id.checkedmobiledata);
            BluetoothAdapter Adapter = BluetoothAdapter.getDefaultAdapter();
            if (Adapter == null) {
                // Device does not support Bluetooth
                bluetooth.setText("not supported");
                bluetooth.setChecked(false);
            } else {
                bluetooth.setText((Adapter.isEnabled())?"ON":"OFF");
                bluetooth.setChecked((Adapter.isEnabled()));


            }
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                wifi.setText("not supported");
                wifi.setChecked(false);
            } else {
                wifi.setText(wifiManager.isWifiEnabled()?"ON":"OFF");
                wifi.setChecked(wifiManager.isWifiEnabled());

            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            mobile.setText(((!wifiManager.isWifiEnabled())&&networkInfo!=null&&networkInfo.isConnected())?"on":"OFF");
            mobile.setChecked(((!wifiManager.isWifiEnabled())&&networkInfo!=null&&networkInfo.isConnected()));




        }

    };

    public ConStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConStatusFragment newInstance() {
        ConStatusFragment fragment = new ConStatusFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_con_status, container, false);
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

            bluetooth = (CheckedTextView) getActivity().findViewById(R.id.checkedBluetooth);
            wifi = (CheckedTextView) getActivity().findViewById(R.id.checkedwifi);
            mobile = (CheckedTextView) getActivity().findViewById(R.id.checkedmobiledata);
            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            context.registerReceiver(mReceiver, new IntentFilter(filter));
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
