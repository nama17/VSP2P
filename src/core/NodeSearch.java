package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Function;

import message.P2PNodeSearchMsg;
import util.ArrayHelper;
import util.StreamHelper;
import util.ThreadHelper;

public class NodeSearch implements Runnable {
    private NodeList nodes;
    private Node self;
    private short searchId;
    private short id;
    private boolean found = false;
    
    @Override
    public void run() {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        synchronized(nodes) {
            if (nodes.getNode(id) != null) {
                System.out.println("Client: Node mit ID " + id + " bereits bekannt.");
                return;
            }
            for (int i = 0; i < nodes.nodes.size(); i++) {
                Node node = nodes.nodes.get(i);
                Thread t = new Thread(() -> {
                    Socket socket;
                    try {
                        socket = new Socket(node.ip, node.port);
                        OutputStream out = socket.getOutputStream();
                        InputStream in = socket.getInputStream();
                        Message searchMsg = new P2PNodeSearchMsg(node, (short)self.id, searchId, id);
                        out.write(searchMsg.create());
                        System.out.println("Client: P2PNodeSearchMsg gesendet");
                        boolean res = StreamHelper.waitForTag(in, 1000, 7, () -> {
                            ConnectionHandler handler = new ConnectionHandler(socket, nodes, self, 7);
                            Thread thread = new Thread(handler);
                            thread.start();
                            thread.join();
                        });
                        if (!res) {
                            return;
                        }
                        found = true;
                        Message foundMsg = new IAmFoundMsg();
                        foundMsg.read(in);
                        synchronized (nodes) {
                            nodes.addNode(foundMsg.node);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                threads.add(t);
                t.start();
            }
        }
        ThreadHelper.multiJoin(threads);
        System.out.println("Client: Node mit ID " + id + (found ? " nicht" : "") + " gefunden.");
        synchronized(nodes) {            
            if (found) {
                nodes.getNode(id).print();
            }
        }
    }
    
    public NodeSearch(NodeList nodes, Node self, short searchID, short idToSearch) {
        this.nodes = nodes;
        this.self = self;
        this.searchId = searchID;
        id = idToSearch;
    }

}
