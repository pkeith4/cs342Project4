package serverMessages;

import java.io.Serializable;

public class SendGameReady implements Serializable {
  private static final long serialVersionUID = 1;
  private String firstUsername;

  public SendGameReady(String firstUsername) {
    this.firstUsername = firstUsername;
  }

  public String getFirstUsername() {
    return this.firstUsername;
  }

}
