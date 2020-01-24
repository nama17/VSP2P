package message_handler;

import core.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import message.*;

public class EntryMsgHandler extends MsgHandler {

    public EntryMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
	}

	@Override
	public void handle() {
		try {
			InputStream in = connectionSocket.getInputStream();
			OutputStream out = connectionSocket.getOutputStream();
			EntryMsg entryMsg = new EntryMsg();
			entryMsg.read(in);
			for(int i = 1; i <= 25; i++){
				if (nodeList.getNode(i) == null) {
					nodeList.addNode(entryMsg.ip, entryMsg.port, i);
					EntryResponseMsg entryResponseMsg = new EntryResponseMsg(pickRandomNodes(nodeList, 4), i);
					out.write(entryResponseMsg.create());
					System.out.println("Zugangsserver: EntryResponseMsg gesendet");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private NodeList pickRandomNodes(NodeList nodeList, int nodeCount) {
		ArrayList<Integer> idMemory = new ArrayList<Integer>();
		NodeList randomizedNodes = new NodeList(nodeCount);
		synchronized (nodeList) {
			if (nodeList.nodes.size() > 4){
				for (int i = 0; i < nodeCount; i++) {
					int randomNum = ThreadLocalRandom.current().nextInt(0, nodeList.nodes.size());
					int currentId = nodeList.nodes.get(randomNum).id;
					if (nodeList.nodes.get(i).time >= System.currentTimeMillis() - 60 * 1000) {
						if (idMemory.contains(currentId)){
							i--;
							continue;
						}
						idMemory.add(currentId);
						randomizedNodes.addNode((nodeList.getNode(currentId)));
					} else {
						nodeList.nodes.remove(i);
					}
				}
			} else {
				for (int i = 0; i < nodeList.nodes.size(); i++) {
					if (nodeList.nodes.get(i).time >= System.currentTimeMillis() - 60 * 1000) {
						int currentId = nodeList.nodes.get(i).id;
						randomizedNodes.addNode((nodeList.getNode(currentId)));
					} else {
						nodeList.nodes.remove(i);
					}
				}
				while (randomizedNodes.nodes.size() < nodeCount){
				    randomizedNodes.nodes.add(new Node("0.0.0.0", 0, 0));
				}
			}
			return randomizedNodes;
		}
	}
}