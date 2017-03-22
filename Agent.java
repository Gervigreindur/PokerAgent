package PokerAgent;

import java.util.concurrent.ThreadLocalRandom;

public class Agent extends Player {

	State environment;
	Board board;
	Agent(String name, int deposit, Board board) {
		super(name, deposit);
		this.board = new Board(board);
		environment = new State(this, board);
	}
	
	public String getInput() {
		environment.update();
		Integer value = monteCarloSimulation();

		return value.toString();
	}
	
	public int monteCarloSimulation() {
		
		return ThreadLocalRandom.current().nextInt(1, 4); 
	}

	
}
