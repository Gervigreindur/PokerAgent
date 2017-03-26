package PokerAgent;

import java.util.concurrent.ThreadLocalRandom;

public class RandomAgent extends Player {
	State environment;
	Board board;
	RandomAgent(String name, int deposit, Board board) {
		super(name, deposit);
		this.board = new Board(board);
		environment = new State(board, this);
	}
	
	public String getInput() {
		Integer value = ThreadLocalRandom.current().nextInt(1, 4);
		return value.toString();
	}
	
}
