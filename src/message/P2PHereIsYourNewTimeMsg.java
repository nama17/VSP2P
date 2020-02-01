package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PHereIsYourNewTimeMsg extends Message {
    public Node node;
    public long time;

    public P2PHereIsYourNewTimeMsg() {
    }

    public P2PHereIsYourNewTimeMsg(Node node, long time) {
        this.node = node;
        this.time = time;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
            time = byteArrToTime(readTime(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 13;
        data[1] = 1;
        return ArrayHelper.merge(ArrayHelper.merge(data, node.toByteArr()), timeToByteArr(time));
    }
}