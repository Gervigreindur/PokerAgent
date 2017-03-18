package PokerAgent;

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
		Integer value = environment.monteCarloSimulation();

		return value.toString();
	}

	
}
