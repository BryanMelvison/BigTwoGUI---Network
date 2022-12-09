/**
 * A subclass of the Hand class and is used to model a hand of triple in a Big Two card game,
 * respectively. It should override methods of the Hand class as appropriate.
 * 
 * @author Bryan Melvison
 */

public class Triple extends Hand{
	
	/**
	 * a constructor for building a triple hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand.
	 */
	
	public Card getTopCard() {
		this.sort();
		return this.getCard(2);
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true or false indicating if the hand is valid or not
	 */
	
	public boolean isValid() {
		if(this.size() == 3) {
			if((this.getCard(0).getRank() == this.getCard(1).getRank()) && (this.getCard(1).getRank()== this.getCard(2).getRank())) {
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
		return "Triple";
	}
	
}
