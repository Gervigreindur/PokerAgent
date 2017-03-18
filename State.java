package PokerAgent;

import java.util.concurrent.ThreadLocalRandom;

public class State {
	
	Player me;
	Board myBoard;
	
	public State(Player agent, Board board) {
		me = agent;
		myBoard = board;

	}
	
	public void update() {
		
	}
	
	public int monteCarloSimulation() {
		
		return ThreadLocalRandom.current().nextInt(1, 4); 
	}
}
