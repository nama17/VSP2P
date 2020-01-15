
public class Node {
    public String ip;
    public int port;
    public int id;
    public long time;
    
    public void updateTime() {
        time = System.currentTimeMillis();
    }
    
    public Node(String ip, int port, int id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.time = System.currentTimeMillis();
    }
}
