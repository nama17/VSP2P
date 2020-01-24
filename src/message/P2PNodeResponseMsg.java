package message;

import java.io.IOException;
import java.io.InputStream;
import core.*;
import util.*;

public class P2PNodeResponseMsg extends Message {
    public NodeList nodeList;
 
    public P2PNodeResponseMsg(){}
    public P2PNodeResponseMsg(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    public void read(InputStream in) {
        try {
            in.read();
            for(int i = 0; i<4; i++){
                nodeList.addNode(readIp(in), readPort(in), readId(in));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        // nodelist should always be of size 4
        if (nodeList != null) {
            byte[] data = new byte[2];
            data[0] = 4;
            data[1] = 1;
            return ArrayHelper.merge(data,ArrayHelper.merge(ArrayHelper.merge(nodeList.nodes.get(1).toByteArr(), nodeList.nodes.get(2).toByteArr()),
            ArrayHelper.merge(nodeList.nodes.get(3).toByteArr(), nodeList.nodes.get(4).toByteArr())));
        } else {
            return null;
        }
    }
}