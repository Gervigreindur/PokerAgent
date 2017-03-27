package PokerAgent;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloSimulation {
	
	private Board myBoard;
	private Player me;

	private int numberOfSimulations;
	private double check;
	private double raise;
	private ArrayList<Double> outs;

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

			check += simulateAction(simulation, 1, 1000);
			raise += simulateAction(simulation, 2, 1000);

		}
		
		System.out.println("Check: " + check + " raise: " + raise );
		
		check = check / numberOfSimulations;
		raise = raise / numberOfSimulations;
		
		//System.out.println("Check: " + check + " raise: " + raise );
		double result = Math.max(check, raise);
		System.out.println(result);
		if(result < 0) {
			return 3;
		}
		if(result == raise) {
			return 2;
		}
		else {
			return 1;
		}
	}
	
	public int simulateAction(State simmi, int action, int depth) {
		if(depth == 0) {
			//System.out.println("depth");
			//System.out.println(simmi.getNumberOfPLayersInRound());

			return 1;
		}
		
		if(simmi.isTerminal()) {
			return simmi.terminal(me);
		}
		
		State simulation = new State(simmi);
		simulation.takeAction(action);
		
		double prob = propabilityWinPercentage(simulation);
		
		int numberOfPeopleInRound = simulation.getNumberOfPLayersInRound();
		
		prob -= ((numberOfPeopleInRound-1) * 3.75);
		
		double foldProb = propabilityOfFold(prob);
		double checkCall = propabilityOfCheckCall(prob);
		double raise = propabilityOfRaise(prob);
		
		double decision = Math.max(Math.max(foldProb, checkCall), raise);
		
		if(decision == checkCall) {
			return simulateAction(simulation, 1, depth-1);
		}
		else if(decision == raise) {
			return simulateAction(simulation, 2, depth-1);
		}
		else if(decision == foldProb) {
			return simulateAction(simulation, 3, depth-1);
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
	
	private double propabilityWinPercentage(State simulation) {
		
		Hand hand = simulation.getCurrPlayHands();
		
		outs = new ArrayList<Double>();	
		double val = 0;
		for(double i = 0; i <= 21; i++)
		{
			outs.add(val += 3.8);
		}
		
		int outsForNext = 0;
		
		int cardOne = simulation.getCurrPlayHands().getHand()[0].getRank();
		int suitOne = simulation.getCurrPlayHands().getHand()[0].getSuit();
		int cardTwo = simulation.getCurrPlayHands().getHand()[1].getRank();
		int suitTwo = simulation.getCurrPlayHands().getHand()[1].getSuit();
		
		if(simulation.preFlop)
		{
			if(hand.isPair()) { //Pör undir og með 7 og ekki ásapar
				if(cardOne < 7 && cardOne != 0) {
					return 79.34 + outs.get(1);
				}
				else { //Pör yfir 7 og ásar
					return 87.43 + outs.get(1);
				}				
			}
			else if(suitOne != suitTwo){// Ekki sama suit
				if(cardOne < 9 && cardTwo < 9) { //Ekki ásar og spil undir 9
					return 48.5 + outs.get(3);
				}
				else if(cardOne >= 9 || cardTwo >= 9) {
					return 79.5 + outs.get(3);
				}
			}
			else if (suitOne == suitTwo){//sama suit.
				if(cardOne < 9 && cardTwo < 9) { //Ekki ásar og spil undir 9
					return 52.5 + outs.get(3);
				}
				else if(cardOne >= 9 && cardTwo >= 9) {
					return 81.5 + outs.get(3);
				}
				else if(cardOne >= 9 && cardTwo < 9) {
					return 60.5 + outs.get(3);
				}
				else if(cardOne <= 9 && cardTwo >= 9) {
					return 60.5 + outs.get(3);
				}
			}
			else
				return 50;
		}
		else if(simulation.flop) {				
			if(hand.isRoyalFlush()) {
				return 100;
			}
			else if(hand.isStraightFlush()) {
				return 99;
			}
			else if(hand.isFourOfKind()) {
				return 100;
			}
			else if(hand.isFullHouse()) {
				return 95;
			}
			else if(hand.isFlush()) {
				return 95;
			}
			else if(hand.isStraight()) {
				return 95;
			}
			else if(hand.isThreeOfKind()) {
				return 80 + outs.get(0) + outs.get(1);
			}
			else if(hand.isTwoPairs()){
				outsForNext = 2;
				if(cardOne < 7 && cardOne != 0 || cardTwo < 7 && cardTwo != 0) {
					return 65.34 + outsForNext;
				}
				else if(cardOne > 7 || cardOne == 0|| cardTwo > 7 || cardTwo == 0){ //Pör yfir 7 og ásar
					return 77.43 + outsForNext;
				}
			}				
			else if(hand.isPair()) {
				outsForNext = 4;
				if(cardOne < 7 && cardOne != 0 || cardTwo < 7 && cardTwo != 0) {
					return 59.34 + outsForNext;
				}
				else if(cardOne > 7 || cardOne == 0|| cardTwo > 7 || cardTwo == 0){ //Pör yfir 7 og ásar
					return 77.43 + outsForNext;
				}
				return 0;
			}
			if(hand.isPair()) {
				return 0;

			}
			else {
				return 5;
			}
		}
		else if(simulation.turn)
		{
			if(hand.isRoyalFlush()) {
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
			}
			if(hand.isPair()) {
				return 30;
			}
			else {
				return 10;
			}
		}
		else if(simulation.river)
		{
			if(hand.isRoyalFlush()) {
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
			}
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
