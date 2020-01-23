package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import message.IAmAliveMsg;
import util.ArrayHelper;

public class Heartbeat implements Runnable {
    private Node server;
    private NodeList nodes;
    private Node self;

    @Override
    public void run() {
        try {
            register();
            keepAlive();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void keepAlive() throws InterruptedException, IOException {
        while (true) {
            Thread.sleep(50 * 1000);
            Socket socket = new Socket(server.ip, server.port);
            OutputStream out = socket.getOutputStream();
            Message heartbeatMsg = new IAmAliveMsg(self);
            out.write(heartbeatMsg.create());
            System.out.println("Client: IAmAliveMsg gesendet");
        }
    }
    
    private void register() throws UnknownHostException, IOException {
        Socket socket = new Socket(server.ip, server.port);
        OutputStream out = socket.getOutputStream();
        byte[] data = new byte[2];
        data[0] = 1; // Tag
        data[1] = 1; // Version
        byte[] selfData = self.toByteArr();
        selfData = ArrayHelper.slice(selfData, 0, 6);
        data = ArrayHelper.merge(data, selfData);
        ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
        new Thread(handler).start();
        out.write(data);
        System.out.println("Client: EntryMsg gesendet");
    }
    
    public Heartbeat(NodeList nodes, Node server, Node self) {
        this.nodes = nodes;
        this.server = server;
        this.self = self;
    }

}
