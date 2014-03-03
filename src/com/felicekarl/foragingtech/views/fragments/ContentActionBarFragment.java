package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.*;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ContentActionBarFragment extends BaseFragment implements OnClickListener, 
		UpdateFlipBackwardButtonListener, UpdateTakePhotoListener, UpdateDroneCommandListener {
	private static final String TAG = ContentActionBarFragment.class.getSimpleName();
	
	private Button btn_back;
	private Button btn_debug;
	private Button btn_toggle_video;
	private Button btn_takeoff;
	private Button btn_test;
	private Button btn_emergency;
	private Button btn_takephoto;
	
	private FlipBackwardButtonListener mFlipBackwardButtonListener;
	private TakePhotoListener mTakePhotoListener;
	private DroneCommandListener mDroneCommandListener;
	
	private boolean isFlying;
	
	public ContentActionBarFragment() {
		isFlying = false;
	}
	
	public static ContentActionBarFragment create() {
		ContentActionBarFragment fragment = new ContentActionBarFragment();
        return fragment;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	view = (ViewGroup) inflater.inflate(R.layout.fragment_content_actionbar, container, false);
    	
    	btn_back = (Button) view.findViewById(R.id.btn_back);
    	btn_back.setOnClickListener(this);
    	btn_debug = (Button) view.findViewById(R.id.btn_debug);
    	btn_debug.setOnClickListener(this);
    	btn_toggle_video = (Button) view.findViewById(R.id.btn_toggle_video);
    	btn_toggle_video.setOnClickListener(this);
    	btn_takeoff = (Button) view.findViewById(R.id.btn_takeoff);
    	btn_takeoff.setOnClickListener(this);
    	btn_test = (Button) view.findViewById(R.id.btn_test);
    	btn_test.setOnClickListener(this);
    	btn_emergency = (Button) view.findViewById(R.id.btn_emergency);
    	btn_emergency.setOnClickListener(this);
    	btn_takephoto = (Button) view.findViewById(R.id.btn_takephoto);
    	btn_takephoto.setOnClickListener(this);
    	
    	return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			Log.d(TAG, "btn_back");
			if (mFlipBackwardButtonListener != null)	mFlipBackwardButtonListener.flip();
			break;
		case R.id.btn_debug:
			//mDebugSlideButtonListener.slide();
			break;
		case R.id.btn_toggle_video:
			//mCameraOnButtonListener.toggle();
			break;
		case R.id.btn_takeoff:
			//mDroneTakeOffButtonListener.toggle();
			if (mDroneCommandListener != null)	mDroneCommandListener.takeOffLand();
			break;
		case R.id.btn_test:
			if (mTakePhotoListener != null)	mTakePhotoListener.saveImage();
//			if (!isNavigating) {
//				mMagnetoCalibrationListener.start();
//				isNavigating = true;
//				Log.d(TAG, "Navigating Start");
//			} else {
//				mMagnetoCalibrationListener.stop();
//				isNavigating = false;
//				Log.d(TAG, "Navigating Stop");
//			}
			
			break;
		case R.id.btn_emergency:
			//mDroneResetListener.resetDrone();
			break;
		case R.id.btn_takephoto:
			if (mTakePhotoListener != null)	mTakePhotoListener.takePhoto();
			break;
		}
	}
	
	public boolean isFlying() {
		return isFlying;
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
	public void updateFlipBackwardButtonListener(FlipBackwardButtonListener mFlipBackwardButtonListener) {
		this.mFlipBackwardButtonListener = mFlipBackwardButtonListener;
	}

	@Override
	public void updateTakePhotoListener(TakePhotoListener mTakePhotoListener) {
		this.mTakePhotoListener = mTakePhotoListener;
	}

	@Override
	public void updateDroneCommandListener(DroneCommandListener mDroneCommandListener) {
		this.mDroneCommandListener = mDroneCommandListener;
	}
	
}
