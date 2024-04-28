// package gameLogic;
//
// import java.util.Random;
//
// public class AIPlayer {
// private Board board;
// private Random random;
//
// public AIPlayer() {
// this.board = new board;
// this.random = new Random();
// // need to implement random placement
// intializeShips(board);
// }
//
// private void intializeShips(board board){
// int[] shipSize = [2, 2, 3, 4, 5];
// for (int size: shipSizes) {
// Ship ship = new Ship(size);
// boolean placed = false;
// while (!placed) {
// int x = random.nextInt(10);
// int y = random.nextInt(10);
// boolean vertical = random.nextBoolean();
// placed = board.placeShip(ship, x, y, vertical);
// }
// }
// }
//
// public Coordinate makeMove() {
// // Implement the logic to decide a move
// return decideMove();
// }
//
// private Coordinate decideMove() {
// int x, y;
// do {
// x = random.nextInt(10);
// y = random.nextInt(10);
// } while (!board.isValidMove(x, y));
//
// return new Coordinate(x, y);
// }
// }
