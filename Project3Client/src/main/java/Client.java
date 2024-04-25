import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

import serverMessages.*;
import clientMessages.*;

public class Client extends Thread{

    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    // callback functions
    Consumer<serverMessages.CreateUsername> createUsernameCallback;

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
                        default:
                            throw new IllegalStateException("Unexpected value: " + obj);
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
