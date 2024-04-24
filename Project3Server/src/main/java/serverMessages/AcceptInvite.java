package serverMessages;

import java.io.Serializable;

public class AcceptInvite implements Serializable {
    private static final long serialVersionUID = 1;
    private final boolean success;

    public AcceptInvite(boolean success) {
        this.success = success;
    }

    /*
        Get success status of accepting the invite.
        If someone invites you, then accepts another persons party,
        and then you accept, the accept invite should fail.
     */
    public boolean getSuccess() { return this.success; }
}
