package message_handler;
import core.*;
import util.ArrayHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class P2PIamFoundMsgHandler extends MsgHandler{
    
	public P2PIamFoundMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            in.read(); // Version
            String ip = readIp(in);
            int port = readPort(in);
            int sourceId = readId(in);
            if (sourceId == self.id) {
                nodeList.addNode(ip, port, id); // TODO woher bekomme ich die ID des gefundenen peers?? muss ich die passende search id merken??
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
