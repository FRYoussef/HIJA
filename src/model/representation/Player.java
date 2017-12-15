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
	
	public void setId(int id){
		this.ID = id; 
	}
	public int getNCards () {
		return cards.length;
	}
	
	public Card getCard (int i) {
		return cards[i];
	}

	public String toString(){
		String res = "ID: " + this.getID() + " Cartas: ";
		for(int i = 0; i < this.cards.length; i++)
			res += this.cards[i].toString();
		res += "\n";
		
		return res;
	}
}
