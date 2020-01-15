package core;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandMonitor implements Runnable {
    private NodeList nodes;
    private Node self;

    @Override
    public void run() {
        String searchPattern = "^suche \\d{1,2}$";
        String msgPattern = "^sende \\d{1,2} .+$";
        String listNodesPattern = "^list$";
        String leaderPattern = "^leader$";
        Pattern idPattern = Pattern.compile("\\d{1,2}(?=.*$)");
        Pattern msgContentPattern = Pattern.compile("(?<=sende \\d{1,2} ).+$");
        System.out.println("Client: Folgende Befehle koennen verwendet werden:\nsuche <Id>\nsende <Id> <Nachricht>\nlist\nleader");
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
    }
    
    private void searchPeerById(int id) {
        // TODO
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
