import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server extends Thread {
  private Consumer<String> serverCallback;
  private ArrayList<ClientThread> clients;
  private ArrayList<String> usernames;
  private ArrayList<String> queue;
  private int clientCounter;

  Server(Consumer<String> callback) {
    this.serverCallback = callback;
    this.clients = new ArrayList<>();
    this.usernames = new ArrayList<>();
    this.queue = new ArrayList<>();
    this.clientCounter = 1;
  }

  @Override
  public void run() {
    ServerSocket socket;
    try {
      socket = new ServerSocket(5556);
    } catch (IOException e) {
      serverCallback.accept("Fatal error: Server could not launch");
      return;
    }
    try {
      serverCallback.accept("Server has launched, awaiting client connections...");

      while (true) { // iterate infinitely looking for new socket connections
        ClientThread client = new ClientThread(socket.accept(), clientCounter, this, false);
        serverCallback.accept("Client #" + clientCounter + " has connected!");
        clients.add(client);
        client.start();

        clientCounter++; // increment the client counter
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Fatal exception: " + e.getMessage());
      serverCallback.accept("Fatal exception caused in client connection loop: " + e.getMessage());
    }
    try {
      socket.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void addUsername(String username) {
    this.usernames.add(username);
  }

  public void addToQueue(String username) {
    if (this.queue.contains(username)) {
      throw new IllegalStateException("Cannot add duplicate username to queue");
    }
    this.queue.add(username);
    this.serverCallback.accept("Sending queue change to clients 1-" + String.valueOf(this.clientCounter - 1));
    for (ClientThread client : this.clients) {
      client.sendQueueChange(username, true);
    }
  }

  public void removeFromQueue(String username) {
    if (!this.queue.remove(username))
      throw new IllegalStateException("You are attempting to remove a username that does not exist in the queue");
    for (ClientThread client : this.clients) {
      client.sendQueueChange(username, false);
    }
  }

  public ClientThread getClient(String username) {
    if (username == null)
      throw new IllegalStateException("Cannot get client with username null");
    for (ClientThread client : this.clients) { // iterate through clients
      if (client.getUsername() != null && client.getUsername().equals(username)) {
        return client;
      }
    }
    return null;
  }

  public ArrayList<String> getQueue() {
    return this.queue;
  }

  public ArrayList<String> getUsernames() {
    return this.usernames;
  }

  public Consumer<String> getServerCallback() {
    return this.serverCallback;
  }
}
