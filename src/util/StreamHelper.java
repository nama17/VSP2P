package util;

import java.io.IOException;
import java.io.InputStream;

public class StreamHelper {
    public static boolean waitForTag(InputStream in, int millis, int tag, Runnable wrongTagHandler) {
        try {
            long maxTime = millis + System.currentTimeMillis();
            while (System.currentTimeMillis() < maxTime) {
                if (in.available() > 0) {
                    int readTag = in.read();
                    if (readTag != tag) {
                        wrongTagHandler.run();
                        continue;
                    }
                    return true;
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
