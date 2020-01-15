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
                    searchPeerById(Integer.parseInt(id));
                } else if (command.matches(msgPattern)) {
                    Matcher m = idPattern.matcher(command);
                    m.find();
                    String id = m.group();
                    Matcher mt = msgContentPattern.matcher(command);
                    mt.find();
                    String msgContent = mt.group();
                    sendP2PMsgMsg(Integer.parseInt(id), msgContent);
                } else if (command.matches(listNodesPattern)) {
                    listNodes();
                } else if (command.matches(leaderPattern)) {
                    election();
                }
            }
        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void searchPeerById(int id) throws UnknownHostException, IOException {
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
            byte[] searchData = new byte[4];
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort(searchID);
            byte[] searchIDArr = buffer.array();
            searchData[0] = searchIDArr[0];
            searchData[1] = searchIDArr[1];
            buffer = ByteBuffer.allocate(2);
            buffer.putShort((short)id);
            byte[] idArr = buffer.array();
            searchData[2] = idArr[0];
            searchData[3] = idArr[1];
            data = ArrayHelper.merge(data, searchData);
            out.write(data);
            System.out.println("Client: P2PNodeSearchMsg gesendet");
            ConnectionHandler handler = new ConnectionHandler(socket, nodes, self);
            new Thread(handler).start();
        }
    }
    
    private void sendP2PMsgMsg(int id, String msg) {
        // TODO
    }
    
    private void listNodes() {
        for (int i = 0; i < nodes.nodes.size(); i++) {
            nodes.nodes.get(i).print();
        }
    }
    
    private void election() {
        // TODO Leader election
    }
    
    public CommandMonitor(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }

}
