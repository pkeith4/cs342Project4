package clientMessages;

import java.io.Serializable;

public class InviteUser implements Serializable {
    static final long serialVersionUID = 1;
    private final String username;

    public InviteUser(String username) {
       this.username = username;
    }

    // Username of user you're trying to invite
    public String getUsername() { return this.username; }
}
