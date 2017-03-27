package PokerAgent;

import java.util.ArrayList;
import java.util.Arrays;

public class State {
	
	private ArrayList<Player> playersInRound;
	public Deck deck;
	//private Card[] table; //mögulega þarf þetta ekki en kannski er betra að geyma það uppá að reikna möguleika hinna leikmannana á vinningi
	protected boolean preFlop, flop, turn, river, raise;
	private int bigBlind, smallBlind;
	private Player currPlayer;
	protected int size, pot, currBet, callCounter, raiseCounter;

	public State(State state) {
		
		playersInRound = new ArrayList<Player>();
		
		for(Player p : state.getPlayers()) {
			Player newPlaya = new Player(p);
			playersInRound.add(newPlaya);
		}
		/*		
		for(int i = 0; i < 5; i++) {
			table[i] = new Card(state.getTable()[i].getSuit(), state.getTable()[i].getRank());
		}
		*/
		
		this.currPlayer = new Player(state.currPlayer);	
		this.raise = state.raise;
		this.callCounter = state.callCounter;
		pot = state.pot;
		smallBlind = 5;
		bigBlind = 10;
		currBet = state.currBet;
		deck = new Deck(state.deck);
		preFlop = state.preFlop;
		flop = state.flop;
		turn = state.turn;
		river = state.river;
	}
	
	public State(Board board, Player currentPlayer) {
		
		playersInRound = new ArrayList<Player>();
		int highestBet = 0;
		for(Player p : board.getPlayers()) {
			Player newPlaya = new Player(p);
			playersInRound.add(newPlaya);
			
			if(p.getCurrBet() > highestBet) {
				highestBet = p.getCurrBet();
			}
		}
		

		/*callCounter = playersInRound.size();
		callCounter = 0;

		//TODO Fix BB pre flop call counter bug.
		for(int i = 0; i < playersInRound.size(); i++) {
			if(playersInRound.get(i).getCurrBet() == highestBet) {
				if(highestBet != bigBlind && !board.preFlop) {
					callCounter++;
				}			
			}
		}*/
		
		
		
		/*
		for(int i = 0; i < 5; i++) {
			table[i] = new Card(board.getTable()[i].getSuit(), board.getTable()[i].getRank());
		}
		*/
		
		raise = false;
		
		this.currPlayer = new Player(currentPlayer);
		pot = board.pot;
		smallBlind = 5;
		bigBlind = 10;
		currBet = board.currBet;
		deck = new Deck(board.deck);
		preFlop = board.preFlop;
		flop = board.flop;
		turn = board.turn;
		river = board.river;

	}
	
	public boolean isTerminal() {

		if(playersInRound.size() == 1) {
			return true;
		}
		else if(river) {
			for(Player p : playersInRound) {
				if(p.getCurrBet() != currBet) {
					return false;
				}
			}
			return true;
		}			
				

		return false;
	}
	
	public int terminal(Player me) {
		/*TODO Make two terminal states:
		 * One returns 1 for win, 0 for draw and -1 for loss
		 * The other one returns the pot for win and 0 for loss
		 */
		
		//Returns number wins
		if(playersInRound.size() == 1) {
			if(playersInRound.get(0).getID() == me.getID() ) {
				return 1;
			}
			else {
				return -1;
			}
		}
		else if(river) {
			System.out.println("ég er inní river!!!");
			int best = -1;
			ArrayList<Player> winners = new ArrayList<Player>();
			//Evaluate best hand 
			for(Player player : playersInRound) {
				int value = player.getHandValueSimulation();
				if(value == best) {
					winners.add(player);
				}
				if(value > best) {
					best = value;
					winners.clear();
				}
			}
			
			if(winners.isEmpty()) {
				for(Player player : playersInRound) {
					if(player.getID() == me.getID()) {
						return 1;
					}
					else {
						return -1;
					}
				}
			}
			// If we need to divide the pot 
			for(Player player : winners) {
				if(player.getID() == me.getID()) {
					return 0;
				}
				else {
					return -1;
				}
			}
		}
		return 0;
		
	}
	
	public int getNumberOfPLayersInRound() {
		return playersInRound.size();
	}
	
	private void incrementCurrPlayer() {

		for(int i = 0; i < playersInRound.size(); i++) {
			if(playersInRound.get(i).getID() == currPlayer.getID()) {
				if(i+1 != playersInRound.size()) {
					currPlayer = playersInRound.get(i+1);
				}
				else {
					currPlayer = playersInRound.get(i);
				}
				break;
			}
		}
	}
	
	private boolean cardsExist(Card c, Player agent) {
		
		for(int i = 0; i < agent.getHand().getHand().length; i++) {
			if(agent.getHand().getHand()[i] != null) {
				if(c.equals(agent.getHand().getHand()[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void simulateOpponentsHands(Player agent) {
		
		for(Player player : playersInRound) {			
			if(player.getID() != agent.getID()) {
				player.emptyHand();
				int counter = 0;
				while(counter < 2) {
					Card c = deck.drawFromDeck();
					if(!cardsExist(c, agent)) {
						player.recievesCards(c);
						counter++;
					}
				}			
			}
						
			if(flop && player.getID() != agent.getID()) {
				player.recievesCards(agent.getHand().getHand()[2]);
				player.recievesCards(agent.getHand().getHand()[3]);
				player.recievesCards(agent.getHand().getHand()[4]);
			}
			else if(turn && player.getID() != agent.getID()) {
				player.recievesCards(agent.getHand().getHand()[5]);
			}
			else if(river && player.getID() != agent.getID()) {
				player.recievesCards(agent.getHand().getHand()[6]);
			}
		}
	}
	
	private void dealCards() {
		ArrayList<Card> takenCards = new ArrayList<Card>();
		
		for(Player p : playersInRound) {
			for(int i = 0; i < 2; i++) {
				if(p.getHand().getHand()[i] != null) {
					takenCards.add(new Card(p.getHand().getHand()[i].getSuit(), p.getHand().getHand()[i].getRank()));
				}
			}
		}
		for(int i = 2; i < currPlayer.getHand().getHand().length; i++) {
			if(currPlayer.getHand().getHand()[i] != null) {
				takenCards.add(new Card(currPlayer.getHand().getHand()[i].getSuit(), currPlayer.getHand().getHand()[i].getRank()));
			}
		}
		if(flop) {
			for(Player p : playersInRound) {
				int counter = 0;
				
				while(counter <= 3) {
					boolean checker = true;
					Card c = deck.drawFromDeck();
					for(Card card : takenCards) {
						if(c.equals(card)) {
							checker = false;
							break;
						}
					}
					if(checker) {
						p.recievesCards(c);
						counter++;
					}
				}
			}
		}
		else if(turn || river) {
			for(Player p : playersInRound) {
				int counter = 0;
				
				while(counter < 1) {
					boolean checker = true;
					Card c = deck.drawFromDeck();
					for(Card card : takenCards) {
						if(c.equals(card)) {
							checker = false;
							break;
						}
					}
					if(checker) {
						p.recievesCards(c);
						counter++;
					}
				}
			}
		}
	}
	
	public Hand getCurrPlayHands() {
		return currPlayer.getHand();
	}
	
	public void takeAction(int action) {
		
		if(action == 1) {
			
			int diff = currBet - currPlayer.getCurrBet();
			if(diff > 0) {
				currPlayer.madeBet(diff);
				pot += diff;
			}
			callCounter++;
			incrementCurrPlayer();
		}
		else if(action == 2) {
			raise = true;
			callCounter = 0;
			currBet += bigBlind;
			currPlayer.madeBet(currBet);
			pot += currBet;
			incrementCurrPlayer();
		}
		else {
			for(Player p : playersInRound) {
				if(p.getID() == currPlayer.getID()) {
					incrementCurrPlayer();
					playersInRound.remove(p);
					break;
				}
			}
		}
		changeState();
	}
	
	private void changeState() {
		//TODO remember the pre flop BB call counter bug
		if(playersInRound.size() != 1) {
			if((raise && callCounter == playersInRound.size() -1) || (!raise && callCounter == playersInRound.size())) {

				if(preFlop) {
					preFlop = false;
					flop = true;
				}
				else if(flop) {
					flop = false;
					turn = true;
				}
				else if(turn) {
					turn = false;
					river = true;
				}

				currPlayer = new Player(playersInRound.get(0));
				dealCards();
				currBet = 0;	
				raise = false;
				callCounter = 0;			
			}	
		}
	}
/*
	private boolean checkForWinner() {
		//System.out.println("CHECKING FOR WINNER");
		if(playersInRound.size() == 1) {
			for(Player player : playersInRound) {
				player.addPot(pot);
			}
			
			return true;
		}
		else if(river) {
			int best = -1;
			int winnerID = -1;
			ArrayList<Player> winners = new ArrayList<Player>();
			//Evaluate best hand 
			for(Player player : playersInRound) {
				int value = player.getHandValue();
				if(value == best) {
					winners.add(player);
				}
				if(value > best) {
					winnerID = player.getID();
					best = value;
					winners.clear();
				}
			}
			
			if(winners.isEmpty()) {
				for(Player player : playersInRound) {
					if(player.getID() == winnerID) {
						player.addPot(pot);
						return true;
					}
				}
			}
			// If we need to divide the pot 
			int potDivided = pot / winners.size();
			for(Player player : winners) {
				player.addPot(potDivided);
			}
			
			return true;
		}
		
		return false;
	}
*/
/*
	private void placeBets(boolean betMade) {
		if(playersInRound.isEmpty()) {
			playersInRound.addAll(playersInGame);
		}
		
		int players = playersInRound.size();
		int betCounter = 0;
		
		while(betCounter < players) {
			for(int i = 0; i < playersInRound.size() ; i++) {
				Player currPlayer = playersInRound.get(i);
				//System.out.println("pot size is: " + pot);
				//System.out.println(currPlayer.seeName() + ", place your bets..\n1 to check/call\t\t2 to raise\t\t3 to fold");
				
				String playerChoice = currPlayer.getInput();

				if(playerChoice.equals("2")) {
					//raise
					//System.out.println(currPlayer.seeName() + " raised " + bigBlind + "$");
					currBet += bigBlind;
					currBetsMadeByPlayer[currPlayer.getID()] += bigBlind;
					currPlayer.madeBet(bigBlind);
					pot += bigBlind;
					betMade = true;
					betCounter = 1;
				}
				else if(playerChoice.equals("3")) {
					//fold
					//System.out.println(currPlayer.seeName() + " folded");
					playersInRound.remove(playersInRound.indexOf(currPlayer));
					i--;
					players--;
				}
				else {
					//check/call
					if(betMade) {
						//call
						int diff = currBet - currBetsMadeByPlayer[currPlayer.getID()];
						currPlayer.madeBet(diff);
						currBetsMadeByPlayer[currPlayer.getID()] = currBet;
						pot += diff;
						//System.out.println(currPlayer.seeName() + " called " + diff + "$");
					}
					//else {//System.out.println(currPlayer.seeName() + " checked");} //check
					betCounter++;
				}
				//System.out.println("------");
				if(betCounter == players) { break; }
			}
		}
		
		currBet = 0;
	}
	*/
	/*
	public Card[] getTable() {
		return table;
	}
	*/
	public State getState() {
		return this;
	}
	
	public ArrayList<Player> getPlayers() {
		return playersInRound;
	}

	/**
	 * @return the currPlayer
	 */
	public Player getCurrPlayer() {
		return currPlayer;
	}
	
}
