package message_handler;
import core.*;
import util.ArrayHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import message.*;

public class P2PIamFoundMsgHandler extends MsgHandler{
    
	public P2PIamFoundMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        try {
            InputStream in = connectionSocket.getInputStream();
            P2PIAmFoundMsg p2pfound = new P2PIAmFoundMsg();
            p2pfound.read(in);
            System.out.println("This is unintended behaviour, caused by non-maintained Connections");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
