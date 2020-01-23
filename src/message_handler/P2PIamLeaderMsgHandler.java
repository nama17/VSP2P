package message_handler;

import java.net.Socket;
import core.Node;
import core.NodeList;
import java.net.*;

public class P2PIamLeaderMsgHandler extends MsgHandler{
    
	public P2PIamLeaderMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		
	}
}
