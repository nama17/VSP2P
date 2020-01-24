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
            for (int i = 0; i < 4; i++) {
                Node node = p2presp.nodeList.nodes.get(i);
                if (node.ip != null && (!node.ip.equals(self.ip) || node.port != self.port)) {                   
                    nodeList.addNode(node);
                }
            }
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}