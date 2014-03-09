package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.*;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ContentActionBarFragment extends BaseFragment implements OnClickListener,
		UpdateContentActionBarFragmentButtonListener {
	private static final String TAG = ContentActionBarFragment.class.getSimpleName();
	
	private Button btn_back;
	private Button btn_debug;
	private Button btn_toggle_video;
	private Button btn_takeoff;
	private Button btn_test;
	private Button btn_emergency;
	private Button btn_takephoto;
	
	private ContentActionBarFragmentButtonListener mContentActionBarFragmentButtonListener;
	
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
			if (mContentActionBarFragmentButtonListener != null)
				mContentActionBarFragmentButtonListener.backToMenu();
			break;
		case R.id.btn_debug:
			//mDebugSlideButtonListener.slide();
			break;
		case R.id.btn_toggle_video:
			//mCameraOnButtonListener.toggle();
			break;
		case R.id.btn_takeoff:
			//mDroneTakeOffButtonListener.toggle();
			break;
		case R.id.btn_test:
			
			break;
		case R.id.btn_emergency:
			if (mContentActionBarFragmentButtonListener != null)
				mContentActionBarFragmentButtonListener.emergency();
			break;
		case R.id.btn_takephoto:
			if (mContentActionBarFragmentButtonListener != null)
				mContentActionBarFragmentButtonListener.takePhoto();
			break;
		}
	}
	
	public boolean isFlying() {
		return isFlying;
	}
	
	public void setIsEmergency(boolean isEmergency) {
		Log.d(TAG, "change text");
		if(isEmergency) {
			getActivity().runOnUiThread(new Runnable(){
    			@Override
    			public void run() {
    				Log.d(TAG, "change text1");
    				btn_emergency.setText("Calibrating");
    				btn_emergency.setTextColor(Color.parseColor("#008a00"));
    				btn_emergency.setBackgroundResource(R.drawable.btn_calibration_unpressed);
    			}
        	});
			
		} else {
			getActivity().runOnUiThread(new Runnable(){
    			@Override
    			public void run() {
    				Log.d(TAG, "change text2");
    				btn_emergency.setText("Emergency");
    				btn_emergency.setTextColor(Color.parseColor("#e51400"));
    				btn_emergency.setBackgroundResource(R.drawable.btn_emergency_unpressed);
    			}
        	});
		}
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
	public void resetFragment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateContentActionBarFragmentButtonListener(
			ContentActionBarFragmentButtonListener mContentActionBarFragmentButtonListener) {
		this.mContentActionBarFragmentButtonListener = mContentActionBarFragmentButtonListener;
	}
	
}
