/*
 * Copyright 2010 Cliff L. Biffle.  All Rights Reserved.
 * Use of this source code is governed by a BSD-style license that can be found
 * in the LICENSE file.
 */

package com.felicekarl.ardrone.managers.navdata;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.felicekarl.ardrone.managers.navdata.listeners.*;

import android.util.Log;

public class NavDataParser implements UpdateAttitudeListener,
UpdateBatteryListener, UpdateGpsListener, UpdateMagnetoListener, UpdateStateListener, UpdateVelocityListener {
	private static final String TAG = NavDataParser.class.getSimpleName();
	
	private AttitudeListener mAttitudeListener;
	private StateListener mStateListener;
	private VelocityListener mVelocityListener;
	private BatteryListener mBatteryListener;
	private GpsListener mGpsListener;
	private MagnetoListener mMagnetoListener;

	long lastSequenceNumber = 1;

	public void parseNavData(ByteBuffer buffer) throws NavDataException {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int magic = buffer.getInt();
		// System.out.printf("%02x\n", magic);
		requireEquals("Magic must be correct", 0x55667788, magic);

		int state = buffer.getInt();
		//Log.d(TAG, "state: " + state);
		long sequence = buffer.getInt() & 0xFFFFFFFFL;
		@SuppressWarnings("unused")
		int vision = buffer.getInt();

		if (sequence <= lastSequenceNumber && sequence != 1) {
			return;
		}
		lastSequenceNumber = sequence;

		if (mStateListener != null) {
			mStateListener.stateChanged(new DroneState(state));
		}

		while (buffer.position() < buffer.limit()) {
			int tag = buffer.getShort() & 0xFFFF;
			int payloadSize = (buffer.getShort() & 0xFFFF) - 4;
			//Log.d(TAG, "tag: " + tag + " | payloadSize: " + payloadSize);
			ByteBuffer optionData = buffer.slice().order(
					ByteOrder.LITTLE_ENDIAN);
			optionData.limit(payloadSize);
			
			buffer.position(buffer.position() + payloadSize);
			
			dispatch(tag, optionData);
		}
	}

	private void dispatch(int tag, ByteBuffer optionData) {
		
		switch (tag) {
		case 0:		// DEMO
			processNavDataDemo(optionData);
			break;
		case 22:	// MAGNETO
			processNavDataMagneto(optionData);
			break;
		case 27:	// GPS 
			processNavDataGPS(optionData);
			break;
		}
	}
	
	private void processNavDataMagneto(ByteBuffer optionData) {
		short mx = optionData.getShort();
		short my = optionData.getShort();
		short mz = optionData.getShort();
		
		float raw_x = optionData.getFloat();
		float raw_y = optionData.getFloat();
		float raw_z = optionData.getFloat();
		
		float rectified_x = optionData.getFloat();
		float rectified_y = optionData.getFloat();
		float rectified_z = optionData.getFloat();
		
		float offset_x = optionData.getFloat();
		float offset_y = optionData.getFloat();
		float offset_z = optionData.getFloat();
		
		float head_unwrapped = optionData.getFloat();
		float head_gyroUnwrapped = optionData.getFloat();
		float head_fusionUnwrapped = optionData.getFloat();
		
		char ok = optionData.getChar();
		
		if (mMagnetoListener != null) {
			mMagnetoListener.updateMagData(mx, my, mz, raw_x, raw_y, raw_z, rectified_x, rectified_y, rectified_z,
					offset_x, offset_y, offset_z, head_unwrapped, head_gyroUnwrapped, head_fusionUnwrapped);
		}
		//Log.d("Karl", "mx: " + mx + "| my: " + my + "| mz: " + mz);
		//Log.d("Karl", "head_unwrapped: " + head_unwrapped + "| head_gyroUnwrapped: " + head_gyroUnwrapped + "| head_fusionUnwrapped: " + head_fusionUnwrapped);
		//Log.d("Karl", "ok: " + ok);
		
	}
	
	private void processNavDataGPS(ByteBuffer optionData) {
		double lat = optionData.getDouble();
		double lon = optionData.getDouble();
		double elevation = optionData.getDouble();
		double hdop = optionData.getDouble();
		int data_available = optionData.getInt();
		byte[] unk_0 = new byte[8];						//TODO: unsignedint
		for (int i=0; i<unk_0.length; i++) {
			unk_0[i] = optionData.get();
		}
		double lat0 = optionData.getDouble();
		double lon0 = optionData.getDouble();
		double lat_fuse = optionData.getDouble();
		double lon_fuse = optionData.getDouble();
		long gps_state = optionData.getInt() & 0xFFFFFFFF;
		byte[] unk_1 = new byte[40];					//TODO: unsignedint
		for (int i=0; i<unk_1.length; i++) {
			unk_1[i] = optionData.get();
		}
		double vdop = optionData.getDouble();
		double pdop = optionData.getDouble();
		float speed = optionData.getFloat();
		long last_frame_timestamp = optionData.getInt()  & 0xFFFFFFFF;
		float degree = optionData.getFloat();
		float degree_mag = optionData.getFloat();
		
		
		if(mGpsListener != null && lat != 0.0d && lon != 0.0d) {
			mGpsListener.gpsLocChanged(lat, lon, elevation, hdop, data_available, lat0, lon0, lat_fuse
					, lon_fuse, gps_state, vdop, pdop, speed, last_frame_timestamp, degree, degree_mag);
		}
	}

	private void processNavDataDemo(ByteBuffer optionData) {
		@SuppressWarnings("unused")
		int controlState = optionData.getInt();
		int batteryPercentage = optionData.getInt();
		//Log.d(TAG, "batteryPercentage: " + batteryPercentage);

		float theta = optionData.getFloat() / 1000;
		float phi = optionData.getFloat() / 1000;
		float psi = optionData.getFloat() / 1000;

		int altitude = optionData.getInt();

		float vx = optionData.getFloat();
		float vy = optionData.getFloat();
		float vz = optionData.getFloat();

		if (mBatteryListener != null) {
			mBatteryListener.batteryLevelChanged(batteryPercentage);
		}

		if (mAttitudeListener != null) {
			mAttitudeListener.attitudeUpdated(theta, phi, psi, altitude);
			// System.out.println("update in parser");
		}

		if (mVelocityListener != null) {
			mVelocityListener.velocityChanged(vx, vy, vz);
		}
	}

	private void requireEquals(String message, int expected, int actual)
			throws NavDataException {
		if (expected != actual) {
			//throw new NavDataException(message + " : expected " + expected + ", was " + actual);
			Log.d(TAG, message + " : expected " + expected + ", was " + actual);
		}
	}

	@Override
	public void updateVelocityListener(VelocityListener mVelocityListener) {
		this.mVelocityListener = mVelocityListener;
	}

	@Override
	public void updateStateListener(StateListener mStateListener) {
		this.mStateListener = mStateListener;
	}

	@Override
	public void updateMagnetoListener(MagnetoListener mMagnetoListener) {
		this.mMagnetoListener = mMagnetoListener;
	}

	@Override
	public void updateGpsListener(GpsListener mGpsListener) {
		this.mGpsListener = mGpsListener;
	}

	@Override
	public void updateBatteryListener(BatteryListener mBatteryListener) {
		this.mBatteryListener = mBatteryListener;
	}

	@Override
	public void updateAttitudeListener(AttitudeListener mAttitudeListener) {
		this.mAttitudeListener = mAttitudeListener;
	}
}