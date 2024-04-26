package serverMessages;

import java.io.Serializable;

public class QueueChange implements Serializable {
    public static long serialVersionUID = 1;
    private String change;
    private boolean addition;

    public QueueChange(String change, boolean addition) {
        this.change = change;
        this.addition = addition;
    }

    public String getChange() { return this.change; }
    public boolean getAddition() { return this.addition; }
}
