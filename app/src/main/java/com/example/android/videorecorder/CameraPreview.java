package com.example.android.videorecorder;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{
    private Camera mcamera;
    private int mCameraId;
    private SurfaceHolder msurfaceHolder;
    private Activity mActivity;
    public CameraPreview(Activity activity,Camera camera,int cameraId) {
        super(activity.getApplicationContext());
        mcamera=camera;
        msurfaceHolder=getHolder();
        //to get notification when the underlying surface is created or destroyed.
        msurfaceHolder.addCallback(this);
        mCameraId=cameraId;
        mActivity=activity;
    }
    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link android.view.Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mcamera.setPreviewDisplay(msurfaceHolder);
            CameraPreview.setCameraDisplayOrientation(mActivity, mCameraId, mcamera);
            mcamera.startPreview();
            Log.d("Activity ","Surface Created");
        }catch (Exception e) {
            Log.d("Camera preview", e.toString());
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @[param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("Activity ","Surface Changed");
       if(msurfaceHolder.getSurface()==null)
            return;
        //stop preview before making changes
        try {
            mcamera.stopPreview();
        }
        catch(Exception e)
        {}

        //mcamera.setDisplayOrientation(90);
        //start preview with new settings
        try {
            mcamera.setPreviewDisplay(msurfaceHolder);
            CameraPreview.setCameraDisplayOrientation(mActivity,mCameraId,mcamera);
            mcamera.startPreview();
        }catch(Exception e)
        {Log.d("Hel",e.toString());}
    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            if(mcamera!=null) {
                mcamera.release();
                mcamera=null;
            }
    }
    public static void setCameraDisplayOrientation(Activity activity,int cameraId,Camera camera)
    {
        Log.d("Activity ","Surface Destroyed");
        Camera.CameraInfo cameraInfo=new Camera.CameraInfo();
        camera.getCameraInfo(cameraId,cameraInfo);
        int rotation=activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees=0;
        int result=0;
        switch (rotation)
        {
            case Surface.ROTATION_0:degrees=0;break;
            case Surface.ROTATION_90:degrees=90;break;
            case Surface.ROTATION_180:degrees=180;break;
            case Surface.ROTATION_270:degrees=270;break;
        }
        if(cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT)
        {}
        else
        {
            result=(cameraInfo.orientation-degrees+360)%360;
        }
        camera.setDisplayOrientation(result);
    }
}