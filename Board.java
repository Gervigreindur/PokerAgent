package Poker;

import java.util.ArrayList;

public class Board {
	
	ArrayList<Player> players;
	Deck deck;
	
	Board(int size) {
		
		if(size > 8) {
			throw new IndexOutOfBoundsException("Board size can't be bigger than 8, throwed from Board constructor");
		}
		if(size < 0) {
			throw new IndexOutOfBoundsException("Board size has to be a positive number, throwed from Board constructor");
		}
		
		ArrayList<Player> players = new ArrayList<Player>();
		deck = new Deck();
		
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public void play() {
		// TODO Auto-generated method stub
		
	}

	public void addPlayer(Player player) {
		// TODO Auto-generated method stub
		
	}
}
