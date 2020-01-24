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
        Socket socket = connect();
        while (true) {
            try {
                sendMsg(socket);
            } catch (IOException e) {
                socket = connect();
                sendMsg(socket);
            }
            Thread.sleep(50 * 1000);
        }
    }
    
    private void sendMsg(Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        IAmAliveMsg heartbeatMsg = new IAmAliveMsg(self);
        out.write(heartbeatMsg.create());
        System.out.println("Client: IAmAliveMsg gesendet");
    }
    
    private Socket connect() {
        try {
            return new Socket(server.ip, server.port);
        } catch (IOException e) {
            System.out.println("Client: Verbindung zum Server kann nicht hergestellt werden");
            return null;
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
