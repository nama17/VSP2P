package core;

import java.util.ArrayList;

public class NodeDataList {
    private static ArrayList<NodeData> list = new ArrayList<NodeData>();
    
    public static void add(NodeData data) {
        list.add(data);
    }
    
    public static NodeData get(int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) {
                return list.get(i);
            }
        }
        return null;
    }
    
    public static synchronized void remove(int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) {
                list.remove(i);
                return;
            }
        }
    }

}
