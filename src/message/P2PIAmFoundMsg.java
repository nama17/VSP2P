package message;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
import core.*;
import util.*;

public class P2PIAmFoundMsg extends Message {
    public Node node;
    public int sourceId;
    public int searchId;
    public int destinationId;

    public P2PIAmFoundMsg(Node node, int sourceId, int searchId, int destinationId) {
        this.node = node;
        this.sourceId = sourceId;
        this.searchId = searchId;
        this.destinationId = destinationId;
    }

    public void read(InputStream in) {
        try {
            node = new Node(readIp(in), readPort(in), readId(in));
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
            buffer.putInt(sourceId);
            buffer.putInt(searchId);
            buffer.putInt(destinationId);
            return ArrayHelper.merge(ArrayHelper.merge(data, node.toByteArr()), searchData);
        } else {
            return null;
        }
    }
}