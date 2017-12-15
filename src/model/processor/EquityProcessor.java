package model.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Timer;

import model.processor.concurrency.HEWorker;
import model.processor.concurrency.Shared;
import model.representation.Card;
import model.representation.Player;
import model.representation.game.Deck;

public class EquityProcessor{
	
	private ArrayList<Thread> threads; 
	private Shared sharedData; 
	private static final int nThreads = 2; 
	private Timer timer; 
	
	public EquityProcessor(){
		this.threads = new ArrayList<>();
	}
	
	public void calculateEquity(HashSet<Player> players, HashSet<Card> boardCards, Deck deck){
		this.sharedData = new Shared(players.size());
		this.timer = new Timer();
		for(int i = 0; i < nThreads; i++){
			Thread t = new Thread(new HEWorker(this.sharedData, players, boardCards, deck));
			this.threads.add(t);
			t.start();
		}
		this.timer.scheduleAtFixedRate(new UpdateEquities(this.sharedData), 0, 300);
		this.timer.scheduleAtFixedRate(new UpdateSims(this.sharedData), 0, 300);
	}
	
	public void stopThreads(){
		if(this.sharedData != null)
			this.sharedData.stop(); 
		for(Thread t : this.threads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//cancel and removes tasks.
		if(this.timer != null){
			this.timer.cancel();
			this.timer.purge();
		}
	}
	
}
