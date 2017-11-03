package model.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.ioFiles.InputNPlayerHE;
import model.representation.Card;
import model.representation.HandScore;

public class NPlayerHE extends Game {

	private int numPlayers = 0;
	
	public NPlayerHE(String sPath, String dPath) throws IOException{
		super(dPath);
		this.reader = new InputNPlayerHE(sPath); 
		this.bestHand = new ArrayList<HandScore>();
		
	}

    public NPlayerHE(String sPath) throws IOException{
        super();
        this.reader = new InputNPlayerHE(sPath);
        this.bestHand = new ArrayList<HandScore>();
    }

    @Override
    public int getNumberPlayers() {
        return this.numPlayers;
    }

	@Override
	public void startGame() throws Exception {
		while(this.reader.readNext()){
			this.boardCards.clear();
            this.bestHand.clear();
			this.reader.setN();
			this.numPlayers = this.reader.getNPlayers();

			Card c = reader.getBoardCard();
			while(c != null){
				this.handProcessor.addCard(c);
				this.boardCards.add(c);
				c = reader.getBoardCard();
			}

			HandScore bestHand;
			for(int i = 0; i < this.numPlayers; i++){
				bestHand = this.handProcessor.getBestPlay(this.reader.getPlayerCard(i),
						this.reader.getPlayerCard(i));
				bestHand.setPlayerId("J" + (i + 1));

				this.bestHand.add(bestHand);
			}
            ArrayList<HandScore> aux = (ArrayList<HandScore>) this.bestHand.clone();

			Collections.sort(aux);

			this.writeGame(aux.toArray(new HandScore[aux.size()]));
			this.writeOutputFile();
            if(HandlerObserver.getoSolution() != null)
			    HandlerObserver.getoSolution().notifySolution(output.toString());
			this.handProcessor.resetHandProcessor();
			this.output.setLength(0);
		}
        if(writer != null)
		    this.writer.closeWriter();
	}

	@Override
	protected void writeGame(HandScore ...bestHand) throws IOException {
		output.append(this.reader.getCurrWholeLine());
		for(int i = bestHand.length-1; i >= 0 ; i--){
			//write  player's id
			//write input data
            output.append("\n"+bestHand[i].getPlayerId() + ": " + bestHand[i].toString());
		}
        //empty line between inputs
        output.append(SEPARATOR);
	}

	@Override
	public Card[] getPlayerCards(int i) {
		return this.bestHand.get(i-1).getPlayerCards();
	}

}
