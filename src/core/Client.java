package core;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
    private Node server;
    private Node self;
    private NodeList nodes;
    
    public Client (Node self, Node server) {
        this.server = server;
        nodes = new NodeList(4);
        this.self = self;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 25; i++) {
            NodeDataList.add(new NodeData(i));
        }
        CommandMonitor monitor = new CommandMonitor(nodes, self);
        new Thread(monitor).start();
        AccessContainer.monitor = monitor;
        Heartbeat heartbeat = new Heartbeat(nodes, server, self);
        new Thread(heartbeat).start();
        try {
            ServerSocket serverSocket = new ServerSocket(self.port);
            System.out.println("Client: Serversocket gestartet, warte auf eingehende Verbindungen");
            while (true) {
                Socket connectionsocket = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(connectionsocket, nodes, self);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
