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
package com.felicekarl.ardrone.managers.navdata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import com.felicekarl.ardrone.ARDroneConstants;
import com.felicekarl.ardrone.managers.AbstractManager;
import com.felicekarl.ardrone.managers.command.CommandManager;
import com.felicekarl.ardrone.managers.navdata.listeners.*;

public abstract class NavDataManager extends AbstractManager implements UpdateAttitudeListener,
		UpdateBatteryListener, UpdateGpsListener, UpdateMagnetoListener, UpdateStateListener, UpdateVelocityListener {

	protected CommandManager commandManager;
	
	private AttitudeListener mAttitudeListener;
	private StateListener mStateListener;
	private VelocityListener mVelocityListener;
	private BatteryListener mBatteryListener;
	private GpsListener mGpsListener;
	private MagnetoListener mMagnetoListener;

	public NavDataManager(InetAddress inetaddr, CommandManager commandManager) {
		this.inetaddr = inetaddr;
		this.commandManager = commandManager;
	}

	@Override
	public void run() {
		initializeDrone();
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		NavDataParser parser = new NavDataParser();
		// update parser listener
		parser.updateAttitudeListener(mAttitudeListener);
		parser.updateBatteryListener(mBatteryListener);
		parser.updateGpsListener(mGpsListener);
		parser.updateMagnetoListener(mMagnetoListener);
		parser.updateStateListener(mStateListener);
		parser.updateVelocityListener(mVelocityListener);

		while (!Thread.currentThread().isInterrupted()) {
			try {
				ticklePort(ARDroneConstants.NAV_PORT);
				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024, inetaddr, ARDroneConstants.NAV_PORT);
				socket.receive(packet);
				ByteBuffer buffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
				parser.parseNavData(buffer);
			} catch (IOException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			} catch (NavDataException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void initializeDrone();
	
	@Override
	public void updateAttitudeListener(AttitudeListener mAttitudeListener) {
		this.mAttitudeListener = mAttitudeListener;
	}

	@Override
	public void updateBatteryListener(BatteryListener mBatteryListener) {
		this.mBatteryListener = mBatteryListener;
	}

	@Override
	public void updateGpsListener(GpsListener mGpsListener) {
		this.mGpsListener = mGpsListener;
	}

	@Override
	public void updateMagnetoListener(MagnetoListener mMagnetoListener) {
		this.mMagnetoListener = mMagnetoListener;
	}

	@Override
	public void updateStateListener(StateListener mStateListener) {
		this.mStateListener = mStateListener;
	}

	@Override
	public void updateVelocityListener(VelocityListener mVelocityListener) {
		this.mVelocityListener = mVelocityListener;
	}	
}