/**
 * A subclass of the Hand class and is used to model a hand of quad in a Big Two card game,
 * respectively. It should override methods of the Hand class as appropriate.
 * 
 * @author Bryan Melvison
 */

public class Quad extends Hand{
	
	/**
	 * a constructor for building a quad hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand.
	 */
	
	public Card getTopCard() {
		this.sort();
		
		//For example 3 3 3 3 4, hence top card is the 4th card assuming its valid and sorted
		
		if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
			return this.getCard(3);
		}
		
		//For example 4 5 5 5 5, hence top card is the last card assuming its valid and sorted
		else {
			return this.getCard(4);
		}
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true or false indicating if the hand is valid or not
	 */
	
	public boolean isValid() {
		this.sort();
		if(this.size() == 5) {
			// Checks if xxxxy assuming sorted
			if (this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() == this.getCard(3).getRank()) {
				return true;
			}
			// Checks if xyyyy assuming sorted
			else if(this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(4).getRank()) {
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
		return "Quad";
	}
	
}
