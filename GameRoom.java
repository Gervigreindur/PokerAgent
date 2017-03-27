package PokerAgent;

public class GameRoom {
	
	public static int buyIn = 50;

	public static void main(String[] args) {
		Board board = new Board(8);
		
		//board.addPlayer(new Human("Gisli", buyIn));
		board.addPlayer(new Agent("Steini", buyIn, board));
		board.addPlayer(new Agent("Vedis", buyIn, board));
		board.addPlayer(new Agent("Einir", buyIn, board));
		
		while(board.isActive()) {
			board.play();
		}
	}
}
