package PokerAgent;

public class Card {
	private int rank, suit;
	
	private static String[] suits = { "hearts", "spades", "diamonds", "clubs" };
	private static String[] ranks  = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
	
	Card(int suit, int rank) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public static String rankAsString(int rank) {
		return ranks[rank];
	}
	
	public boolean equals(Card c) {
		if(this.rank == c.rank && this.suit == c.suit) {
			return true;
		}
		return false;
	}
	
	public @Override String toString() {
		return ranks[rank] + " of " + suits[suit];
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return the suit
	 */
	public int getSuit() {
		return suit;
	}
	
}
