package message_handler;

import java.net.*;
import core.Node;
import core.NodeList;

public class P2PNodeSearchMsgHandler extends MsgHandler{
    
	public P2PNodeSearchMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		
	}
}
