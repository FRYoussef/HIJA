package model.processor;

import model.representation.Card;
import model.representation.Suit;
import model.representation.game.HandScore;
import model.representation.game.Play;
import model.representation.rank.CoupleCards;
import java.util.HashSet;

public class RangeProcessor {
    private HashSet<Card> hsBoard = null;
    private HashSet<CoupleCards> hsRange = null;
    private HandScore[] plays = null;
    private int[] playsCounter = null;
    private HandProcessor handProcessor = null;
    private int combos = 0;

    public RangeProcessor(HashSet<Card> hsBoard, HashSet<CoupleCards> hsRange) throws Exception {
        this.hsBoard = hsBoard;
        this.hsRange = hsRange;
        handProcessor = new HandProcessor();
        for(Card c : hsBoard)
            handProcessor.addCard(c);

        plays = new HandScore [Play.NUM_PLAYS];
        playsCounter = new int [Play.NUM_PLAYS];
    }

    public void run() throws Exception {
        for(CoupleCards cp : hsRange){
            if(!cp.isOffSuited())
                suitedConbination(cp);
            else
                offSuitedConbination(cp);

        }
        handProcessor.resetHandProcessor();
    }

    private void offSuitedConbination(CoupleCards cp) throws Exception {
        Card c1 = null;
        Card c2 = null;
        for (int i = 0; i < Suit.NUM_SUIT; i++) {
            for (int j = i+1; j < Suit.NUM_SUIT; j++) {
                c1 = new Card(cp.getHigherValue(), Suit.getFromInt(i));
                c2 = new Card(cp.getLowerValue(), Suit.getFromInt(j));
                handScoreProcess(c1, c2);
            }
        }
    }

    private void suitedConbination(CoupleCards cp) throws Exception {
        Card c1 = null;
        Card c2 = null;
        for (int i = 0; i < Suit.NUM_SUIT; i++) {
            c1 = new Card(cp.getHigherValue(), Suit.getFromInt(i));
            c2 = new Card(cp.getLowerValue(), Suit.getFromInt(i));
            handScoreProcess(c1, c2);
        }
    }

    private void handScoreProcess(Card c1, Card c2) throws Exception {

        if(hsBoard.contains(c1) || hsBoard.contains(c2))
            return;

        HandScore handScore = handProcessor.getBestPlay(c1, c2);
        combos++;
        if(plays[handScore.getHandValue().ordinal()] == null)
            plays[handScore.getHandValue().ordinal()] = handScore;

        else if(plays[handScore.getHandValue().ordinal()].compareTo(handScore) < 0)
            plays[handScore.getHandValue().ordinal()] = handScore;

        playsCounter[handScore.getHandValue().ordinal()] =
                playsCounter[handScore.getHandValue().ordinal()] + 1;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = plays.length-1; i >= 0; i--) {
            if(plays[i] != null)
                sb.append(plays[i] + " -> " + ((int)Math.floor((playsCounter[i]*100)/combos)) + "%");
        }

        return sb.toString();
    }

}
