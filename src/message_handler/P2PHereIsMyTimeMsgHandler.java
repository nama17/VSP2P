package message_handler;

import core.*;
import message.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class P2PHereIsMyTimeMsgHandler extends MsgHandler {

    public P2PHereIsMyTimeMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
    public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            P2PHereIsMyTimeMsg p2pmytime = new P2PHereIsMyTimeMsg();
            p2pmytime.read(in);
            System.out.println("This is unintended behaviour. P2PHereIsMyTimeMsgHandler");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}