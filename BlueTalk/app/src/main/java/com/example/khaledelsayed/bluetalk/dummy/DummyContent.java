package com.example.khaledelsayed.bluetalk.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 5;

      public DummyContent(Context c){
        // Add some sample items.
        BluetoothAdapter Adapter = BluetoothAdapter.getDefaultAdapter();
        if (Adapter == null) {
 // Device does not support Bluetooth
            addItem(new DummyItem("Bluetooth","not supported","details"));
        } else {

                addItem(new DummyItem("Bluetooth",(Adapter.isEnabled())?"on":"off","details"));

        }
        WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            addItem(new DummyItem("WiFi","not supported","details"));
        } else {
            addItem(new DummyItem("WiFi",wifiManager.isWifiEnabled()?"ON":"OFF","wifi state"));
        }
         ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

          NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        addItem(new DummyItem("Mobile Data",networkInfo.isConnected()?"on":"OFF","details"));

    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
