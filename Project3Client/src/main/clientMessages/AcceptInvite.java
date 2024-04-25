package clientMessages;

import java.io.Serializable;

public class AcceptInvite implements Serializable {
    private static final long serialVersionUID = 1;
    private final String username;

    public AcceptInvite(String username) {
        this.username = username;
    }

    // Username that you're accepting the invite from
    public String getUsername() { return this.username; }
}
