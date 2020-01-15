
public class CommandMonitor implements Runnable {
    private NodeList nodes;
    private Node self;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
    
    public CommandMonitor(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }

}
