// Enumerations or state flags indicating whether it's the player's turn, if the game is waiting for the other player, etc.
package gameLogic;

public enum GameState {
    SETUP,       // Players are setting up their boards
    IN_PROGRESS, // Game is in progress
    FINISHED     // Game has ended
}
