package serverMessages;

import java.io.Serializable;
import java.util.ArrayList;

public class GetQueue implements Serializable {
    static final long serialVersionUID = 1;
    private ArrayList<String> queue;

    public GetQueue(ArrayList<String> queue) {
       this.queue = queue;
    }

    // return a list of the queue of players waiting to join a game
    public ArrayList<String> getQueue() { return this.queue; }
}
