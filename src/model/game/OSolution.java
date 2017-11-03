package model.game;


import java.util.Observable;

public class OSolution extends Observable{

	public void notifySolution(String solution){
	    setChanged();
	    notifyObservers(solution);
    }
}
