package message_handlers;

import core.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import util.*;

public class EntryMsgHandler extends MsgHandler {
	private NodeList nodeList;
	private Node self;
	private Socket connectionSocket;
	int inPort = connectionSocket.getPort();
	InetAddress inIp = connectionSocket.getInetAddress();

	public EntryMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
		this.nodeList = nodeList;
		this.self = self;
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void handle() throws IOException {
		System.out.println("Zugangsserver: EntryMsg von " + inIp + ":" + inPort + " erhalten");
		try {
			InputStream in = connectionSocket.getInputStream();
			in.read(); // Version
			entryMsg(in);
			sendEntryResponseMsg();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void entryMsg(InputStream in) throws IOException {
		String ip = readIp(in);
		int port = readPort(in);

		if (ip == null || port == 0) {
			return;
		}
		if (ip.length() == 0 || port == -1) {
			System.out.println("Zugangsserver: Ungueltige IP oder Port erhalten");
			return;
		}
		Node node = findNode(id);
		if (node != null) {
			node.updateTime();
			System.out.println("Zugangsserver: Node timeout zurueckgesetzt:");
			node.print();
			return;
		}
		synchronized (nodeList) {
			nodeList.addNode(ip, port, id);
		}
		System.out.println("Zugangsserver: Neue Node hinzugefuegt:");
		node.print();
	}

	private void sendEntryResponseMsg() throws IOException {
		byte[] data = new byte[34];
		data[0] = 2;
		data[1] = 0;
		data = pickRandomNodes(nodeList, 4);
		OutputStream out = connectionSocket.getOutputStream();
		out.write(data);
		System.out.println("Zugangsserver: EntryResponseMsg gesendet");
	}

	private byte[] pickRandomNodes(NodeList nodeList, int nodeCount) {
		ArrayList<byte[]> randomNodes = new ArrayList<byte[]>();
		synchronized (nodeList) {
			for (int i = 1; i < nodeCount || i < nodeList.getSize(); i++) {
				int randomNum = ThreadLocalRandom.current().nextInt(1, nodeList.getSize() + 1);
				randomNodes.add((nodeList.getNode(randomNum)).toByteArr());
			}
		}
		return ArrayHelper.merge(ArrayHelper.merge(randomNodes.get(1), randomNodes.get(2)),
				ArrayHelper.merge(randomNodes.get(3), randomNodes.get(4)));

	}
}
