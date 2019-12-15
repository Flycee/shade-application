package com.example.itapplication;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class PresetTimeFragment extends Fragment {

    BluetoothSocket socket;

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button setButton;

    public PresetTimeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preset_fragment, container, false);

        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);
        setButton = view.findViewById(R.id.date_time_set);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this Java class is counting months from 0 to 11 so we have to add 1
                int month = datePicker.getMonth()+1;
                String date = datePicker.getYear() + "/"+ month + "/" + datePicker.getDayOfMonth();
                String time = timePicker.getHour() + "/" + timePicker.getMinute();
                Toast.makeText(getActivity(), "Shade will run on:" + "\n" + date + "\n" +
                        timePicker.getHour() + ":" + timePicker.getMinute(), Toast.LENGTH_LONG).show();
                Log.d("", date+"/"+time);

                try {
                    //send data only if we are already connected
                    if(socket != null)
                        socket.getOutputStream().write((date+"/"+time).getBytes());
                    else
                        Toast.makeText(getActivity(), "There is no connected device!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e("PresetTimeFragment", e.toString(), e);
                    try {
                        socket.close();
                    }
                    catch (IOException closeException) {
                        Log.e("", e.getMessage(), closeException);
                    }
                }
            }
        });

        return view;
    }

    public void getSocket(BluetoothSocket mmSocket) {
        socket = mmSocket;
        Log.e("Preset frag. socket: ", socket.toString() + " " + socket.isConnected());
    }

}
