package message_handler;
import java.io.*;
import java.net.*;
import core.*;
import message.*;

public class P2PNodeRequestMsgHandler extends MsgHandler{
    
	public P2PNodeRequestMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
		try {
			InputStream in = connectionSocket.getInputStream();
			P2PNodeRequestMsg p2preq = new P2PNodeRequestMsg();
			p2preq.read(in);
			P2PNodeResponseMsg p2presp = new P2PNodeResponseMsg(nodeList);
			OutputStream out = connectionSocket.getOutputStream();
			out.write(p2presp.create());
		} catch (IOException e){
			System.out.println(e);
		}
		
	}

}