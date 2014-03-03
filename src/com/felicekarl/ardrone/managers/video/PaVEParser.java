package com.felicekarl.ardrone.managers.video;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

import com.felicekarl.foragingtech.util.UnsignedInt;

/**
 * The AR Drone 2.0 allows a tcp client to receive H264 (MPEG4.10 AVC) video
 * from the drone. However, the frames are wrapped by Parrot Video
 * Encapsulation (PaVE), which this class parses.
 */

public class PaVEParser {
	public enum PState{
		HEADER, PAYLOAD
	}
	
	private static final String TAG = PaVEParser.class.getSimpleName();
	private PState state;
	
	
	public String signature;
	public int version;
	public int video_codec;
	public int header_size;
	public long payload_size;
	public int encoded_stream_width;
	public int encoded_stream_height;
	public int display_width;
	public int display_height;
	public long frame_number;
	public long timestamp;
	public int total_chunks;
	public int chunk_index;
	public int frame_type;
	public int control;
	public long stream_byte_position_lw;
	public long stream_byte_position_uw;
	public int stream_id;
	public int total_slices;
	public int slice_index;
	public int header1_size;
	public int header2_size;
	public byte[] reserved2;
	public long advertised_size;
	public byte[] reserved3;
	
	public PaVEParser() {
		state = PState.HEADER;
		reserved2 = new byte[2];
		reserved3 = new byte[12];
		
	}
	
	public boolean parseVideo(ByteBuffer buffer){
		if(buffer.capacity() >= 64){
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append((char) buffer.get());
			strBuilder.append((char) buffer.get());
			strBuilder.append((char) buffer.get());
			strBuilder.append((char) buffer.get());
			signature = strBuilder.toString();
			
			version = UnsignedInt.getUInt8(buffer);
			video_codec = UnsignedInt.getUInt8(buffer);
			header_size = UnsignedInt.getUInt16(buffer);
			payload_size = UnsignedInt.getUInt32(buffer);
			encoded_stream_width = UnsignedInt.getUInt16(buffer);
			encoded_stream_height = UnsignedInt.getUInt16(buffer);
			display_width = UnsignedInt.getUInt16(buffer);
			display_height = UnsignedInt.getUInt16(buffer);
			frame_number = UnsignedInt.getUInt32(buffer);
			timestamp = UnsignedInt.getUInt32(buffer);
			total_chunks = UnsignedInt.getUInt8(buffer);
			chunk_index = UnsignedInt.getUInt8(buffer);
			frame_type = UnsignedInt.getUInt8(buffer);
			control = UnsignedInt.getUInt8(buffer);
			stream_byte_position_lw = UnsignedInt.getUInt32(buffer);
			stream_byte_position_uw = UnsignedInt.getUInt32(buffer);
			stream_id = UnsignedInt.getUInt16(buffer);
			total_slices = UnsignedInt.getUInt8(buffer);
			slice_index = UnsignedInt.getUInt8(buffer);
			header1_size = UnsignedInt.getUInt8(buffer);
			header2_size = UnsignedInt.getUInt8(buffer);
			for(int i=0; i<reserved2.length; i++) {
				reserved2[i] = buffer.get();
			}
			advertised_size = UnsignedInt.getUInt32(buffer);
			for(int i=0; i<reserved3.length; i++) {
				reserved3[i] = buffer.get();
			}
			UnsignedInt.getUInt32(buffer);
			UnsignedInt.getUInt32(buffer);
			UnsignedInt.getUInt32(buffer);
			/*
			Log.d(TAG, signature + "|" + version + "|" + video_codec + "|" + header_size
					+ "|" + payload_size + "|" + encoded_stream_width + "|" + encoded_stream_height
					+ "|" + display_width + "|" + display_height + "|" + frame_number + "|" + timestamp
					+ "||" + total_chunks + "|" + chunk_index + "|" + frame_type + "|" + control
					+ "||" + stream_byte_position_lw + "|" + stream_byte_position_uw + "|" + stream_id
					+ "||" + total_slices + "|" + slice_index + "|||" + header1_size + "|" + header2_size
					+ "||" + advertised_size);
			*/
			if (signature.equals("PaVE"))	return true;
		}
		return false;
		
	}
}
