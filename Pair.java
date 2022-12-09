/**
 * A subclass of the Hand class and is used to model a hand of pair in a Big Two card game,
 * respectively. It should override methods of the Hand class as appropriate.
 * 
 * @author Bryan Melvison
 */

public class Pair extends Hand{
	
	/**
	 * a constructor for building a pair hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand.
	 */
	
	public Card getTopCard() {
		this.sort();
		return this.getCard(1);
		
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true or false indicating if the hand is valid or not
	 */
	
	public boolean isValid() {
		if (this.size() == 2) {
			if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @return the type of hand
	 */
	
	public String getType() {
		return "Pair";
	}
}
