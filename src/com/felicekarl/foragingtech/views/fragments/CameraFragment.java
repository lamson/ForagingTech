package com.felicekarl.foragingtech.views.fragments;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.CameraFragmentButtonListener;
import com.felicekarl.foragingtech.listeners.UpdateCameraFragmentButtonListener;

public class CameraFragment extends BaseFragment implements
        TextureView.SurfaceTextureListener, OnClickListener,
        UpdateCameraFragmentButtonListener {
    private static final String TAG = CameraFragment.class.getSimpleName();

    private TextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private RelativeLayout camera_texture_view_wrappter;

    private CameraFragmentButtonListener mCameraFragmentButtonListener;

    private Bitmap video;
    private long startMs;
    private int imageWidth;
    private int imageHeight;
    // opencv variables
    // private Mat tmp;
    // private Mat mIntermediateMat, mIntermediateMat2;
    // private Mat thresholdImage;
    // private Mat circles;
    private Point pt;

    private Object mFrameSyncObject = new Object(); // guards mFrameAvailable
    private boolean mFrameAvailable = true;

    public enum IMAGEMODE {
        CANNY, HOUGHCIRCLES, REDHOUGHCIRCLES, REDTHRESHOLD
    }

    public enum CAMERAMODE {
        FLYING, NAVIGATING
    }

    private IMAGEMODE imgMode;
    private CAMERAMODE camMode;

    public void setImageMode(IMAGEMODE imgMode) {
        this.imgMode = imgMode;
    }

    public IMAGEMODE getImageMode() {
        return imgMode;
    }

    public CAMERAMODE getCameraMode() {
        return camMode;
    }

    public CameraFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_camera,
                container, false);
        mTextureView = (TextureView) view
                .findViewById(R.id.camera_texture_view);
        mTextureView.setSurfaceTextureListener(this);
        camera_texture_view_wrappter = (RelativeLayout) view
                .findViewById(R.id.camera_texture_view_wrappter);
        slideUpFragment();

        return view;
    }

    public Surface getSurface() {
        return mSurface;
    }

    public TextureView getTextureView() {
        return mTextureView;
    }

    public static CameraFragment create() {
        return new CameraFragment();
    }

    @Override
    protected void enableEditText() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void disableEditText() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
            int height) {
        mSurfaceTexture = surface;
        mSurface = new Surface(mSurfaceTexture);
        startMs = System.currentTimeMillis();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
            int height) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        
    }

    public void setFrameColor(String color) {
        // ViewGroup.LayoutParams params =
        // camera_texture_view_wrappter.getLayoutParams();
        camera_texture_view_wrappter
                .setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public void onClick(View v) {
        Drawable mode_on = getActivity().getResources().getDrawable(
                R.drawable.btn_mode_on);
        mode_on.setBounds(0, 0, 40, 40);
        Drawable mode_off = getActivity().getResources().getDrawable(
                R.drawable.btn_mode_off);
        mode_off.setBounds(0, 0, 40, 40);
    }

    private class CanvasListener implements SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface,
                int width, int height) {
            imageWidth = width;
            imageHeight = height;
            Log.d(TAG, "onSurfaceTextureAvailable");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d(TAG, "onSurfaceTextureDestroyed");
            return true;
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                int width, int height) {
            imageWidth = width;
            imageHeight = height;
            Log.d(TAG, "onSurfaceTextureSizeChanged");
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // Log.d(TAG, "onSurfaceTextureUpdated");
        }

    }

    @Override
    public void resetFragment() {
        // reset Image Processing Mode as Canny
        imgMode = IMAGEMODE.CANNY;
        Drawable mode_on = getActivity().getResources().getDrawable(
                R.drawable.btn_mode_on);
        mode_on.setBounds(0, 0, 40, 40);
        Drawable mode_off = getActivity().getResources().getDrawable(
                R.drawable.btn_mode_off);
        mode_off.setBounds(0, 0, 40, 40);
    }

    @Override
    public void updateCameraFragmentButtonListener(
            CameraFragmentButtonListener mCameraFragmentButtonListener) {
        this.mCameraFragmentButtonListener = mCameraFragmentButtonListener;
    }
}
