package PokerAgent;

public class MonteCarloSimulation {
	
	private Board myBoard;
	private Player me;
	private int result;
	private int numberOfSimulations;
	private int check;
	private int raise;

	public MonteCarloSimulation(Player agent, Board board) {
		myBoard = new Board(board);
		me = agent;
		numberOfSimulations = 1000;
		check = 0;
		raise = 0;
	}
	
	public int simulate() {
		
		
	
		for(int i = 0; i < numberOfSimulations; i++) {
			
			State simulation = new State(myBoard, me);
			simulation.simulateOpponentsHands(me);
			//simulation.dealCards();
			check += simulateAction(simulation, 1);
			raise += simulateAction(simulation, 2);
			
		}
		
		check = check / numberOfSimulations;
		raise = raise / numberOfSimulations;
		
		return Math.max(check, raise);
			
	}
	
	public int simulateAction(State simmi, int action) {
		/*
		 * TODO: return amount bet by player.
		if(simmi.isTerminal()) {
			
			return 0;
		}
		*/
		State simulation = new State(simmi);
		simulation.takeAction(action);
		
		int foldProb = propabilityOfFold(simulation.getCurrPlayHands());
		int checkCall = propabilityOfCheckCall(simulation.getCurrPlayHands());
		int raise = propabilityOfRaise(simulation.getCurrPlayHands());
		
		int decision = Math.max(Math.max(foldProb, checkCall), raise);
		
		if(decision == checkCall) {
			return simulateAction(simulation, 1);
		}
		else if(decision == raise) {
			return simulateAction(simulation, 2);
		}
		else if(decision == foldProb) {
			return simulateAction(simulation, 3);
		}
		
		return 0;
		
		
	}
	
	private int propabilityOfFold(Hand hand) {
		return 10;
	}
	
	private int propabilityOfCheckCall(Hand hand) {
		return 11;
	}
	
	private int propabilityOfRaise(Hand hand) {
		return 11;
	}
	
}
