package core;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private Node node;
    
    public Connection(Socket socket, Node node) {
        this.socket = socket;
        this.node = node;
    }
    
    public Connection(Node node) {
        this.node = node;
        try {
            this.socket = new Socket(node.ip, node.port);
        } catch (IOException e) {
            System.out.println("Verbindung zu " + node.ip + ":" + node.port + "konnte nicht hergestellt werden!");
        }
    }
}
