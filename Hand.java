/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards. It has
 * a private instance variable for storing the player who plays this hand. It also has methods for
 * getting the player of this hand, checking if it is a valid hand, getting the type of this hand,
 * getting the top card of this hand, and checking if it beats a specified hand. Below is a detailed
 * description for the Hand class.
 * 
 * @author Bryan Melvison
 */

public abstract class Hand extends CardList{
	
	private CardGamePlayer player;
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * a method for retrieving the player of this hand.
	 * 
	 * @return the player of this hand.
	 */
	
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * This is a dummy method and is going to be overridden in its subclasses
	 * 
	 * @return the top card of this hand.
	 */
	
	public Card getTopCard() {
		return null;
	}
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * 
	 * @param hand the hand of cards that will be evaluated with a specified hand.
	 * 
	 * @return boolean value indicating true if specified hand is stronger than this hand, otherwise false.
	 */
	
	public boolean beats(Hand hand) {
		
		//For Single
		if((this.size() == 1 && hand.size() == 1) && (this.isValid() && hand.isValid())) {
			if(this.getCard(0).compareTo(hand.getCard(0)) == 1) {
				return true;
			}
		}
		
		//For Pair
		if((this.size() == 2 && hand.size() == 2) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "Pair" && hand.getType() == "Pair") {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
			}
		}
		
		//For Triple
		if((this.size() == 3 && hand.size() == 3) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "Triple" && hand.getType() == "Triple") {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
			}
		}
		
		//For Straight
		if((this.size() == 5 && hand.size() == 5) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "Straight" && hand.getType() == "Straight") {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
			}
		}
		
		//For Flush
		if((this.size() == 5 && hand.size() == 5) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "Flush" && hand.getType() == "Straight") {
				return true;
			}
			else if (this.getType() == "Flush" && hand.getType() == "Flush") {
				if(this.getTopCard().getSuit() > hand.getTopCard().getSuit()) {
					return true;
				}
				else if (this.getTopCard().getSuit() == hand.getTopCard().getSuit()) {
					if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
						return true;
					}
				}
			}
		}
		
		//Full House
		if((this.size() == 5 && hand.size() == 5) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "FullHouse" && (hand.getType() == "Straight" || hand.getType() == "Flush" )) {
				return true;
			}
			else if (this.getType() == "FullHouse" && hand.getType() == "FullHouse") {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
			}
		}
		
		//Quad
		if((this.size() == 5 && hand.size() == 5) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "Quad" && (hand.getType() == "FullHouse" || hand.getType() == "Straight" || hand.getType() == "Flush" )) {
				return true;
			}
			else if (this.getType() == "Quad" && hand.getType() == "Quad") {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
			}
		}
		
		//Straight Flush
		if((this.size() == 5 && hand.size() == 5) && (this.isValid() && hand.isValid())) {
			if (this.getType() == "StraightFlush" && hand.getType() == "StraightFlush") {
				if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true or false indicating if the hand is valid or not
	 */
	
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @return the type of hand
	 */
	
	public abstract String getType();

}
