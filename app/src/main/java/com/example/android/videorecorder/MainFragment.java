package com.example.android.videorecorder;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 06-03-2016.
 */
public class MainFragment extends Fragment {
    private static int cameraId=-1;
    private  static Camera camera;
    private static MediaRecorder mediaRecorder;
    private  static CameraPreview cameraPreview;
    private boolean isRecording=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main,container,false);
        cameraPreview=new CameraPreview(getActivity(),getCameraInstance(),cameraId);
        FrameLayout preview=(FrameLayout)rootView.findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);
        Button capture=(Button) rootView.findViewById(R.id.button_capture_image);
        capture.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
               camera.takePicture(new Camera.ShutterCallback() {
                   @Override
                   public void onShutter() {

                   }
               }, null, new Camera.PictureCallback() {
                   @Override
                   public void onPictureTaken(byte[] data, Camera camera) {
                       File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                       if (pictureFile == null) {
                           Log.d("Storage tag", "Error creating media file, check storage permissions"
                           );
                           return;
                       }
                       try {
                           FileOutputStream outputStream = new FileOutputStream(pictureFile);
                           outputStream.write(data);
                           outputStream.close();
                           camera.startPreview();
                       } catch (FileNotFoundException e) {
                           Log.d("Storage tag", "File not found: " + e.getMessage());
                       } catch (IOException e) {
                           Log.d("Storage tag", "Error accessing file: " + e.getMessage());
                       }
                   }
               });
            }
        });
        final Button captureVideo=(Button)rootView.findViewById(R.id.button_capture_video);
        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording)
                {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    camera.lock();
                    captureVideo.setText("Capture Video");
                    isRecording=false;
                }
                else
                {
                    if(prepareVideoRecorder()) {
                        mediaRecorder.start();
                        captureVideo.setText("Stop");
                        isRecording=true;
                    }
                    else
                    {
                        mediaRecorder.release();
                    }
                }
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
                   camera = Camera.open(i);
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
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private  static Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type)
    {
        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"VideoRecorder");
        if(!mediaStorageDir.exists())
        {
            if(!mediaStorageDir.mkdir()) {
                Log.d("VideoRecorder", "Failed to create dir");
                return null;
            }
        }
        //create a new media file
        String timestamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if(type==MEDIA_TYPE_IMAGE)
            mediaFile=new File(mediaStorageDir+File.separator+"IMG_"+timestamp+".jpg");
        else if(type==MEDIA_TYPE_VIDEO)
            mediaFile=new File(mediaStorageDir+File.separator+"VID_"+timestamp+".mp4");
        else
            return null;
        return mediaFile;
    }

    private boolean prepareVideoRecorder()
    {
        try {
            mediaRecorder = new MediaRecorder();
            //unlock and set camera to media recorder
            camera.unlock();
            mediaRecorder.setCamera(camera);
            // set audio and video source
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //set profile of media recorder
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            //set output file
            mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
            //preview display
            mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());
            //prepare
            mediaRecorder.prepare();
        }catch (IllegalStateException e) {
            Log.d("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            //releaseMediaRecorder();
            mediaRecorder.release();
            return false;
        } catch (IOException e) {
            Log.d("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
            //releaseMediaRecorder();
            mediaRecorder.release();
            return false;
        }
        return true;
    }
}
