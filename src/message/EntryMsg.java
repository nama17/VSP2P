package message;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import core.*;
import util.*;

public class EntryMsg extends Message {
    public Node node;

    public EntryMsg(Node node){
        this.node = node;
    }
    public void read(InputStream in){
        try{
            in.read();
            node = new Node(readIp(in), readPort(in), readId(in));
        }catch(IOException e){
            e.printStackTrace();
        } 
    }
    public byte[] create(){
        if(node != null){
            byte[] data = new byte[2];
            data[0] = 1;
            data[1] = 0;
            return ArrayHelper.merge(data,node.toByteArr());
        }
        else{
            return null;
        }
    }
}