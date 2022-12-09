/**
 * A subclass of the Hand class and is used to model a hand of single in a Big Two card game,
 * respectively. It should override methods of the Hand class as appropriate.
 * 
 * @author Bryan Melvison
 */

public class Single extends Hand {
	
	/**
	 * a constructor for building a single hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Single(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}

	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand.
	 */
	
	public Card getTopCard() {
		return this.getCard(0);				
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true or false indicating if the hand is valid or not
	 */
	
	public boolean isValid() {
		if(this.size() == 1) {
			return true;
		}
		else {
			return false;	
		}
	}

	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @return the type of hand
	 */
	
	public String getType() {
		return "Single";
	}
}
