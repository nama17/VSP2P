package core;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import message_handlers.EntryMsgHandler;
import message_handlers.EntryResponseMsgHandler;
import message_handlers.P2PAreYouAliveMsgHandler;
import message_handlers.P2PIamFoundMsgHandler;
import message_handlers.P2PIamLeaderMsgHandler;
import message_handlers.P2PMsgMsgHandler;
import message_handlers.P2PNodeRequestMsgHandler;
import message_handlers.P2PNodeResponseMsgHandler;
import message_handlers.P2PNodeSearchMsgHandler;

public class ConnectionHandler implements Runnable {
	private Socket connectionSocket;
	private NodeList nodeList;
	private Node self;

	public ConnectionHandler(Socket socket, NodeList nodeList, Node node) {
		connectionSocket = socket;
		this.nodeList = nodeList;
		self = node;
	}

	@Override
	public void run() {
		InputStream in;
		int tag;

		try {
			in = connectionSocket.getInputStream();
			InetAddress inIp = connectionSocket.getInetAddress();
			int inPort = connectionSocket.getPort();
			tag = in.read();
			switch (tag) {
			case 1:
				System.out.println("Server: EntryMsg empfangen von Client mit IP:" + inIp + "und Port:" + inPort);
				EntryMsgHandler tag1_handler = new EntryMsgHandler(nodeList, self, connectionSocket);
				break;
			case 2:
				System.out.println("Client: EntryResponseMsg empfangen von Server mit IP:" + inIp + "und Port:" + inPort);
				EntryResponseMsgHandler tag2_handler = new EntryResponseMsgHandler(nodeList, self, connectionSocket);
				break;
			case 3:
				System.out.println("Client: P2PNodeRequestMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeRequestMsgHandler tag3_handler = new P2PNodeRequestMsgHandler(nodeList, self, connectionSocket);
				break;
			case 4:
				System.out.println("Client: P2PNodeResponseMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeResponseMsgHandler tag4_handler = new P2PNodeResponseMsgHandler(nodeList, self, connectionSocket);
				break;
			case 5:
				System.out.println("Server: IAmAliveMsg empfangen von Client mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeRequestMsgHandler tag5_handler = new P2PNodeRequestMsgHandler(nodeList, self, connectionSocket);
				break;
			case 6:
				System.out.println("Client: P2PNodeSearchMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeSearchMsgHandler tag6_handler = new P2PNodeSearchMsgHandler(nodeList, self, connectionSocket);
				break;
			case 7:
				System.out.println("Client: P2PIAMFOUNDMSG empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PIamFoundMsgHandler tag7_handler = new P2PIamFoundMsgHandler(nodeList, self, connectionSocket);
				break;
			case 8:
				System.out.println("Client: P2PMsgMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PMsgMsgHandler tag8_handler = new P2PMsgMsgHandler(nodeList, self, connectionSocket);
				break;
			case 9:
				System.out.println("Client: P2PAreYouAliveMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PAreYouAliveMsgHandler tag9_handler = new P2PAreYouAliveMsgHandler(nodeList, self, connectionSocket);
				break;
			case 10:
				System.out.println("Client: P2PIamLeaderMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PIamLeaderMsgHandler tag10_handler = new P2PIamLeaderMsgHandler(nodeList, self, connectionSocket);
				break;
			default:
				System.out.println("Client: Ungueltiges Tag in Nachricht erhalten. Tag: " + tag);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
