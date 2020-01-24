package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import message.IAmAliveMsg;
import message.Message;
import message.P2PAreYouAliveMsg;
import util.StreamHelper;

public class PeerPing implements Runnable {
    private Node receiver;
    private Node self;
    private NodeList nodes;
    private CommandMonitor monitor;

    @Override
    public void run() {
        try {
            Socket socket = new Socket(receiver.ip, receiver.port);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            Message ping = new P2PAreYouAliveMsg(receiver);
            out.write(ping.create());
            System.out.println("Client: P2PAreYouAliveMsg an " + receiver.ip + " gesendet");
            boolean res = StreamHelper.waitForTag(in, 1000, 5, () -> {
                ConnectionHandler handler = new ConnectionHandler(socket, nodes, self, 5);
                Thread thread = new Thread(handler);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {}
            });
            if (!res) {
                return;
            }
            Message aliveMsg = new IAmAliveMsg();
            aliveMsg.read(in);
            synchronized (monitor) {
                monitor.foundHigherId = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public PeerPing(Node node, Node self, NodeList nodes, CommandMonitor monitor) {
        receiver = node;
        this.self = self;
        this.nodes = nodes;
        this.monitor = monitor;
    }

}
