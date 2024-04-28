package serverMessages;

import java.io.Serializable;

public class SendGameReady implements Serializable {
  private static final long serialVersionUID = 1;
  private boolean goesFirst;

  public SendGameReady(boolean goesFirst) {
    this.goesFirst = goesFirst;
  }

  public boolean goesFirst() {
    return this.goesFirst;
  }

}
