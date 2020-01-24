package message_handler;

import java.net.Socket;

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
            System.out.println("Client: Nachricht erhalten von " + msgmsg.node.ip + ":" + msgmsg.node.port + "'" + msgmsg.msg + "'");    
        } catch (IOException e){
            e.printStackTrace();
        }
	}
}
