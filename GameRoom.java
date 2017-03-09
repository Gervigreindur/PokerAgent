package Poker;

public class GameRoom {

	public static void main(String[] args) {
		Board board = new Board(2);
		
		board.addPlayer(new Player("Gisli", 1000));
		board.addPlayer(new Player("Steini", 1000));
		
		while(board.isActive()) {
			board.play();
		}
	}
}