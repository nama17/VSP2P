package message_handler;
import java.io.*;
import java.net.*;
import java.util.Random;

import core.*;
import message.*;

public class P2PNodeRequestMsgHandler extends MsgHandler{
    
	public P2PNodeRequestMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try {
			InputStream in = connectionSocket.getInputStream();
			P2PNodeRequestMsg p2preq = new P2PNodeRequestMsg();
			p2preq.read(in);
            if (nodeList.nodes.size() < 4 || new Random().nextInt(10) < 1) {
                Node node = p2preq.node;
                if (node.ip != null && (node.ip.equals(self.ip) || node.port != self.port)) {                   
                    nodeList.addNode(node);
                }
            }
			P2PNodeResponseMsg p2presp = new P2PNodeResponseMsg(nodeList);
			OutputStream out = connectionSocket.getOutputStream();
			out.write(p2presp.create());
		} catch (IOException e){
			System.out.println(e);
		}
		
	}

}