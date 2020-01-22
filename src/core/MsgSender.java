package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import util.ArrayHelper;

public class MsgSender {
    private NodeList nodes;
    private Node receiver;
    private Node self;
    
    public void send(String msg) throws UnknownHostException, IOException {
        Socket socket = new Socket(receiver.ip, receiver.port);
        OutputStream out = socket.getOutputStream();
        byte[] data = new byte[2];
        data[0] = 8; // Tag
        data[1] = 1; // Version
        byte[] selfData = self.toByteArr();
        data = ArrayHelper.merge(data, selfData);
        byte[] msgBytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short)msgBytes.length);
        data = ArrayHelper.merge(data, buffer.array());
        data = ArrayHelper.merge(data, msgBytes);
        ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
        new Thread(handler).start();
        out.write(data);
        System.out.println("Client: P2PMsgMsg gesendet");
    }

    public MsgSender(NodeList nodes, Node receiver, Node self) {
        this.nodes = nodes;
        this.receiver = receiver;
        this.self = self;
    }
}
