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
		System.out.println("Emp han");
		pivot = 0;
		Arrays.fill(hand, null);
	}
	
	public String getCards() {
		//TODO:
		//bæta við villumeðhöndlun
		return "\n" + hand[0].toString() + "\n" + hand[1].toString();
	}
	
	//snú við öllum forlykkjum sem fara í gegnum hand...
	public int valueOfHand() {
		sortHand();
		
		if(isRoyalFlush()) {
			//can return value 248
			return 248;
		}
		else if(isStraightFlush()) {
			//can return value from 238 to 247
			return 9;
		}
		else if(isFourOfKind()) {
			//can return value from 224 to 237
			return 8;
		}
		else if(isFullHouse()) {
			//can return value from 145 to 223
			return 7;
		}
		else if(isFlush()) {
			//can return value from 131 to 144
			return 6;
		}
		else if(isStraight()) {
			//can return value from 121 to 130
			return 5;
		}
		else if(isThreeOfKind()) {
			//can return value from 107 to 120
			return 4;
		}
		else if(isTwoPairs()) {
			//can return value from 28 to 106
			return 3;
		}
		else if(isPair()) {
			//can return value from 15 to 27
			return 2;
		}
		else {
			return highCard();
		}
	}

	private void sortHand() {
		//Sorts from highest(A) to lowest(2)	
		for(int i = 0; i < 6; i++) {
			int high = i;
			for(int j = i+1; j < 7; j++) {
				if(hand[j].getRank() > hand[high].getRank()) {
					high = j;
				}
				
				Card temp = hand[i];
				hand[i] = hand[high];
				hand[high] = temp;
			}
		}
	}

	private int highCard() {
		//can return value from 2 to 14
		int highCard = 0;
		for(Card card : hand) {
			if(card.getRank() > highCard) {
				highCard = card.getRank();
			}
		}
		
		return highCard;
	}

	private boolean isPair() {
		firstMatch = null;

		for(int i = hand.length - 1; i >= 1 && firstMatch == null; i--) {
			if(hand[i].getRank() == hand[i-1].getRank()) {
				firstMatch = hand[i];
			}
		}
		return !(firstMatch == null);
	}

	private boolean isTwoPairs() {
		firstMatch = null;
		secondMatch = null;
		
		for(int i = hand.length - 1; i >= 1 && (firstMatch == null || secondMatch == null); i--) {
			if(hand[i].getRank() == hand[i-1].getRank()) {
				if(firstMatch == null) {
					firstMatch = hand[i];
				}
				else {
					secondMatch = hand[i];
				}
			}
		}
		
		return !(secondMatch == null) && !(firstMatch == null);
	}

	private boolean isThreeOfKind() {
		firstMatch = null;
		
		for(int i = hand.length - 1; i >= 2 && firstMatch == null; i++) {
			if(hand[i].getRank() == hand[i-1].getRank() && hand[i-1].getRank() == hand[i-2].getRank()) {
				firstMatch = hand[i];
			}
		}
		return !(firstMatch == null);
	}

	private boolean isStraight() {
		ArrayList<Card> dupRemoved = new ArrayList<Card>();
		dupRemoved.add(hand[6]);
		
		for(int i = hand.length - 1; i <= 1; i--) {
			if(hand[i].getRank() != hand[i-1].getRank()) {
				dupRemoved.add(hand[i-1]);
			}
		}
		
		if(dupRemoved.size() < 5) {
			return false;
		}
		
		
		
		return false;
	}

	private boolean isFlush() {
		Set<Integer> set = new HashSet<Integer>();
		for(Card c : hand) {
			set.add(c.getSuit());
		}
		
		return set.size() <= 3;
	}

	private boolean isFullHouse() {
		firstMatch = null;
		secondMatch = null;
		
		for(int i = hand.length - 1; i >= 2 && firstMatch == null; i++) {
			if(hand[i].getRank() == hand[i-1].getRank() && hand[i-1].getRank() == hand[i-2].getRank()) {
				firstMatch = hand[i];
			}
		}
		
		for(int i = hand.length - 1; i >= 1 && secondMatch == null; i--) {
			if(hand[i].getRank() == hand[i-1].getRank() && hand[i] != firstMatch) {
				firstMatch = hand[i];
			}
		}
		
		return !(secondMatch == null) && !(firstMatch == null);
	}

	private boolean isFourOfKind() {
		firstMatch = null;
		
		for(int i = hand.length - 1; i >= 3 && firstMatch == null; i++) {
			if(hand[i].getRank() == hand[i-1].getRank() && hand[i-1].getRank() == hand[i-2].getRank() && hand[i-2].getRank() == hand[i-3].getRank()) {
				firstMatch = hand[i];
			}
		}
		
		return !(firstMatch == null);
	}

	private boolean isStraightFlush() {
		return false;
	}

	private boolean isRoyalFlush() {
		return false;
	}
}
