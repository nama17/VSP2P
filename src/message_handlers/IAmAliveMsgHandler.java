package message_handlers;

import java.net.Socket;

import core.CommandMonitor;
import core.Node;
import core.NodeDataList;
import core.NodeList;
import java.io.*;
import java.net.*;
import java.util.*;

public class IAmAliveMsgHandler extends MsgHandler {
	
	public IAmAliveMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try{
			InputStream in = connectionSocket.getInputStream();
			in.read(); // Version
			entryMsg(in);		
		}
		catch (IOException e){
			System.out.println(e);
		}
	}
	
	private void entryMsg(InputStream in) throws IOException {
		String ip = readIp(in);
		int port = readPort(in);
		int id = readId(in);

		if (ip == null || port == 0) {
			return;
		}
		if (ip.length() == 0 || port == -1) {
			System.out.println("Zugangsserver: Ungueltige IP oder Port erhalten");
			return;
		}
		if (self != null && id > self.id) { // Check if running in Client and ID > own ID
	        CommandMonitor.foundHigherId = true;
		}
		Node node = nodeList.getNode(id);
		// synchronized
		if (node != null) {
			node.updateTime();
			System.out.println("Zugangsserver: Node timeout zurueckgesetzt:");
			node.print();
			return;
		}
		synchronized (nodeList) {
			nodeList.addNode(ip, port, id);
			System.out.println("Zugangsserver: Neue Node hinzugefuegt:");
			nodeList.getNode(id).print();
		}
	}

}
