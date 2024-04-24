package clientMessages;

import java.io.Serializable;

public class CreateUsername implements Serializable {
    static final long serialVersionUID = 1;
    private String username;

    public CreateUsername(String username) {
        this.username = username;
    }

    String getUsername() { return this.username; }
}
