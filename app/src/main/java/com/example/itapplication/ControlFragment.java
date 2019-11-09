package com.example.itapplication;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class ControlFragment extends Fragment {

    BluetoothSocket socket;

    Button upButton;
    Button downButton;

    public ControlFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_fragment, container, false);

        upButton = view.findViewById(R.id.upButton);
        downButton = view.findViewById(R.id.downButton);

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnLED();
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffLED();
            }
        });

        return view;
    }

    private void turnOnLED() {
        BluetoothFragment fragment = new BluetoothFragment();

        if(fragment.getSocket()!=null) {
            socket = fragment.getSocket();

            try {
                socket.getOutputStream().write("ON".getBytes());
            } catch (IOException e) {
                Log.e("ControlFragment", e.toString(), e);
            }
        }
    }

    private void turnOffLED() {
        BluetoothFragment fragment = new BluetoothFragment();

        if(fragment.getSocket()!=null) {
            socket = fragment.getSocket();

            try {
                socket.getOutputStream().write("OFF".getBytes());
            } catch (IOException e) {
                Log.e("ControlFragment", e.toString(), e);
            }
        }
    }
}
