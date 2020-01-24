package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import message.EntryMsg;
import message.IAmAliveMsg;
import message.Message;

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
            Socket socket = connect();
            Thread.sleep(50 * 1000);
            sendMsg(socket);
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
        Message registerMsg = new EntryMsg(self.ip, self.port);
        out.write(registerMsg.create());
        ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
        new Thread(handler).start();
        System.out.println("Client: EntryMsg gesendet");
    }
    
    public Heartbeat(NodeList nodes, Node server, Node self) {
        this.nodes = nodes;
        this.server = server;
        this.self = self;
    }

}
