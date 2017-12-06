package model.processor.concurrency;

import java.util.HashSet;

import model.representation.Card;
import model.representation.Player;

public class OmahaWorker extends HEWorker {

	public OmahaWorker(Shared shared, HashSet<Player> players, HashSet<Card> boardCards, HashSet<Card> deck) {
		super(shared, players, boardCards, deck);
	}
	
	
	@Override
	protected void simulate() {
		
	}

}
