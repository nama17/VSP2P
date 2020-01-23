package message;

import java.io.InputStream;

public interface Message {
    public byte[] create();
    public MessageData read(InputStream in);
}   