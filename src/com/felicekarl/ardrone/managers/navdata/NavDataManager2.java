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

import java.net.InetAddress;

import com.felicekarl.ardrone.ARDroneConstants;
import com.felicekarl.ardrone.managers.command.CommandManager;
import com.felicekarl.ardrone.managers.navdata.listeners.AttitudeListener;
import com.felicekarl.ardrone.managers.navdata.listeners.BatteryListener;
import com.felicekarl.ardrone.managers.navdata.listeners.GpsListener;
import com.felicekarl.ardrone.managers.navdata.listeners.MagnetoListener;
import com.felicekarl.ardrone.managers.navdata.listeners.StateListener;
import com.felicekarl.ardrone.managers.navdata.listeners.VelocityListener;

import android.util.Log;

public class NavDataManager2 extends NavDataManager {

	public NavDataManager2(InetAddress inetaddr, CommandManager manager) {
		super(inetaddr, manager);
	}

	@Override
	protected void initializeDrone() {
		Log.d("Karl", "initializeDrone()");
		ticklePort(ARDroneConstants.NAV_PORT);
		//manager.disableBootStrap();
		//manager.enableDemoData();
		//manager.setDefaultDemoOptions();
		//ticklePort(ARDroneConstants.NAV_PORT);
		//manager.sendControlAck();
	}

	
	
	
	
}
