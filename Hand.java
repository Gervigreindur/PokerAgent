package PokerAgent;

import java.util.ArrayList;
import java.util.Arrays;

public class Hand {
	private Card[] hand;
	private int pivot;
	private Card firstMatch;
	private Card secondMatch;
	
	public Hand() {
		hand = new Card[7];
		pivot = 0;
		firstMatch = null;
		secondMatch = null;
	}
	
	public Hand(Hand hand) {
		this.hand = new Card[7];
		for(int i = 0; i < hand.getHand().length; i++) {
			this.hand[i] = hand.getHand()[i];
		}
		this.pivot = hand.pivot;
		this.firstMatch = hand.firstMatch;
		this.secondMatch = hand.secondMatch;
	}
	
	public void addCardToHand(Card card) {
		hand[pivot] = card;
		pivot++;
	}
	
	public void clearHand() {
		pivot = 0;
		Arrays.fill(hand, null);
	}
	
	public Card[] getHand() {
		return hand;
	}
	
	public int getNumberOfCardsOnPlayer() {
		return pivot;
	}
	
	public String getCards() {
		String result = "";
		for(Card c : hand) {
			if(c != null) {
				result += c.toString() + " - ";
			}
		}
		
		return result;
	}
	
	public int valueOfHand() {
		sortHand();
		
		if(isRoyalFlush()) {
			System.out.println("ROYAL FLUSH");

			return 900;
		}
		else if(isStraightFlush()) {
			// 800 + (279JA = 14, 23567 = 7)
			System.out.println("STRAIGHT FLUSH " + (firstMatch.getRank() + 2) + "high");

			return 800 + firstMatch.getRank() + 2;
		}
		else if(isFourOfKind()) {
			// 700 + (AAAA = 14, 5555 = 5) + highCard
			System.out.println("FOUR OF A KIND " + (firstMatch.getRank() + 2) + "s");

			return 700 + firstMatch.getRank() + 2 + getHighCard(firstMatch, null).getRank() + 2;
		}
		else if(isFullHouse()) {
			// "When comparing full houses, the rank of the three cards determines which is higher. For example 9-9-9-4-4 beats 8-8-8-A-A"
			// 600 + (KKK AA = 13, AAA KK = 14)
			System.out.println("FULL HOUSE " + (firstMatch.getRank() + 2) + "s on " + (secondMatch.getRank() + 2));

			return 600 + firstMatch.getRank() + 2;
		}
		else if(isFlush()) {
			// 500 + (279JA = 14, 23567 = 7)
			System.out.println("FLUSH " + (firstMatch.getRank() + 2) + " high");
			
			return 500 + firstMatch.getRank() + 2;
		}
		else if(isStraight()) {
			// 400 + (23456 = 6, 10JQKA = 14)
			System.out.println("STRAIGHT " + (firstMatch.getRank() + 2) + " high");

			return 400 + firstMatch.getRank() + 2;
		}
		else if(isThreeOfKind()) {
			//300 + (KKK = 13, 777 = 7) + highCard
			System.out.println("THREE OF A KIND of " + (firstMatch.getRank() + 2) + "s");

			return 300 + firstMatch.getRank() + 2 + getHighCard(firstMatch, null).getRank() + 2;
		}
		else if(isTwoPairs()) {
			//200 + (KK AA = 13 + 14 = 27, 77 88 = 7 + 8 = 15) + highCard
			System.out.println("TWO PAIRS of " + (firstMatch.getRank() + 2) + "s and " + (secondMatch.getRank() + 2) + "s");
			
			return 200 + firstMatch.getRank() + 2 + secondMatch.getRank() + 2 + getHighCard(firstMatch, secondMatch).getRank() + 2;
		}
		else if(isPair()) {
			//there are 13 different pairs. the value of the pair is 100 + the rank of the pair(KK = 12, 88 = 8) plus the value of the highcard
			System.out.println("PAIR of " + (firstMatch.getRank() + 2) + "s");

			return 100 + firstMatch.getRank() + 2 + getHighCard(firstMatch, null).getRank() + 2;
		}
		else {
			//1-13
			//we add two to the result since the cards' rank is stored in an array so the rank of 2 would be 0 but we want it to be 2
			System.out.println("HIGH CARD");
			return highCard() + 2;
		}
	}

	private Card getHighCard(Card first, Card second) {
		for(int i = 0; i < hand.length; i++) {
			if(second == null) {
				//if 'second' is null we only have to check if the card has the same rank as 'first'
				if(hand[i].getRank() != first.getRank()) {
					return hand[i];
				}
			}
			else {
				//if 'second' is not null we need to make sure that the card does not have the same rank as 'first' and 'second'
				if(hand[i].getRank() != first.getRank() && hand[i].getRank() != second.getRank()) {
					return hand[i];
				}
			}
		}
		return null;
	}

	private void sortHand() {
		//Sorts from highest(A) to lowest(2)	
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				if(hand[i].getRank() > hand[j].getRank()) {
					Card tmp = hand[i];
					hand[i] = hand[j];
					hand[j] = tmp;
				}
			}
		}
	}

	public int highCard() {
		return hand[0].getRank();
	}

	public boolean isPair() {
		firstMatch = null;
		
		for(int i = 0; i < hand.length - 1; i++) {
			if(hand[i+1] != null) {
				if(hand[i].getRank() == hand[i+1].getRank()) {
					firstMatch = hand[i];
					break;
				}
			}			
		}
		
		return firstMatch != null;
	}
	

	public boolean isTwoPairs() {
		firstMatch = null;
		secondMatch = null;
		
		for(int i = 0; i < hand.length - 1; i++) {
			if(hand[i].getRank() == hand[i+1].getRank()) {
				if(firstMatch == null) {
					firstMatch = hand[i];
				}
				else {
					secondMatch = hand[i];
					break;
				}
			}
		}
		
		return firstMatch != null && secondMatch != null;
	}

	public boolean isThreeOfKind() {
		firstMatch = null;
		
		for(int i = 0; i < hand.length - 2; i++) {
			if(hand[i].getRank() == hand[i+1].getRank() && hand[i].getRank() == hand[i+2].getRank()) {
				firstMatch = hand[i];
				break;
			}
		}
		
		return firstMatch != null;
	}

	public boolean isStraight() {
		firstMatch = null;
		
		ArrayList<Card> dupRemoved = new ArrayList<Card>();
		
		dupRemoved.add(hand[0]);
		
		for(int i = 1; i < hand.length; i++) {
			if(hand[i].getRank() != dupRemoved.get(dupRemoved.size()-1).getRank()) {
				dupRemoved.add(hand[i]);
			}
		}
		
		if(dupRemoved.size() < 5) {
			return false;
		}

		//TODO:
		//Bæta við svo A 2 3 4 5 geti verið röð
		
		getStraight(dupRemoved);
		
		return firstMatch != null;
	}

	public boolean isFlush() {
		firstMatch = null;
		
		int hearts = 0;
		int spades = 0;
		int diamonds = 0;
		int clubs = 0;
		Card heart = null;
		Card spade = null;
		Card diamond = null;
		Card club = null;
		
		
		for(int i = 0; i < hand.length; i++) {
			if(hand[i].getSuit() == 0) {
				hearts++;
				if(heart == null) {
					heart = hand[i];
				}
			}
			else if(hand[i].getSuit() == 1) {
				spades++;
				if(spade == null) {
					spade = hand[i];
				}
			}
			else if(hand[i].getSuit() == 2) {
				diamonds++;
				if(diamond == null) {
					diamond = hand[i];
				}
			}
			else if(hand[i].getSuit() == 3) {
				clubs++;
				if(club == null) {
					club = hand[i];
				}
			}
		}
		
		if(hearts >= 5) {
			firstMatch = heart;
		}
		else if(spades >= 5) {
			firstMatch = spade;
		}
		else if(diamonds >= 5) {
			firstMatch = diamond;
		}
		else if(clubs >= 5) {
			firstMatch = club;
		}
		
		return firstMatch != null;
	}

	public boolean isFullHouse() {
		firstMatch = null;
		secondMatch = null;
		
		if(isThreeOfKind()) {
			for(int i = 0; i < hand.length - 1; i++) {
				if(hand[i].getRank() == hand[i+1].getRank() && hand[i].getRank() != firstMatch.getRank()) {
					secondMatch = hand[i];
					break;
				}
			}
		}
		
		return firstMatch != null && secondMatch != null;
	}

	public boolean isFourOfKind() {
		firstMatch = null;
		
		for(int i = 0; i < hand.length - 3; i++) {
			if(hand[i].getRank() == hand[i+1].getRank() && hand[i+1].getRank() == hand[i+2].getRank() && hand[i+2].getRank() == hand[i+3].getRank()) {
				firstMatch = hand[i];
				break;
			}
		}
		return firstMatch != null;
	}

	public boolean isStraightFlush() {
		firstMatch = null;
		
		ArrayList<Card> hearts = new ArrayList<Card>();
		ArrayList<Card> spades = new ArrayList<Card>();
		ArrayList<Card> diamonds = new ArrayList<Card>();
		ArrayList<Card> clubs = new ArrayList<Card>();
		
		for(int i = 0; i < hand.length; i++) {
			if(hand[i].getSuit() == 0) {
				hearts.add(hand[i]);
			}
			else if(hand[i].getSuit() == 1) {
				spades.add(hand[i]);
			}
			else if(hand[i].getSuit() == 2) {
				diamonds.add(hand[i]);
			}
			else if(hand[i].getSuit() == 3) {
				clubs.add(hand[i]);
			}
		}
		
		if(hearts.size() >= 5) {
			getStraight(hearts);
		}
		else if(spades.size() >= 5) {
			getStraight(spades);
		}
		else if(diamonds.size() >= 5) {
			getStraight(diamonds);
		}
		else if(clubs.size() >= 5) {
			getStraight(clubs);
		}
		
		return firstMatch != null;
	}

	public boolean isRoyalFlush() {
		firstMatch = null;
		
		return isStraightFlush() && firstMatch.getRank() == 12;
	}
	
	public void getStraight(ArrayList<Card> tmpHand) {
		for(int i = 0; i <= tmpHand.size() - 5; i++) {
			if(tmpHand.get(i).getRank()-1 == tmpHand.get(i+1).getRank() &&
					tmpHand.get(i+1).getRank()-1 == tmpHand.get(i+2).getRank() &&
				tmpHand.get(i+2).getRank()-1 == tmpHand.get(i+3).getRank() &&
				tmpHand.get(i+3).getRank()-1 == tmpHand.get(i+4).getRank()) {
					firstMatch = tmpHand.get(i);
					break;
				}
		}
	}
}
