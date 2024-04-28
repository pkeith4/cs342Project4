import clientMessages.*;
import gameLogic.Coordinate;
import gameLogic.GameState;
import gameLogic.AIPlayer;
import serverMessages.*;
import serverMessages.GetQueue;
import java.util.ArrayList;
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
  private boolean isAI;
  private AIPlayer ai;
  private boolean isAIGame;

  ClientThread(Socket socket, int clientCount, Server server, boolean isAI) {
    this.socket = socket;
    this.clientCount = clientCount;
    this.server = server;
    this.isAI = isAI;
    this.isAIGame = false;

    if(isAI) {
      server.getServerCallback().accept("AI Thread Succesfully started");
      instantiateAI();
      this.player = ai.getPlayer();
      this.isAIGame = true;
    }
  }

  public void run() {
    try {
//      if(!isAI) {
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        socket.setTcpNoDelay(true);
//      }
    } catch (Exception e) {
      server.getServerCallback().accept("Stream is not open, exiting program");
      return;
    }

    while (true) {
      try {
        Object obj = in.readObject();
        if(!isAI) {
          handleCommand(obj);
          server.getServerCallback().accept("command Hnadler invoked");
        } else {
          server.getServerCallback().accept("AIcommand Hnadler invoked");
          aiCommandHandler(obj);
        }
      } catch (IOException | ClassNotFoundException e) {
        server.getServerCallback()
            .accept("Client #" + this.clientCount + " ran into an error, breaking out of read loop");
        break;
      }
    }
  }

  private void aiCommandHandler(Object obj){
    try {
      if (obj instanceof clientMessages.SendBoard) {
        // Receive the board setup from the server.
        server.getServerCallback().accept("AI Thread recieved sendBoard message");
        clientMessages.SendBoard message = (clientMessages.SendBoard) obj;
        this.addBoard(message.getBoard());
        if (isAI) {
          performAIActions(); // AI decides its moves after board setup
        }
      } else if (obj instanceof clientMessages.Shoot) {
        // Handle incoming shot from another player
        clientMessages.Shoot message = (clientMessages.Shoot) obj;
        server.getServerCallback().accept("AI Thread recieved shot");
        if(!message.getShotFromAI()) {
          this.shoot(message.getCoord(), message.getShotFromAI());  // recieved from player
        }
      } else if (obj instanceof clientMessages.RemoveFromQueue) {
        // If AI needs to be removed from queue, typically not needed for AI but included for completeness
        this.removeFromQueue();
      } else if (obj instanceof clientMessages.PlayAI) {
        // AI is invoked to play against another client
        server.getServerCallback().accept("Client #" + clientCount + " is playing a game against AI.");
        startGameAgainstAI();
      } else {
        server.getServerCallback().accept("AI received an unexpected command.");
      }
    } catch (Exception e) {
      server.getServerCallback().accept("AI Thread had an error processing command: " + e.getMessage());
    }
  }

  private void performAIActions() {
    // This method is where AI processes its strategies and makes moves
    Coordinate aiMove = this.ai.makeMove(); // Assuming AIPlayer class has a method to decide moves
    shoot(aiMove, true); // AI performs a shoot based on its decision
  }


  private void handleCommand(Object obj) {
    try {
      if (obj instanceof clientMessages.AcceptInvite) {
        clientMessages.AcceptInvite message = (clientMessages.AcceptInvite) obj;
        this.acceptInvite(message.getUsername());
      } else if (obj instanceof clientMessages.AddToQueue) {
        this.addToQueue();
      } else if (obj instanceof clientMessages.CreateUsername) {
        clientMessages.CreateUsername message = (clientMessages.CreateUsername) obj;
        this.server.getServerCallback().accept("Incoming username...");
        this.createUsername(message.getUsername());
      } else if (obj instanceof clientMessages.GetQueue) {
        this.server.getServerCallback().accept("Incoming get queue request...");
        this.getQueue();
      } else if (obj instanceof clientMessages.InviteUser) {
        clientMessages.InviteUser message = (clientMessages.InviteUser) obj;
        this.server.getServerCallback().accept("Incoming invite user request...");
        this.inviteUser(message.getUsername());
      } else if (obj instanceof clientMessages.SendBoard) {
        this.server.getServerCallback().accept("Incoming send board request...");
        clientMessages.SendBoard message = (clientMessages.SendBoard) obj;
        this.addBoard(message.getBoard());
      } else if (obj instanceof clientMessages.Shoot) {
        clientMessages.Shoot message = (clientMessages.Shoot) obj;
        if(isAIGame && !message.getShotFromAI()) {
//          do nothing because client recieved shoot message from itself
        } else {
          this.shoot(message.getCoord(), message.getShotFromAI());
        }
      } else if (obj instanceof clientMessages.RemoveFromQueue) {
        this.removeFromQueue();
      } else if (obj instanceof clientMessages.PlayAI) {
        server.getServerCallback().accept("Client #" + clientCount + " has started a game against the AI...");
        startGameAgainstAI();
      } else {
        server.getServerCallback().accept("Error: Unexpected command type received.");
      }
    } catch (Exception e) {
      server.getServerCallback().accept("Error processing command: " + e.getMessage());
    }
  }

  public void removeFromQueue() {
    this.server.removeFromQueue(this.getUsername());
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
      // remove both users from queue
      this.server.removeFromQueue(this.getUsername());
      this.server.removeFromQueue(client.getUsername());
    }
    this.writeToClient(new serverMessages.AcceptInvite(success, username)); // send accept invite response to the client
                                                                            // who sent accepted
    client.writeToClient(new serverMessages.AcceptInvite(success, this.username)); // send accept invite response to the
                                                                                   // client who invited
  }


  public void startGameAgainstAI() {
    try {
      // Create a new socket for the AI to use
      Socket aiSocket = new Socket("127.0.0.1", 5556);
      isAIGame = true;
      ClientThread AI = new ClientThread(aiSocket, clientCount, server, true);
      AI.start();  // Start the AI thread

      setOpponent(AI);
      AI.setOpponent(this);

      // Link AI Player with this thread for game control
      this.gameController = new gameLogic.GameController(player, AI.getPlayer());
      this.gameController.initializeBoard(AI.getPlayer(), AI.getPlayer().getBoard());
    } catch (IOException e) {
      server.getServerCallback().accept("Error setting up AI game: " + e.getMessage());
    }
  }

  private void instantiateAI() {
    if(isAI) {
      this.ai = new AIPlayer();
      if(this.ai != null) {
        server.getServerCallback().accept("AI Initialized");
      } else {
        server.getServerCallback().accept("AI Initialization failed");
      }
    }
  }

  // shoot the shot on the board
  public synchronized void shoot(gameLogic.Coordinate coord, boolean shotFromAI) {
    if (this.getPlayer() == null)
      throw new IllegalStateException("You're trying to shoot when your player is not initialized");
    if (this.gameController == null)
      throw new IllegalStateException("You're trying to shoot a shot when there isn't a game");

    try {
      boolean result = this.gameController.makeMove(coord.getX(), coord.getY(), this.getPlayer());
      boolean gameOver = this.gameController.getGameState() == GameState.FINISHED;
      gameLogic.Coordinate[] revealedShip = null;
      boolean hit = false;
      if (result) {
        hit = true;
        revealedShip = this.opponent.getRevealedShip(coord);
      }
      this.sendShoot(coord, hit, revealedShip, gameOver); // send shoot object back to both clients
    } catch (Exception e) {
      server.getServerCallback().accept("Error during shooting action: " + e.getMessage());
    }
  }

  // helper function to get the revealed ship given a coord of the hit
  private synchronized gameLogic.Coordinate[] getRevealedShip(gameLogic.Coordinate coord) {
    try {
      for (gameLogic.Ship ship : this.getPlayer().getBoard().getShips()) {
        if (this.coordInShip(coord, ship) && ship.isSunk()) { // if coordinate is in ship, assume it was hit | also
                                                              // check that ship was sunk
          // convert ship to gameLogic.Coordinate[]
          return shipToList(ship);
        }
      }
    } catch (Exception e) {
      server.getServerCallback().accept("Error finding revealed ship: " + e.getMessage());
    }
    return null;
  }

  private synchronized gameLogic.Coordinate[] shipToList(gameLogic.Ship ship) {
    gameLogic.Coordinate[] list = new gameLogic.Coordinate[ship.getSize()];
    try {
      for (int i = 0; i < ship.getSize(); i++) {
        int x = ship.getStartX();
        int y = ship.getStartY();
        if (ship.isVertical())
          y += i;
        else
          x += i;
        list[i] = new Coordinate(x, y);
      }
    } catch (Exception e) {
      server.getServerCallback().accept("Error converting ship to coordinate list: " + e.getMessage());
    }
    return list;
  }

  private boolean coordInShip(gameLogic.Coordinate coord, gameLogic.Ship ship) {
    if (ship.isVertical() && coord.getX() != ship.getStartX())
      return false;
    if (!ship.isVertical() && coord.getY() != ship.getStartY())
      return false;
    if (coord.getX() < ship.getStartX())
      return false;
    if (coord.getY() < ship.getStartY())
      return false;
    if (!ship.isVertical() && coord.getX() > ship.getStartX() + ship.getSize())
      return false;
    if (ship.isVertical() && coord.getX() != ship.getStartX())
      return false;
    if (ship.isVertical() && coord.getY() > ship.getStartY() + ship.getSize())
      return false;
    if (!ship.isVertical() && coord.getY() != ship.getStartY())
      return false;
    return true;
  }

  // send shoot object to both client playing the game
  public void sendShoot(gameLogic.Coordinate coord, boolean hit, gameLogic.Coordinate[] revealedShip, boolean gameOver) {
    serverMessages.Shoot message = new serverMessages.Shoot(coord, hit, revealedShip, gameOver, isAI);
    // send message to both clients
    this.writeToClient(message);
    this.opponent.writeToClient(message);
  }

  // process and add the board
  public synchronized void addBoard(gameLogic.Board board) {
    try {
      System.out.println("my ships " + this.getUsername() + ":");
      System.out.println(board.getShips());
      this.player.setShips(board.getShips());
      if (gameController.initializeBoard(this.player, board)) { // if true, game is ready to start
        this.sendGameReady();
      }
    } catch (Exception e) {
      server.getServerCallback().accept("Error adding board: " + e.getMessage());
    }
  }

  // send game ready object to both clients apart from game
  public void sendGameReady() {
    this.writeToClient(new serverMessages.SendGameReady(this.gameController.getPlayer1() == this.player)); // write to
                                                                                                           // your own
                                                                                                           // client
    this.opponent
        .writeToClient(new serverMessages.SendGameReady(this.gameController.getPlayer1() == this.opponent.player)); // write
                                                                                                                    // to
                                                                                                                    // the
                                                                                                                    // other
                                                                                                                    // client,
                                                                                                                    // so
                                                                                                                    // they
                                                                                                                    // know
                                                                                                                    // it
                                                                                                                    // started
                                                                                                                    // as
                                                                                                                    // well
  }

  // assign game controller to itself and respective client
  public synchronized void assignGameController(ClientThread client) {
    try {
      gameLogic.Player player1 = this.getPlayer();
      gameLogic.Player player2 = client.getPlayer();
      if (player1 == null || player2 == null)
        throw new IllegalStateException("Cannot assign gameController to a null player");
      gameLogic.GameController gameController = new gameLogic.GameController(player1, player2);
      client.setGameController(gameController);
      this.setGameController(gameController);
      this.setOpponent(client);
      client.setOpponent(this);
    } catch (Exception e) {
      server.getServerCallback().accept("Error assigning game controller: " + e.getMessage());
    }
  }

  // send message data about the invitation to the respected client
  public void inviteUser(String username) {
    ClientThread client = this.server.getClient(username);
    if (client == null)
      throw new IllegalStateException("You're trying to invite a user that doesn't exist");
    if (client == this)
      throw new IllegalStateException("A client cannot invite itself");
    serverMessages.SendInvite message = new serverMessages.SendInvite(this.getUsername());
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
    if (!usernameExists(username)) { // if username exists
      // add username to client
      success = true;
      this.username = username;
      this.server.addUsername(username);
      this.player = new gameLogic.Player(username); // assign player to client
    }

    // send status back to client
    serverMessages.CreateUsername message = new serverMessages.CreateUsername(username, success);
    writeToClient(message);
  }

  public boolean usernameExists(String username) {
    for (String tempUsername : this.server.getUsernames())
      if (tempUsername.equals(username))
        return true;
    return false;
  }

  public void sendQueueChange(String username, boolean addition) {
    serverMessages.QueueChange message = new serverMessages.QueueChange(username, addition);
    writeToClient(message); // send to client

  }

  private void writeToClient(Object message) {
    try {
      out.writeObject(message);
      out.reset();
    } catch (IOException e) {
      /* do nothing */ }
  }

  public String getUsername() {
    return this.username;
  }

  public gameLogic.Player getPlayer() {
    return this.player;
  }

  public void setGameController(gameLogic.GameController gameController) {
    this.gameController = gameController;
  }

  public void setOpponent(ClientThread client) {
    this.opponent = client;
  }

  public void setOut(ObjectOutputStream out) {
    this.out = out;
    if(isAI && out != null){
      server.getServerCallback().accept("AI outputStream succesfully set");
    }
  }

  public void setIn(ObjectInputStream in) {
    this.in = in;
    if(isAI && in != null ){
      server.getServerCallback().accept("AI inputStream succesfully set");
    }
  }
}
