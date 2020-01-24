package message_handler;

import core.*;
import java.net.*;
import java.io.*;
import message.*;

public class EntryResponseMsgHandler extends MsgHandler {

	public EntryResponseMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try {
			InputStream in = connectionSocket.getInputStream();
			EntryResponseMsg erm = new EntryResponseMsg();
			erm.read(in);
			for (int i = 0; i < 4; i++) {
			    Node node = erm.nodeList.nodes.get(i);
			    if (node.ip != null && (!node.ip.equals(self.ip) || node.port != self.port)) {
			        nodeList.addNode(node);
			    }
			}
			self.id = erm.id;
			System.out.println("Client: Id " + self.id + " wurde vom Server zugewiesen");
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
