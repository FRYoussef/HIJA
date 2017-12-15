package model.game;


import java.util.Observable;

public class OSolution extends Observable{

	public void notifySolution(String solution){
	    setChanged();
	    notifyObservers(solution);
    }
	
	//--------------PR3
	
	public void notifyEquity(double[] is){
		this.setChanged();
		this.notifyObservers(is);
	}
	
	public void notifySimulations(int sims){
		this.setChanged();
		this.notifyObservers(sims);
	}
}
