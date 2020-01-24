package core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import message.Message;
import message.P2PIamLeaderMsg;
import util.ThreadHelper;

public class Election {
    public boolean foundHigher;
    
    public void election() throws InterruptedException, IOException {
        System.out.println("Client: Leader election gestartet");
        foundHigherId = false;
        if (self.id == 25) {
            System.out.println("Client: Eigene ID = 25, daher Leader");
            sendLeaderMsgToAll();
            return;
        }
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = self.id + 1; i <= 25; i++) {
            Node node = nodes.getNode(i);
            PeerPing ping = new PeerPing(node, self, nodes, this);
            Thread t = new Thread(ping);
            threads.add(t);
            t.start();
        }
        ThreadHelper.multiJoin(threads);
        if (!foundHigherId) {
            System.out.println("Client: Leader election gewonnen.");
            sendLeaderMsgToAll();
        }
        System.out.println("Client: Anderer Peer ist Leader");
    }
    
    private void sendLeaderMsgToAll() throws IOException, InterruptedException {
        for (int i = 1; i <= 25; i++) {
            Node node = nodes.getNode(i);
            if (node == null) {
                searchPeerById((short) i);
            }
            sendLeaderMsg(i);
        }
    }
    
    private void sendLeaderMsg(int id) throws IOException {
        Node node = nodes.getNode(id);
        Socket socket = new Socket(node.ip, node.port);
        OutputStream out = socket.getOutputStream();
        Message leaderMsg = new P2PIamLeaderMsg(self);
        out.write(leaderMsg.create());
        socket.close();
        System.out.println("Client: P2PIamLeaderMsg an " + node.ip + " gesendet");
    }
}
