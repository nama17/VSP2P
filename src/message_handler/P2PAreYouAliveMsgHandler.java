package message_handler;

import core.*;
import message.IAmAliveMsg;
import message.P2PAreYouAliveMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class P2PAreYouAliveMsgHandler extends MsgHandler {
    
	public P2PAreYouAliveMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            InetAddress inIp = connectionSocket.getInetAddress();
            P2PAreYouAliveMsg p2pmsg = new P2PAreYouAliveMsg(); 
            p2pmsg.read(in);
            OutputStream out = connectionSocket.getOutputStream();
            IAmAliveMsg iaam = new IAmAliveMsg(self);
            out.write(iaam.create());
            System.out.println("Client: IAmAliveMsg an " + inIp + " gesendet (Leader election)");
            // TODO start election
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}