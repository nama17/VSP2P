package core;

import java.net.Socket;
import java.util.HashMap;

public class ConnectionMap {
    public static HashMap<Integer, Socket> connections = new HashMap<Integer, Socket>();
}
