package core;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandMonitor implements Runnable {
    private NodeList nodes;
    private Node self;
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
                @SuppressWarnings("resource")
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
        NodeSearch search = new NodeSearch(nodes, self, id);
        Thread t = new Thread(search);
        t.start();
        t.join();
    }
    
    private void sendP2PMsgMsg(short id, String msg) throws UnknownHostException, IOException, InterruptedException {
        Node node = nodes.getNode(id);
        if (node == null) {
            searchPeerById(id);
        }
        MsgSender sender = new MsgSender(node, self);
        sender.send(msg);
    }
    
    private void listNodes() {
        synchronized(nodes) {            
            for (int i = 0; i < nodes.nodes.size(); i++) {
                nodes.nodes.get(i).print();
            }
        }
    }
    
    private void election() throws InterruptedException, IOException {
        Election election = new Election(nodes, self);
        election.start();
    }
    
    public CommandMonitor(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }
    
}
