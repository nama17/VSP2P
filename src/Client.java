
public class Client implements Runnable {
    private int port;
    private String serverIp;
    private int serverPort;
    
    public Client (int port, String serverIp, int serverPort) {
        this.port = port;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

}
