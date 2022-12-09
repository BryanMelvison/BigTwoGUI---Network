import java.util.ArrayList;

import javax.swing.*;


/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card
 * game. It has private instance variables for storing the number of players, a deck of cards, a
 * list of players, a list of hands played on the table, an index of the current player, and a user
 * interface. Below is a detailed description for the BigTwo class.
 * 
 * @author Bryan Melvison
 *
 */

public class BigTwo implements CardGame{
	
	private int numOfPlayers;
	
	private Deck deck;
	
	private ArrayList<CardGamePlayer> playerList;
	
	private ArrayList<Hand> handsOnTable;
	
	private int currentPlayerIdx;
	
	private BigTwoGUI ui;
	
	private BigTwoClient bigTwoClient;
	
	//To track the last person to place their cards on the table
	private int lasthand;
	
	/**
	 * A constructor for creating a Big Two card game. You should (i) create 4
	 * players and add them to the player list; and (ii) create a BigTwoUI object for providing
	 * the user interface. 
	 */
	
	public BigTwo() {
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++) {
			CardGamePlayer player = new CardGamePlayer();
			this.playerList.add(player);
		}
		handsOnTable = new ArrayList<Hand>();
		this.ui = new BigTwoGUI(this);
		this.bigTwoClient = new BigTwoClient(this, ui);
	}
	
//	/**
//	 * a method for getting state of game
//	 * 
//	 * @return whether the game has started or not
//	 */
//	public boolean getStart() {
//		return this.start;
//	}
//	
//	/**
//	 * a method for setting state of game
//	 * 
//	 * @param start whether the game has started or not
//	 */
//	public void setStart(boolean start) {
//		this.start = start;
//	}
//	

	/**
	 * a method for getting the client of the game
	 * 
	 * @return the client of the game
	 */
	public BigTwoClient getBigTwoClient() {
		return this.bigTwoClient;
	}
	
	/**
	 * a method for getting the gui of the game
	 * 
	 * @return the gui of the game
	 */
	public BigTwoGUI getBigTwoGUI() {
		return this.ui;
	}
	
	/**
	 * a method for getting the number of players
	 * 
	 * @return the number of players
	 */
	
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}
	
	/**
	 * a method for retrieving the deck of cards being used
	 * 
	 * @return deck of cards
	 */
	
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * a method for retrieving the list of players.
	 * 
	 * @return list of players
	 */
	
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}
	
	/**
	 * a method for retrieving the list of hands played on the table.
	 * 
	 * @return list of hands on the table
	 */
	
	public ArrayList<Hand> getHandsOnTable(){
		return this.handsOnTable;
	}
	
	/**
	 * a method for retrieving the index of the current player.
	 * 
	 * @return the index of the current player
	 */
	
	public int getCurrentPlayerIdx() {
		return this.currentPlayerIdx;
	}
	
	/**
	 * a method for starting/restarting the game with a given
	 * shuffled deck of cards. 
	 * 
	 * @param deck the deck of cards used for the game.
	 */
	
	public void start(Deck deck) {
		
		//Removes all cards from the players and the table
		for (int i = 0; i < 4; i++) {
			this.playerList.get(i).removeAllCards();
		}
		
		this.handsOnTable.clear();
		//Distribute the cards to the players
		for (int i = 0; i < 52; i++) {
			this.playerList.get(i % 4).addCard(deck.getCard(i));
			
			//Look for the 3 diamond holder
			if(deck.getCard(i).getSuit()== 0 && deck.getCard(i).getRank() == 2) {
				this.currentPlayerIdx = i%4;
			}
		}
		this.ui.setActivePlayer(this.bigTwoClient.getPlayerID());
		for (int i = 0; i < 4; i++) {
			this.playerList.get(i).getCardsInHand().sort();
		}
		
		this.ui.repaint();
		this.ui.clearMsgArea();
//		this.ui.promptActivePlayer();
	}
	
	// Prints statement if move is not legal and prompt for the input of active player again
	private void printNotLegal() {
		this.ui.printMsg("Not a legal move!!!");
		this.ui.promptActivePlayer();
	}
	
	// Prints statement if move is legal 
	private void printResults(Hand hand) {
		String cards = "";
		for (int i = 0; i < hand.size(); i++) {
			cards += hand.getCard(i) + " ";
		}
		this.ui.printMsg(this.playerList.get(currentPlayerIdx).getName() + " played: "+"{"+ hand.getType().toString() + "} " + cards);
	}
	
	/**
	 * a method for checking a move made by a player. This method should be called from the makeMove() method.
	 * 
	 * @param playerIdx the index of the player making the move.
	 * @param cardIdx the list of indices specifying the cards played.
	 */
	
	public synchronized void checkMove(int playerIdx, int[] cardIdx) {
		
		boolean flag = false;
		//If a person were to pass
		if (cardIdx == null) {
			
			//Checks if its not the first move and not the same person as the one who last placed their cards there
			if (this.handsOnTable.size() != 0 && lasthand != this.currentPlayerIdx ) {
				this.ui.printMsg(this.playerList.get(currentPlayerIdx).getName() + " played: " + "{Pass}");
			}
			
			else {
				this.printNotLegal();
				flag = true;
			}
		}
		
		else {

			CardList temp = new CardList();
			for (int j = 0; j < cardIdx.length; j++) {
				temp.addCard(this.getPlayerList().get(currentPlayerIdx).getCardsInHand().getCard(cardIdx[j]));
			}
			Hand play_hand = this.composeHand(this.getPlayerList().get(currentPlayerIdx), temp);
			
			// Check if combo is incorrect
			if (play_hand == null) {
				this.printNotLegal();
				flag = true;
			}
			
			//At the start of the game
			
			if (this.handsOnTable.size() == 0) {
				Card three_dia = new Card(0,2);
				if (play_hand.contains(three_dia)) {
					
					//add cards to table and remove the cards from the player's hand
					
					this.handsOnTable.add(play_hand);
					for (int i = cardIdx.length - 1; i >= 0; i-- ) {
						this.getPlayerList().get(currentPlayerIdx).getCardsInHand().removeCard(cardIdx[i]);
					}
					
					//Print out the cards to the screen
					this.printResults(play_hand);
					this.lasthand = this.currentPlayerIdx;
					flag = false;
				}
				
				//If there's no three diamond then it's an illegal move
				else {
					this.printNotLegal();
					flag = true;
				}
			}
			
			// Else if its not the first turn anymore
			else if (this.handsOnTable.size() > 0) {
				
				Hand prev_hand = this.handsOnTable.get(this.handsOnTable.size() - 1);
				
				// Check for the similarities of the size of the hand
				
				if (play_hand.size() == prev_hand.size() && this.lasthand != this.currentPlayerIdx) {
					
					// If the played cards are stronger than the previous ones
					
					if (play_hand.beats(prev_hand)) {
						this.handsOnTable.add(play_hand);
						for (int i = cardIdx.length - 1; i >= 0; i-- ) {
							this.getPlayerList().get(currentPlayerIdx).getCardsInHand().removeCard(cardIdx[i]);
						}
						
						//Print out the cards to the screen
						this.printResults(play_hand);
						this.lasthand = this.currentPlayerIdx;
						flag = false;
					}
					
					// If the played cards are weaker
					else {
						this.printNotLegal();
						flag = true;
					}
					
				}
				
				else if (this.lasthand == this.currentPlayerIdx) {
					
					//add cards to table from hand 
					this.handsOnTable.add(play_hand);
					for (int i = cardIdx.length - 1; i >= 0; i-- ) {
						this.getPlayerList().get(currentPlayerIdx).getCardsInHand().removeCard(cardIdx[i]);
					}
					
					//Print out the cards to the screen
					this.printResults(play_hand);
					this.lasthand = this.currentPlayerIdx;
					flag = false;
				}
				
				else {
					this.printNotLegal();
					flag = true;
				}
			}
		}
		//If game hasn't ended	
		if (this.endOfGame() == false) {
			if (flag == false) {
				int index = (this.getCurrentPlayerIdx() + 1) % 4;
				this.currentPlayerIdx = index;
//				this.ui.setActivePlayer(index);
				this.ui.repaint();
//				this.ui.promptActivePlayer();
			}
		}
		
		//Otherwise
		else if (this.endOfGame() == true){
			print_gameend();
		}
	}
	
	// prints out game end ui
	private void print_gameend() {
		this.ui.printMsg("Game ends  \n");
		for(int i = 0; i < 4; i ++) {
			if (this.getPlayerList().get(i).getCardsInHand().size() != 0) {
				this.ui.printMsg(this.playerList.get(i).getName() + " has " + this.getPlayerList().get(i).getCardsInHand().size() + " cards in hand. \n");	
			}
			else {
				this.ui.printMsg(this.playerList.get(i).getName() + " wins the game. \n");
				JOptionPane.showMessageDialog(new JFrame(), this.playerList.get(i).getName() + " wins the game. \n");
				CardGameMessage readyMessage = new CardGameMessage(CardGameMessage.READY, -1, null);
				bigTwoClient.sendMessage(readyMessage);
			}
		}
	}
	
	/**
	 * a method for making a move by a player with the specified index
	 * using the cards specified by the list of indices. This
	 * method should be called from the BigTwoUI after the active player has selected cards
	 * to make his/her move. 
	 * 
	 * @param playerIdx the index of the player making the move.
	 * @param cardIdx the list of indices specifying the cards played.
	 */
	
	public void makeMove(int playerIdx, int[] cardIdx) {
		this.bigTwoClient.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}

	/**
	 * a method for checking if the game ends.
	 * 
	 * @return the state of the game whether it has ended or not.
	 */
	
	public boolean endOfGame() {
		for (int i = 0; i < 4; i++) {
			if (this.playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * a method for starting a Big Two card game
	 * 
	 * @param args
	 */
	
	public static void main(String args[]) {
		BigTwo bigtwo = new BigTwo();
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null if no valid hand can be composed from the specified list of cards.
	 * 
	 * @param player the player holding the list of cards.
	 * @param cards list of cards played.
	 * 
	 * @return a valid hand made from the list of cards.
	 */
	
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		StraightFlush straightflush = new StraightFlush(player, cards);
		Quad quad = new Quad(player, cards);
		FullHouse fullhouse = new FullHouse(player, cards);
		Flush flush = new Flush(player, cards);
		Straight straight = new Straight(player, cards);
		Triple triple = new Triple(player, cards);
		Pair pair = new Pair(player, cards);
		Single single= new Single(player, cards);
		
		if (straightflush.isValid()) {
			return straightflush;
		}
		if (quad.isValid()) {
			return quad;
		}
		if (fullhouse.isValid()) {
			return fullhouse;
		}
		if (flush.isValid()) {
			return flush;
		}
		if (straight.isValid()) {
			return straight;
		}
		if (triple.isValid()) {
			return triple;
		}
		if (pair.isValid()) {
			return pair;
		}
		if (single.isValid()) {
			return single;
		}
		return null;
	}
}
