package com.felicekarl.ardrone.managers.navdata.listeners;

public interface GpsListener {
	//void gpsLocChanged(double lat, double lon, double elevation);

	void gpsLocChanged(double lat, double lon, double elevation, double hdop,
			int data_available, double lat0, double lon0, double lat_fuse,
			double lon_fuse, long gps_state, double vdop, double pdop,
			float speed, long last_frame_timestamp, float degree,
			float degree_mag);
}
