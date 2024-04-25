package serverMessages;

import java.io.Serializable;

public class SendInvite implements Serializable {
    private static final long serialVersionUID = 1;
    private final String username;

    public SendInvite(String username) {
        this.username = username;
    }

    // Username that invited the user
    public String getUsername() { return this.username; }
}
