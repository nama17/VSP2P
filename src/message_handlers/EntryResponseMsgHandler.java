package message_handlers;
import java.net.Socket;
import core.Node;
import core.NodeList;


public class EntryResponseMsgHandler extends MsgHandler{
	EntryResponseMsgHandler(NodeList nodeList, Node currentClient, Socket connectionSocket) {
		super(nodeList, currentClient, connectionSocket);
	}
	
	@Override
	public void handle(){
		
	}
}
