package PokerAgent;

import java.util.Arrays;

public class Hand {
	protected Card[] hand;
	private int pivot;
	
	public Hand() {
		hand = new Card[2];
		pivot = 0;
	}
	
	public void addCardToHand(Card card) {
		hand[pivot] = card;
		pivot++;
	}
	
	public void clearHand() {
		Arrays.fill(hand, null);
		pivot = 0;
	}
	
	
	
	public String getCards() {
		String result = "";
		for(Card c : hand) {
			if(c != null) {
				result += c.toString() + " ";
			}
		}
		return result;
	}
}
