package PokerAgent;

import java.util.Arrays;

public class Hand {
	private Card[] hand;
	private int pivot;
	
	public Hand() {
		hand = new Card[7];
		pivot = 0;
	}
	
	public void addCardToHand(Card card) {
		hand[pivot] = card;
		pivot++;
	}
	
	public void clearHand() {
		Arrays.fill(hand, null);
	}
	
	public String getCards() {
		String result = "";
		for(Card c : hand) {
			if(c != null) {
				result += c.toString() + "\n";
			}
		}
		return result;
	}
}
