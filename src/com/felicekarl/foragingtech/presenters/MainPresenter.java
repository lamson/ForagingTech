package com.felicekarl.foragingtech.presenters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.felicekarl.ardrone.ARDrone;
import com.felicekarl.ardrone.ARDroneConstants;
import com.felicekarl.ardrone.managers.navdata.DroneState;
import com.felicekarl.ardrone.managers.navdata.listeners.AttitudeListener;
import com.felicekarl.ardrone.managers.navdata.listeners.StateListener;
import com.felicekarl.foragingtech.ForagingTechConstraint;
import com.felicekarl.foragingtech.listeners.DroneCommandListener;
import com.felicekarl.foragingtech.listeners.FlipBackwardButtonListener;
import com.felicekarl.foragingtech.listeners.FlipForwardButtonListener;
import com.felicekarl.foragingtech.listeners.TakePhotoListener;
import com.felicekarl.foragingtech.models.IModel;
import com.felicekarl.foragingtech.views.IView;
import com.felicekarl.foragingtech.views.IView.TypeView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class MainPresenter implements Runnable {
	@SuppressWarnings("unused")
	private static final String TAG = MainPresenter.class.getSimpleName();
	
	private static final int SPLASH_TIME = 2000;
	private int timeElapsed = 0;
	
	private Context context;
	private IView view;
	private IModel model;
	private Thread thread;
	
	private ARDrone mARDrone;
	

	public MainPresenter(Context context, IView view, IModel model){
		this.context = context;
		this.view = view;
		this.model = model;
		
		initListeners();
		
		/* splash fragment timer */
		thread = new Thread(this);
        thread.start();
	}
	
	private void initListeners() {
		/* add drone command listener */
		view.updateDroneCommandListener(new DroneCommandListener() {
			@Override
			public void takeOffLand() {
				if (mARDrone.isConnected()) {
					mARDrone.takeOff();
				}
			}
		});
		/* add flip page from menu to content */
		view.updateFlipForwardButtonListener(new FlipForwardButtonListener() {
			@Override
			public void flip(int pageNumber) {
				if (pageNumber == 0) {
					Log.d(TAG, "pageNumber == 0");
					initDrone();
					if(mARDrone.isConnected()) {
						Toast.makeText(context, "Drone is successfully connected.", Toast.LENGTH_SHORT).show();
						view.setView(TypeView.FLYINGMODE);
					} else {
						Toast.makeText(context, "Fail to connect Drone. Check the Wi-Fi again.", Toast.LENGTH_SHORT).show();
					}
				} else if (pageNumber == 1) {
					Log.d(TAG, "pageNumber == 1");
					view.setView(TypeView.NAVIGATINGMODE);
				} else if (pageNumber == 2) {
					
				}
			}
		});
		/* add back button listener to flip page from content to menu */
		view.updateFlipBackwardButtonListener(new FlipBackwardButtonListener() {
			@Override
			public void flip() {
				if(mARDrone.isConnected())	mARDrone.disconnect();
				view.setView(TypeView.MENU);
			}
		});
		/* add photo take listener */
		view.updateTakePhotoListener(new TakePhotoListener() {
			@Override
			public void takePhoto() {
				Log.d(TAG, "takePhoto()");
				BufferedOutputStream bos = null;
				FileOutputStream fos = null;
				try {
					File dir = new File(ForagingTechConstraint.defaultPath);
					if(!dir.isDirectory()){
						dir.mkdirs();
					}
					String filename = ForagingTechConstraint.PT + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
					fos = new FileOutputStream(ForagingTechConstraint.defaultPath + filename + ".jpg");
		        	bos = new BufferedOutputStream(fos);	
					Bitmap bmp = view.getCameraBitmap();
					bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
		            bmp.recycle();
		            if (bos != null) {
		        		bos.close();
		        	}
		        	if (fos != null) {
		        		fos.flush();
						fos.close();
		        	}
		        	Toast.makeText(context, filename + ".jpg" + " is successfully saved.", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(context, "Error occurs while saving Photo.", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}

			@Override
			public void saveImage() {
				Log.d(TAG, "saveImage()");
				BufferedOutputStream bos = null;
				FileOutputStream fos = null;
				try {
					File dir = new File(ForagingTechConstraint.defaultPath);
					if(!dir.isDirectory()){
						dir.mkdirs();
					}
					String filename = ForagingTechConstraint.IG + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
					fos = new FileOutputStream(ForagingTechConstraint.defaultPath + filename + ".jpg");
		        	bos = new BufferedOutputStream(fos);	
					Bitmap bmp = view.getImageBitmap();
					bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
		            bmp.recycle();
		            if (bos != null) {
		        		bos.close();
		        	}
		        	if (fos != null) {
		        		fos.flush();
						fos.close();
		        	}
		        	Toast.makeText(context, filename + ".jpg" + " is successfully saved.", Toast.LENGTH_SHORT).show();
				} catch (IOException e) {
					Toast.makeText(context, "Error occurs while saving Image.", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
	}
	
	
	@Override
	public void run() {
		try {
			while (timeElapsed < SPLASH_TIME) {
				Thread.sleep(100);
				timeElapsed += 100;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			view.setView(TypeView.MENU);
		}
	}
	
	public boolean initDrone() {
		// TODO: Change the thread as Async Thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		/* connect to drone and initialize it. */
		mARDrone = new ARDrone(ARDroneConstants.IP_ADDRESS);
		boolean isConnected = mARDrone.connect();
		if (isConnected) Log.d(TAG, "CommandManager is created.");
		if (!isConnected) return false;
		boolean isConnectedNav = mARDrone.connectNav();
		if (isConnectedNav) Log.d(TAG, "NavDataManager is created.");
		if (!isConnectedNav) return false;
		boolean isConnectedVideo = mARDrone.connectVideo(view.getCameraSurface());
		if (isConnectedVideo) Log.d(TAG, "VideoManager is created.");
		if (!isConnectedVideo) return false;
		
		
		/* Add Drone State Listener */
		mARDrone.updateStateListener(new StateListener() {
			@Override
			public void stateChanged(DroneState state) {
				if(state.isTagOn(ARDroneConstants.FLY_MASK)) {
					//Log.d(TAG, "isFlying");
					//mGLView.setIsFlying(true);
//					if (mActionBarFragment != null) {
//						mActionBarFragment.setIsFlying(true);
//						isFlying = true;
//					}
				} else {
					//mGLView.setIsFlying(false);
					//Log.d(TAG, "isNotFlying");
//					if (mActionBarFragment != null) {
//						mActionBarFragment.setIsFlying(false);
//						isFlying = false;
//					}
				}
				
				if(state.isTagOn(ARDroneConstants.NAVDATA_DEMO)) {
					//Log.d(TAG, "ARDroneConstants.NAVDATA_DEMO");
				}
				
				if(state.isTagOn(ARDroneConstants.NAVDATA_BOOTSTRAP)) {
					//Log.d(TAG, "ARDroneConstants.NAVDATA_DEMO");
				}
				
				if(state.isTagOn(ARDroneConstants.MAGNETOMETER)) {
					Log.d(TAG, "MAGNETO CALIBRATION IS NEEDED");
				}
			}
		});
		/* Add Drone Attitude Listener */
		mARDrone.updateAttitudeListener(new AttitudeListener() {
			@Override
			public void attitudeUpdated(float pitch, float roll, float yaw, int altitude) {
				
			}
		});
		
		mARDrone.start();
		Log.d(TAG, "ARDrone is started.");
		return true;
	}

}
