package com.felicekarl.ardrone.managers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class AbstractManager implements Runnable {

	protected InetAddress inetaddr;
	protected DatagramSocket socket;

	public boolean connect(int port) {
		try {
			socket = new DatagramSocket(port);
			socket.setSoTimeout(3000);
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void close() {
		Thread.currentThread().interrupt();
		socket.close();
	}

	protected void ticklePort(int port) {
		byte[] buf = { 0x01, 0x00, 0x00, 0x00 };
		DatagramPacket packet = new DatagramPacket(buf, buf.length, inetaddr,
				port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
