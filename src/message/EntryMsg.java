package message;

import java.io.IOException;
import java.io.InputStream;
import util.*;

public class EntryMsg extends Message {
    public String ip;
    public int port;

    public EntryMsg() {}
    public EntryMsg(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void read(InputStream in){
        try {
            in.read();
            ip = readIp(in);
            port =  readPort(in);
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
    
    public byte[] create(){
        byte[] data = new byte[2];
        data[0] = 1;
        data[1] = 0;
        data = ArrayHelper.merge(data, ipToByteArr(ip));
        data = ArrayHelper.merge(data, shortToByteArr(port));
        return data;
    }
}