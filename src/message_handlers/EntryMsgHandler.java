package message_handlers;
import java.io.IOException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import core.Node;
import core.NodeList;

import java.net.*;

public class EntryMsgHandler extends MsgHandler{
	EntryMsgHandler(NodeList nodeList, Node currentClient, Socket connectionSocket) {
		super(nodeList, currentClient, connectionSocket);
	}
	
	@Override
	public void handle(){
		
	}
}
