package Poker;

public class Player {

	String name;
	int stack;
	Card[] cards;
	boolean fold, turnToDo;
	
	Player(String name, int deposit) {
		this.name = name;
		this.stack = deposit;
		
		cards = new Card[2];
	}
	
	public void recievesCards(Card first, Card second) {
		cards[0] = first;
		cards[1] = second;
	}
}
