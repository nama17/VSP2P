package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PHereIsMyTimeMsg extends Message {
    public Node node;
    public byte[] time = new byte[8];

    public P2PHereIsMyTimeMsg() {
    }

    public P2PHereIsMyTimeMsg(Node node, byte[] time) {
        this.node = node;
        this.time = time;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
            time = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 12;
        data[1] = 1;
        return ArrayHelper.merge(data, node.toByteArr());
    }
}