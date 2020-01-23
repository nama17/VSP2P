package message;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import core.*;
import util.*;

public class IAmAliveMsg extends Message {
    public Node node;

    public IAmAliveMsg(Node node) {
        this.node = node;
    }

    public void read(InputStream in) {
        try {
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
         }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] create() {
        // nodelist should always be of size 4
        if (node != null) {
            byte[] data = new byte[2];
            data[0] = 5;
            data[1] = 1;
            return ArrayHelper.merge(data, node.toByteArr());
        } else {
            return null;
        }
    }
}