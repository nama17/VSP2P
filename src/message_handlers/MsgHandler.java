package message_handlers;

import java.net.Socket;

import core.Node;
import core.NodeList;
import java.io.*;
import util.*;
import java.nio.*;

public abstract class MsgHandler {
	abstract void handle() throws IOException;

	// copyPASTA!
	protected String readIp(InputStream in) throws IOException {
		byte[] ip = new byte[4];
		int readBytes = readBytes(in, ip);
		if (readBytes == 4) {
			String ipString = byteArrToIp(ip);
			if (ipString.equals("0.0.0.0")) {
				return null;
			}
			if (AddressValidator.validateIP(ipString)) {
				return ipString;
			}
			return "";
		}
		return "";
	}

	protected int readPort(InputStream in) throws IOException {
		byte[] port = new byte[2];
		int readBytes = readBytes(in, port);
		if (readBytes == 2) {
			ByteBuffer buffer = ByteBuffer.wrap(port);
			int portInt = buffer.getShort() & 0xFFFF;
			if (portInt <= 65535 && (portInt >= 1024 || portInt == 0)) {
				return portInt;
			}
			return -1;
		}
		return -1;
	}

	protected String byteArrToIp(byte[] arr) {
		String ip = "";
		for (int i = 0; i < 4; i++) {
			ip += arr[i] & 0xFF;
			if (i < 3) {
				ip += ".";
			}
		}
		return ip;
	}

	protected byte[] ipToByteArr(String ip) {
		byte[] arr = new byte[4];
		String[] blocks = ip.split("\\.");
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (byte) Integer.parseInt(blocks[i]);
		}
		return arr;
	}

	protected byte[] portToByteArr(int port) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort((short) port);
		return buffer.array();
	}

	private int readBytes(InputStream in, byte[] arr) throws IOException {
		int readBytes = 0;
		while (readBytes < arr.length) {
			int bytes = in.read(arr, readBytes, arr.length - readBytes);
			if (bytes == -1) {
				readBytes = bytes;
				break;
			}
			readBytes += bytes;
		}
		return readBytes;
	}
}
