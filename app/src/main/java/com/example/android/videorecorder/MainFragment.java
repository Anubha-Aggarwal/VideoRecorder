package com.example.android.videorecorder;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 06-03-2016.
 */
public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        return rootView;
    }
    // detecting a camera
    private boolean checkCameraHardware(Context context)
    {
       return  context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    //accessing cameras
    public void getCameraInstance()
    {
        try
        {
            CameraManager cameraManager=(CameraManager)getActivity().getSystemService(Context.CAMERA_SERVICE);

        }
        catch(Exception e) {
            Log.d("Accessing Camera ", e.toString());
        }
    }
}
