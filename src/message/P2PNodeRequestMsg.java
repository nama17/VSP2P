package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PNodeRequestMsg extends Message {
    public Node node;

    public P2PNodeRequestMsg() {}
    public P2PNodeRequestMsg(Node node) {
        this.node = node;
    }

    public void read(InputStream in) {
        try {
            in.read();
            this.node = new Node(readIp(in), readPort(in), readId(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 3;
        data[1] = 1;
        return ArrayHelper.merge(data, node.toByteArr());
    }
}