package PokerAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	
	public void addCardToHand(Card card) {
		hand[pivot] = card;
		pivot++;
	}
	
	public void clearHand() {
		pivot = 0;
		Arrays.fill(hand, null);
	}
	
	public String getCards() {
		sortHand();
		//TODO:
		//bæta við villumeðhöndlun
		//return "\n" + hand[0].toString() + "\n" + hand[1].toString();
		
		String result = "";
		for(Card c : hand) {
			if(c != null) {
				result += c.toString() + "\n";
			}
		}
		
		return result;
	}
	
	public int valueOfHand() {
		sortHand();
		
		if(isRoyalFlush()) {
			System.out.println("ROYAL FLUSH");
			//can return value 248
			return 248;
		}
		else if(isStraightFlush()) {
			System.out.println("STRAIGHT FLUSH");
			//can return value from 238 to 247
			return 9;
		}
		else if(isFourOfKind()) {
			System.out.println("FOUR OF A KIND " + firstMatch.toString());
			//can return value from 224 to 237
			return 8;
		}
		else if(isFullHouse()) {
			System.out.println("FULL HOUSE " + firstMatch.toString() + " and " + secondMatch.toString());
			//can return value from 145 to 223
			return 7;
		}
		else if(isFlush()) {
			System.out.println("FLUSH " + firstMatch.toString() + " high");
			//can return value from 131 to 144
			return 6;
		}
		else if(isStraight()) {
			System.out.println("STRAIGHT " + firstMatch.toString() + " high");
			//can return value from 121 to 130
			return 5;
		}
		else if(isThreeOfKind()) {
			System.out.println("THREE OF A KIND of " + firstMatch.toString());
			//can return value from 107 to 120
			return 4;
		}
		else if(isTwoPairs()) {
			System.out.println("TWO PAIRS of " + firstMatch.toString() + " and " + secondMatch.toString());
			//can return value from 28 to 106
			return 3;
		}
		else if(isPair()) {
			System.out.println("PAIR of " + firstMatch.toString());
			//can return value from 15 to 27
			return 2;
		}
		else {
			System.out.println("HIGH CARD");
			return highCard();
		}
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

	private int highCard() {
		return hand[0].getRank();
	}

	private boolean isPair() {
		firstMatch = null;
		
		for(int i = 0; i < hand.length - 1; i++) {
			if(hand[i].getRank() == hand[i+1].getRank()) {
				firstMatch = hand[i];
				break;
			}
		}
		
		return firstMatch != null;
	}

	private boolean isTwoPairs() {
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

	private boolean isThreeOfKind() {
		firstMatch = null;
		
		for(int i = 0; i < hand.length - 2; i++) {
			if(hand[i].getRank() == hand[i+1].getRank() && hand[i].getRank() == hand[i+2].getRank()) {
				firstMatch = hand[i];
				break;
			}
		}
		
		return firstMatch != null;
	}

	private boolean isStraight() {
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

	private boolean isFlush() {
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

	private boolean isFullHouse() {
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

	private boolean isFourOfKind() {
		firstMatch = null;
		
		for(int i = 0; i < hand.length - 3; i++) {
			if(hand[i].getRank() == hand[i+1].getRank() && hand[i+1].getRank() == hand[i+2].getRank() && hand[i+2].getRank() == hand[i+3].getRank()) {
				firstMatch = hand[i];
				break;
			}
		}
		return firstMatch != null;
	}

	private boolean isStraightFlush() {
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

	private boolean isRoyalFlush() {
		firstMatch = null;
		
		return isStraightFlush() && firstMatch.getRank() == 12;
	}
	
	private void getStraight(ArrayList<Card> tmpHand) {
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
