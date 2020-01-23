package core;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import message.P2PIamLeaderMsg;
import util.ArrayHelper;
import util.ThreadHelper;

public class CommandMonitor implements Runnable {
    private NodeList nodes;
    private Node self;
    private short searchID = 0;
    public boolean foundHigherId = false;

    @Override
    public void run() {
        try {
            String searchPattern = "^suche \\d{1,2}$";
            String msgPattern = "^sende \\d{1,2} .+$";
            String listNodesPattern = "^list$";
            String leaderPattern = "^leader$";
            Pattern idPattern = Pattern.compile("\\d{1,2}(?=.*$)");
            Pattern msgContentPattern = Pattern.compile("(?<=sende \\d{1,2} ).+$");
            System.out.println("Client: Folgende Befehle koennen verwendet werden:\nsuche <ID>\nsende <ID> <Nachricht>\nlist\nleader");
            while (true) {
                Scanner sc = new Scanner(System.in);
                String command = sc.nextLine();
                if (command.matches(searchPattern)) {
                    Matcher m = idPattern.matcher(command);
                    m.find();
                    String id = m.group();
                    searchPeerById(Short.parseShort(id));
                } else if (command.matches(msgPattern)) {
                    Matcher m = idPattern.matcher(command);
                    m.find();
                    String id = m.group();
                    Matcher mt = msgContentPattern.matcher(command);
                    mt.find();
                    String msgContent = mt.group();
                    sendP2PMsgMsg(Short.parseShort(id), msgContent);
                } else if (command.matches(listNodesPattern)) {
                    listNodes();
                } else if (command.matches(leaderPattern)) {
                    election();
                }
            }
        } catch (NumberFormatException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void searchPeerById(short id) throws IOException, InterruptedException {
        NodeSearch search = new NodeSearch(nodes, self, ++searchID, id);
        Thread t = new Thread(search);
        t.start();
        t.join();        
    }
    
    private void sendP2PMsgMsg(short id, String msg) throws UnknownHostException, IOException, InterruptedException {
        Node node = nodes.getNode(id);
        if (node == null) {
            searchPeerById(id);
        }
        MsgSender sender = new MsgSender(nodes, node, self);
        sender.send(msg);
    }
    
    private void listNodes() {
        for (int i = 0; i < nodes.nodes.size(); i++) {
            nodes.nodes.get(i).print();
        }
    }
    
    public void election() throws InterruptedException, IOException {
        foundHigherId = false;
        if (self.id == 25) {
            System.out.println("Client: Eigene ID = 25, daher Leader");
            sendLeaderMsgToAll();
            return;
        }
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = self.id + 1; i <= 25; i++) {
            PeerPing ping = new PeerPing(nodes.getNode(i), self, nodes, this);
            Thread t = new Thread(ping);
            threads.add(t);
            t.start();
        }
        ThreadHelper.multiJoin(threads);
        if (!foundHigherId) {
            System.out.println("Client: Leader election gewonnen.");
            sendLeaderMsgToAll();
        }
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
        System.out.println("Client: P2PIamLeaderMsg an " + node.ip + " gesendet");
    }
    
    public CommandMonitor(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }
    
}
