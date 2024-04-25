package serverMessages;

import java.io.Serializable;

public class AcceptInvite implements Serializable {
    private static final long serialVersionUID = 2;
    private final boolean success;
    private final String username;

    public AcceptInvite(boolean success, String username) {
        this.success = success;
        this.username = username;
    }

    /*
        Get success status of accepting the invite.
        If someone invites you, then accepts another persons party,
        and then you accept, the accept invite should fail.
     */
    public boolean getSuccess() { return this.success; }
    /*
        Get username that accepted the invite.
        Because AcceptInvite is sent to the accepter and the inviter,
        Sending the username tells the inviter who accepted their match
     */
    public String getUsername() { return this.username; }
}
