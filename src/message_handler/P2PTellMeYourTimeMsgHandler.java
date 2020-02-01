package message_handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import core.ConnectionHandler;
import core.Node;
import core.NodeList;
import message.Message;
import message.P2PHereIsMyTimeMsg;
import message.P2PTellMeYourTimeMsg;

public class P2PTellMeYourTimeMsgHandler extends MsgHandler {

    public P2PTellMeYourTimeMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
    public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            OutputStream out = connectionSocket.getOutputStream();
            P2PTellMeYourTimeMsg timeRequest = new P2PTellMeYourTimeMsg();
            timeRequest.read(in);
            long randomTime = ThreadLocalRandom.current().nextLong(System.currentTimeMillis() - 24 * 60 * 60 * 1000, System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            Message myTime = new P2PHereIsMyTimeMsg(self, randomTime);
            out.write(myTime.create());
            ConnectionHandler handler = new ConnectionHandler(connectionSocket, nodeList, self);
            new Thread(handler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
