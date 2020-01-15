import java.io.IOException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.net.*;


public class ConnectionHandler implements Runnable{
	private Socket connectionSocket;
	ConnectionHandler(Socket socket) {
		connectionSocket = socket;
	}
	@Override
	public void run() {
	if(connectionSocket != null) {
	InputStream in = connectionSocket.getInputStream();	
	InetAddress inIp = connectionSocket.getInetAddress();
	int inPort = connectionSocket.getPort();
	int tag = in.read();
	switch(tag) {
		case 1:
			System.out.println("Server: EntryMsg empfangen von Client mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new EntryMsgHandler();
			break;
		case 2:
			System.out.println("Client: EntryResponseMsg empfangen von Server mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new EntryResponseMsgHandler();
		case 3:
			System.out.println("Client: P2PNodeRequestMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PNodeRequestMsgHandler();
		case 4:
			System.out.println("Client: P2PNodeResponseMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PNodeResponseMsgHandler();
		case 5:
			System.out.println("Server: IAmAliveMsg empfangen von Client mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PNodeRequestMsgHandler();
		case 6:
			System.out.println("Client: P2PNodeSearchMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PNodeSearchMsgHandler();
		case 7:
			System.out.println("Client: P2PIAMFOUNDMSG empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PIAMFOUNDMsgHandler();
		case 8:
			System.out.println("Client: P2PMsgMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PMsgMsgHandler();
		case 9:
			System.out.println("Client: P2PAreYouAliveMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PAreYouAliveMsgHandler();
		case 10:
			System.out.println("Client: P2PIamLeaderMsg empfangen von Peer mit IP:" + inIp + "und Port:" + inPort);
			tag_handler = new P2PIamLeaderMsgHandler();
	}
	}
	//auf nachrichten warten
	//anhand der nachricht tag herausfinden und tag klasse aufrufen.
    
}
