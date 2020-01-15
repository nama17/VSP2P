package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import util.ArrayHelper;

public class Heartbeat implements Runnable {
    private Node server;
    private NodeList nodes;
    private Node self;

    @Override
    public void run() {
        register();
        keepAlive();
    }
    
    private void keepAlive() {
        while (true) {
            Thread.sleep(50 * 1000);
            Socket socket = new Socket(server.ip, server.port);
            OutputStream out = socket.getOutputStream();
            byte[] data = new byte[2];
            data[0] = 5; // Tag
            data[1] = 1; // Version
            byte[] selfData = self.toByteArr();
            data = ArrayHelper.merge(data, selfData);
            out.write(data);
            System.out.println("Client: IAmAliveMsg gesendet");
            ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
            new Thread(handler).start();
        }
    }
    
    private void register() {
        Socket socket = new Socket(server.ip, server.port);
        OutputStream out = socket.getOutputStream();
        byte[] data = new byte[2];
        data[0] = 1; // Tag
        data[1] = 1; // Version
        byte[] selfData = self.toByteArr();
        selfData = ArrayHelper.slice(selfData, 0, 6);
        data = ArrayHelper.merge(data, selfData);
        out.write(data);
        System.out.println("Client: EntryMsg gesendet");
        ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
        new Thread(handler).start();
    }
    
    public Heartbeat(NodeList nodes, Node server, Node self) {
        this.nodes = nodes;
        this.server = server;
        this.self = self;
    }

}
