package message_handler;

import java.net.Socket;

import core.Node;
import core.NodeList;
import java.io.*;
import message.*;

public class IAmAliveMsgHandler extends MsgHandler {
	
	public IAmAliveMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        if (self != null) {
            System.out.println("Client: Unerwartete IAmAliveMsg erhalten" );
            return;
        }
		try {
			InputStream in = connectionSocket.getInputStream();
			IAmAliveMsg iaam = new IAmAliveMsg();
			iaam.read(in);
		    Node node = nodeList.getNode(iaam.node.id);
		    if (node == null) {
                System.out.println("Server: IAmAliveMsg von toter Node erhalten");
		        return;
		    }
		    node.updateTime();
		} catch (IOException e){
			System.out.println(e);
		}
	}

}
