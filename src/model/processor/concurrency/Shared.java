package model.processor.concurrency;

public class Shared {
	private volatile int playerStats [];
	private volatile boolean run;
	private volatile int sims;
	
	
	public Shared(int nPlayers) {
		playerStats = new int [nPlayers];
		run = true;
		sims = 0;
	}
	
	public synchronized void  increase (int player) {
		playerStats[player]++;
		sims++;
	}
	
	public synchronized int getStat (int player) {
		return playerStats[player];
	}
	
	public synchronized boolean run() {
		return run;
	}
	
	public synchronized void stop() {
		run = false;
	}
	
	public synchronized int getSims () {
		return sims;
	}
}
