package com.example.itapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    SwipeScreenAdapter swipeScreenAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        swipeScreenAdapter = new SwipeScreenAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeScreenAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Shade Control");
        tabLayout.getTabAt(1).setText("Bluetooth Connection");
        tabLayout.getTabAt(2).setText("Preset");
    }

}
