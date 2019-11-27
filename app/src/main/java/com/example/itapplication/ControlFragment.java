package com.example.itapplication;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class ControlFragment extends Fragment {

    private BluetoothSocket socket;

    private Button upButton;
    private Button downButton;

    private int barProgress;

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
        SeekBar seekBar = view.findViewById(R.id.mySeekBar);

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                barProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    if (socket != null)
                        socket.getOutputStream().write(Integer.toString(barProgress).getBytes());
                }
                catch (IOException e) {
                    Log.e("ControlFragment", e.toString(), e);
                    try {
                        socket.close();
                    }
                    catch (IOException closeException) {
                        Log.e("", e.getMessage(), closeException);
                    }
                }
                Toast.makeText(getActivity(), "Shade position: " + barProgress, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void getSocket(BluetoothSocket mmSocket) {
        socket = mmSocket;
        Log.e("Control socket: ", socket.toString() + " " + socket.isConnected());
    }

    private void turnOnLED() {
            try {
                if(socket != null)
                    socket.getOutputStream().write("ON".getBytes());
                else
                    Toast.makeText(getActivity(), "There is no connected device!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("ControlFragment", e.toString(), e);
                try {
                    socket.close();
                }
                catch (IOException closeException) {
                    Log.e("", e.getMessage(), closeException);
                }
            }
    }

    private void turnOffLED() {
            try {
                if(socket != null)
                    socket.getOutputStream().write("OFF".getBytes());
                else
                    Toast.makeText(getActivity(), "There is no connected device!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("ControlFragment", e.toString(), e);
                try {
                    socket.close();
                }
                catch (IOException closeException) {
                    Log.e("", e.getMessage(), closeException);
                }
            }
    }
}
