import core.*;
import message.IAmAliveMsg;
import message.P2PAreYouAliveMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Random;

public class P2PHereIsMyTimeMsgHandler extends MsgHandler {

    public P2PHereIsMyTimeMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
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