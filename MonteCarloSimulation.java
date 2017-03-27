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
		
		check = check / numberOfSimulations;
		raise = raise / numberOfSimulations;
		
		System.out.println("Check: " + check + " raise: " + raise );
		double result = Math.max(check, raise);
		System.out.println(result);

		if(myBoard.getCurrBet() - me.getCurrBet() <= 5 && result < 0) {
			return 1;
		}
		if(result < 0) { 
			return 3;
		}
		if(result == check) {
			return 1;
		}
		else {
			return 2;
		}
	}
	
	public double simulateAction(State simmi, int action, int depth) {
		if(depth == 0) {
			return 1*propabilityWinPercentage(simmi);
		}
		
		if(simmi.isTerminal()) {
			return simmi.terminal(me);
		}
		
		State simulation = new State(simmi);
		simulation.takeAction(action);
			
		int numberOfPeopleInRound = simulation.getNumberOfPLayersInRound();
		double prob = propabilityWinPercentage(simulation);
		prob -= ((numberOfPeopleInRound-1) * 5.75);
		
		double checkCall = propabilityOfCheckCall(prob);
		double raise;
		if(simulation.getCurrPlayer().getRaiseCounter() == 0) {
			raise = propabilityOfRaise(prob);
		}
		else {
			raise = 0;
		}
		//System.out.println(raise);
		if(simulation.getCurrPlayer().getID() != me.getID()) {
			
			double foldProb = propabilityOfFold(prob, simulation);
			double decision = Math.max(Math.max(foldProb, checkCall), raise);
			
			if(raise > 80) {
				return simulateAction(simulation, 2, depth-1);
			}
			else if(decision == checkCall) {
				return simulateAction(simulation, 1, depth-1);
			}
			else if(decision == foldProb) {
				return simulateAction(simulation, 3, depth-1);
			}
		}
		else {
			double decision = Math.max(checkCall, raise);
			
			if(raise > 80) {
				return simulateAction(simulation, 2, depth-1);
			}
			else if(decision == checkCall) {
				return simulateAction(simulation, 1, depth-1);
			}
		}
		return 0;
	}
	
	private double propabilityOfFold(double probOfWinning, State simulation) {
		//System.out.println(simulation.currBet);
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
		
		int cardOne = simulation.getCurrPlayHands().getHand()[0].getRank();
		int suitOne = simulation.getCurrPlayHands().getHand()[0].getSuit();
		int cardTwo = simulation.getCurrPlayHands().getHand()[1].getRank();
		int suitTwo = simulation.getCurrPlayHands().getHand()[1].getSuit();
		
		if(simulation.preFlop)
		{
			if(hand.isPair()) { //Pör undir og með 7 og ekki ásapar
				if(cardOne < 7) {
					return 50.34 + outs.get(1);
				}
				else { //Pör yfir 7 og ásar
					return 60.43 + outs.get(1);
				}				
			}
			else if(suitOne != suitTwo){// Ekki sama suit
				if(cardOne < 7 && cardTwo < 7) { 
					return 32.25 + outs.get(1);
				}
				else if((cardOne < 7 && cardTwo >= 7) || (cardOne >= 7 && cardTwo < 7)){
					return 35.5 + outs.get(1);
				}
				else if(cardOne >= 7 || cardTwo >= 7) {
					return 39.75 + outs.get(1);
				}
			}
			else if (suitOne == suitTwo){//sama suit.
				if(cardOne < 7 && cardTwo < 7) { 
					return 35.25 + outs.get(1);
				}
				else if((cardOne < 7 && cardTwo >= 7) || (cardOne >= 7 && cardTwo < 7)){
					return 37.5 + outs.get(1);
				}
				else if(cardOne >= 7 || cardTwo >= 7) {
					return 41.75 + outs.get(1);
				}
			}
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
				if(hand.getFirstMatch().getRank() < 7) {
					return 65.34 + outs.get(0);
				}
				else if(hand.getFirstMatch().getRank() >= 7){ //Pör yfir 7 og ásar
					return 77.43 + outs.get(0);
				}
			}				
			else if(hand.isPair()) {
				if(hand.getFirstMatch().getRank() < 7) {
					return 39.34 + outs.get(2);
				}
				else if(hand.getFirstMatch().getRank() >= 7){ //Pör yfir 7 og ásar
					return 57.43 + outs.get(2);
				}
			}
			else{
				return 25;
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
				return 95;
			}
			else if(hand.isFullHouse()) {
				return 90;
			}
			else if(hand.isFlush()) {
				return 88;
			}
			else if(hand.isStraight()) {
				return 87;
			}
			else if(hand.isThreeOfKind()) {
				return 80 + outs.get(0) + outs.get(1);
			}
			else if(hand.isTwoPairs()){
				if(hand.getFirstMatch().getRank() < 7) {
					return 62.34 + outs.get(0);
				}
				else if(hand.getFirstMatch().getRank() >= 7){ //Pör yfir 7 og ásar
					return 73.43 + outs.get(0);
				}
			}				
			else if(hand.isPair()) {
				if(hand.getFirstMatch().getRank() < 7) {
					return 29.34 + outs.get(2);
				}
				else if(hand.getFirstMatch().getRank() >= 7){ //Pör yfir 7 og ásar
					return 41.43 + outs.get(2);
				}
			}
			else{
				return 18;
			}
		}
		else if(simulation.river)
		{			if(hand.isRoyalFlush()) {
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
			if(hand.getFirstMatch().getRank() < 7) {
				return 61.34 + outs.get(0);
			}
			else if(hand.getFirstMatch().getRank() >= 7){ //Pör yfir 7 og ásar
				return 71.43 + outs.get(0);
			}
		}				
		else if(hand.isPair()) {
			if(hand.getFirstMatch().getRank() < 7) {
				return 20.34;
			}
			else if(hand.getFirstMatch().getRank() >= 7){ //Pör yfir 7 og ásar
				return 28.43;
			}
		}
		else{
			return 15;
			}
		}		
		return 0;	
	}
	
	private int getRandVal() {
		return ThreadLocalRandom.current().nextInt(0, 11);
	}
	
}
