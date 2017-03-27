package PokerAgent;

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
		//getAllCards();
		shuffle();		
	}
	
	Deck(Deck deck) {
		cards = new ArrayList<Card>();
		rand = new Random();
		
		for(int i = 0; i < deck.cards.size(); i++) {
			cards.add(new Card(deck.cards.get(i).getSuit(), deck.cards.get(i).getRank()));
		}
		
		this.pivot = deck.pivot;
	}
	
	public void shuffle() {
		
		for(int i = 0; i < 100; i++) {
			int index_1 = rand.nextInt(cards.size() - 1);
			int index_2 = rand.nextInt(cards.size() - 1);
			
			Card temp = cards.get(index_2);
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
		Card tmp = card;
		cards.remove(pivot);
		pivot++;
		
		if(pivot < 0 || pivot >= cards.size()) {
			throw new IndexOutOfBoundsException("pivot out of order, " + pivot + " thrown from drawFromDeck() in Deck class");
		}
		
		//System.out.println("Number of cards left: " + getNumberOfCardsInDeck() + " what hand to romve: " + cards.get(pivot));
		//System.out.println("what card we are returning: " + tmp);
		
		return tmp;
	}
	
	public int getNumberOfCardsInDeck() {
		return cards.size();
	}
	
	public void getAllCards()
	{
		for(Card cards1: cards)
		{
			System.out.println(cards1);
		}
	}
}
