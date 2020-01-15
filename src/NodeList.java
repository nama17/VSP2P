import java.util.ArrayList;

public class NodeList {
    public ArrayList<Node> nodes;
    private int maxNodes;
    
    public Node addNode(String ip, int port, int id) {
        Node node = new Node(ip, port, id);
        if (nodes.size() >= maxNodes && maxNodes > 0) {
            removeOldest();
        }
        nodes.add(node);
        return node;
    }
    
    public Node getNode(int id) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.id == id) {
                node.updateTime();
                return node;
            }
        }
        return null;
    }
    
    private void removeOldest() {
        if (nodes.size() == 0) {
            return;
        }
        long min = nodes.get(0).time;
        int index = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (min > nodes.get(i).time) {
                min = nodes.get(i).time;
                index = i;
            }
        }
        nodes.remove(index);
    }
    
    public NodeList(int maxNodes) {
        this.maxNodes = maxNodes;
    }

}
