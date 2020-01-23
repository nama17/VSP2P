package message;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import core.*;
import util.*;
import java.nio.*;

public class EntryMsg extends Message {
    public String ip;
    public int port;

    public EntryMsg(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    public void read(InputStream in){
        try{
            in.read();
            ip = readIp(in);
            port =  readPort(in);
        }catch(IOException e){
            e.printStackTrace();
        } 
    }
    public byte[] create(){
        if(ip != null){
            byte[] data = new byte[2];
            data[0] = 1;
            data[1] = 0;
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort((short)port);
            byte[] portArr = buffer.array();
            return ArrayHelper.merge(ip.getBytes(), portArr);
        }
        else{
            return null;
        }
    }
}