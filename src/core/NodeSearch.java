package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import util.ArrayHelper;

public class NodeSearch {
    private NodeList nodes;
    private Node self;
    private short searchID;
    private short id;
    
    public void search() throws IOException {
        Node node = nodes.getNode(id);
        if (node != null) {
            System.out.println("Client: Node mit ID " + id + " bereits bekannt.");
            return;
        }
        for (int i = 0; i < nodes.nodes.size(); i++) {
            node = nodes.nodes.get(i);
            Socket socket = new Socket(node.ip, node.port);
            OutputStream out = socket.getOutputStream();
            byte[] data = new byte[2];
            data[0] = 6; // Tag
            data[1] = 1; // Version
            byte[] selfData = self.toByteArr();
            data = ArrayHelper.merge(data, selfData);
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort(searchID);
            data = ArrayHelper.merge(data, buffer.array());
            buffer = ByteBuffer.allocate(2);
            buffer.putShort((short)id);
            data = ArrayHelper.merge(data, buffer.array());
            ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
            new Thread(handler).start();
            out.write(data);
            System.out.println("Client: P2PNodeSearchMsg gesendet");
        }
    }
    
    public NodeSearch(NodeList nodes, Node self, short searchID, short idToSearch) {
        this.nodes = nodes;
        this.self = self;
        this.searchID = searchID;
        id = idToSearch;
    }

}
