package message_handlers;

import java.net.*;
import core.Node;
import core.NodeList;

public class P2PNodeSearchMsgHandler extends MsgHandler{
	private NodeList nodeList;
	private Node self;
	private Socket connectionSocket;
	
	public P2PNodeSearchMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
		this.nodeList = nodeList;
		this.self = self;
		this.connectionSocket = connectionSocket;
	}
	@Override
	public void handle() {
		
	}
}
