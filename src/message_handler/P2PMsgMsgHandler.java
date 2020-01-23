package message_handler;

import java.net.Socket;

import core.Node;
import core.NodeList;
import java.net.*;

public class P2PMsgMsgHandler extends MsgHandler{
    
	public P2PMsgMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {

	}
}
