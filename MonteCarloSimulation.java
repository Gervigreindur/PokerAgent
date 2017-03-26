package PokerAgent;

import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloSimulation {
	
	private Board myBoard;
	private Player me;

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
		
		if(simmi.isTerminal()) {
			return simmi.terminal(me);
		}
		
		State simulation = new State(simmi);
		simulation.takeAction(action);
		
		//double prob = propabilityWinPercentage(simulation.getCurrPlayHands());
		double prob = 50.4;
		int numberOfPeopleInRound = simulation.getNumberOfPLayersInRound();
		
		prob -= (numberOfPeopleInRound * 7.75);
		
		double foldProb = propabilityOfFold(prob);
		double checkCall = propabilityOfCheckCall(prob);
		double raise = propabilityOfRaise(prob);
		
		double decision = Math.max(Math.max(foldProb, checkCall), raise);
		
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
	
	private double propabilityOfFold(double probOfWinning) {		
		return(100 - probOfWinning + getRandVal());
	}

	private double propabilityOfCheckCall(double probOfWinning) {
		return(probOfWinning + getRandVal());
	}
	
	private double propabilityOfRaise(double probOfWinning) {
		return(probOfWinning + getRandVal());
	}
	
	private double propabilityWinPercentage(Hand hand) {
		
		if(hand.getNumberOfCardsOnPlayer() == 2)
		{
			int cardOne = hand.getHand()[0].getRank();
			int rankOne = hand.getHand()[0].getSuit();
			int cardTwo = hand.getHand()[1].getRank();
			int rankTwo = hand.getHand()[1].getSuit();
			
			if(hand.isPair()) { //Pör undir og með 7 og ekki ásapar
				if(cardOne < 7 && cardOne != 0) {
					return 59.34;
				}
				else { //Pör yfir 7 og ásar
					return 77.43;
				}				
			}
			else if(rankOne != rankTwo){// Ekki sama suit
				if(cardOne < 9 && cardOne != 0 || cardTwo < 9 && cardTwo != 0) { //Ekki ásar og spil undir 9
					return 42.5;
				}
				else if((cardOne >= 9 || cardOne == 0 ) || (cardTwo >= 9 || cardTwo == 0) ) {
					return 64.5;
				}
			}
			else {//sama suit.
				return 10;
			}
		}
		else if(hand.getNumberOfCardsOnPlayer() == 5)
		{
			/*if(hand.isRoyalFlush()) {
				return 100;
			}
			else if(hand.isStraightFlush()) {
				return 99;
			}
			else if(hand.isFourOfKind()) {
				return 100;
			}
			else if(hand.isFullHouse()) {
				return 99;
			}
			else if(hand.isFlush()) {
				return 0;
			}
			else if(hand.isStraight()) {
				return 0;
			}
			else if(hand.isThreeOfKind()) {
				return 0;
			}*/
			if(hand.isPair()) {
				return 0;
			}
			else {
				return 0;
			}
		}
		else if(hand.getNumberOfCardsOnPlayer() == 6)
		{
			/*if(hand.isRoyalFlush()) {
				return 100;
			}
			else if(hand.isStraightFlush()) {
				return 99;
			}
			else if(hand.isFourOfKind()) {
				return 100;
			}
			else if(hand.isFullHouse()) {
				return 99;
			}
			else if(hand.isFlush()) {
				return 50;
			}
			else if(hand.isStraight()) {
				return 50;
			}
			else if(hand.isThreeOfKind()) {
				return 50;
			}*/
			if(hand.isPair()) {
				return 30;
			}
			else {
				return 10;
			}
		}
		else
		{
			/*if(hand.isRoyalFlush()) {
				return 100;
			}
			else if(hand.isStraightFlush()) {
				return 99;
			}
			else if(hand.isFourOfKind()) {
				return 100;
			}
			else if(hand.isFullHouse()) {
				return 99;
			}
			else if(hand.isFlush()) {
				return 50;
			}
			else if(hand.isStraight()) {
				return 50;
			}
			else if(hand.isThreeOfKind()) {
				return 50;
			}*/
			if(hand.isPair()) {
				return 30;
			}
			else {
				return 10;
			}
		}
		return 0;
	}
	
	private int getRandVal() {
		return ThreadLocalRandom.current().nextInt(0, 11);
	}
	
}
