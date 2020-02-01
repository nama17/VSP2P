package message_handler;

import core.*;
import message.IAmAliveMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Random;
import message.*;

public class P2PHereIsYourNewTimeMsgHandler extends MsgHandler {

    public P2PHereIsYourNewTimeMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
    public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            P2PHereIsYourNewTimeMsg timeMsg = new P2PHereIsYourNewTimeMsg();
            timeMsg.read(in);
            System.out.println("Meine zugewiesene Zeit ist" + timeMsg.time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}