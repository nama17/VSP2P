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
			nodeList.addNode(erm.nodeList.nodes.get(0));
			nodeList.addNode(erm.nodeList.nodes.get(1));
			nodeList.addNode(erm.nodeList.nodes.get(2));
			nodeList.addNode(erm.nodeList.nodes.get(3));
			self.id = erm.id;
			System.out.println("Client: Id " + self.id + " wurde vom Server zugewiesen");
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
