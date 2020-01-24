package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import message.Message;
import message.P2PMsgMsg;

public class MsgSender {
    private Node receiver;
    private Node self;
    
    public void send(String msg) throws UnknownHostException, IOException {
        Socket socket = new Socket(receiver.ip, receiver.port);
        OutputStream out = socket.getOutputStream();
        Message message = new P2PMsgMsg(receiver, (short)self.id, msg);
        out.write(message.create());
        socket.close();
        System.out.println("Client: P2PMsgMsg gesendet");
    }

    public MsgSender(Node receiver, Node self) {
        this.receiver = receiver;
        this.self = self;
    }
}
