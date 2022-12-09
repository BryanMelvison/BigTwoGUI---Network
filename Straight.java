/**
 * A subclass of the Hand class and is used to model a hand of straight in a Big Two card game,
 * respectively. It should override methods of the Hand class as appropriate.
 * 
 * @author Bryan Melvison
 */

public class Straight extends Hand{
	
	/**
	 * a constructor for building a straight hand with the specified player and list of cards.
	 * 
	 * @param player the player specified.
	 * @param cards list of cards used to create hand.
	 */
	
	public Straight(CardGamePlayer player, CardList cards) {
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
		if(this.size() == 5) {
			this.sort();
			int rank1 = (this.getCard(0).getRank() < 2)? this.getCard(0).getRank() + 13 : this.getCard(0).getRank();
			int rank2 = (this.getCard(1).getRank() < 2)? this.getCard(1).getRank() + 13 : this.getCard(1).getRank();
			int rank3 = (this.getCard(2).getRank() < 2)? this.getCard(2).getRank() + 13 : this.getCard(2).getRank();
			int rank4 = (this.getCard(3).getRank() < 2)? this.getCard(3).getRank() + 13 : this.getCard(3).getRank();
			int rank5 = (this.getCard(4).getRank() < 2)? this.getCard(4).getRank() + 13 : this.getCard(4).getRank();
			if((rank2 - rank1 == rank3 - rank2) && (rank4 - rank3 == rank5 - rank4)) {
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
		return "Straight";
	}

}
