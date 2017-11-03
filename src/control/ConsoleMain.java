package control;

import model.game.*;

import java.io.IOException;

public class ConsoleMain {

    public static void main(String[] args) {
        Game game = null;
        try{
            switch(Integer.parseInt(args[0])){
                case 1:
                    game = new Cards5(args[1], args[2]);
                    break;
                case 2:
                    game = new Player1HE(args[1], args[2]);
                    break;
                case 3:
                    game = new NPlayerHE(args[1], args[2]);
                    break;
                case 4:
                    game = new Omaha(args[1], args[2]);
                    break;
                default:
                    throw new Exception("Wrong exercise."
                            + " " + System.getProperty("line.separator"));
            }
            long startTime = System.currentTimeMillis();
            game.startGame();
            long endTime = System.currentTimeMillis();
            long deltaTime = endTime-startTime;
            System.out.println("Time: " + deltaTime + "ms");
        }
        catch (Exception e){
            System.out.print("Error in the number of args\n");
            e.printStackTrace();
        }
    }

}
