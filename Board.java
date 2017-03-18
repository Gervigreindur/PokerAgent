package PokerAgent;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
	

	private ArrayList<Player> playersInGame;
	private ArrayList<Player> playersInRound;
	private Integer[] currBetsMadeByPlayer;
	public Deck deck;
	private Card[] table; //mögulega þarf þetta ekki en kannski er betra að geyma það uppá að reikna möguleika hinna leikmannana á vinningi
	private boolean preFlop, flop, turn, river;
	private int pot, bigBlind, smallBlind, currBet; 
	protected int size;
	private int handsPlayed; //uppá að hækka blinds eftir einhvern tíma...?

	public Board(int size) {
		if(size > 8) {
			throw new IndexOutOfBoundsException("Board size can't be bigger than 8, throwed from Board constructor");
		}
		if(size < 0) {
			throw new IndexOutOfBoundsException("Board size has to be a positive number, throwed from Board constructor");
		}
		this.size = size;
		playersInGame = new ArrayList<Player>();
		playersInRound = new ArrayList<Player>();
		currBetsMadeByPlayer = new Integer[size];
		table = new Card[5];
		
		pot = 0;
		smallBlind = 5;
		bigBlind = 10;
		currBet = 0;
		handsPlayed = 0;
		deck = new Deck();
		preFlop = true;
		flop = turn = river = false;
	}
	
	public Board(Board board) {
		
		playersInGame = new ArrayList<Player>();
		playersInRound = new ArrayList<Player>();
		currBetsMadeByPlayer = new Integer[board.size];
		table = new Card[5];
		
		pot = 0;
		smallBlind = 5;
		bigBlind = 10;
		currBet = 0;
		handsPlayed = 0;
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
		while(true) {
			if(preFlop) {
				//Preflop---------------------------------------------------------
				handsPlayed++;
				System.out.println("PLAYING HAND #" + handsPlayed);
				System.out.println("PREFLOP STARTED");
				deck.shuffle();
				//deck.getAllCards();
				
				//each player gets two cards
				for(Player player : playersInGame) {
					player.recievesCards(deck.drawFromDeck());
					player.recievesCards(deck.drawFromDeck());
					
					System.out.println(player.seeName() + "'s cards: " + player.seeCards());
				}
				
				initializeCurrBetsMadeByPlayer();
				playersInRound.addAll(playersInGame);
				placeBlinds();
				placeBets(true);
				System.out.println("PREFLOP ENDED");
				
				if(checkForWinner()) {
					break;
				}
				preFlop = false;
				flop = true;
			}
			else if(flop) {
				//flop-----------------------------------------------------------
				System.out.println("FLOP STARTED");
				//the cards in flop are added to each player's hand and the the Board stores the table each time...
				Card card1 = deck.drawFromDeck();
				Card card2 = deck.drawFromDeck();
				Card card3 = deck.drawFromDeck();
				table[0] = card1;
				table[1] = card2;
				table[2] = card3;
				
				for(Player p : playersInRound) {
					p.recievesCards(card1);
					p.recievesCards(card2);
					p.recievesCards(card3);
					System.out.println(p.seeName() + "'s cards: " + p.seeCards());
				}
				System.out.print("Table:: ");
				for(int i = 0; i < 3; i++) {
					System.out.print(table[i] + " - ");
				}
				System.out.println("");
				
				initializeCurrBetsMadeByPlayer();
				placeBets(false);
				System.out.println("FLOP ENDED");
				flop = false;
				if(checkForWinner()) {
					preFlop = true;
					break;
				}
				turn = true;
			}
			else if(turn) {
				//turn-----------------------------------------------------------
				System.out.println("TURN STARTED");
				Card card4 = deck.drawFromDeck();
				table[3] = card4;
				for(Player p : playersInRound) {
					p.recievesCards(card4);
					System.out.println(p.seeName() + "'s cards: " + p.seeCards());
				}
				System.out.print("Table:: ");
				for(int i = 0; i < 4; i++) {
					System.out.print(table[i] + " - ");
				}
				System.out.println("");
				
				initializeCurrBetsMadeByPlayer();
				placeBets(false);
				System.out.println("TURN ENDED");
				turn = false;
				if(checkForWinner()) {
					preFlop = true;
					break;
				}
				river = true;
			}
			else if(river) {
				//river----------------------------------------------------------
				System.out.println("RIVER STARTED");
				Card card5 = deck.drawFromDeck();
				table[4] = card5;
				for(Player p : playersInRound) {
					p.recievesCards(card5);
					System.out.println(p.seeName() + "'s cards: " + p.seeCards());
				}
				
				System.out.print("Table:: ");
				for(int i = 0; i < 5; i++) {
					System.out.print(table[i] + " - ");
				}
				
				System.out.println("");
				initializeCurrBetsMadeByPlayer();
				placeBets(false);
				System.out.println("RIVER ENDED");
				river = false;
				preFlop = true;
				if(/*checkForWinner()*/true) {
					break;
				}
			}
		}
		//Round ends reset for next round..
		rearrangePlayers();
		
		//empty each players hand
		for(Player p : playersInGame) {
			System.out.println("EMPTYING HAND");
			p.emptyHand();
		}
		pot = 0;
		playersInRound.removeAll(playersInRound);
	}

	private boolean checkForWinner() {
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
				System.out.println("pot size is: " + pot);
				System.out.println(currPlayer.seeName() + ", place your bets..\n1 to check/call\t\t2 to raise\t\t3 to fold");
				
				String playerChoice = currPlayer.getInput();

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
	
	public Board getBoard() {
		return this;
	}
}
