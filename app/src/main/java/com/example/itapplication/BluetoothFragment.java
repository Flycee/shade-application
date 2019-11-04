package com.example.itapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends Fragment {

    public BluetoothFragment() {}

    private final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    Button searchDevicesButton;

    ListView pairedDevicesList;
    ListView foundDevicesList;

    Set<BluetoothDevice> pairedDevices;

    ArrayAdapter pairedDevicesAdapter;
    ArrayAdapter <String> foundDevicesAdapter;

    BluetoothAdapter bluetoothAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.bluetooth_fragment);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Ask for location permission if not already allowed
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if(bluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Your device doesn't support Bluetooth!", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(receiver, filter);
    }

    private void printPairedDevices() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size()>0) {
            for(BluetoothDevice device : pairedDevices) {
                list.add(device.getName() + "\n" + device.getAddress());
            }
        }
        else {
            Toast.makeText(getActivity(), "No paired devices found", Toast.LENGTH_SHORT).show();
        }

        pairedDevicesAdapter.clear();
        pairedDevicesAdapter.addAll(list);
        pairedDevicesAdapter.notifyDataSetChanged();
    }

    private void discover() {
        if(bluetoothAdapter.isEnabled()) {
            foundDevicesAdapter.clear();
            bluetoothAdapter.startDiscovery();
            Toast.makeText(getActivity(), "Discovery started", Toast.LENGTH_SHORT).show();
        }
    }

    final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //Do not show paired devices again
                if(pairedDevices.size()>0) {
                    for (BluetoothDevice paired : pairedDevices) {
                        if(!paired.equals(device)) {
                            Log.d(TAG, "paired!=device");
                            foundDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                            Log.d(TAG, device.getName() + "\n" + device.getAddress());
                            foundDevicesAdapter.notifyDataSetChanged();
                        }
                    }
                }
                else {
                    foundDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                    Log.d(TAG, device.getName() + "\n" + device.getAddress());
                    foundDevicesAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bluetooth_fragment, container, false);

        pairedDevicesList = (ListView) view.findViewById(R.id.pairedDevicesListView);
        foundDevicesList = (ListView) view.findViewById(R.id.searchedDevices);
        searchDevicesButton = (Button) view.findViewById(R.id.searchDevicesButton);

        pairedDevicesAdapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1);
        pairedDevicesList.setAdapter(pairedDevicesAdapter);

        foundDevicesAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        foundDevicesList.setAdapter(foundDevicesAdapter);
        //foundDevicesList.setOnClickListener(); todo

        searchDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled()) {
                    printPairedDevices();
                    discover();
                }
                else {
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
                }
            }
        });

        return view;
    }
}
