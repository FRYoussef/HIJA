package model.game;

import java.io.IOException;
import java.util.ArrayList;

import control.ObserverPatron.HandlerObserver;
import model.ioFiles.Input5Cards;
import model.representation.Card;
import model.representation.game.HandScore;

public class Cards5 extends Game {

    public static final int NUM_PLAYERS = 0;
	
	public Cards5(String sPath, String dPath) throws IOException{
		super(dPath);
		this.reader = new Input5Cards(sPath); 
		this.bestHand = new ArrayList<HandScore>(1);
	}
    public Cards5(String sPath) throws IOException{
        super();
        this.reader = new Input5Cards(sPath);
        this.bestHand = new ArrayList<HandScore>(1);
    }

    @Override
    public int getNumberPlayers() {
        return NUM_PLAYERS;
    }

    @Override
	public void startGame() throws Exception {
		while(this.reader.readNext()){
            this.boardCards.clear();
			//add every card on board
			Card c = reader.getBoardCard();
			while(c != null){
				this.handProcessor.addCard(c);
				this.boardCards.add(c);
				c = reader.getBoardCard();
			}
			HandScore bestH = this.handProcessor.getBestPlay();	//Tal vez es null
			saveHand(bestH);
			writeGame(bestH);
			this.writeOutputFile();
			if(HandlerObserver.getoSolution() != null)
			    HandlerObserver.getoSolution().notifyPlayerCards(output.toString());

			//clear for the next set of cards.
			this.handProcessor.resetHandProcessor();
			this.output.setLength(0);
		}
		if(writer != null)
			this.writer.closeWriter();
	}

	@Override
	protected void writeGame(HandScore ...bestHand) throws IOException {
		output.append(this.reader.getCurrWholeLine());
		//write input data
		for(HandScore s : bestHand){
            output.append(s.toString());
			if(this.handProcessor.getgutShotStraight())
				output.append("\n- Draw: Straight Gutshot");
			if(this.handProcessor.getdrawFlush())
                output.append("\n- Draw: Flush");
			if(this.handProcessor.getopenEndedStraight())
                output.append("\n- Draw: Straight");
			//empty line between inputs 
            output.append(SEPARATOR);
		}
	}
	@Override
	public Card[] getPlayerCards(int i) {
		return this.bestHand.get(0).getPlayerCards();
	}
}
