package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PNodeResponseMsg extends Message {
    public NodeList nodeList;

    public P2PNodeResponseMsg() {}
    public P2PNodeResponseMsg(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    public void read(InputStream in) {
        try {
            in.read();
            for(int i = 0; i < 4; i++){
                nodeList.addNode(readIp(in), readPort(in), readId(in));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        byte[] data = new byte[2];
        data[0] = 4;
        data[1] = 1;
        data = ArrayHelper.merge(data, nodeList.nodes.get(0).toByteArr());
        data = ArrayHelper.merge(data, nodeList.nodes.get(1).toByteArr());
        data = ArrayHelper.merge(data, nodeList.nodes.get(2).toByteArr());
        data = ArrayHelper.merge(data, nodeList.nodes.get(3).toByteArr());
        return data;
    }
}