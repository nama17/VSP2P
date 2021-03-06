package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PAreYouAliveMsg extends Message {
    public Node node;

    public P2PAreYouAliveMsg() {}
    public P2PAreYouAliveMsg(Node node) {
        this.node = node;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 9;
        data[1] = 1;
        return ArrayHelper.merge(data, node.toByteArr());
    }
}