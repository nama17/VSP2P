package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PNodeSearchMsg extends Message {
    public Node node;
    public short searchId;
    public short destinationId;

    public P2PNodeSearchMsg() {}
    public P2PNodeSearchMsg(Node node, short searchId, short destinationId) {
        this.node = node;
        this.searchId = searchId;
        this.destinationId = destinationId;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
            searchId = readUnsignedShort(in);
            destinationId = readUnsignedShort(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 6;
        data[1] = 1;
        data = ArrayHelper.merge(data, node.toByteArr());
        data = ArrayHelper.merge(data, shortToByteArr(searchId));
        data = ArrayHelper.merge(data, shortToByteArr(destinationId));
        return data;
    }
}