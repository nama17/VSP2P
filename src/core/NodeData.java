package core;

import java.util.ArrayList;

public class NodeData {
    public int id;
    public ArrayList<Action> onFound = new ArrayList<Action>();
    public ArrayList<Integer> serachData = new ArrayList<Integer>(); // SearchIDs
    
    public NodeData(int id) {
        this.id = id;
    }

}
