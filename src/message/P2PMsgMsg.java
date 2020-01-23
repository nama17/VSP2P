package message;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
import core.*;
import util.*;

public class P2PMsgMsg extends Message {
    public Node node;
    public short sourceId;
    public short lengthMsg;
    public String msg;

    public P2PMsgMsg(){}
    public P2PMsgMsg(Node node, short sourceId, short lengthMsg, String msg) {
        this.node = node;
        this.sourceId = sourceId;
        this.lengthMsg = lengthMsg;
        this.msg = msg;
    }

    public void read(InputStream in) {
        try {
            in.read();
            byte[] searchData = new byte[4];
            node = new Node(readIp(in), readPort(in), readId(in));
            searchData = in.readNBytes(4);
            ByteBuffer buffer = ByteBuffer.wrap(searchData);
            sourceId = buffer.getShort();
            lengthMsg = buffer.getShort();
            msg = new String(in.readNBytes(lengthMsg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        // nodelist should always be of size 4
        if (node != null) {
            byte[] data = new byte[2];
            data[0] = 8;
            data[1] = 1;
            byte[] searchData = new byte[2];
            ByteBuffer buffer = ByteBuffer.wrap(searchData);
            buffer.putShort(sourceId);
            byte[] msgData = new byte[2];
            buffer = ByteBuffer.wrap(msgData);
            buffer.putShort(lengthMsg);
            return ArrayHelper.merge(ArrayHelper.merge(ArrayHelper.merge(data, node.toByteArr()), searchData), ArrayHelper.merge(msgData, msg.getBytes()));
        } else {
            return null;
        }
    }
}