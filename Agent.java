package PokerAgent;


public class Agent extends Player {

	Board board;

	Agent(String name, int deposit, Board board) {
		super(name, deposit);
		this.board = board;
	}
	
	public String getInput() {
		System.out.println(Integer.toString(monteCarloSimulation()));
		return Integer.toString(monteCarloSimulation());
	}
	
	public int monteCarloSimulation() {
		MonteCarloSimulation mcts = new MonteCarloSimulation(this, board);
		
		return mcts.simulate(); 
	}

	
}
