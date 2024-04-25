import clientMessages.*;
import gameLogic.GameState;
import serverMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private int clientCount;
    private Server server;
    private String username;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private gameLogic.Player player;
    private ClientThread opponent;
    private gameLogic.GameController gameController;

    ClientThread(Socket socket, int clientCount, Server server) {
        this.socket = socket;
        this.clientCount = clientCount;
        this.server = server;
    }

    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            socket.setTcpNoDelay(true);
        } catch (Exception e) {
            server.getServerCallback().accept("Stream is not open, exiting program");
            return;
        }

        while (true) {
            try {
                Object obj = in.readObject();
                switch (obj) {
                    case clientMessages.AcceptInvite message:
                        this.acceptInvite(message.getUsername());
                        break;
                    case clientMessages.AddToQueue message:
                        this.addToQueue();
                        break;
                    case clientMessages.CreateUsername message:
                        this.createUsername(message.getUsername());
                        break;
                    case clientMessages.GetQueue message:
                        this.getQueue();
                        break;
                    case clientMessages.InviteUser message:
                        this.inviteUser(message.getUsername());
                        break;
                    case clientMessages.SendBoard message:
                        this.addBoard(message.getBoard());
                        break;
                    case clientMessages.Shoot message:
                        this.shoot(message.getCoord());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected object was sent: " + obj);
                }
            } catch (IOException | ClassNotFoundException e) {
                server.getServerCallback().accept("Client #" + this.clientCount + " ran into an error, breaking out of read loop");
            }
        }
    }

    // accept invite from a user
    public void acceptInvite(String username) {
        ClientThread client = this.server.getClient(username);
        if (client == null)
           throw new IllegalStateException("You're trying to accept an invite from someone who doesn't exist");
        if (client == this)
            throw new IllegalStateException("You're trying to accept an invite from yourself");
        boolean success = false;
        if (this.server.getQueue().contains(username)) { // if user is still in the queue
            success = true;
            assignGameController(client);
        }

        this.writeToClient(new serverMessages.AcceptInvite(success, username)); // send accept invite response to the client who sent accepted
        client.writeToClient(new serverMessages.AcceptInvite(success, this.username)); // send accept invite response to the client who invited
    }

    // shoot the shot on the board
    public void shoot(gameLogic.Coordinate coord) {
        if (this.getPlayer() == null)
            throw new IllegalStateException("You're trying to shoot when your player is not initialized");
        if (this.gameController == null)
            throw new IllegalStateException("You're trying to shoot a shot when there isn't a game");
        boolean result = this.gameController.makeMove(coord.getX(), coord.getY(), this.getPlayer());

        boolean gameOver = this.gameController.getGameState() == GameState.FINISHED;
        gameLogic.Coordinate[] revealedShip = null;
        boolean hit = false;
        if (result) {
            hit = true;
            revealedShip = getRevealedShip(coord);
        }
        this.sendShoot(coord, hit, revealedShip, gameOver); // send shoot object back to both clients
    }

    // helper function to get the revealed ship given a coord of the hit
    private gameLogic.Coordinate[] getRevealedShip(gameLogic.Coordinate coord) {

    }

    // send shoot object to both client playing the game
    public void sendShoot(gameLogic.Coordinate coord, boolean hit, gameLogic.Coordinate[] revealedShip, boolean gameOver) {
        new serverMessages.Shoot(coord, hit, revealedShip, gameOver);
    }
    // process and add the board
    public void addBoard(gameLogic.Board board) {
        if (gameController.initializeBoard(this.player, board)) { // if true, game is ready to start
            this.sendGameReady();
        }
    }
    // send game ready object to both clients apart from game
    public void sendGameReady() {
        serverMessages.SendGameReady message = new serverMessages.SendGameReady();
        this.writeToClient(message); // write to your own client
        this.opponent.writeToClient(message); // write to the other client, so they know it started as well
    }
    // assign game controller to itself and respective client
    public void assignGameController(ClientThread client) {
        gameLogic.Player player1 = this.getPlayer();
        gameLogic.Player player2 = this.getPlayer();
        if (player1 == null || player2 == null)
            throw new IllegalStateException("Cannot assign gameController to a null player");
        gameLogic.GameController gameController = new gameLogic.GameController(player1, player2);
        client.setGameController(gameController);
        this.setGameController(gameController);
        this.setOpponent(client);
        client.setOpponent(this);
    }
    // send message data about the invitation to the respected client
    public void inviteUser(String username) {
        ClientThread client = this.server.getClient(username);
        if (client == null)
            throw new IllegalStateException("You're trying to invite a user that doesn't exist");
        if (client == this)
            throw new IllegalStateException("A client cannot invite itself");
        serverMessages.SendInvite message = new serverMessages.SendInvite(username);
        client.writeToClient(message); // send message to respective client
    }
    // write the queue received from the server to the client
    public void getQueue() {
        serverMessages.GetQueue message = new serverMessages.GetQueue(this.server.getQueue());
        writeToClient(message);
    }
    public void addToQueue() {
        this.server.addToQueue(this.username);
    }

    public void createUsername(String username) {
        boolean success = false;
        if (usernameExists(username)) { // if username exists
            // add username to client
            success = true;
            this.username = username;
            this.server.addUsername(username);
            this.player = new gameLogic.Player(username); // assign player to client
        }

        // send status back to client
        serverMessages.CreateUsername message = new serverMessages.CreateUsername(username, success);

    }
    public boolean usernameExists(String username) {
        for (String tempUsername : this.server.getUsernames())
            if (tempUsername.equals(username))
                return true;
        return false;
    }

    private void writeToClient(Object message) {
        try {
            out.writeObject(message);
        } catch (IOException e) { /* do nothing */ }
    }
    public String getUsername() { return this.username; }
    public gameLogic.Player getPlayer() { return this.player; }

    public void setGameController(gameLogic.GameController gameController) { this.gameController = gameController; }
    public void setOpponent(ClientThread client) { this.opponent = client; }
}
