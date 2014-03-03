/**
   ARDroneForP5
   https://github.com/shigeodayo/ARDroneForP5
   Copyright (C) 2013, Shigeo YOSHIDA.
   Copyright (C) 2014, Karl Kim.

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
package com.felicekarl.ardrone.managers.video;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.felicekarl.ardrone.managers.command.CommandManager;

public class VideoManager2 extends VideoManager {
	private static final String TAG = VideoManager2.class.getSimpleName();
	private boolean isSurfaceOn = false;
	
	// using TCP
	private Socket socket;
	private InputStream fin;
	private MediaCodec decoder;
	private Surface mSurface;
	
	public static final int INBUF_SIZE = 65535;
	
	public VideoManager2(InetAddress inetaddr, CommandManager commandManager) {
		super(inetaddr, commandManager);
	}
	
	public VideoManager2(InetAddress inetaddr, CommandManager commandManager, Surface mSurface) {
		super(inetaddr, commandManager);
		this.mSurface = mSurface;
	}

	@Override
	public void run() {
		Log.d(TAG, "run()");
		setVideoPort();
		try {
			decode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void decode() throws IOException {
		Log.d(TAG, "decode()");
		int decodeCount = 0;
		socket.setSoTimeout(0);
		fin = socket.getInputStream();
		String mimeType = "video/avc";
		decoder = MediaCodec.createDecoderByType(mimeType);
		MediaFormat format = MediaFormat.createVideoFormat(mimeType, 640, 368);
		
		byte[] header_sps = { 0, 0, 0, 1, 103, 66, -128, 30, -117, 104, 10, 2, -9, -107 };
		byte[] header_pps = { 0, 0, 0, 1, 104, -50, 1, -88, 119, 32 };
		
		format.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
		format.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
		decoder.configure(format, mSurface, null, 0);
		isSurfaceOn = true;
		decoder.start();
		
		ByteBuffer[] inputBuffers = decoder.getInputBuffers();
		ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
		BufferInfo info = new BufferInfo();
		boolean isEOS = false;
		long startMs = System.currentTimeMillis();
		
		
		
		PaVEParser parser = new PaVEParser();
		boolean isFirstPacket = true;
    	while(!Thread.currentThread().isInterrupted() && isSurfaceOn) {
    		ByteBuffer mHeader = ByteBuffer.allocate(76);
    		while(mHeader.position() < 76) {
    			byte[] extra = new byte[mHeader.remaining()];
    			int extraSize = fin.read(extra);
    			mHeader.put(extra, 0, extraSize);
    		}
    		
    		mHeader.rewind();
        	parser.parseVideo(mHeader);
        	
        	ByteBuffer mBuffer = ByteBuffer.allocate((int) parser.payload_size);
        	//Log.d(TAG, "parser.payload_size: " + parser.payload_size);
    		while(mBuffer.position() < (int) parser.payload_size){
        		byte[] extra = new byte[mBuffer.remaining()];
        		int extraSize = fin.read(extra);
        		mBuffer.put(extra, 0, extraSize);
        	}
        	
        	
        	
        	mBuffer.rewind();
        	
        	
        	if(!isFirstPacket) {
        		int inIndex = decoder.dequeueInputBuffer(1000);
        		//Log.d(TAG, "inIndex: " + inIndex);
        		if (inIndex >= 0) {
        			ByteBuffer buffer = inputBuffers[inIndex];
        			buffer.clear();
        			buffer.put(mBuffer);
        			decoder.queueInputBuffer(inIndex, 0, mBuffer.capacity(), 0, 0);
        		}
        		int outIndex = decoder.dequeueOutputBuffer(info, 1000);
        		//Log.d(TAG, "outIndex: " + outIndex);
        		switch (outIndex) {
        	    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
        	        Log.d(TAG, "INFO_OUTPUT_BUFFERS_CHANGED");
        	        outputBuffers = decoder.getOutputBuffers();
        	        break;
        	    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
        	        Log.d(TAG, "New format " + decoder.getOutputFormat());
        	        break;
        	    case MediaCodec.INFO_TRY_AGAIN_LATER:
        	        Log.d(TAG, "dequeueOutputBuffer timed out! " + info);
        	        break;
        	    default:
        	        ByteBuffer buffer = outputBuffers[outIndex];
        	        //Log.v(TAG, "We can't use this buffer but render it due to the API limit, " + buffer);
        	        boolean doRender = true;
        	        decoder.releaseOutputBuffer(outIndex, doRender);
//        	        if (doRender) {
//        	        	outputSurface.awaitNewImage();
//                        outputSurface.drawImage(true);
//                        outputSurface.saveFrame(Environment.getExternalStorageDirectory().getAbsolutePath() + "/map/test.png");
//                        decodeCount++;
//                        Log.d(TAG, "decodeCount: " + decodeCount);
//        	        } else {
//        	        	Log.d(TAG, "doRender == false");
//        	        }
        	        
        	        break;
        	    }

        	    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
        	        Log.d("DecodeActivity", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
        	        break;
        	    }
        	}
        	
        	isFirstPacket = false;
    	}
    	Log.i(TAG, "decoder stop");
		decoder.stop();
		decoder.release();
		decoder = null;
		
		
	};
	
	@Override
	public boolean connect(int port) {
		try {
			socket = new Socket(inetaddr, port);
			socket.setSoTimeout(3000);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void close() {
		Thread.currentThread().interrupt();
		try {
			if(fin != null)		fin.close();
			if(socket != null)	socket.close();
			if (decoder != null) {
				decoder.release();
				decoder = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void ticklePort(int port) {
		byte[] buf = { 0x01, 0x00, 0x00, 0x00 };
		try {
			OutputStream os = socket.getOutputStream();
			os.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}