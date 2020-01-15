package message_handlers;
import java.io.IOException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.net.*;
import core.*;

abstract class MsgHandler {
	NodeList nodeList;
	Node self;
	Socket connectionSocket;
	public MsgHandler(NodeList nodeList, Node currentClient, Socket connectionSocket){
		this.nodeList = nodeList;
		self = currentClient;
		this.connectionSocket = connectionSocket;
	}
	public void handle(){
	}
}
