package message_handler;

import core.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import util.*;
import message.*;

public class EntryMsgHandler extends MsgHandler {

    public EntryMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }
	// gucken welche id in der nodelist noch nicht vergeben

    private int inPort = connectionSocket.getPort();
	private InetAddress inIp = connectionSocket.getInetAddress();

	@Override
	public void handle() {
		try {
			InputStream in = connectionSocket.getInputStream();
			OutputStream out = connectionSocket.getOutputStream();
			in.read(); // Version
			EntryMsg entryMsg = new EntryMsg();
			entryMsg.read(in);
			EntryResponseMsg entryResponseMsg = new EntryResponseMsg(pickRandomNodes(nodeList, 4));
			out.write(entryResponseMsg.create());
			System.out.println("Zugangsserver: EntryResponseMsg gesendet");
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private NodeList pickRandomNodes(NodeList nodeList, int nodeCount) {
		ArrayList<byte[]> randomNodes = new ArrayList<byte[]>();
		byte[] dummyNode = new byte[8];
		ArrayList<Integer> idMemory = new ArrayList<Integer>();
		NodeList randomizedNodes = new NodeList(nodeCount);

		synchronized (nodeList) {
			if (nodeList.nodes.size() > 4){
				for (int i = 1; i < nodeCount; i++) {
					int randomNum = ThreadLocalRandom.current().nextInt(1, nodeList.nodes.size() + 1);
					int currentId = nodeList.nodes.get(randomNum).id;
					// alivecheck
					if(nodeList.nodes.get(i).time >= System.currentTimeMillis()-60*1000) {
						if(idMemory.contains(currentId)){
							i--;
						}
						else{ 
							idMemory.add(currentId);
							randomizedNodes.addNode((nodeList.getNode(currentId)));
						}
					}
					else{
						nodeList.nodes.remove(i);
					}
				}
			}
			else{
				for(int i = 1; i < nodeList.nodes.size(); i++){
					if(nodeList.nodes.get(i).time >= System.currentTimeMillis()-60*1000) {
						int currentId = nodeList.nodes.get(i).id;
						randomizedNodes.addNode((nodeList.getNode(currentId)));
					}
					else{
						nodeList.nodes.remove(i);
					}
				}
				while(randomNodes.size() < nodeCount){
					randomNodes.add(dummyNode);
				}
			}
			return randomizedNodes;

		}
	}
}