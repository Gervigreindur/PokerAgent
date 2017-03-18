package PokerAgent;

public class GameRoom {

	public static void main(String[] args) {
		Board board = new Board(8);
		
		board.addPlayer(new Agent("Gisli", 1000, board));
		board.addPlayer(new Agent("Steini", 1000, board));
		board.addPlayer(new Agent("Vedis", 1000, board));
		board.addPlayer(new Human("Einir", 1000));
		
		while(board.isActive()) {
			board.play();
		}
	}
}
