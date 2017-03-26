package PokerAgent;

import java.util.ArrayList;
import java.util.Arrays;

public class State {
	
	private ArrayList<Player> playersInRound;
	public Deck deck;
	private Card[] table; //mögulega þarf þetta ekki en kannski er betra að geyma það uppá að reikna möguleika hinna leikmannana á vinningi
	protected boolean preFlop, flop, turn, river;
	private int bigBlind, smallBlind;
	private Player currPlayer;
	protected int size, pot, currBet;

	public State(State state) {
		
		playersInRound = new ArrayList<Player>();
		
		for(Player p : state.getPlayers()) {
			Player newPlaya = new Player(p.seeName(), p.seeStack());
			playersInRound.add(newPlaya);
		}
				
		for(int i = 0; i < 5; i++) {
			table[i] = new Card(state.getTable()[i].getSuit(), state.getTable()[i].getRank());
		}
		
		this.currPlayer = new Player(currPlayer);	
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
	
	public State(Board board, Player currPlayer) {
		
		playersInRound = new ArrayList<Player>();
		
		for(Player p : board.getPlayers()) {
			Player newPlaya = new Player(p.seeName(), p.seeStack());
			playersInRound.add(newPlaya);
		}
		
		for(int i = 0; i < 5; i++) {
			table[i] = new Card(board.getTable()[i].getSuit(), board.getTable()[i].getRank());
		}
		
		this.currPlayer = new Player(currPlayer);	
		pot = board.pot;
		smallBlind = 5;
		bigBlind = 10;
		currBet = board.currBet;
		deck = new Deck();
		preFlop = board.preFlop;
		flop = board.flop;
		turn = board.turn;
		river = board.river;
	}
	
	private void incrementCurrPlayer() {
		for(int i = 0; i < playersInRound.size(); i++) {
			if(playersInRound.get(i).getID() == currPlayer.getID()) {
				if(i+1 != playersInRound.size()) {
					currPlayer = new Player(playersInRound.get(i+1));
				}
				else {
					currPlayer = new Player(playersInRound.get(0));
				}
			}
		}
	}
	
	private boolean cardsExist(Card c, Player agent) {
		
		for(int i = 0; i < agent.getHand().getHand().length; i++) {
			if(c.equals(agent.getHand().getHand()[i])) {
				return true;
			}
		}
		return false;
	}
	
	public void simulateOpponentsHands(Player agent) {
		
		for(Player player : playersInRound) {
			if(player.getID() != agent.getID()) {
				int counter = 0;
				while(counter <= 2) {
					Card c = deck.drawFromDeck();
					if(!cardsExist(c, agent)) {
						player.recievesCards(deck.drawFromDeck());
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
		incrementCurrPlayer();
		if(action == 1) {
			int diff = currBet - currPlayer.getCurrBet();
			if(diff > 0) {
					currPlayer.madeBet(diff);
					pot += diff;
			}
		}
		else if(action == 2) {
			currBet += bigBlind;
			currPlayer.madeBet(currBet);
			pot += currBet;
		}
		else {
			playersInRound.remove(currPlayer);
		}
	}

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
	public Card[] getTable() {
		return table;
	}
	public State getState() {
		return this;
	}
	
	public ArrayList<Player> getPlayers() {
		return playersInRound;
	}
}
