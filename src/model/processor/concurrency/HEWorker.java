package model.processor.concurrency;

import java.util.HashSet;

import model.representation.Card;
import model.representation.Player;

public class HEWorker implements Runnable{
	
	
	protected Shared shared;
	protected HashSet<Player> players;
	protected HashSet<Card> boardCards;
	protected HashSet<Card> deck;
	private long id;
	
	
	/**
     * Constructs a new worker.
     * @param deck : The deck of cards to draw from. Each worker must have their own unique deck. 
     */
	public HEWorker(Shared shared, HashSet<Player> players, HashSet<Card> boardCards, HashSet<Card> deck) {
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
	
	private void execute() {		
		while (shared.run()) {
			simulate();
		}		
	}

	/**
     * Runs one simulation.
     * Draws random cards from the deck to complete the board and calculate winners.
     * Acceses **shared** to increase the stats of the player(s) who won.
     * After the simulation is ended, drawn cards must be returned to the deck.
     */
	protected void simulate() {
		
	}
	
	

}
