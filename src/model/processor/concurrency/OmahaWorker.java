package model.processor.concurrency;

import java.util.HashSet;

import model.representation.Card;
import model.representation.Player;
import model.representation.game.Deck;

public class OmahaWorker extends HEWorker {

	public OmahaWorker(Shared shared, HashSet<Player> players, HashSet<Card> boardCards, Deck deck) {
		super(shared, players, boardCards, deck);
	}
	
	
	@Override
	protected void simulate() {
		
	}

}
