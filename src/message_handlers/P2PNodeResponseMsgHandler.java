package message_handlers;

import java.net.*;
import core.*;
import java.io.*;
import java.util.*;

public class P2PNodeResponseMsgHandler extends MsgHandler{
    
	public P2PNodeResponseMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try{
		InputStream in = connectionSocket.getInputStream();
		in.read(); // Version
		for (int i = 0; i < 4; i++) {
			p2pNodeRequestMsg(in);
		}}catch(IOException e){
			System.out.println(e);
		}
	}
	
	private void p2pNodeRequestMsg(InputStream in) throws IOException {
		String ip = readIp(in);
		int port = readPort(in);
		int id = readId(in);
		if (ip == null || port == 0) {
			return;
		}
		if (ip.length() == 0 || port == -1) {
			System.out.println("Client: Ungueltige IP oder Port erhalten");
			return;
		}
		Node node = nodeList.getNode(id);
		// String ownIp = InetAddress.getLocalHost().getHostAddress();
		if (node == null && !ip.equals(self.ip) && port != self.port) {
			synchronized (nodeList) {
				nodeList.addNode(ip, port, id);
			}
			System.out.println("Client: Neue Node hinzugefuegt:");
			node = new Node(ip, port, id);
			node.print();
		}
	}
}
