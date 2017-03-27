package PokerAgent;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloSimulation {
	
	private Board myBoard;
	private Player me;

	private int numberOfSimulations;
	private int check;
	private int raise;
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
			check += simulateAction(simulation, 1, 500);
			raise += simulateAction(simulation, 2, 500);
		}
		
		check = check / numberOfSimulations;
		raise = raise / numberOfSimulations;
		
		//System.out.println("Check: " + check + " raise: " + raise );
		int result = Math.max(check, raise);	
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
		if(depth == 0) {return 1;}
		
		if(simmi.isTerminal()) {
			return simmi.terminal(me);
		}
		
		State simulation = new State(simmi);
		simulation.takeAction(action);
		
		double prob = propabilityWinPercentage(simulation.getCurrPlayHands());
		
		int numberOfPeopleInRound = simulation.getNumberOfPLayersInRound();
		
		prob -= ((numberOfPeopleInRound-1) * 7.75);
		
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
	
	private double propabilityWinPercentage(Hand hand) {
		
		outs = new ArrayList<Double>();	
		double val = 0;
		for(double i = 0; i <= 21; i++)
		{
			outs.add(val += 3.8);
		}
		
		int outsForNext = 0;
		int numberOfCardsOnPLayer = hand.getNumberOfCardsOnPlayer();
		
		int cardOne = hand.getHand()[0].getRank();
		int rankOne = hand.getHand()[0].getSuit();
		int cardTwo = hand.getHand()[1].getRank();
		int rankTwo = hand.getHand()[1].getSuit();
		
		if(hand.getNumberOfCardsOnPlayer() == 2)
		{
			if(hand.isPair()) { //Pör undir og með 7 og ekki ásapar
				if(cardOne < 7 && cardOne != 0) {
					return 59.34 + outs.get(1);
				}
				else { //Pör yfir 7 og ásar
					return 77.43 + outs.get(1);
				}				
			}
			else if(rankOne != rankTwo){// Ekki sama suit
				if(cardOne < 9 && cardOne != 0 || cardTwo < 9 && cardTwo != 0) { //Ekki ásar og spil undir 9
					return 38.5 + outs.get(3);
				}
				else if((cardOne >= 9 || cardOne == 0 ) || (cardTwo >= 9 || cardTwo == 0) ) {
					return 59.5 + outs.get(3);
				}
			}
			else {//sama suit.
				return 22 + outs.get(3);
			}
		}
		else if(numberOfCardsOnPLayer == 5) {				
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
		else if(numberOfCardsOnPLayer == 6)
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
		else
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
