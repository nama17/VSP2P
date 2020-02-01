package message_handler;

import java.net.Socket;
import java.util.Random;

import core.Node;
import core.NodeList;

import java.io.IOException;
import java.io.InputStream;
import message.*;

public class P2PTellMeYourTimeMsgHandler extends MsgHandler {

    public P2PTellMeYourTimeMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
    public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            P2PIamLeaderMsg iamlead = new P2PIamLeaderMsg();
            iamlead.read(in);
            if (nodeList.nodes.size() < 4 || new Random().nextInt(10) < 1) {
                Node node = iamlead.node;
                if (node.ip != null && (!node.ip.equals(self.ip) || node.port != self.port)) {
                    nodeList.addNode(node);
                }
            }
            System.out.println("Leader ist " + iamlead.node.ip + ":" + iamlead.node.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
