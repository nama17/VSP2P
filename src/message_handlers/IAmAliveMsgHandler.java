package message_handlers;

import java.net.Socket;

import core.Node;
import core.NodeList;
import java.io.*;
import java.net.*;
import java.util.*;

public class IAmAliveMsgHandler extends MsgHandler {
	private NodeList nodeList;
	private Node self;
	private Socket connectionSocket;
	
	public IAmAliveMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
		this.nodeList = nodeList;
		this.self = self;
		this.connectionSocket = connectionSocket;
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
