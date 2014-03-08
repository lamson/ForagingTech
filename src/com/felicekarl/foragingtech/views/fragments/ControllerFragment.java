package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.activities.MainActivity;
import com.felicekarl.foragingtech.listeners.JoyStickListener;
import com.felicekarl.foragingtech.listeners.UpdateJoyStickListener;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ControllerFragment extends BaseFragment implements UpdateJoyStickListener {
	private static final String TAG = ControllerFragment.class.getSimpleName();
	private View controller_left_joystick;
	private View controller_right_joystick;
	private ImageView left_joystick;
	private ImageView right_joystick;
	
	private JoyStickListener mJoyStickListener;
	
	public ControllerFragment() {
		
	}
	
	public static ControllerFragment create() {
		return new ControllerFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = (ViewGroup) inflater.inflate(R.layout.fragment_controller, container, false);
		controller_left_joystick = view.findViewById(R.id.controller_left_joystick);
		controller_left_joystick.setOnTouchListener(new LeftJoystickListener());
		controller_right_joystick = view.findViewById(R.id.controller_right_joystick);
		controller_right_joystick.setOnTouchListener(new RightJoystickListener());
		left_joystick = (ImageView) view.findViewById(R.id.left_joystick);
		right_joystick = (ImageView) view.findViewById(R.id.right_joystick);
		left_joystick.setVisibility(View.INVISIBLE);
		right_joystick.setVisibility(View.INVISIBLE);
		
		slideUpFragment();
		return view;
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

	@Override
	protected void enableEditText() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void disableEditText() {
		// TODO Auto-generated method stub
		
	}
	
	private class LeftJoystickListener implements OnTouchListener {
		// Delay Timer
		private long lastPressProcessed = 0;
		private static final long SENSOR_DELAY = 5;		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			//if (MainActivity.VERBOSE) Log.d(TAG, "Left Action = " + action);
		    //if (MainActivity.VERBOSE) Log.d(TAG, "Left X = " + event.getX() + "Left Y = " + event.getY());
			
		    switch(action & MotionEvent.ACTION_MASK){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				if(System.currentTimeMillis() - lastPressProcessed > SENSOR_DELAY) {
					lastPressProcessed = System.currentTimeMillis();
					if (MainActivity.VERBOSE)	Log.d(TAG, "ACTION_DOWN || ACTION_MOVE");
					float width = controller_left_joystick.getWidth();
					float height = controller_left_joystick.getHeight();
					float centerX = width / 2;
					float centerY = height / 2;
					float x = event.getX();
					float y = event.getY();
					float radius;
					if(centerX >= centerY){
						radius = (float) (centerY * 0.9);
					}else{
						radius = (float) (centerX * 0.9);
					}
					float speedXRate = Math.abs(Math.abs(x - centerX) / radius) * 100;
					float speedYRate = Math.abs(Math.abs(y - centerY) / radius) * 100;
					if(speedXRate >= 100) speedXRate = 100;
					if(speedYRate >= 100) speedYRate = 100;
					
					if (Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) < Math.pow(radius, 2)) {
						// update joystick image position
						RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) left_joystick.getLayoutParams();
	                    mParams.leftMargin = (int) (x - width/2);
	                    mParams.topMargin = (int) (y - height/2);
	                    left_joystick.setLayoutParams(mParams);
	                    left_joystick.setVisibility(View.VISIBLE);
	                    
						if( Math.abs(y - centerY) >= Math.abs(x - centerX)){
							if( (y - centerY) >=0 )	{
								// backward
								if (MainActivity.VERBOSE) Log.d(TAG, "L-speedYRate: " + speedYRate);
								mJoyStickListener.setSpeedY(-speedYRate);
							}
							else {
								// forward
								if (MainActivity.VERBOSE) Log.d(TAG, "L-speedYRate: " + speedYRate);
								mJoyStickListener.setSpeedY(speedYRate);
							}
						}else{
							if( (x - centerX) >= 0) {
								// right
								if (MainActivity.VERBOSE) Log.d(TAG, "L-speedXRate: " + speedXRate);
								mJoyStickListener.setSpeedX(speedXRate);
							}
							else {
								// left
								if (MainActivity.VERBOSE) Log.d(TAG, "L-speedXRate: " + speedXRate);
								mJoyStickListener.setSpeedX(-speedXRate);
							}
						}
					} else {
						// stop
						//if (MainActivity.VERBOSE) Log.d(TAG, "L-stop");
					}
					return true;
				}
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (MainActivity.VERBOSE) Log.d(TAG, "ACTION_UP || ACTION_POINTER_UP");
				// stop
				if (MainActivity.VERBOSE) Log.d(TAG, "L-stop");
				mJoyStickListener.setSpeedX(0);
				mJoyStickListener.setSpeedY(0);
				left_joystick.setVisibility(View.INVISIBLE);
				return false;
			}
			return false;
		}
	}
	
	private class RightJoystickListener implements OnTouchListener {
		// Delay Timer
		private long lastPressProcessed = 0;
		private static final long SENSOR_DELAY = 5;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			//if (MainActivity.VERBOSE) Log.d(TAG, "Left Action = " + action);
		    //if (MainActivity.VERBOSE) Log.d(TAG, "Left X = " + event.getX() + "Left Y = " + event.getY());
			
		    switch(action & MotionEvent.ACTION_MASK){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				if(System.currentTimeMillis() - lastPressProcessed > SENSOR_DELAY) {
					lastPressProcessed = System.currentTimeMillis();
					if (MainActivity.VERBOSE)	Log.d(TAG, "ACTION_DOWN || ACTION_MOVE");
					float x = event.getX();
					float y = event.getY();
					float width = controller_right_joystick.getWidth();
					float height = controller_right_joystick.getHeight();
					float centerX = width / 2;
					float centerY = height / 2;
					float radius;
					if(centerX >= centerY){
						radius = (float) (centerY * 0.9);
					}else{
						radius = (float) (centerX * 0.9);
					}
					float speedXRate = Math.abs(Math.abs(x - centerX) / radius) * 100;
					float speedYRate = Math.abs(Math.abs(y - centerY) / radius) * 100;
					if(speedXRate >= 100) speedXRate = 100;
					if(speedYRate >= 100) speedYRate = 100;
					
					if (Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) < Math.pow(radius, 2)) {
						// update joystick image position
						RelativeLayout.LayoutParams mRightParams = (RelativeLayout.LayoutParams) right_joystick.getLayoutParams();
						mRightParams.leftMargin = (int) (x - width/2);
						mRightParams.topMargin = (int) (y - height/2);
	                    right_joystick.setLayoutParams(mRightParams);
	                    right_joystick.setVisibility(View.VISIBLE);
						if( Math.abs(y - centerY) >= Math.abs(x - centerX)){
							if( (y - centerY) >=0 )	{
								// backward
								if (MainActivity.VERBOSE) Log.d(TAG, "R-speedYRate: " + speedYRate);
								mJoyStickListener.setSpeedZ(-speedYRate);
							}
							else {
								// forward
								if (MainActivity.VERBOSE) Log.d(TAG, "R-speedYRate: " + speedYRate);
								mJoyStickListener.setSpeedZ(speedYRate);
							}
						}else{
							if( (x - centerX) >= 0) {
								// right
								if (MainActivity.VERBOSE) Log.d(TAG, "R-speedXRate: " + speedXRate);
								mJoyStickListener.setSpeedSpin(speedXRate);
							}
							else {
								// left
								if (MainActivity.VERBOSE) Log.d(TAG, "R-speedXRate: " + speedXRate);
								mJoyStickListener.setSpeedSpin(-speedXRate);
							}
						}
					} else {
						// stop
						//if (MainActivity.VERBOSE) Log.d(TAG, "R-stop");
					}
					return true;
				}
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (MainActivity.VERBOSE) Log.d(TAG, "ACTION_UP || ACTION_POINTER_UP");
				// stop
				if (MainActivity.VERBOSE) Log.d(TAG, "R-stop");
				mJoyStickListener.setSpeedZ(0);
				mJoyStickListener.setSpeedSpin(0);
				right_joystick.setVisibility(View.INVISIBLE);
				return false;
			}
			return false;
		}
	}

	@Override
	public void updateJoyStickListener(JoyStickListener mJoyStickListener) {
		this.mJoyStickListener = mJoyStickListener;
	}
	
	private class LeftJoyStickCanvasListener implements SurfaceTextureListener {

		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
				int width, int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface) {
			// TODO Auto-generated method stub
			
		}
		
	}
}