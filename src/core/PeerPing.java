package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import util.ArrayHelper;

public class PeerPing implements Runnable {
    private Node receiver;
    private Node self;
    private NodeList nodes;

    @Override
    public void run() {
        try {
            Socket socket = new Socket(receiver.ip, receiver.port);
            OutputStream out;
            out = socket.getOutputStream();
            byte[] data = new byte[2];
            data[0] = 9; // Tag
            data[1] = 1; // Version
            byte[] selfData = self.toByteArr();
            data = ArrayHelper.merge(data, selfData);
            ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
            new Thread(handler).start();
            out.write(data);
            System.out.println("Client: P2PAreYouAliveMsg an " + receiver.ip + " gesendet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public PeerPing(Node node, Node self, NodeList nodes) {
        receiver = node;
        this.self = self;
        this.nodes = nodes;
    }

}
