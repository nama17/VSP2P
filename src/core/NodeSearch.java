package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import message.Message;
import message.P2PIAmFoundMsg;
import message.P2PNodeSearchMsg;
import util.StreamHelper;
import util.ThreadHelper;

public class NodeSearch implements Runnable {
    private static short searchId = 0;
    private NodeList nodes;
    private Node self;
    private short id;
    private boolean found = false;
    
    @Override
    public void run() {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        if (nodes.getNode(id) != null) {
            //System.out.println("Client: Node mit ID " + id + " bereits bekannt.");
            return;
        }
        synchronized(nodes) {
            for (int i = 0; i < nodes.nodes.size(); i++) {
                Node node = nodes.nodes.get(i);
                Thread t = new Thread(() -> {
                    Socket socket;
                    try {
                        socket = new Socket(node.ip, node.port);
                        OutputStream out = socket.getOutputStream();
                        InputStream in = socket.getInputStream();
                        Message searchMsg = new P2PNodeSearchMsg(self, searchId, id);
                        out.write(searchMsg.create());
                        //System.out.println("Client: P2PNodeSearchMsg gesendet");
                        boolean res = StreamHelper.waitForTag(in, 1000, 7, () -> {
                            ConnectionHandler handler = new ConnectionHandler(socket, nodes, self, 7);
                            Thread thread = new Thread(handler);
                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {}
                        });
                        if (!res) {
                            return;
                        }
                        found = true;
                        P2PIAmFoundMsg foundMsg = new P2PIAmFoundMsg();
                        foundMsg.read(in);
                        nodes.addNode(foundMsg.node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                threads.add(t);
                t.start();
            }
        }
        ThreadHelper.multiJoin(threads);
        //System.out.println("Client: Node mit ID " + id + (found ? "" : " nicht") + " gefunden.");        
        if (found) {
            nodes.getNode(id).print();
        }
    }
    
    public NodeSearch(NodeList nodes, Node self, short idToSearch) {
        this.nodes = nodes;
        this.self = self;
        id = idToSearch;
        searchId++;
    }

}
