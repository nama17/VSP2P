package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import message.Message;
import message.P2PHereIsMyTimeMsg;
import message.P2PHereIsYourNewTimeMsg;
import message.P2PTellMeYourTimeMsg;
import util.StreamHelper;
import util.ThreadHelper;

public class TimeSync {
    private NodeList nodes;
    private Node self;
    
    public void start() {
        System.out.println("Client: Zeit Synchronosieung gestartet");
        ArrayList<Long> times = new ArrayList<Long>();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        ArrayList<Socket> sockets = new ArrayList<Socket>();
        for (int id = self.id - 1 ; id > 0; id--) {
        	Socket socket = findSocket(id);
        	if (socket != null) {
        		sockets.add(socket);
        	}
        }
        for (Socket s : sockets) {
            Thread t = new Thread(() -> {
                try {
                    OutputStream out = s.getOutputStream();
                    InputStream in = s.getInputStream();
                    Message timeMsg = new P2PTellMeYourTimeMsg(self);
                    out.write(timeMsg.create());
                    System.out.println("Client: P2PTellMeYourTimeMsg gesendet");
                    boolean res = StreamHelper.waitForTag(in, 200, 12, (int tag) -> {
                        ConnectionHandler handler = new ConnectionHandler(s, nodes, self, tag);
                        Thread thread = new Thread(handler);
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {}
                    });
                    if (!res) {
                        return;
                    }
                    P2PHereIsMyTimeMsg myTimeMsg = new P2PHereIsMyTimeMsg();
                    myTimeMsg.read(in);
                    times.add(myTimeMsg.time);
                    Date date = new Date();
                    date.setTime(myTimeMsg.time);
                    System.out.println("Client: Zeit von peer empfangen: "  + date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threads.add(t);
            t.start();
        }
        ThreadHelper.multiJoin(threads);
        long sum = 0;
        for (long time : times) {
            sum += time;
        }
        sum += System.currentTimeMillis();
        long avg = sum / (times.size() + 1);
        byte[] newTimeMsg = new P2PHereIsYourNewTimeMsg(self, avg).create();
        for (Socket socket : sockets) {
            Thread t = new Thread(() -> {
                OutputStream out;
                try {
                    out = socket.getOutputStream();
                    out.write(newTimeMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
        Date date = new Date();
        date.setTime(avg);
        System.out.println("Client: Neue Zeit an alle gesendet: "  + date);
    }
    
    public Socket findSocket(int id) {
		try {
	        Node receiver = nodes.getNode(id);
	        if (receiver == null) {
	            NodeSearch search = new NodeSearch(nodes, self, (short) id);
	            Thread t = new Thread(search);
	            t.start();
	            try {
	                t.join();
	            } catch (InterruptedException e) {}
	        }
	        receiver = nodes.getNode(id);
	        if (receiver == null) {
	            return null;
	        }
	        Socket socket;
			socket = new Socket(receiver.ip, receiver.port);
			return socket;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public TimeSync(NodeList nodes, Node self) {
        this.nodes = nodes;
        this.self = self;
    }
}
