package message_handler;
import core.*;
import message.IAmAliveMsg;
import message.P2PAreYouAliveMsg;
import util.ArrayHelper;

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
            if (p2pmsg.node.id >= self.id) {
                return;
            };
            OutputStream out = connectionSocket.getOutputStream();
            IAmAliveMsg iaam = new IAmAliveMsg(self);
            ConnectionHandler handler = new ConnectionHandler(connectionSocket, nodeList, self);
            new Thread(handler).start();
            out.write(iaam.create());
            System.out.println("Client: IAmAliveMsg an " + inIp + " gesendet (Leader election)");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}