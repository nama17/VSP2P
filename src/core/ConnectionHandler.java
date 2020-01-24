package core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import message_handler.*;

public class ConnectionHandler implements Runnable {
	private Socket connectionSocket;
	private NodeList nodeList;
	private Node self = null;
	private int tag = 0;

    public ConnectionHandler(Socket socket, NodeList nodeList, Node node, int tag) {
        connectionSocket = socket;
        this.nodeList = nodeList;
        self = node;
        this.tag = tag;
    }
    
    public ConnectionHandler(Socket socket, NodeList nodeList, Node node) {
        connectionSocket = socket;
        this.nodeList = nodeList;
        self = node;
    }
	
	public ConnectionHandler(Socket socket, NodeList nodeList) {
        connectionSocket = socket;
        this.nodeList = nodeList;
    }

	@Override
	public void run() {
		InputStream in;
		MsgHandler handler = null;

		try {
			in = connectionSocket.getInputStream();
			InetAddress inIp = connectionSocket.getInetAddress();
			int inPort = connectionSocket.getPort();
			if (tag == 0) {			    
			    tag = in.read();
			}

			switch (tag) {
			case 1:
				System.out.println("Server: EntryMsg empfangen von " + inIp + ":" + inPort);
				handler = new EntryMsgHandler(nodeList, self, connectionSocket);
				break;
			case 2:
				System.out.println("Client: EntryResponseMsg empfangen");
				handler = new EntryResponseMsgHandler(nodeList, self, connectionSocket);
				break;
			case 3:
				System.out.println("Client: P2PNodeRequestMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PNodeRequestMsgHandler(nodeList, self, connectionSocket);
				break;
			case 4:
				System.out.println("Client: P2PNodeResponseMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PNodeResponseMsgHandler(nodeList, self, connectionSocket);
				break;
			case 5:
				System.out.println("Server: IAmAliveMsg empfangen von " + inIp + ":" + inPort);
				handler = new IAmAliveMsgHandler(nodeList, self, connectionSocket);
				break;
			case 6:
				System.out.println("Client: P2PNodeSearchMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PNodeSearchMsgHandler(nodeList, self, connectionSocket);
				break;
			case 7:
				System.out.println("Client: P2PIAmFoundMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PIamFoundMsgHandler(nodeList, self, connectionSocket);
				break;
			case 8:
				System.out.println("Client: P2PMsgMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PMsgMsgHandler(nodeList, self, connectionSocket);
				break;
			case 9:
				System.out.println("Client: P2PAreYouAliveMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PAreYouAliveMsgHandler(nodeList, self, connectionSocket);
				break;
			case 10:
				System.out.println("Client: P2PIamLeaderMsg empfangen von " + inIp + ":" + inPort);
				handler = new P2PIamLeaderMsgHandler(nodeList, self, connectionSocket);
				break;
			default:
				System.out.println("Client: Ungueltiges Tag in Nachricht erhalten. Tag: " + tag);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (handler != null){
			try {
				handler.handle();
			} catch (IOException e){
			    e.printStackTrace();
			}
    	}
    }
}
