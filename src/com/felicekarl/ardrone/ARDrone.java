/**
   ARDroneForP5
   https://github.com/shigeodayo/ARDroneForP5
   Copyright (C) 2013, Shigeo YOSHIDA.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.felicekarl.ardrone;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;

import com.felicekarl.ardrone.managers.command.*;
import com.felicekarl.ardrone.managers.navdata.*;
import com.felicekarl.ardrone.managers.navdata.listeners.*;
import com.felicekarl.ardrone.managers.video.*;



public class ARDrone implements ARDroneInterface {
	private static final String TAG = ARDrone.class.getSimpleName();
	private String ipaddr;
	private InetAddress inetaddr;
	private ARDroneVersion ardroneVersion;
	
	// managers
	private CommandManager commandManager;
	private VideoManager videoManager;
	private NavDataManager navdataManager;
	
	// listeners
	private AttitudeListener mAttitudeListener;
	private StateListener mStateListener;
	private VelocityListener mVelocityListener;
	private BatteryListener mBatteryListener;
	private GpsListener mGpsListener;
	private MagnetoListener mMagnetoListener;
	
	// status
	private boolean isConnected = false;
	private boolean isFlying = false;

	public ARDrone() {
		this(ARDroneConstants.IP_ADDRESS, null);
	}
	
	public ARDrone(String ipaddr) {
		this(ipaddr, null);
	}
	
	public ARDrone(ARDroneVersion ardroneVersion) {
		this(ARDroneConstants.IP_ADDRESS, ardroneVersion);
	}
	
	public ARDrone(String ipaddr, ARDroneVersion ardroneVersion) {
		this.ipaddr = ipaddr;
		this.ardroneVersion = ardroneVersion;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public boolean isFlying() {
		return isFlying;
	}
	
	public void setIsFlying(boolean isFlying) {
		this.isFlying =  isFlying;
	}
	
	public static void error(String message, Object obj) {
		Log.d(TAG, "[" + obj.getClass().getSimpleName() + "] " + message);
	}
	
	private InetAddress getInetAddress(String ipaddr) {
		InetAddress inetaddr = null;
		StringTokenizer st = new StringTokenizer(ipaddr, ".");
		byte[] ipBytes = new byte[4];
		if (st.countTokens() == 4) {
			for (int i = 0; i < 4; i++) {
				ipBytes[i] = (byte) Integer.parseInt(st.nextToken());
			}
		} else {
			error("Incorrect IP address format: " + ipaddr, this);
			return null;
		}
		try {
			inetaddr = InetAddress.getByAddress(ipBytes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inetaddr;
	}
	
	@Override
	public boolean connect() {
		return connect(false);
	}
	
	private boolean connect(boolean useHighRezVideoStreaming) {
		if (inetaddr == null) {
			inetaddr = getInetAddress(ipaddr);
		}
		if (ardroneVersion == null)
			ardroneVersion = new ARDroneInfo().getDroneVersion();			
		
		Log.d(TAG, "(connect) AR.Drone version:" + ardroneVersion);

		if (ardroneVersion == ARDroneVersion.ARDRONE1) {
			// manager = new CommandManager1(inetaddr, useHighRezVideoStreaming);
		} else if (ardroneVersion == ARDroneVersion.ARDRONE2)
			commandManager = new CommandManager2(inetaddr, useHighRezVideoStreaming);
		else {
			error("Cannot create Control manager", this);
			error("Maybe this is not AR.Drone?", this);
			return false;
		}
		return commandManager.connect(ARDroneConstants.PORT);
	}

	@Override
	public boolean connectVideo() {
		if (inetaddr == null) {
			inetaddr = getInetAddress(ipaddr);
		}
		if (ardroneVersion == ARDroneVersion.ARDRONE1) {
			// videoManager = new VideoManager1(inetaddr, manager);
		} else if (ardroneVersion == ARDroneVersion.ARDRONE2) {
			videoManager = new VideoManager2(inetaddr, commandManager);
		} else {
			error("Cannot create Video manager", this);
			error("Maybe this is not AR.Drone?", this);
			return false;
		}
		return videoManager.connect(ARDroneConstants.VIDEO_PORT);
	}
	
	public boolean connectVideo(Surface mSurface) {
		if (inetaddr == null) {
			inetaddr = getInetAddress(ipaddr);
		}
		if (ardroneVersion == ARDroneVersion.ARDRONE1) {
			// videoManager = new VideoManager1(inetaddr, manager);
		} else if (ardroneVersion == ARDroneVersion.ARDRONE2) {
			videoManager = new VideoManager2(inetaddr, commandManager, mSurface);
		} else {
			error("Cannot create Video manager", this);
			error("Maybe this is not AR.Drone?", this);
			return false;
		}
		return videoManager.connect(ARDroneConstants.VIDEO_PORT);
	}

	@Override
	public boolean connectNav() {
		if (inetaddr == null) {
			inetaddr = getInetAddress(ipaddr);
		}

		if (ardroneVersion == ARDroneVersion.ARDRONE1) {
			// navdataManager = new NavDataManager1(inetaddr, manager);
		} else if (ardroneVersion == ARDroneVersion.ARDRONE2) {
			navdataManager = new NavDataManager2(inetaddr, commandManager);
		} else {
			error("Cannot create NavData manager", this);
			error("Maybe this is not AR.Drone?", this);
			return false;
		}
		
		
		return navdataManager.connect(ARDroneConstants.NAV_PORT);
	}

	@Override
	public void disconnect() {
		stop();
		landing();
		commandManager.close();
		if (videoManager != null)	videoManager.close();
		if (navdataManager != null)	navdataManager.close();
		isConnected = false;
		Log.d(TAG, "ARDrone disconnect()");
	}

	@Override
	public void start() {
		if (commandManager != null)	new Thread(commandManager).start();
		if (videoManager != null)	new Thread(videoManager).start();
		if (navdataManager != null)	new Thread(navdataManager).start();
		isConnected = true;
	}
	
	@Override
	public void stop() {
		if (commandManager != null)	commandManager.stop();
	}

	@Override
	public void setHorizontalCamera() {
		if (commandManager != null)	commandManager.setHorizontalCamera();
	}

	@Override
	public void setVerticalCamera() {
		if (commandManager != null)	commandManager.setVerticalCamera();
	}
	
	@Override
	public void toggleCamera() {
		if (commandManager != null)	commandManager.toggleCamera();
	}

	@Override
	public void landing() {
		if (commandManager != null)	commandManager.landing();
	}

	@Override
	public void takeOff() {
		if (commandManager != null)	commandManager.takeOff();
	}

	@Override
	public void reset() {
		if (commandManager != null)	commandManager.reset();
	}
	
	@Override
	public void move3D(int speedX, int speedY, int speedZ, int speedSpin) {
		if (commandManager != null)
			commandManager.move3D(speedX, speedY, speedZ, speedSpin);
	}

	@Override
	public void updateAttitudeListener(AttitudeListener mAttitudeListener) {
		this.mAttitudeListener = mAttitudeListener;
		if (navdataManager != null)	navdataManager.updateAttitudeListener(mAttitudeListener);
	}

	@Override
	public void updateBatteryListener(BatteryListener mBatteryListener) {
		this.mBatteryListener = mBatteryListener;
		if (navdataManager != null)	navdataManager.updateBatteryListener(mBatteryListener);
	}

	@Override
	public void updateGpsListener(GpsListener mGpsListener) {
		this.mGpsListener = mGpsListener;
		if (navdataManager != null)	navdataManager.updateGpsListener(mGpsListener);
	}

	@Override
	public void updateMagnetoListener(MagnetoListener mMagnetoListener) {
		this.mMagnetoListener = mMagnetoListener;
		if (navdataManager != null)	navdataManager.updateMagnetoListener(mMagnetoListener);
	}

	@Override
	public void updateStateListener(StateListener mStateListener) {
		this.mStateListener = mStateListener;
		if (navdataManager != null)	navdataManager.updateStateListener(mStateListener);
	}

	@Override
	public void updateVelocityListener(VelocityListener mVelocityListener) {
		this.mVelocityListener = mVelocityListener;
		if (navdataManager != null)	navdataManager.updateVelocityListener(mVelocityListener);
	}

}
