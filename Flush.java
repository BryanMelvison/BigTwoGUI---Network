/**
 * A subclass of the Hand class and is used to model a hand of flush in a Big Two card game,
 * respectively. It should override methods of the Hand class as appropriate.
 * 
 * @author Bryan Melvison
 */
  
public class Flush extends Hand{
	
	/**
	 * a constructor for building a flush hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return the top card of this hand.
	 */
	
	public Card getTopCard() {
		this.sort();
		return this.getCard(4);
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return true or false indicating if the hand is valid or not
	 */
	
	public boolean isValid() {
		if (this.size() == 5) {
			int suit1 = this.getCard(0).getSuit();
			int suit2 = this.getCard(1).getSuit();
			int suit3 = this.getCard(2).getSuit();
			int suit4 = this.getCard(3).getSuit();
			int suit5 = this.getCard(4).getSuit();
			if ((suit1 == suit2) && (suit2 == suit3) && (suit3 == suit4) && (suit4 == suit5)) {
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
		return "Flush";
	}
}
