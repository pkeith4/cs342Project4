package clientMessages;

import gameLogic.Board;
import java.io.Serializable;

public class SendBoard implements Serializable {
  public static long serialVersionUID = 1;
  private gameLogic.Board board;

  public SendBoard(gameLogic.Board board) {
    this.board = board;
  }

  public gameLogic.Board getBoard() {
    return this.board;
  }
}
