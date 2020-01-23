package core;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ArrayHelper;

public class CommandMonitor implements Runnable {
    private NodeList nodes;
    private Node self;
    private short searchID = 0;
    public static boolean foundHigherId = false;

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
    
    private void searchPeerById(short id) throws IOException {
        NodeSearch search = new NodeSearch(nodes, self, searchID, id);
        new Thread(search).start();
    }
    
    private void sendP2PMsgMsg(short id, String msg) throws UnknownHostException, IOException, InterruptedException {
        Node node = nodes.getNode(id);
        if (node == null) {
            NodeData data = NodeDataList.get(id);
            data.onFound.add(new Action() {
                public void run() {
                    try {
                        sendP2PMsgMsg(id, msg);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            searchPeerById(id);
            return;
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
        if (self.id == 25) {
            System.out.println("Client: Eigene ID = 25, daher Leader");
            sendLeaderMsgToAll();
            return;
        }
        for (int i = self.id + 1; i <= 25; i++) {
            PeerPing ping = new PeerPing(nodes.getNode(i), self, nodes);
            new Thread(ping).start();
        }
        Thread.sleep(1 * 1000);
        if (!foundHigherId) {
            sendLeaderMsgToAll();
        }
        foundHigherId = false;
    }
    
    private void sendLeaderMsgToAll() throws IOException {
        for (int i = 1; i <= 25; i++) {
            Node node = nodes.getNode(i);
            if (node == null) {
                NodeData data = NodeDataList.get(i);
                fakeActionConstructor(data, i);
                searchPeerById((short)i);
                return;
            }
            sendLeaderMsg(i);
        }
    }
    
    private void fakeActionConstructor(NodeData data, int id) {
        data.onFound.add(new Action() {
            public void run() {
                try {
                    sendLeaderMsg(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void sendLeaderMsg(int id) throws IOException {
        Node node = nodes.getNode(id);
        Socket socket = new Socket(node.ip, node.port);
        OutputStream out = socket.getOutputStream();
        byte[] data = new byte[2];
        data[0] = 10; // Tag
        data[1] = 1; // Version
        byte[] selfData = self.toByteArr();
        data = ArrayHelper.merge(data, selfData);
        ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
        new Thread(handler).start();
        out.write(data);
        System.out.println("Client: P2PIamLeaderMsg an " + node.ip + " gesendet");
    }
    
    public CommandMonitor(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }
    
}
