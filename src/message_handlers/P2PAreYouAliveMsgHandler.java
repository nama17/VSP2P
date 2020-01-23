package message_handlers;
import core.*;
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
            in.read(); // Version
            readIp(in);
            readPort(in);
            int id = readId(in);
            if (id >= self.id) {
                return;
            };
            OutputStream out = connectionSocket.getOutputStream();
            byte[] data = new byte[2];
            data[0] = 5; // Tag
            data[1] = 1; // Version
            byte[] selfData = self.toByteArr();
            data = ArrayHelper.merge(data, selfData);
            ConnectionHandler handler = new ConnectionHandler(connectionSocket, nodeList, self);
            new Thread(handler).start();
            out.write(data);
            System.out.println("Client: IAmAliveMsg an " + inIp + " gesendet (Leader election)");
            AccessContainer.monitor.election();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}
}
