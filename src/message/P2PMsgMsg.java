package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PMsgMsg extends Message {
    public Node node;
    public short sourceId;
    public short lengthMsg;
    public String msg;

    public P2PMsgMsg() {}
    public P2PMsgMsg(Node node, short sourceId, String msg) {
        this.node = node;
        this.sourceId = sourceId;
        this.msg = msg;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
            sourceId = (short) readId(in);
            lengthMsg = readUnsignedShort(in);
            byte[] msgBytes = new byte[lengthMsg];
            in.read(msgBytes);
            msg = new String(msgBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
            byte[] data = new byte[2];
            data[0] = 8;
            data[1] = 1;
            data = ArrayHelper.merge(data, node.toByteArr());
            data = ArrayHelper.merge(data, shortToByteArr(sourceId));
            data = ArrayHelper.merge(data, shortToByteArr(msg.getBytes().length));
            data = ArrayHelper.merge(data, msg.getBytes());
            return data;
    }
}