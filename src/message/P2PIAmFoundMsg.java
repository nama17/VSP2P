package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PIAmFoundMsg extends Message {
    public Node node;
    public short sourceId;
    public short searchId;

    public P2PIAmFoundMsg() {}
    public P2PIAmFoundMsg(Node node, short sourceId, short searchId) {
        this.node = node;
        this.sourceId = sourceId;
        this.searchId = searchId;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
            sourceId = (short) readId(in);
            searchId = readUnsignedShort(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 7;
        data[1] = 1;
        data = ArrayHelper.merge(data, node.toByteArr());
        data = ArrayHelper.merge(data, shortToByteArr(sourceId));
        data = ArrayHelper.merge(data, shortToByteArr(searchId));
        return data;
    }
}