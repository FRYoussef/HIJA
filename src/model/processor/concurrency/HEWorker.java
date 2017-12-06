package model.processor.concurrency;

import java.util.HashSet;
import java.util.TreeSet;

import model.processor.HandProcessor;
import model.representation.Card;
import model.representation.Player;
import model.representation.game.Deck;
import model.representation.game.HandScore;

public class HEWorker implements Runnable{
	
	
	protected Shared shared;
	protected HashSet<Player> players;
	protected HashSet<Card> boardCards;
	protected Deck deck;
	private long id;
	
	
	/**
     * Constructs a new worker.
     * @param deck : The deck of cards to draw from. Each worker must have their own unique deck (use Deck.clone()). 
     */
	public HEWorker(Shared shared, HashSet<Player> players, HashSet<Card> boardCards, Deck deck) {
		this.shared = shared;
		this.players = players;
		this.boardCards = boardCards;
		this.deck = deck;
	}

	@Override
	public void run() {
		this.id = Thread.currentThread().getId();
		try {
			execute();
		}
		catch (Exception e) {
			System.err.println("Thread " + id + " has encountered an unexpected error.");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}		
	}
	
	private void execute() throws Exception {		
		while (shared.run()) {
			simulate();
		}		
	}

	/**
     * Runs one simulation.
     * Draws random cards from the deck to complete the board and calculate winners.
     * Acceses **shared** to increase the stats of the player(s) who won.
     * After the simulation is ended, drawn cards must be returned to the deck.
	 * @throws Exception 
     */
	protected void simulate() throws Exception {
		int nBoard = 5 - boardCards.size();
		HandProcessor hp = new HandProcessor();
		for (Card card : boardCards) {
			hp.addCard(card);
		}
		
		HashSet<Card> drawnCards = new HashSet<Card>();
		for (int i = 0; i < nBoard; i++) {
			Card c = deck.drawCard();
			drawnCards.add(c);
			hp.addCard(c);
		}
		
		TreeSet<HandScore> results = new TreeSet<HandScore>();
		for (Player p : players) {
			results.add(hp.getBestPlay(p.getCard(0), p.getCard(1)));
		}
		
		HandScore best = null;
		do {
			best = results.pollLast();
			if (best == null)
				break;
			shared.increase(best.getPlayer());
		}while(best.compareTo(results.last()) == 0);
		
		deck.insertCards(drawnCards);
	}
	
	

}
