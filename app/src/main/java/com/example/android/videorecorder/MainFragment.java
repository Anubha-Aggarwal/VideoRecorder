package com.example.android.videorecorder;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by User on 06-03-2016.
 */
public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        CameraPreview cameraPreview=new CameraPreview(getActivity(),getCameraInstance());
        FrameLayout preview=(FrameLayout)rootView.findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);
        return rootView;
    }
    // detecting a camera
    private boolean checkCameraHardware(Context context)
    {
       return  context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    //accessing cameras
    public static Camera getCameraInstance()
    {
        Camera camera=null;
        try
        {
            camera=Camera.open();
          //  Camera.Parameters parameters=camera.getParameters();
        }
        catch(Exception e) {
            Log.d("Accessing Camera ", e.toString());
        }
        return camera;
    }

}
