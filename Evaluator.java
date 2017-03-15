package PokerAgent;

public class Evaluator {
	
	/* Evaluator returns higher integers for better hands 
	 * HighCards adds integers from 2-14 
	 * Pair 	 adds 20
	 * Two Pairs adds 40
	 * Set 		      60
	 * Straight       80
	 * Flush          100
	 * Full House     120
	 * Four of Kind   140
	 * Straight Flush 160
	 * 
	 */

	public static int evaluate(Card[] table, Hand hand) {
		
		 
		
		//finding out how many cards there are with same rank
		
		int sameCards = 1; //we know there will be at least one card of any rank
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 5; j++) {
				if(hand.hand[i].getRank() == table[j].getRank()) {
					sameCards++;
				}
			}
			
		}
		return 0;
	}
}
