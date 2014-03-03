package com.felicekarl.foragingtech.views.fragments;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.felicekarl.foragingtech.R;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CameraFragment extends BaseFragment implements TextureView.SurfaceTextureListener,
		OnClickListener {
	private static final String TAG = CameraFragment.class.getSimpleName();
	
	private TextureView mTextureView;
	private ImageView mImageView;
	private SurfaceTexture mSurfaceTexture;
	private Surface mSurface;
	private RelativeLayout camera_texture_view_wrappter;
	private RelativeLayout camera_image_view_wrappter;
	private Button btn_image_mode1;
	private Button btn_image_mode2;
	private Button btn_image_mode3;
	private Button btn_image_mode4;
	
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	
	
	
	// opencv variables
	private Mat tmp;
	private Mat mIntermediateMat, mIntermediateMat2;
	private Mat thresholdImage;
	private Mat circles;
	private Point pt;
	
	public enum IMAGEMODE{
		CANNY, HOUGHCIRCLES, REDHOUGHCIRCLES, REDTHRESHOLD
	}
	
	private IMAGEMODE mode;
	
	public void setImageMode(IMAGEMODE mode) {
		this.mode = mode;
	}
	
	public IMAGEMODE getImageMode() {
		return mode;
	}
	
	public CameraFragment() {
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	view = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);
    	mTextureView = (TextureView) view.findViewById(R.id.camera_texture_view);
    	mTextureView.setSurfaceTextureListener(this);
    	mImageView = (ImageView) view.findViewById(R.id.camera_image_view);
    	camera_texture_view_wrappter = (RelativeLayout) view.findViewById(R.id.camera_texture_view_wrappter);
    	camera_image_view_wrappter = (RelativeLayout) view.findViewById(R.id.camera_image_view_wrappter);
    	btn_image_mode1 = (Button) view.findViewById(R.id.btn_image_mode1);
    	btn_image_mode1.setOnClickListener(this);
    	btn_image_mode2 = (Button) view.findViewById(R.id.btn_image_mode2);
    	btn_image_mode2.setOnClickListener(this);
    	btn_image_mode3 = (Button) view.findViewById(R.id.btn_image_mode3);
    	btn_image_mode3.setOnClickListener(this);
    	btn_image_mode4 = (Button) view.findViewById(R.id.btn_image_mode4);
    	btn_image_mode4.setOnClickListener(this);
    	slideUpFragment();
    	
    	return view;
    }
    
    public Surface getSurface() {
    	return mSurface;
    }
    
    public TextureView getTextureView() {
    	return mTextureView;
    }
    
    public Bitmap getProcessedImage() {
    	mImageView.buildDrawingCache();
    	Bitmap bmp = Bitmap.createBitmap(mImageView.getDrawingCache());
    	mImageView.destroyDrawingCache();
    	return bmp;
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
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		mSurfaceTexture = surface;
    	mSurface = new Surface(mSurfaceTexture);
    	mSurfaceWidth = width;
    	mSurfaceHeight = height;
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		Log.d(TAG, "surface width: " + width + " | height: " + height);
		tmp = new Mat(width, height, CvType.CV_8UC1);
		mIntermediateMat = new Mat(width, height, CvType.CV_8UC4);
		mIntermediateMat2 = new Mat(width, height, CvType.CV_8UC1);
		thresholdImage = new Mat(width, height, CvType.CV_8UC1);
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		//Log.d(TAG, surface.toString());
		Bitmap video = mTextureView.getBitmap();
		if (mode.equals(IMAGEMODE.CANNY)) {
			Utils.bitmapToMat(video, tmp);
			
			Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
		    Imgproc.Canny(tmp, mIntermediateMat, 80, 100);
		    Imgproc.cvtColor(mIntermediateMat, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
		    Utils.matToBitmap(tmp, video);
		} else if (mode.equals(IMAGEMODE.HOUGHCIRCLES)) {
			Utils.bitmapToMat(video, tmp);
			Imgproc.cvtColor(tmp, thresholdImage, Imgproc.COLOR_RGB2GRAY, 2);
            Imgproc.GaussianBlur(thresholdImage, thresholdImage, new Size(9, 9), 2, 2);
            
            Mat circles = new Mat();
			
            int iCannyUpperThreshold = 65;
            int iMinRadius = 5;
            int iMaxRadius = 400;
            int iAccumulator = 200;
            int iLineThickness = 3;
            
            Imgproc.HoughCircles(thresholdImage, circles, Imgproc.CV_HOUGH_GRADIENT, 
            		2.0, thresholdImage.rows() / 4, iCannyUpperThreshold, iAccumulator, 
            		iMinRadius, iMaxRadius);
            
            if (circles.cols() > 0){
            	for(int i=0; i<circles.cols(); i++){
            		double vCircle[] = circles.get(0,i);
            		if (vCircle == null)	break;

					pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
					int radius = (int)Math.round(vCircle[2]);
					
					// draw the found circle
					Core.circle(tmp, pt, radius, new Scalar(255,0,0), iLineThickness);
					Core.circle(tmp, pt, 3, new Scalar(255,0,0), iLineThickness);
            	}
            }
            Utils.matToBitmap(tmp, video);
			
		} else if (mode.equals(IMAGEMODE.REDHOUGHCIRCLES)) {
			Utils.bitmapToMat(video, tmp);
			Imgproc.cvtColor(tmp, mIntermediateMat2, Imgproc.COLOR_RGB2HSV, 4);
			
			Core.inRange(mIntermediateMat2, new Scalar(-2, 80, 80), new Scalar(3, 255, 255), thresholdImage); // for blue color
            Imgproc.GaussianBlur(thresholdImage, thresholdImage, new Size(9, 9), 2, 2);
            
            Mat circles = new Mat();
			
            int iCannyUpperThreshold = 55;
            int iMinRadius = 3;
            int iMaxRadius = 400;
            int iAccumulator = 68;
            int iLineThickness = 3;
            
            Imgproc.HoughCircles(thresholdImage, circles, Imgproc.CV_HOUGH_GRADIENT, 
            		2.0, thresholdImage.rows() / 4, iCannyUpperThreshold, iAccumulator, 
            		iMinRadius, iMaxRadius);
            
            
            
            if (circles.cols() > 0){
            	for(int i=0; i<circles.cols(); i++){
            		double vCircle[] = circles.get(0,i);
            		if (vCircle == null)	break;

					pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
					int radius = (int)Math.round(vCircle[2]);
					
					// draw the found circle
					Core.circle(tmp, pt, radius, new Scalar(255,0,0), iLineThickness);
					Core.circle(tmp, pt, 3, new Scalar(255,0,0), iLineThickness);
            	}
            }
            //Imgproc.cvtColor(thresholdImage, tmp, Imgproc.COLOR_GRAY2BGR, 0);
			Utils.matToBitmap(tmp, video);
		} else if (mode.equals(IMAGEMODE.REDTHRESHOLD)) {
			Utils.bitmapToMat(video, tmp);
			Imgproc.cvtColor(tmp, mIntermediateMat2, Imgproc.COLOR_RGB2HSV, 4);
			
			Core.inRange(mIntermediateMat2, new Scalar(-2, 80, 80), new Scalar(3, 255, 255), thresholdImage); // for blue color
            Imgproc.GaussianBlur(thresholdImage, thresholdImage, new Size(9, 9), 2, 2);
            
            Mat circles = new Mat();
			
            int iCannyUpperThreshold = 55;
            int iMinRadius = 3;
            int iMaxRadius = 400;
            int iAccumulator = 68;
            int iLineThickness = 3;
            
            Imgproc.HoughCircles(thresholdImage, circles, Imgproc.CV_HOUGH_GRADIENT, 
            		2.0, thresholdImage.rows() / 4, iCannyUpperThreshold, iAccumulator, 
            		iMinRadius, iMaxRadius);
            
            
            
            if (circles.cols() > 0){
            	for(int i=0; i<circles.cols(); i++){
            		double vCircle[] = circles.get(0,i);
            		if (vCircle == null)	break;

					pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
					int radius = (int)Math.round(vCircle[2]);
					
					// draw the found circle
					Core.circle(thresholdImage, pt, radius, new Scalar(255,0,0), iLineThickness);
					Core.circle(thresholdImage, pt, 3, new Scalar(255,0,0), iLineThickness);
            	}
            }
            Imgproc.cvtColor(thresholdImage, tmp, Imgproc.COLOR_GRAY2BGR, 0);
			Utils.matToBitmap(tmp, video);
		}
		
	    
		mImageView.setImageBitmap(video);
	}
	
	public void setFullScreen() {
		ViewGroup.LayoutParams params = camera_texture_view_wrappter.getLayoutParams();
		params.width = 660;
		params.height = 380;
		camera_texture_view_wrappter.requestLayout();
	}
	
	public void setSmallScreen() {
		ViewGroup.LayoutParams params = camera_texture_view_wrappter.getLayoutParams();
		params.width = 340;
		params.height = 200;
		camera_texture_view_wrappter.requestLayout();
	}
	
	public void setFrameColor(String color) {
		ViewGroup.LayoutParams params = camera_texture_view_wrappter.getLayoutParams();
		camera_texture_view_wrappter.setBackgroundColor(Color.parseColor(color));
		camera_image_view_wrappter.setBackgroundColor(Color.parseColor(color));
	}

	@Override
	public void onClick(View v) {
		Drawable icon_pressed= getActivity().getResources().getDrawable(R.drawable.btn_play_pressed);
		icon_pressed.setBounds(0, 0, 53, 53);
		Drawable icon_unpressed= getActivity().getResources().getDrawable(R.drawable.btn_play_unpressed);
		icon_unpressed.setBounds(0, 0, 53, 53);
		switch (v.getId()) {
		case R.id.btn_image_mode1:
			mode = IMAGEMODE.CANNY;
			btn_image_mode1.setCompoundDrawables(icon_pressed, null, null, null);
			btn_image_mode2.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode3.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode4.setCompoundDrawables(icon_unpressed, null, null, null);
			break;
		case R.id.btn_image_mode2:
			mode = IMAGEMODE.HOUGHCIRCLES;
			btn_image_mode1.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode2.setCompoundDrawables(icon_pressed, null, null, null);
			btn_image_mode3.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode4.setCompoundDrawables(icon_unpressed, null, null, null);
			break;
		case R.id.btn_image_mode3:
			mode = IMAGEMODE.REDHOUGHCIRCLES;
			btn_image_mode1.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode2.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode3.setCompoundDrawables(icon_pressed, null, null, null);
			btn_image_mode4.setCompoundDrawables(icon_unpressed, null, null, null);
			break;
		case R.id.btn_image_mode4:
			mode = IMAGEMODE.REDTHRESHOLD;
			btn_image_mode1.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode2.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode3.setCompoundDrawables(icon_unpressed, null, null, null);
			btn_image_mode4.setCompoundDrawables(icon_pressed, null, null, null);
			break;
		}
	}
}
