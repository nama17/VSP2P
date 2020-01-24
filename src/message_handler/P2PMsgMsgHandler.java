package message_handler;

import java.net.Socket;
import java.util.Random;

import core.*;
import java.io.*;
import message.*;

public class P2PMsgMsgHandler extends MsgHandler{
    
	public P2PMsgMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            P2PMsgMsg msgmsg = new P2PMsgMsg();
            msgmsg.read(in);
            if (nodeList.nodes.size() < 4 || new Random().nextInt(10) < 1) {
                Node node = msgmsg.node;
                if (node.ip != null && (!node.ip.equals(self.ip) || node.port != self.port)) {                   
                    nodeList.addNode(node);
                }
            }
            System.out.println("Client: Nachricht erhalten von " + msgmsg.node.ip + ":" + msgmsg.node.port + "'" + msgmsg.msg + "'");    
        } catch (IOException e){
            e.printStackTrace();
        }
	}
}
