package message_handlers;

import java.net.Socket;
import core.Node;
import core.NodeList;
import java.net.*;

public class P2PIamLeaderMsgHandler extends MsgHandler{
	private NodeList nodeList;
	private Node self;
	private Socket connectionSocket;
	
	public P2PIamLeaderMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
		this.nodeList = nodeList;
		this.self = self;
		this.connectionSocket = connectionSocket;
	}
	@Override
	public void handle() {
		
	}
}
