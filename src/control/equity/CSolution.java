package control.equity;

import java.util.ArrayList;
import java.util.Observable;

import model.representation.Card;
import model.representation.game.Deck;

public class CSolution extends Observable{
	
	ArrayList<Card> cards;
	int numPlayer;
	Deck deck;

	public void notifySolution(ArrayList<Card> cards, Deck deck, int numPlayer){
		this.cards = cards;
		this.numPlayer = numPlayer;
		this.deck = deck;
	    setChanged();
	    notifyObservers(this);
    }

	public ArrayList<Card> getCards(){
		return this.cards;
	}
	
	public int getNumPlayer(){
		return this.numPlayer;
	}
	
	public Deck getDeck(){
		return this.deck;
	}
}
