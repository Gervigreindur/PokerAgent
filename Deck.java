package Poker;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	
	private ArrayList<Card> cards;
	private Random rand;
	private int pivot;
	
	Deck() {
		cards = new ArrayList<Card>();
		rand = new Random();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				cards.add(new Card(i,j));
			}
		}

		shuffle();		
	}
	
	public void shuffle() {
		
		for(int i = 0; i < 100; i++) {
			int index_1 = rand.nextInt(cards.size() - 1);
			int index_2 = rand.nextInt(cards.size() - 1);
			
			Card temp = cards.get(index_1);
			cards.set(index_2, cards.get(index_1));
			cards.set(index_1, temp);
		}
		
		pivot = 0;
	}
	
	public void burnCard() {
		pivot++;
		if(pivot < 0 || pivot >= cards.size()) {
			throw new IndexOutOfBoundsException("pivot out of order, " + pivot + " thrown from drawFromDeck() in Deck class");
		}
	}
	
	public Card drawFromDeck() {
		Card card = cards.get(pivot);
		pivot++;
		
		if(pivot < 0 || pivot >= cards.size()) {
			throw new IndexOutOfBoundsException("pivot out of order, " + pivot + " thrown from drawFromDeck() in Deck class");
		}
		
		return card;
		
	}
}
