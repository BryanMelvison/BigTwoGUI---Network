/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a
 * Big Two card game. It should override the compareTo() method it inherits from the Card
 * class to reflect the ordering of cards used in a Big Two card game. Below is a detailed
 * description for the BigTwoCard class.
 * 
 * @author Bryan Melvison
 */

public class BigTwoCard extends Card{
	
	/**
	 * a constructor for building a card with the specified
	 * suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and 12.
	 * 
	 * @param suit the suit of the card, an integer between 0 and 3.
	 * @param rank the rank of the card, an integer between 0 and 12.
	 */
	
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	
	/**
	 * a method for comparing the order of this card with the
	 * specified card. Returns a negative integer, zero, or a positive integer when this card is
	 * less than, equal to, or greater than the specified card.
	 * 
	 * @param card the card compared to with the specified card
	 * @return an integer indicating the comparison between the cards, a negative integer, zero, or a positive integer 
	 * when this card is less than, equal to, or greater than the specified card.
	 */
	
	public int compareTo(Card card) {

		int thisrank = this.rank;
		
		int cardrank = card.rank;
		
		if (this.rank == 0) {
			thisrank = 13;
		}
		
		if (this.rank == 1) {
			thisrank = 14;
		}
		
		if (card.rank == 0) {
			cardrank = 13;
		}
		
		if (card.rank == 1) {
			cardrank = 14;
		
		}
		if (thisrank > cardrank) {
			return 1;
		} 
		
		else if (thisrank < cardrank) {
			return -1;
		} 
		
		else if (this.suit > card.suit) {
			return 1;
		} 
		
		else if (this.suit < card.suit) {
			return -1;
		} 
		
		else {
			return 0;
		}
	}
}
