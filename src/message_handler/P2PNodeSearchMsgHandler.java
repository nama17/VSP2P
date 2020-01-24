package message_handler;

import core.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import util.*;
import message.*;


public class P2PNodeSearchMsgHandler extends MsgHandler{
    
	public P2PNodeSearchMsgHandler(NodeList nodeList, Node self, Socket connectionSocket) {
        super(nodeList, self, connectionSocket);
    }

    @Override
	public void handle() {
        try{
            InputStream in = connectionSocket.getInputStream();
            OutputStream out = connectionSocket.getOutputStream();
            P2PNodeSearchMsg searchMsg = new P2PNodeSearchMsg();
            searchMsg.read(in);
            if(searchMsg.destinationId == self.id){
                P2PIAmFoundMsg foundMsg = new P2PIAmFoundMsg(self,  searchMsg.sourceId, searchMsg.searchId);
                out.write(foundMsg.create());
            }else{
                synchronized(nodeList){
                    for(int i=0; i<nodeList.nodes.size(); i++){
                        nodeList.nodes.get(i);
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
	}
}
