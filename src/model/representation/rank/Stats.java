package model.representation.rank;

public class Stats {
	private String playAndCards; 
	private int percentage;
	
	public Stats(String playAndCards, int percentage){
		this.playAndCards = playAndCards;
		this.percentage = percentage; 
	}
	public String getPlayAndCards() {
		return playAndCards;
	}
	public void setPlay(String playAndCards) {
		this.playAndCards = playAndCards;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	} 	
	
}
