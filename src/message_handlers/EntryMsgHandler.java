package message_handlers;
import core.*;
import java.net.*;

public class EntryMsgHandler extends MsgHandler {
	private NodeList nodeList;
	private Node self;
	private Socket connectionSocket;
	
	public EntryMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
		this.nodeList = nodeList;
		this.self = self;
		this.connectionSocket = connectionSocket;
	}
	@Override
	public void handle() {

	}
}
