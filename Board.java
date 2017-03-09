package PokerAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	private ArrayList<Player> playersInGame;
	private ArrayList<Player> playersInRound;
	private Integer[] currBetsMadeByPlayer;
	private Deck deck;
	private Card[] table;
	private boolean preFlop, flop, turn, river;
	private int pot, bigBlind, smallBlind, currBet;
	
	public Board(int size) {
		if(size > 8) {
			throw new IndexOutOfBoundsException("Board size can't be bigger than 8, throwed from Board constructor");
		}
		if(size < 0) {
			throw new IndexOutOfBoundsException("Board size has to be a positive number, throwed from Board constructor");
		}
		
		playersInGame = new ArrayList<Player>();
		playersInRound = new ArrayList<Player>();
		currBetsMadeByPlayer = new Integer[size];
		table = new Card[5];
		
		pot = 0;
		smallBlind = 5;
		bigBlind = 10;
		currBet = 0;
		deck = new Deck();
		preFlop = true;
		flop = turn = river = false;
	}

	private void initializeCurrBetsMadeByPlayer() {
		Arrays.fill(currBetsMadeByPlayer, 0);
	}

	public boolean isActive() {
		if(playersInGame.size() > 1) {
			return true;
		}
		return false;
	}

	public void play() {
		//TODO:
		//* Finna ef gaur er að fara all in eða á ekki fyrir depositinu, eins og td ef stóri blindi á bara 5$ eftir
		//* Fylla inní checkForWinner fallið svo það skoði spilin og finni hver er með bestu höndina
		
		//Preflop---------------------------------------------------------
		System.out.println("PREFLOP STARTED");
		deck.shuffle();
		
		for(Player player : playersInGame) {
			player.recievesCards(deck.drawFromDeck(), deck.drawFromDeck());
			System.out.println(player.seeCards());
		}
		
		initializeCurrBetsMadeByPlayer();
		playersInRound.addAll(playersInGame);
		placeBlinds();
		placeBets(true);
		System.out.println("PREFLOP ENDED");
		
		//flop-----------------------------------------------------------
		System.out.println("FLOP STARTED");
		table[0] = deck.drawFromDeck();
		table[1] = deck.drawFromDeck();
		table[2] = deck.drawFromDeck();
		initializeCurrBetsMadeByPlayer();
		placeBets(false);
		System.out.println("FLOP ENDED");
	
		//turn-----------------------------------------------------------
		System.out.println("TURN STARTED");
		table[3] = deck.drawFromDeck();
		initializeCurrBetsMadeByPlayer();
		placeBets(false);
		System.out.println("TURN ENDED");
	
		//river----------------------------------------------------------
		System.out.println("RIVER STARTED");
		table[4] = deck.drawFromDeck();
		initializeCurrBetsMadeByPlayer();
		placeBets(false);
		System.out.println("RIVER ENDED");
		
		//check who won the hand and pay out!----------------------------
		checkForWinner();
		
		rearrangePlayers();
		pot = 0;
		playersInRound.removeAll(playersInRound);
	}

	private void checkForWinner() {
		// TODO Auto-generated method stub
		
	}

	private void rearrangePlayers() {
		Player temp = playersInGame.get(0);
		playersInGame.remove(0);
		playersInGame.add(temp);
	}

	private void placeBlinds() {
		System.out.println("Big blind : " + playersInGame.get(playersInRound.size() - 1).seeName() + "\nSmall blind : " + playersInGame.get(playersInRound.size() - 2).seeName() + "\n");

		int bb = playersInRound.size() - 1;
		int sb = playersInRound.size() - 2;
		playersInRound.get(bb).madeBet(bigBlind);
		playersInRound.get(sb).madeBet(smallBlind);
		
		currBetsMadeByPlayer[bb] = bigBlind;
		currBetsMadeByPlayer[sb] = smallBlind;
		
		currBet = bigBlind;
		pot = bigBlind + smallBlind;
		System.out.println("BLINDS PLACED");
	}

	/*private boolean betsAreBeingTaken() {
		//Find out if all bets are equal if not return true
		return false;
	}*/

	private void placeBets(boolean betMade) {
		if(playersInRound.isEmpty()) {
			playersInRound.addAll(playersInGame);
		}
		
		int players = playersInRound.size();
		int betCounter = 0;
		
		while(betCounter < players) {
			for(int i = 0; i < playersInRound.size() ; i++) {
				Player currPlayer = playersInRound.get(i);
				System.out.println(currPlayer.seeName() + ", place your bets..\n1 to check/call\t\t2 to raise\t\t3 to fold");
				BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
				String playerChoice = "";
				
				try {
					playerChoice = buffer.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(playerChoice.equals("2")) {
					//raise
					System.out.println(currPlayer.seeName() + " raised " + bigBlind + "$");
					currBet += bigBlind;
					currBetsMadeByPlayer[currPlayer.getID()] += bigBlind;
					currPlayer.madeBet(bigBlind);
					pot += bigBlind;
					betMade = true;
					betCounter = 1;
				}
				else if(playerChoice.equals("3")) {
					//fold
					System.out.println(currPlayer.seeName() + " folded");
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
						System.out.println(currPlayer.seeName() + " called " + diff + "$");
					}
					else {System.out.println(currPlayer.seeName() + " checked");} //check
					betCounter++;
				}
				System.out.println("------");
				if(betCounter == players) { break; }
			}
		}
		
		//Remove after testing============================================
		System.out.println("Pot size is now " + pot);
		for(Player p : playersInGame) {
			System.out.println(p.seeName() + " now has " + p.seeStack());
		}
		System.out.println("");
		//================================================================
		
		currBet = 0;
	}

	public void addPlayer(Player player) {
		playersInGame.add(player);
		player.setID(playersInGame.size() - 1);
	}
}
