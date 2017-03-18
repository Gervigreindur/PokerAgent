package PokerAgent;

public class Player {

	private String name;
	private int stack;
	private Hand hand;
	private int ID;
	
	Player(String name, int deposit) {
		this.name = name;
		this.stack = deposit;
		
		hand = new Hand();
	}
	
	public void recievesCards(Card card) {
		hand.addCardToHand(card);
	}
	
	public void madeBet(int amount) {
		stack -= amount;
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
		//TODO: skrifa út spilin í hand
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
	//===================================================================================================================
}
