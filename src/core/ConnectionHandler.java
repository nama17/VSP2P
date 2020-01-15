package core;
import java.io.IOException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.net.*;
import message_handlers.*;

public class ConnectionHandler implements Runnable {
	private Socket connectionSocket;
	private NodeList nodeList;
	private Node self;
	
	ConnectionHandler(Socket socket, NodeList nodeList, Node node) {
		connectionSocket = socket;
		this.nodeList = nodeList;
		self = node;
	}
	
//	in, out, nodelist, self

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
				EntryMsgHandler tag1_handler = new EntryMsgHandler();
				break;
			case 2:
				System.out
						.println("Client: EntryResponseMsg empfangen von Server mit IP:" + inIp + "und Port:" + inPort);
				EntryResponseMsgHandler tag2_handler = new EntryResponseMsgHandler();
				break;
			case 3:
				System.out
						.println("Client: P2PNodeRequestMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeRequestMsgHandler tag3_handler = new P2PNodeRequestMsgHandler();
				break;
			case 4:
				System.out
						.println("Client: P2PNodeResponseMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeResponseMsgHandler tag4_handler = new P2PNodeResponseMsgHandler();
				break;
			case 5:
				System.out.println("Server: IAmAliveMsg empfangen von Client mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeRequestMsgHandler tag5_handler = new P2PNodeRequestMsgHandler();
				break;
			case 6:
				System.out.println("Client: P2PNodeSearchMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PNodeSearchMsgHandler tag6_handler = new P2PNodeSearchMsgHandler();
				break;
			case 7:
				System.out.println("Client: P2PIAMFOUNDMSG empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PIamFoundMsgHandler tag7_handler = new P2PIamFoundMsgHandler();
				break;
			case 8:
				System.out.println("Client: P2PMsgMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PMsgMsgHandler tag8_handler = new P2PMsgMsgHandler();
				break;
			case 9:
				System.out
						.println("Client: P2PAreYouAliveMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PAreYouAliveMsgHandler tag9_handler = new P2PAreYouAliveMsgHandler();
				break;
			case 10:
				System.out.println("Client: P2PIamLeaderMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
				P2PIamLeaderMsgHandler tag10_handler = new P2PIamLeaderMsgHandler();
				break;
			default:
				System.out.println("Client: Ungueltiges Tag in Nachricht erhalten. Tag: " + tag);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
