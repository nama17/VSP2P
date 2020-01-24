package message_handler;

import java.net.Socket;
import core.Node;
import core.NodeList;

import java.io.IOException;
import java.io.InputStream;
import message.*;

public class P2PIamLeaderMsgHandler extends MsgHandler{
    
	public P2PIamLeaderMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            P2PIamLeaderMsg iamlead = new P2PIamLeaderMsg();
            iamlead.read(in);
            System.out.print("Leader ist " + iamlead.node.ip + ":" + iamlead.node.port);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
