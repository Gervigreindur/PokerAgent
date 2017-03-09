package Poker;

import java.util.ArrayList;

public class Board {
	
	ArrayList<Player> players;
	Deck deck;
	boolean preFlop, flop, turn, river;
	
	Board(int size) {
		
		if(size > 8) {
			throw new IndexOutOfBoundsException("Board size can't be bigger than 8, throwed from Board constructor");
		}
		if(size < 0) {
			throw new IndexOutOfBoundsException("Board size has to be a positive number, throwed from Board constructor");
		}
		
		ArrayList<Player> players = new ArrayList<Player>();
		deck = new Deck();
		preFlop = true;
		flop = turn = river = false;
	}

	public boolean isActive() {
		if(players.size() > 1) {
			return true;
		}
		return false;
	}

	public void play() {
		if(preFlop) {
			deck.shuffle();
			
			for(Player player : players) {
				player.recievesCards(deck.drawFromDeck(), deck.drawFromDeck());
			}
		}
		while(betsAreBeingTaken()) {
			placeBets();
		}
		if(flop) {
			
		}
		else if(turn) {
			
		}
		else if(river){
			
		}
	}

	private boolean betsAreBeingTaken() {
		//Find out if all bets are equal if not return true
		return false;
	}

	private void placeBets() {
		// TODO Auto-generated method stub
		
	}


	public void addPlayer(Player player) {
		players.add(player);
		
	}
}
