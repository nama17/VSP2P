
public class Heartbeat implements Runnable {
    private Node server;
    private NodeList nodes;
    private Node self;

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
    
    public Heartbeat(NodeList nodes, Node server, Node self) {
        this.nodes = nodes;
        this.server = server;
        this.self = self;
    }

}
