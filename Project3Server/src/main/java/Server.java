import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class Server extends Thread {
    private Consumer<String> serverCallback;
    private ArrayList<ClientThread> clients;

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

    public Consumer<String> getServerCallback() { return this.serverCallback; }

    // close the socket connection
//    public void close() {
//        try {
//            if (socketClient != null) {
//                socketClient.close();
//            }
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
