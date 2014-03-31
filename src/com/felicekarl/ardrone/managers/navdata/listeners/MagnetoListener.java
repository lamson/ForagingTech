package com.felicekarl.ardrone.managers.navdata.listeners;

public interface MagnetoListener {

	void updateMagData(short mx, short my, short mz, float raw_x, float raw_y,
			float raw_z, float rectified_x, float rectified_y,
			float rectified_z, float offset_x, float offset_y, float offset_z,
			float head_unwrapped, float head_gyroUnwrapped,
			float head_fusionUnwrapped);

}
