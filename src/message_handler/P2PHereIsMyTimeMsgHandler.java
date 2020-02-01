package message_handler;

import core.*;
import message.IAmAliveMsg;
import message.*;
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
            P2PHereIsMyTimeMsg p2pmytime = new P2PHereIsMyTimeMsg();
            p2pmytime.read(in);
            System.out.println("This is unintended behaviour");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}