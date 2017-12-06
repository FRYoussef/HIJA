package model.representation;

import model.processor.concurrency.HEWorker;

public class Player {
	private int ID;
	private Card cards [];
	
	
	public Player(int ID, Card cards[]) {
		this.ID = ID;
		this.cards = cards;
	}
	
	public int getID() {
		return ID;
	}
	
	public Card getCard (int i) {
		return cards[i];
	}

}
