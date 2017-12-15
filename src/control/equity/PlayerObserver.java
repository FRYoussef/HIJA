package control.equity;

import java.util.Observer;

import model.game.OSolution;

public class PlayerObserver {

    private static CSolution cSolution;

    public static void init(){
        if(cSolution == null)
            cSolution = new CSolution();
    }

    public static void removeObservers() {
        if (cSolution != null){
            cSolution.deleteObservers();
            cSolution = null;
        }
    }

    public static void addObserver(Observer o){
        cSolution.addObserver(o);
    }

    public static void removeObserver(Observer o){
        cSolution.deleteObserver(o);
    }

    public static CSolution getoSolution() {
        return cSolution;
    }
}
