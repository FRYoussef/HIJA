package model.processor.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


import model.processor.HandProcessor;
import model.representation.Card;
import model.representation.Player;
import model.representation.game.Deck;
import model.representation.game.HandScore;

public class HEWorker implements Runnable{
	
	
	protected Shared shared;
	protected HashMap<Integer, Player> players;
	protected HashSet<Card> boardCards;
	protected Deck deck;
	private long id;
	
	/**
     * Constructs a new worker.
     * @param deck : The deck of cards to draw from. Each worker must have their own unique deck (use Deck.clone()). 
     */
	public HEWorker(Shared shared, HashMap<Integer, Player> players, HashSet<Card> boardCards, Deck deck) {
		this.shared = shared;
		this.players = players;
		this.boardCards = boardCards;
		this.deck = (Deck) deck.clone();
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
		
		ArrayList<HandScore> results = new ArrayList<HandScore>();
		for (Integer i : players.keySet()) {
			Player p = players.get(i);
			HandScore bp = hp.getBestPlay(p.getCard(0), p.getCard(1));
			bp.setPlayer(p);
			results.add(bp);
		}
		
		HandScore best;
		Collections.sort(results);
		//it keeps track of tied players. 
		ArrayList<Integer> tiedPlayers = new ArrayList<>(); 
		do {
			best = results.remove(results.size()-1);
			tiedPlayers.add(best.getPlayer());
			//No hace falta 
			if (best == null)
				break;
			//shared.increasePlayer(best.getPlayer());
		}while(results.size() > 0 && best.compareTo(results.get(results.size()-1)) == 0);
		
		for(Integer i : tiedPlayers)
			shared.increasePlayer(i, 1.d / (double) tiedPlayers.size());
		
		shared.increaseSim();
		deck.insertCards(drawnCards);
		
	}
	
	

}
