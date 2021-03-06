package PokerAgent;

public class Player {

	private String name;
	private int stack;
	private Hand hand;
	private int ID;
	private boolean myTurn;
	private int currBet;
	private int raiseCounter;
	
	Player(String name, int deposit) {
		this.name = name;
		this.stack = deposit;
		hand = new Hand();
		currBet = 0;
		myTurn = false;
		ID = -1;
		raiseCounter = 0;
	}
	
	Player(Player p) {
		this.name = p.seeName();
		this.stack = p.seeStack();
		this.hand = new Hand(p.getHand());
		this.currBet = p.getCurrBet();
		this.ID = p.getID();
		this.myTurn = p.getMyTurn();
		this.raiseCounter = p.getRaiseCounter();
	}

	public int getCurrBet() {
		return currBet;
	}
	
	public void resetCurrBet() {
		currBet = 0;
	}
	
	public boolean getMyTurn() {
		return myTurn;
	}
	
	public boolean getAllIn() {
		return stack == 0;
	}
	
	public void setMyTurn() {
		myTurn = !myTurn;
	}
	
	public void recievesCards(Card card) {
		hand.addCardToHand(card);
	}
	
	public void madeBet(int amount) {
		stack -= amount;
		currBet += amount;
	}
	
	public int getID() {
		return ID;
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public void emptyHand() {
		hand.clearHand();
	}
	
	//Functions to get info to test betting function ===================================================================
	public String seeCards() {
		//TODO: skrifa �t spilin � hand
		return hand.getCards();
	}
	
	public String seeName() {
		return name;
	}
	
	public int seeStack() {
		return stack;
	}
	
	public void addPot(int amount) {
		stack += amount;
	}
	
	public int getHandValue() {
		return hand.valueOfHand();
	}
	
	public int getHandValueSimulation() {
		return hand.valueOfHandSimulation();
	}
	//===================================================================================================================

	public String getInput() {
		return null;
	}

	/**
	 * @return the raiseCounter
	 */
	public int getRaiseCounter() {
		return raiseCounter;
	}

	/**
	 * @param raiseCounter the raiseCounter to set
	 */
	public void setRaiseCounter(int raiseCounter) {
		this.raiseCounter = raiseCounter;
	}
	
	
}
