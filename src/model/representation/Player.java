package model.representation;


public class Player {
	private int ID;
	private Card cards [];
	
	
	public Player(int ID, Card ...cards) {
		this.ID = ID;
		this.cards = cards;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getNCards () {
		return cards.length;
	}
	
	public Card getCard (int i) {
		return cards[i];
	}

}
