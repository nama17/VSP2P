import java.nio.ByteBuffer;

public class Node {
    public String ip;
    public int port;
    public int id;
    public long time;
    
    public void updateTime() {
        time = System.currentTimeMillis();
    }
    
    public byte[] toByteArr() {
        byte[] arr = new byte[8];
        String[] blocks = ip.split("\\.");
        for (int i = 0; i < 4; i++) {
            arr[i] = (byte)Integer.parseInt(blocks[i]);
        }
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short)port);
        byte[] portArr = buffer.array();
        arr[4] = portArr[0];
        arr[5] = portArr[1];
        buffer = ByteBuffer.allocate(2);
        buffer.putShort((short)id);
        byte[] idArr = buffer.array();
        arr[6] = idArr[0];
        arr[7] = idArr[1];
        return arr;
    }
    
    protected byte[] ipToByteArr(String ip) {
        byte[] arr = new byte[4];
        return arr;
    }
    
    protected byte[] portToByteArr(int port) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort((short)port);
        return buffer.array();
    }
    
    public Node(String ip, int port, int id) {
        this.ip = ip;
        this.port = port;
        this.id = id;
        this.time = System.currentTimeMillis();
    }
}
