import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server extends Thread {
    private Consumer<String> serverCallback;
    private ArrayList<ClientThread> clients;
    private ArrayList<String> usernames;
    private ArrayList<String> queue;

    @Override
    public void run() {
        ServerSocket socket;
        try {
            socket = new ServerSocket(5555);
        } catch (IOException e) {
            serverCallback.accept("Fatal error: Server could not launch");
            return;
        }
        try {
            serverCallback.accept("Server has launched, awaiting client connections...");

            int clientCounter = 1;
            while (true) { // iterate infinitely looking for new socket connections
                ClientThread client = new ClientThread(socket.accept(), clientCounter, this);
                serverCallback.accept("Client #" + clientCounter + " has connected!");
                clients.add(client);
                client.start();

                clientCounter++; // increment the client counter
            }
        }  catch(Exception e) {
            serverCallback.accept("Fatal exception caused in client connection loop: " + e.toString());
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addUsername(String username) { this.usernames.add(username); }
    public void addToQueue(String username) {
        if (this.queue.contains(username)) {
            throw new IllegalStateException("Cannot add duplicate username to queue");
        }
        this.queue.add(username);
    }
    public void removeFromQueue(String username) {
        if (!this.queue.remove(username))
            throw new IllegalStateException("You are attempting to remove a username that does not exist in the queue");
    }

    public ClientThread getClient(String username) {
       for (ClientThread client : this.clients) { // iterate through clients
          if (client.getUsername().equals(username)) {
              return client;
          }
       }
       return null;
    }
    public ArrayList<String> getQueue() { return this.queue; }
    public ArrayList<String> getUsernames() { return this.usernames; }
    public Consumer<String> getServerCallback() { return this.serverCallback; }
    // close the socket connection
//    public void close() {
//        try {
//            if (socketClient != null) {
//                socketClient.close();

//            if (out != null) {
//                out.close();
//            }
//            if (in != null) {
//                in.close();
//            }
//        } catch (IOException e) {
//            System.out.println("Error closing client resources: " + e.getMessage());
//        }
//    }
}
