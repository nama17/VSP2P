package message_handlers;

import core.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import util.*;

public class EntryMsgHandler extends MsgHandler {

    public EntryMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    int inPort = connectionSocket.getPort();
	InetAddress inIp = connectionSocket.getInetAddress();

	@Override
	public void handle() throws IOException {
		try {
			InputStream in = connectionSocket.getInputStream();
			in.read(); // Version
			entryMsg(in);
			sendEntryResponseMsg();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void sendEntryResponseMsg() throws IOException {
		byte[] allRandomNodes; 
		byte[] data = new byte[2];
		data[0] = 2;
		data[1] = 0;
		allRandomNodes = pickRandomNodes(nodeList, 4);
		byte[] entryResponseMsg = ArrayHelper.merge(data, allRandomNodes);
		OutputStream out = connectionSocket.getOutputStream();
		out.write(entryResponseMsg);
		System.out.println("Zugangsserver: EntryResponseMsg gesendet");
	}

	private byte[] pickRandomNodes(NodeList nodeList, int nodeCount) {
		ArrayList<byte[]> randomNodes = new ArrayList<byte[]>();
		byte[] dummyNode = new byte[10];
		ArrayList<Integer> idMemory = new ArrayList<Integer>();

		synchronized (nodeList) {
			if (nodeList.nodes.size() > 4){
				for (int i = 1; i < nodeCount; i++) {
					int randomNum = ThreadLocalRandom.current().nextInt(1, nodeList.nodes.size() + 1);
					int currentId = nodeList.nodes.get(randomNum).id;
					// enter alivecheck here
					if(idMemory.isEmpty()){
						idMemory.add(currentId);
						randomNodes.add((nodeList.getNode(currentId)).toByteArr());
					}
					else if(idMemory.contains(currentId)){
						i--;
					}
					else{ 
						idMemory.add(currentId);
						randomNodes.add((nodeList.getNode(currentId)).toByteArr());
					}
				}
			}
			else{
				for(int i = 1; i < nodeList.nodes.size(); i++){
					// enter alivecheck here
					int currentId = nodeList.nodes.get(i).id;
					randomNodes.add((nodeList.getNode(currentId)).toByteArr());
				}
				while(randomNodes.size() < nodeCount){
					randomNodes.add(dummyNode);
				}
			}
			return ArrayHelper.merge(ArrayHelper.merge(randomNodes.get(1), randomNodes.get(2)),
					ArrayHelper.merge(randomNodes.get(3), randomNodes.get(4)));

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
		}
		System.out.println("Zugangsserver: Neue Node hinzugefuegt:");
		node.print();
	}
}