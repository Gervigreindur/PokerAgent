package PokerAgent;

public class Player {

	private String name;
	private int stack;
	private Card[] cards;
	private boolean fold, turnToDo;
	private int ID;
	
	Player(String name, int deposit) {
		this.name = name;
		this.stack = deposit;
		
		cards = new Card[2];
	}
	
	public void recievesCards(Card first, Card second) {
		cards[0] = first;
		cards[1] = second;
	}
	
	public void madeBet(int amount) {
		stack -= amount;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	//Functions to get info to test betting function ===================================================================
	public String seeCards() {
		return cards[0].toString() + " and " + cards[1].toString();
	}
	
	public String seeName() {
		return name;
	}
	
	public String seeStack() {
		return Integer.toString(stack);
	}
	//===================================================================================================================
}
