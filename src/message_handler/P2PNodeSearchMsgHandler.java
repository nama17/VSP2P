package message_handler;

import core.*;
import java.net.*;
import java.io.*;
import java.util.*;
import util.*;
import message.*;


public class P2PNodeSearchMsgHandler extends MsgHandler{
    private static HashMap<Integer, ArrayList<Integer>> searchData = new HashMap<Integer, ArrayList<Integer>>();
    private P2PIAmFoundMsg foundMsg = null;
    
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
            if (nodeList.nodes.size() < 4 || new Random().nextInt(10) < 1) {
                Node node = searchMsg.node;
                if (node.ip != null && (!node.ip.equals(self.ip) || node.port != self.port)) {                   
                    nodeList.addNode(node);
                }
            }
            if (searchMsg.sourceId == self.id) {
                return;
            }
            if (searchMsg.destinationId == self.id){
                P2PIAmFoundMsg foundMsg = new P2PIAmFoundMsg(self, searchMsg.searchId);
                out.write(foundMsg.create());
            } else {
                if (searchData.get((int)searchMsg.sourceId) == null) {
                    searchData.put((int)searchMsg.sourceId, new ArrayList<Integer>());
                }
                ArrayList<Integer> searches = searchData.get((int)searchMsg.sourceId);
                if (searches.contains((int)searchMsg.searchId)) {
                    return;
                }

                System.out.println("Client: Neue Suchanfrage von " + searchMsg.sourceId + "(source) bekommen. Gesucht nach: " + searchMsg.destinationId);
                searches.add((int)searchMsg.searchId);
                ArrayList<Thread> threads = new ArrayList<Thread>();
                synchronized(nodeList) {
                    for (int i = 0; i < nodeList.nodes.size(); i++) {
                        Node node = nodeList.nodes.get(i);
                        Thread t = new Thread(() -> {
                            Socket socket;
                            try {
                                socket = new Socket(node.ip, node.port);
                                OutputStream _out = socket.getOutputStream();
                                InputStream _in = socket.getInputStream();
                                Message _searchMsg = new P2PNodeSearchMsg(node, searchMsg.sourceId, searchMsg.searchId, searchMsg.destinationId);
                                _out.write(_searchMsg.create());
                                System.out.println("Client: P2PNodeSearchMsg weitergeleitet");
                                boolean res = StreamHelper.waitForTag(_in, 1000, 7, () -> {
                                    ConnectionHandler handler = new ConnectionHandler(socket, nodeList, self, 7);
                                    Thread thread = new Thread(handler);
                                    thread.start();
                                    try {
                                        thread.join();
                                    } catch (InterruptedException e) {}
                                });
                                if (!res || foundMsg != null) {
                                    return;
                                }
                                P2PIAmFoundMsg _foundMsg = new P2PIAmFoundMsg();
                                _foundMsg.read(_in);
                                foundMsg = _foundMsg;
                                out.write(foundMsg.create());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        threads.add(t);
                        t.start();
                    }
                }
                ThreadHelper.multiJoin(threads);
                System.out.println("Client: Suchanfrage von " + searchMsg.sourceId + " (source) nach: " + searchMsg.destinationId +
                        " konnte " + (foundMsg == null ? " nicht" : "") + " beantwortet werden.");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
	}
}
