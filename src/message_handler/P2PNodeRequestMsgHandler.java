package message_handler;
import java.io.*;
import java.net.*;
import core.*;
import java.util.*;

public class P2PNodeRequestMsgHandler extends MsgHandler{
    
	public P2PNodeRequestMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try{
			InputStream in = connectionSocket.getInputStream();
			in.read(); // Version
			p2pNodeRequestMsg(in);
			sendP2pNodeResponseMsg();
		}catch (IOException e){
			System.out.println(e);
		}
		
	}

	private void sendP2pNodeResponseMsg() throws IOException {
        byte[] data = new byte[34];
        data[0] = 4; // Tag
        data[1] = 1; // Version
        nodesToByteArr(nodeList.nodes, data, 2, 4);
        OutputStream out = connectionSocket.getOutputStream();
        out.write(data);
        System.out.println("Client: Node daten auf Anfrage gesendet");
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
