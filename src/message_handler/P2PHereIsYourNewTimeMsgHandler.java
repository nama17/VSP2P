package message_handler;

import core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Date;

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
            Date date = new Date();
            date.setTime(timeMsg.time);
            System.out.println("Client: Neue Zeit wurde zugewiesen: " + date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}