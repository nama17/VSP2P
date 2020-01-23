package util;

import java.util.ArrayList;

public class ThreadHelper {
    public static void multiJoin(ArrayList<Thread> threads, int millis) {
        long maxTime = System.currentTimeMillis() + millis;
        for (Thread t : threads) {
            long wait = maxTime - System.currentTimeMillis();
            wait = wait < 50 ? 50 : wait;
            try {
                t.join(wait);
            } catch (InterruptedException e) {}
        }
    }
    
    public static void multiJoin(ArrayList<Thread> threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {}
        }
    }
}
