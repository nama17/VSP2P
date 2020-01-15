import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
    private int port;
    private String serverIp;
    private int serverPort;
    private NodeList nodes;
    
    public Client (int port, String serverIp, int serverPort) {
        this.port = port;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        nodes = new NodeList(4);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Client: Serversocket gestartet, warte auf eingehende Verbindungen");
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
