package com.example.itapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements BluetoothFragment.BluetoothFragmentListener {

    private BluetoothFragment bluetoothFragment;
    private ControlFragment controlFragment;

    SwipeScreenAdapter swipeScreenAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothFragment = new BluetoothFragment();
        controlFragment = new ControlFragment();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        swipeScreenAdapter = new SwipeScreenAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeScreenAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Shade Control");
        tabLayout.getTabAt(1).setText("Bluetooth Connection");
        tabLayout.getTabAt(2).setText("Preset");
    }

    @Override
    public void onConnect(BluetoothSocket mmSocket) {
        //ControlFragment ma id 0
        ControlFragment controlFrag = (ControlFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":0");
        if(controlFrag!=null) {
            Log.e("Activity socket: ", mmSocket.toString() + " " + String.valueOf(mmSocket.isConnected()));
            controlFrag.getSocket(mmSocket);
        }
        else {
            Log.e("", "Nie ma controlFrag!");
        }
    }
}
