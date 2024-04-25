package serverMessages;

import java.io.Serializable;

public class CreateUsername implements Serializable {
    static final long serialVersionUID = 2;
    private String username;
    private boolean success;

    public CreateUsername(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    // return the username that was unsuccessful/successful in creating
    public String getUsername() { return this.username; }
    // return the success status of trying to create the username
    public boolean getSuccess() { return this.success; }
}
