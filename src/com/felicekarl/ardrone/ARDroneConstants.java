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

public class ARDroneConstants {
	
	/** default IP address */
	public static final String IP_ADDRESS = "192.168.1.1";
	
	/** default PORT */
	public static final int PORT = 5556;
	public static final int VIDEO_PORT = 5555;
	public static final int NAV_PORT = 5554;
	public static final int FTP_PORT = 5551;
	
	/** default ID, for AR.Drone 2.0 */
	public static final String SESSION_ID = "d2e081a3"; 
	public static final String PROFILE_ID = "be27e2e4";
	public static final String APPLICATION_ID = "d87f7e0c";
	
	/** video codec */
	public static final String VIDEO_CODEC_UVLC = "0x20"; // 320x240, 15fps for AR.Drone 1.0
	public static final String VIDEO_CODEC_H264 = "0x40"; // 640x360, 20fps for AR.Drone 1.0
	public static final String VIDEO_CODEC_360P = "0x81"; // 360p, for AR.Drone 2.0
	public static final String VIDEO_CODEC_720P = "0x83"; // 720p, for AR.Drone 2.0
	
	/*
	NULL_CODEC    = 0,
	UVLC_CODEC    = 0x20,       // codec_type value is used for START_CODE
	MJPEG_CODEC,                // not used
	P263_CODEC,                 // not used
	P264_CODEC    = 0x40,
	MP4_360P_CODEC = 0x80,
	H264_360P_CODEC = 0x81,
	MP4_360P_H264_720P_CODEC = 0x82,
	H264_720P_CODEC = 0x83,
	MP4_360P_SLRS_CODEC = 0x84,
	H264_360P_SLRS_CODEC = 0x85,
	H264_720P_SLRS_CODEC = 0x86,
	H264_AUTO_RESIZE_CODEC = 0x87,    // resolution is automatically adjusted according to bitrate
	MP4_360P_H264_360P_CODEC = 0x88,
	*/
	
	
	
	//Define masks for ARDrone state
	//31                                                             0
	//x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x -> state
	//| | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
	//| | | | | | | | | | | | | | | | | | | | | | | | | | | | | | | FLY MASK : (0) ardrone is landed, (1) ardrone is flying
	//| | | | | | | | | | | | | | | | | | | | | | | | | | | | | | VIDEO MASK : (0) video disable, (1) video enable
	//| | | | | | | | | | | | | | | | | | | | | | | | | | | | | VISION MASK : (0) vision disable, (1) vision enable
	//| | | | | | | | | | | | | | | | | | | | | | | | | | | | CONTROL ALGO : (0) euler angles control, (1) angular speed control
	//| | | | | | | | | | | | | | | | | | | | | | | | | | | ALTITUDE CONTROL ALGO : (0) altitude control inactive (1) altitude control active
	//| | | | | | | | | | | | | | | | | | | | | | | | | | USER feedback : Start button state
	//| | | | | | | | | | | | | | | | | | | | | | | | | Control command ACK : (0) None, (1) one received
	//| | | | | | | | | | | | | | | | | | | | | | | | Camera enable : (0) Camera enable, (1) camera disable
	//| | | | | | | | | | | | | | | | | | | | | | | Travelling enable : (0) disable, (1) enable
	//| | | | | | | | | | | | | | | | | | | | | | USB key : (0) usb key not ready, (1) usb key ready
	//| | | | | | | | | | | | | | | | | | | | | Navdata demo : (0) All navdata, (1) only navdata demo
	//| | | | | | | | | | | | | | | | | | | | Navdata bootstrap : (0) options sent in all or demo mode, (1) no navdata options sent
	//| | | | | | | | | | | | | | | | | | | Motors status : (0) Ok, (1) Motors Com is down
	//| | | | | | | | | | | | | | | | | | Communication Lost : (1) com problem, (0) Com is ok
	//| | | | | | | | | | | | | | | | |
	//| | | | | | | | | | | | | | | | VBat low : (1) too low, (0) Ok
	//| | | | | | | | | | | | | | | User Emergency Landing : (1) User EL is ON, (0) User EL is OFF
	//| | | | | | | | | | | | | | Timer elapsed : (1) elapsed, (0) not elapsed
	//| | | | | | | | | | | | | Magnetometer calibration state : (0) Ok, no calibration needed, (1) not ok, calibration needed
	//| | | | | | | | | | | | Angles : (0) Ok, (1) out of range
	//| | | | | | | | | | | WIND MASK: (0) ok, (1) Too much wind
	//| | | | | | | | | | Ultrasonic sensor : (0) Ok, (1) deaf
	//| | | | | | | | | Cutout system detection : (0) Not detected, (1) detected
	//| | | | | | | | PIC Version number OK : (0) a bad version number, (1) version number is OK
	//| | | | | | | ATCodec thread ON : (0) thread OFF (1) thread ON
	//| | | | | | Navdata thread ON : (0) thread OFF (1) thread ON
	//| | | | | Video thread ON : (0) thread OFF (1) thread ON
	//| | | | Acquisition thread ON : (0) thread OFF (1) thread ON
	//| | | CTRL watchdog : (1) delay in control execution (> 5ms), (0) control is well scheduled // Check frequency of control loop
	//| | ADC Watchdog : (1) delay in uart2 dsr (> 5ms), (0) uart2 is good // Check frequency of uart2 dsr (com with adc)
	//| Communication Watchdog : (1) com problem, (0) Com is ok // Check if we have an active connection with a client
	//Emergency landing : (0) no emergency, (1) emergency
	
	public static final int FLY_MASK		 				= Integer.parseInt("00000000000000000000000000000001", 2);
	public static final int VIDEO_MASK		 				= Integer.parseInt("00000000000000000000000000000010", 2);
	public static final int VISION_MASK		 				= Integer.parseInt("00000000000000000000000000000100", 2);
	public static final int CONTROL_ALGO	 				= Integer.parseInt("00000000000000000000000000001000", 2);
	public static final int ALTITUDE_CONTROL_ALGO	 		= Integer.parseInt("00000000000000000000000000010000", 2);
	public static final int USER_FEEDBACK			 		= Integer.parseInt("00000000000000000000000000100000", 2);
	public static final int CONTROL_CMD_ACK			 		= Integer.parseInt("00000000000000000000000001000000", 2);
	public static final int CAMERA_ENABLE			 		= Integer.parseInt("00000000000000000000000010000000", 2);
	public static final int TRAVELLING_ENABLE		 		= Integer.parseInt("00000000000000000000000100000000", 2);
	public static final int USB_KEY					 		= Integer.parseInt("00000000000000000000001000000000", 2);
	public static final int NAVDATA_DEMO			 		= Integer.parseInt("00000000000000000000010000000000", 2);
	public static final int NAVDATA_BOOTSTRAP		 		= Integer.parseInt("00000000000000000000100000000000", 2);
	public static final int MORTOR_STATUS			 		= Integer.parseInt("00000000000000000001000000000000", 2);
	public static final int COMMUNICATION_LOST		 		= Integer.parseInt("00000000000000000010000000000000", 2);
	
	public static final int VBAT_LOW	 					= Integer.parseInt("00000000000000001000000000000000", 2);
	public static final int USER_EMERGENCY_LANDING			= Integer.parseInt("00000000000000010000000000000000", 2);
	public static final int TIME_ELAPSED			 		= Integer.parseInt("00000000000000100000000000000000", 2);
	public static final int MAGNETOMETER					= Integer.parseInt("00000000000001000000000000000000", 2);
	public static final int ANGLES				 			= Integer.parseInt("00000000000010000000000000000000", 2);
	public static final int WIND_MASK		 				= Integer.parseInt("00000000000100000000000000000000", 2);
	public static final int ULTRASONIC_SENSOR	 			= Integer.parseInt("00000000001000000000000000000000", 2);
	public static final int CUTOUT_SYSTEM_DETECTION	 		= Integer.parseInt("00000000010000000000000000000000", 2);
	public static final int PIC_VERSION_NUMBER_OK		 	= Integer.parseInt("00000000100000000000000000000000", 2);
	public static final int ATCODEC_THREAD_ON		 		= Integer.parseInt("00000001000000000000000000000000", 2);
	public static final int NAVDATA_THREAD_ON			 	= Integer.parseInt("00000010000000000000000000000000", 2);
	public static final int VIDEO_THREAD_ON	 				= Integer.parseInt("00000100000000000000000000000000", 2);
	public static final int ACQUISITIONG_THREAD_ON		 	= Integer.parseInt("00001000000000000000000000000000", 2);
	public static final int CTRL_WATCHDOG			 		= Integer.parseInt("00010000000000000000000000000000", 2);
	public static final int ADC_WATCHDOG	 				= Integer.parseInt("00100000000000000000000000000000", 2);
	public static final int COMMUNICATION_WATCHDOG	 		= Integer.parseInt("01000000000000000000000000000000", 2);
	//public static final int EMERGENCY_LANDING		 		= Integer.parseInt("10000000000000000000000000000000", 2);

	
	/*
	 * NAVDATA_DEMO_TAG(0), NAVDATA_TIME_TAG(1), NAVDATA_RAW_MEASURES_TAG(2), NAVDATA_PHYS_MEASURES_TAG(3), NAVDATA_GYROS_OFFSETS_TAG(
                4), NAVDATA_EULER_ANGLES_TAG(5), NAVDATA_REFERENCES_TAG(6), NAVDATA_TRIMS_TAG(7), NAVDATA_RC_REFERENCES_TAG(
                8), NAVDATA_PWM_TAG(9), NAVDATA_ALTITUDE_TAG(10), NAVDATA_VISION_RAW_TAG(11), NAVDATA_VISION_OF_TAG(12), NAVDATA_VISION_TAG(
                13), NAVDATA_VISION_PERF_TAG(14), NAVDATA_TRACKERS_SEND_TAG(15), NAVDATA_VISION_DETECT_TAG(16), NAVDATA_WATCHDOG_TAG(
                17), NAVDATA_ADC_DATA_FRAME_TAG(18), NAVDATA_VIDEO_STREAM_TAG(19), NAVDATA_CKS_TAG(0xFFFF);
	 */
	
	public static final int NAVDATA_DEMO_TAG				= 0;
	public static final int NAVDATA_MAGNETO_TAG				= 22;
	public static final int ZIMMU_3000						= 27;
}
