package core;
import java.io.IOException;
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
        CommandMonitor monitor = new CommandMonitor(nodes, self);
        new Thread(monitor).start();
        Heartbeat heartbeat = new Heartbeat(nodes, server, self);
        new Thread(heartbeat).start();
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(self.port);
            System.out.println("Client: Serversocket unter der Adressse " + self.ip + ":" + self.port + " gestartet, warte auf eingehende Verbindungen");
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
