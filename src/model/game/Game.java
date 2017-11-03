package model.game;

import java.io.IOException;
import java.util.ArrayList;

import model.ioFiles.FileInputReader;
import model.ioFiles.FileOutputWriter;
import model.processor.HandProcessor;
import model.representation.Card;
import model.representation.HandScore;

public abstract class Game{
	public static final String SEPARATOR = "\n----------------------------------------------------\n";
	protected FileInputReader reader = null;
	protected HandProcessor handProcessor = null;
	protected FileOutputWriter writer = null;
	protected StringBuilder output = null;
	protected ArrayList<HandScore> bestHand = null;
	protected ArrayList<Card> boardCards = null ;

	public Game(String dPath) throws IOException{
		this.handProcessor = new HandProcessor();
		output = new StringBuilder();
		this.writer = new FileOutputWriter(dPath);
		this.boardCards = new ArrayList<Card>();
	}
    public Game(){
        this.handProcessor = new HandProcessor();
        output = new StringBuilder();
		this.boardCards = new ArrayList<Card>();
    }

    protected void writeOutputFile() throws IOException {
        if(writer != null) {
            writer.writeLine(output.toString());
        }
    }
    public Card[] getBoardCards(){
    	return this.boardCards.toArray(new Card[boardCards.size()]);
    }

    protected void saveHand (HandScore bestHand) {
    	if (this.bestHand.size() == 0) {
    		this.bestHand.add(bestHand);
    	}else {
    		this.bestHand.set(0, bestHand);
    	}
    }

	public abstract Card[] getPlayerCards(int i);
    public abstract int getNumberPlayers();
	public abstract void startGame() throws Exception;
	protected abstract void writeGame(HandScore...bestHand) throws IOException;
}
