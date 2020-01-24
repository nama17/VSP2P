package core;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private int port;
    private NodeList nodes;
    
    public Server(int port) {
        this.port = port;
        nodes = new NodeList(0);
    }

    @Override
    public void run() {
        try {
            @SuppressWarnings("resource")
            ServerSocket serverSocket = new ServerSocket(port);
            String ownIp = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Zugangsserver unter der Adressse " + ownIp + ":" + port + " gestartet, warte auf eingehende Verbindungen");
            while (true) {
                Socket connectionsocket = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(connectionsocket, nodes);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
