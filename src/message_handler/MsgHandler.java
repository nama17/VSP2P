package message_handler;

import java.net.Socket;
import core.Node;
import core.NodeList;
import java.io.*;

public abstract class MsgHandler {
    protected NodeList nodeList;
    protected Node self;
    protected Socket connectionSocket;

    public MsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        this.nodeList = nodeList;
        this.self = self;
        this.connectionSocket = connectionSocket;
    }
    
	public abstract void handle() throws IOException;
}

