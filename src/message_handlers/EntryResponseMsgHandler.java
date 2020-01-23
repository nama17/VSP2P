package message_handlers;

import core.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import util.*;

public class EntryResponseMsgHandler extends MsgHandler {

	public EntryResponseMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try {
			InputStream in = connectionSocket.getInputStream();
			in.read(); // Version
			for(int i = 0; i<4; i++){
				nodeList.addNode(readIp(in), readPort(in), readId(in));
			}									
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private void sendP2pNodeRequestMsg(Node node) throws UnknownHostException {
		if (node.ip.equals(self.ip) && node.port == self.port) {
			return;
		}
		try {
			Socket socket = new Socket(node.ip, node.port);
			OutputStream out = socket.getOutputStream();
			byte[] data = ownInfoToByteArr((byte) 3);
			out.write(data);
			 System.out.println("Client: P2pNodeRequestMsg an " + node.ip + ":" + node.port + " gesendet");
			Client client = new Client(self, node);
			new Thread(client).start();
		} catch (IOException e) {
			System.out.println("Client: Fehler beim Versuch eine P2pNodeRequestMsg an " + node.ip + ":" + node.port
					+ " zu senden");
			synchronized (nodeList) {
				for (int i = 1; i < nodeList.nodes.size(); i++) {
					Node nodeInList = nodeList.nodes.get(i);
					if (!nodeInList.ip.equals(node.ip) || nodeInList.port != node.port) {
						continue;
					}
					nodeList.nodes.remove(i);
					System.out.println("Client: Node entfernt:");
					nodeInList.print();
					return;
				}
			}
		}
	}
	
	private byte[] ownInfoToByteArr(byte tag) throws UnknownHostException {
		byte[] data = new byte[10];
		data[0] = tag;
		data[1] = 1; // Version
		ArrayList<Node> list = new ArrayList<Node>();
		list.add(null);
		list.add(new Node(self.ip, self.port, self.id));
		nodesToByteArr(list, data, 2, 1);
		return data;
	}
}


// id zuweisen