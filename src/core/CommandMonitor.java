package core;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void searchPeerById(short id) throws IOException {
        NodeSearch search = new NodeSearch(nodes, self, searchID, id);
        search.search();
    }
    
    private void sendP2PMsgMsg(short id, String msg) throws UnknownHostException, IOException, InterruptedException {
        if (nodes.getNode(id) == null) {
            searchPeerById(id);
            Thread.sleep(100);
            for (int i = 0; i < 9 && nodes.getNode(id) == null; i++) {
                Thread.sleep(100);
            }
        }
        Node node = nodes.getNode(id);
        if (node == null) {
            System.out.println("Client: Msg konnte nicht gesendet werden, da der Empfänger nicht gefunden wurde");
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
    
    private void election() {
        // TODO Leader election
    }
    
    public CommandMonitor(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }

}
