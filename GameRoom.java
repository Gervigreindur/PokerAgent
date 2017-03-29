package PokerAgent;

public class GameRoom {
	
	public static int buyIn = 50;

	public static void main(String[] args) {
		
		Board board = new Board(8);

		board.addPlayer(new Agent("Terminator", buyIn, board));
		board.addPlayer(new RandomAgent("Spongebob", buyIn, board));
		board.addPlayer(new Human("Chuck Norris", buyIn));
		//board.addPlayer(new Agent("Einir", buyIn, board));

		
		while(board.isActive()) {
			board.play();
		}
	}
}
