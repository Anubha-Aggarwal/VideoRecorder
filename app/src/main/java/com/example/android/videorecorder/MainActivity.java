package com.example.android.videorecorder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null)
        {
            getFragmentManager().beginTransaction().add(R.id.container,new MainFragment(),null).commit();
        }

    }

    @Override
    protected void onResume() {
        Log.d("Activity","Resumed");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("Activity","Paused");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("Activity","Stopped");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.d("Activity","Started");
        super.onStart();
    }
}
