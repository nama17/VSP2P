package message_handlers;

import java.net.Socket;

import core.Node;
import core.NodeList;
import java.net.*;

public class P2PMsgMsgHandler extends MsgHandler{
	private NodeList nodeList;
	private Node self;
	private Socket connectionSocket;
	
	public P2PMsgMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
		this.nodeList = nodeList;
		this.self = self;
		this.connectionSocket = connectionSocket;
	}
	@Override
	public void handle() {

	}
}
