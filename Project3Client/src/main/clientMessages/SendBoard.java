package clientMessages;

public class SendBoard {
    private gameLogic.Board board;

    public SendBoard(gameLogic.Board board) {
        this.board = board;
    }

    public gameLogic.Board getBoard() { return this.board; }
}