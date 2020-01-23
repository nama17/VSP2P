package message;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import core.*;
import util.*;

public class EntryResponseMsg extends Message {
    public NodeList nodeList;

    public EntryResponseMsg(NodeList nodeList) {
        this.nodeList = nodeList;
    }

    public void read(InputStream in) {
        try {
            for(int i=0; i < nodeList.nodes.size(); i++){
                nodeList.addNode(readIp(in), readPort(in), readId(in));
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        if (nodeList != null) {
            byte[] data = new byte[2];
            data[0] = 2;
            data[1] = 1;
            return ArrayHelper.merge(data, ArrayHelper.merge(ArrayHelper.merge(nodeList.nodes.get(1).toByteArr(), nodeList.nodes.get(2).toByteArr()),
					ArrayHelper.merge(nodeList.nodes.get(3).toByteArr(), nodeList.nodes.get(4).toByteArr())));
        } else {
            return null;
        }
    }
}