package com.felicekarl.foragingtech.util;

import java.nio.ByteBuffer;

public class UnsignedInt {
	
	public static int getUInt8(ByteBuffer bp) {
		return getUInt8(bp, 0);
	}
	
	public static int getUInt8(ByteBuffer bp, int start) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put(bp.get());
		bb.flip();
		return bb.getInt();
	}
	
	public static int getUInt16(ByteBuffer bp) {
		return getUInt16(bp, 0);
	}
	
	public static int getUInt16(ByteBuffer bp, int start) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		byte[] temp = new byte[2];
		temp[1] = bp.get();
		temp[0] = bp.get();
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put(temp[0]);
		bb.put(temp[1]);
		bb.flip();
		return bb.getInt();
	}
	
	public static long getUInt32(ByteBuffer bp) {
		return getUInt32(bp, 0);
	}
	
	public static long getUInt32(ByteBuffer bp, int start) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		byte[] temp = new byte[4];
		temp[3] = bp.get();
		temp[2] = bp.get();
		temp[1] = bp.get();
		temp[0] = bp.get();
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put((byte) 0);
		bb.put(temp[0]);
		bb.put(temp[1]);
		bb.put(temp[2]);
		bb.put(temp[3]);
		bb.flip();
		return bb.getLong();
	}
	
	
	
}
