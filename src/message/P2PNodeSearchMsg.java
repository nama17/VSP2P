package message;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
import core.*;
import util.*;

public class P2PNodeSearchMsg extends Message {
    public Node node;
    public short sourceId;
    public short searchId;
    public short destinationId;

    public P2PNodeSearchMsg(Node node, short sourceId, short searchId, short destinationId) {
        this.node = node;
        this.sourceId = sourceId;
        this.searchId = searchId;
        this.destinationId = destinationId;
    }

    public void read(InputStream in) {
        try {
            in.read();
            byte[] searchData = new byte[6];
            searchData = in.readNBytes(6);
            node = new Node(readIp(in), readPort(in), readId(in));
            ByteBuffer buffer = ByteBuffer.wrap(searchData);
            sourceId = buffer.getShort();            
            searchId = buffer.getShort();
            destinationId = buffer.getShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        // nodelist should always be of size 4
        if (node != null) {
            byte[] data = new byte[2];
            data[0] = 6;
            data[1] = 1;
            byte[] searchData = new byte[6];
            ByteBuffer buffer = ByteBuffer.wrap(searchData);
            buffer.putShort(sourceId);
            buffer.putShort(searchId);
            buffer.putShort(destinationId);
            return ArrayHelper.merge(ArrayHelper.merge(data, node.toByteArr()), searchData);
        } else {
            return null;
        }
    }
}