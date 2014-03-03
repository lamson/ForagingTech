/*
 * Copyright 2010 Cliff L. Biffle.  All Rights Reserved.
 * Use of this source code is governed by a BSD-style license that can be found
 * in the LICENSE file.
 */
package com.felicekarl.ardrone.managers.navdata;

public class DroneState {
	private final int bits;

	public DroneState(int _bits) {
		bits = _bits;
	}

	public String toString() {
		//return "DroneState(" + Integer.toHexString(bits) + ")";
		return Integer.toHexString(bits);
	}

	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass())
			return false;
		return bits == ((DroneState) o).bits;
	}

	public int hashCode() {
		return 31 * bits;
	}
	
	public boolean isTagOn(int tag) {
		if( (bits & tag) > 0 ) {
			return true;
		}
		return false;
	}
}