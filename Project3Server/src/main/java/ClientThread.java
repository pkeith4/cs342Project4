import clientMessages.*;
import serverMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private int clientCount;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;

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
                        break;
                    case clientMessages.AddToQueue message:
                        break;
                    case clientMessages.CreateUsername message:
                        break;
                    case clientMessages.GetQueue message:
                        break;
                    case clientMessages.InviteUser message:
                        break;
                    case clientMessages.SendBoard message:
                        break;
                    case clientMessages.Shoot message:
                        break;
                    default:
                        throw new IllegalStateException("Unexpected object was sent: " + obj);
                }
            } catch (IOException | ClassNotFoundException e) {
                server.getServerCallback().accept("Client #" + this.clientCount + " ran into an error, breaking out of read loop");
            }
        }
    }
}