package message_handler;

import java.net.*;
import core.*;
import java.io.*;
import message.*;

public class P2PNodeResponseMsgHandler extends MsgHandler{
    
	public P2PNodeResponseMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try {
    		InputStream in = connectionSocket.getInputStream();
    		P2PNodeResponseMsg p2presp = new P2PNodeResponseMsg();
    		p2presp.read(in);
    		nodeList.addNode(p2presp.nodeList.nodes.get(0));
    		nodeList.addNode(p2presp.nodeList.nodes.get(1));
    		nodeList.addNode(p2presp.nodeList.nodes.get(2));
    		nodeList.addNode(p2presp.nodeList.nodes.get(3));
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}