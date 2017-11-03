package model.game;

import java.io.IOException;
import java.util.ArrayList;


import model.ioFiles.InputOmaha;
import model.representation.Card;
import model.representation.game.HandScore;

public class Omaha extends Game {
	public static final int NUM_PLAYERS = 1;
	private static final int PLAYER_CARDS = 4;

	public Omaha(String sPath, String dPath) throws IOException {
		super(dPath);
		this.reader = new InputOmaha(sPath);
		this.bestHand = new ArrayList<HandScore>();
	}

    public Omaha(String sPath) throws IOException {
        super();
        this.reader = new InputOmaha(sPath);
        this.bestHand = new ArrayList<HandScore>();
    }

    @Override
    public int getNumberPlayers() {
        return NUM_PLAYERS;
    }

	@Override
	public void startGame() throws Exception {
		while(this.reader.readNext()){
			this.boardCards.clear();
			this.reader.setN();
			ArrayList<Card> playerCards = new ArrayList<Card>();
			//add every card on board 
			Card c = reader.getBoardCard();
			while(c != null){
				this.boardCards.add(c);
				c = reader.getBoardCard();
			}
			//add player's cards. 
			int k = 0; 
			//we add cards to array to do combinations. 
			while(k < Omaha.PLAYER_CARDS){
				playerCards.add(this.reader.getPlayerCard(0));
				k++;
			}
			//calculate best hand
			HandScore bestH = null;
			HandScore currentHand;
			int nBoardCards = this.reader.getNBoardCards();

			for (int board1 = 0; board1 < nBoardCards-2; board1++) {
				for (int board2 = board1+1; board2 < nBoardCards-1; board2++) {
					for (int board3 = board2+1; board3 < nBoardCards; board3++) {
						this.handProcessor.addCard(this.boardCards.get(board1));
						this.handProcessor.addCard(this.boardCards.get(board2));
						this.handProcessor.addCard(this.boardCards.get(board3));

						for (int player1 = 0; player1 < Omaha.PLAYER_CARDS-1; player1++) {
							for (int player2 = player1+1; player2 < Omaha.PLAYER_CARDS; player2++) {

								currentHand =  this.handProcessor.getBestPlay(playerCards.get(player1), playerCards.get(player2));
								if (bestH == null) {
									bestH = currentHand;
								}
								else if (currentHand.compareTo(bestH) > 0) {
									bestH = currentHand;
								}
							}
						}
						this.handProcessor.resetHandProcessor();
					}
				}
			}
			//BestHand contains the best omaha hand
			saveHand(bestH);
			this.writeGame(bestH);
			this.writeOutputFile();
            if(HandlerObserver.getoSolution() != null)
			    HandlerObserver.getoSolution().notifySolution(output.toString());
			this.output.setLength(0);

		}
        if(writer != null)
    		this.writer.closeWriter();
	}

	@Override
	protected void writeGame(HandScore... bestHand) throws IOException {
		output.append(this.reader.getCurrWholeLine());
        //write input data
        for(HandScore s : bestHand){
            output.append(s.toString());
            if(this.handProcessor.getgutShotStraight())
                output.append("\n- Draw: Straight Gutshot");
            if(this.handProcessor.getopenEndedFlush())
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
