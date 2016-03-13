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
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by User on 06-03-2016.
 */
public class MainFragment extends Fragment {
    private static int cameraId=-1;
    private  static Camera camera;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        CameraPreview cameraPreview=new CameraPreview(getActivity(),getCameraInstance(),cameraId);
        FrameLayout preview=(FrameLayout)rootView.findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);
        Button capture=(Button) rootView.findViewById(R.id.button_capture);
        capture.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                    }
                });
            }
        });
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
         camera=null;
        try
        {
            int noOfCamera=Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo=new Camera.CameraInfo();
            for(int i=0;i<noOfCamera;i++)
            {
                Camera.getCameraInfo(i,cameraInfo);
               if( cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_BACK) {
                   camera = Camera.open();
                   cameraId=i;
                   break;
               }
            }

          //  Camera.Parameters parameters=camera.getParameters();
        }
        catch(Exception e) {
            Log.d("Accessing Camera ", e.toString());
        }
        return camera;
    }
   /* private static File getOutputMediaFile(int type)
    {

    }*/
}
