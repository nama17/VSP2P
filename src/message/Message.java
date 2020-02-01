package message;

import java.io.InputStream;
import java.io.*;
import java.nio.ByteBuffer;
import util.*;

public abstract class Message {

    public abstract byte[] create();

    public abstract void read(InputStream in);

    protected short readUnsignedShort(InputStream in) throws IOException {
        byte[] num = new byte[2];
        readBytes(in, num);
        ByteBuffer buffer = ByteBuffer.wrap(num);
        short res = (short) (buffer.getShort() & 0xFFFF);
        return res;
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

    protected int readId(InputStream in) throws IOException {
        byte[] id = new byte[2];
        int readBytes = readBytes(in, id);
        if (readBytes == 2) {
            ByteBuffer buffer = ByteBuffer.wrap(id);
            int idInt = buffer.getShort();
            if (idInt <= 25) {
                return idInt;
            }
            return -1;
        }
        return -1;
    }

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

    protected long readTime(InputStream in) throws IOException {
        byte[] time = new byte[8];
        int readBytes = readBytes(in, time);
        if (readBytes == 8) {
            ByteBuffer buffer = ByteBuffer.wrap(time);
            return buffer.getLong();
        }
        return 0;
    }

    protected byte[] timeToByteArr(long time) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putLong((long) time);
        return buffer.array();
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

    protected byte[] ipToByteArr(String ip) {
        byte[] arr = new byte[4];
        String[] blocks = ip.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) Integer.parseInt(blocks[i]);
        }
        return arr;
    }

    protected byte[] shortToByteArr(int port) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short) port);
        return buffer.array();
    }
}