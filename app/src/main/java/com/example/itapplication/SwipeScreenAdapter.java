package com.example.itapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SwipeScreenAdapter extends FragmentPagerAdapter {

    public SwipeScreenAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new ControlFragment();
            case 1:
                return new BluetoothFragment();
            case 2:
                return new PresetTimeFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
