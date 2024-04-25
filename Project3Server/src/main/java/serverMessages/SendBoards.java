package serverMessages;

import java.io.Serializable;

public class serverMessages implements Serializable {
    private static final long serialVersionUID = 1;
    Board playerBoard;
    Board oppBoard;

    public SendBoards(Board playerBoard, Board oppBoard){
        this.playerBoard = playerBoard;
        this.oppBoard = oppBOard;
    }

}