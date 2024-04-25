import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

import serverMessages.*;

public class Client extends Thread{

    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    // callback functions
    Consumer<serverMessages.CreateUsername> createUsernameCallback;
    Consumer<serverMessages.AcceptInvite> onAcceptInviteCallback;
    Consumer<serverMessages.GetQueue> getQueueCallback;
    Consumer<serverMessages.SendInvite> onInviteRequestCallback;
    Consumer<serverMessages.SendGameReady> onGameReadyCallback;
    Consumer<serverMessages.Shoot> onOpponentShootCallback;

    Client() { /* do nothing */ }

    @Override
    public void run() {
        try {
            // connect to the server
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);

            while (true) {
                try {
                    // read messages from the server
                    Object obj = in.readObject();
                    switch (obj) {
                        case serverMessages.CreateUsername message:
                            createUsernameCallback.accept(message);
                            break;
                        case serverMessages.AcceptInvite message:
                            onAcceptInviteCallback.accept(message);
                            break;
                        case serverMessages.GetQueue message:
                            getQueueCallback.accept(message);
                            break;
                        case serverMessages.SendGameReady message:
                            onGameReadyCallback.accept(message);
                            break;
                        case serverMessages.SendInvite message:
                            onInviteRequestCallback.accept(message);
                            break;
                        case serverMessages.Shoot message:
                            onOpponentShootCallback.accept(message);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected object was sent: " + obj);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not connect to server: " + e.getMessage());
        } finally {
            close(); // close socket when the loop disconnects
        }
    }

    private void writeToServer(Object message) {
        try {
            out.writeObject(message);
        } catch (IOException e) { /* do nothing */ }
    }

    /*
        This function is going to initialize the callback
        for when the opponent shoots a shot. We will use the callback
        function to understand where the opponent shot, and a wait function
        to understand when their turn is over
     */
    public void onOpponentShoot(Consumer<serverMessages.Shoot> callback) {
        this.onOpponentShootCallback = callback;
    }
    // Make a hit on the opponents ship
    public void shoot(int row, int col) {
        clientMessages.Shoot message = new clientMessages.Shoot(row, col);
        writeToServer(message);
    }
    // Send the board to the server
    public void sendBoard(int[][][] ships) {
        clientMessages.SendBoard message = new clientMessages.SendBoard(ships);
        writeToServer(message);
    }
    /*
        Function to initialize the game ready callback response
        When both users finalize their boards, the server will send
        a serverMessages.sendGameReady object to both clients, that will get picked up
        and trigger the callback in this functions parameter
     */
    public void onGameReady(Consumer<SendGameReady> callback) {
        this.onGameReadyCallback = callback;
    }
    /*
        Function to initialize the invited callback response
        The callback passed in the parameter will run when
        someone else sends an invitation to this user
     */
    public void onInviteRequest(Consumer<serverMessages.SendInvite> callback) {
        this.onInviteRequestCallback = callback;
    }
    // get the queue of people waiting for a game
    public void getQueue(Consumer<serverMessages.GetQueue> callback) {
        this.getQueueCallback = callback;
    }
    /*
        wait for server to say an invitation was accepted
        Note: call this function before you run Client.acceptInvite
        Client.acceptInvite will near instantly cause a response from the server
        that Client.onAcceptInvite will pick up
     */
    public void onAcceptInvite(Consumer<serverMessages.AcceptInvite> callback) {
        this.onAcceptInviteCallback = callback;
    }
    // accept the invite from another user
    public void acceptInvite(String username) {
        clientMessages.AcceptInvite message = new clientMessages.AcceptInvite(username);
        writeToServer(message);
    }
    // add the client to the game queue
    public void addToQueue() {
        clientMessages.AddToQueue message = new clientMessages.AddToQueue();
        writeToServer(message);
    }

    // create/validate username from server
    public void createUsername(String username, Consumer<serverMessages.CreateUsername> callback) {
        this.createUsernameCallback = callback;

        clientMessages.CreateUsername message = new clientMessages.CreateUsername(username);
        writeToServer(message);
    }

    // close the socket connection
    public void close() {
        try {
            if (socketClient != null) {
                socketClient.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client resources: " + e.getMessage());
        }
    }
}