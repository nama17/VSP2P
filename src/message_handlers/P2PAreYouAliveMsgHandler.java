package message_handlers;
import core.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class P2PAreYouAliveMsgHandler extends MsgHandler {
    
	public P2PAreYouAliveMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() throws IOException {
        InputStream in = connectionSocket.getInputStream();
        in.read(); // Version
        readIp(in);
        readPort(in);
        int id = readId(in);
        if (id >= self.id) {
            return;
        }
        // TODO
	}
}
