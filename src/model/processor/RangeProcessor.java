package model.processor;

import model.representation.Card;
import model.representation.Suit;
import model.representation.game.HandScore;
import model.representation.game.Play;
import model.representation.rank.CoupleCards;
import model.representation.rank.PairType;
import model.representation.rank.Stats;

import java.util.ArrayList;
import java.util.HashSet;

public class RangeProcessor {
    private HashSet<Card> hsBoard = null;
    private Card highBoardCard = null;
    private Card sndHighBoardCard = null;
    private HashSet<CoupleCards> hsRange = null;
    private HandScore[] plays = null;
    private HandScore[] pairPlays = null;
    private int[] playsCounter = null;
    private int[] pairPlaysCounter = null;
    private HandProcessor handProcessor = null;    
    private int combos = 0;
    private HandScore boardScore = null;

    private ArrayList<Stats> stats; 
    
    
    public RangeProcessor(HashSet<Card> hsBoard, HashSet<CoupleCards> hsRange) throws Exception {
        this.hsBoard = hsBoard;
        this.hsRange = hsRange;
        handProcessor = new HandProcessor();
        for(Card c : hsBoard) {
            handProcessor.addCard(c);
            if (highBoardCard == null || highBoardCard.compareTo(c) < 0) {
            	sndHighBoardCard = highBoardCard;
            	highBoardCard = c;
            }
            else if (sndHighBoardCard == null || sndHighBoardCard.compareTo(c) < 0)
            	sndHighBoardCard = c;
        }
        boardScore = handProcessor.getBestPlay();

        
        plays = new HandScore [Play.NUM_PLAYS];
        pairPlays = new HandScore [PairType.NUM_PAIRS];
        playsCounter = new int [Play.NUM_PLAYS];
        pairPlaysCounter = new int [PairType.NUM_PAIRS];
        
        stats = new ArrayList<Stats>(); 
    }

    public void run() throws Exception {
        for(CoupleCards cp : hsRange){
            if(cp.isOffSuited())
            	offSuitedCombination(cp);
            else
            	suitedCombination(cp);   
        }
        for (int i = plays.length-1; i >= 0; i--) {
            if(plays[i] != null)
            	stats.add(new Stats(plays[i].toString(), (int)Math.floor((playsCounter[i]*100)/combos)));
            else if (i == Play.Pair.ordinal()) {
        		for (int j = pairPlays.length -1; j >= 0; j--) {
        			if (pairPlays[j] != null) 
        				stats.add(new Stats(pairPlays[j] + " (" + PairType.getFromInt(j).toString() + ") ", (int)Math.floor((pairPlaysCounter[j]*100)/combos)));        			
        		}
            }
        }
        
    }

    private void offSuitedCombination(CoupleCards cp) throws Exception {
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

    private void suitedCombination(CoupleCards cp) throws Exception {
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
        //If the play doesn't include hand cards
        if (allCardsFromBoard(handScore, c1, c2))	
        	return;
        
        combos++;
        if(plays[handScore.getHandValue().ordinal()] == null) {
            plays[handScore.getHandValue().ordinal()] = handScore;
            checkPairType(handScore, c1, c2);
        }
        

        else if(plays[handScore.getHandValue().ordinal()].compareTo(handScore) < 0){
            plays[handScore.getHandValue().ordinal()] = handScore;
            checkPairType(handScore, c1, c2);
        }

        playsCounter[handScore.getHandValue().ordinal()]++;
    }

    private boolean allCardsFromBoard(HandScore handScore, Card c1, Card c2) {
    	boolean res = false;
		if (handScore.getHandValue() == boardScore.getHandValue()){
			res = !(handScore.contains(c1) || handScore.contains(c2));
		}
		return res;
	}
    
    private void checkPairType (HandScore hand, Card c1, Card c2) {
    	if (hand.getHandValue() == Play.Pair) {
    		PairType p = null;
    		if (c1.getValue() == c2.getValue() && c1.getValue() > highBoardCard.getValue()) {
    			 p = PairType.OverPair;
    		}
    		else if (c1.getValue() == c2.getValue() && c1.getValue() < highBoardCard.getValue()) {
    			if (c1.getValue() > 8)	//TODO JJ+ are strong pairs? 
    				p = PairType.BelowPair;
    			else
    				p = PairType.WeakPair;
    		}
    		else if (c1.getValue() == highBoardCard.getValue() || c2.getValue() == highBoardCard.getValue()) {
    			p = PairType.TopPair;
    		}
    		else if (c1.getValue() == sndHighBoardCard.getValue() || c2.getValue() == sndHighBoardCard.getValue()) {
    			p = PairType.MiddlePair;
    		}
    		else {
    			p = PairType.WeakPair;
    		}
    		if (pairPlays [p.ordinal()] == null || pairPlays [p.ordinal()].compareTo(hand) < 0)
    			pairPlays [p.ordinal()] = hand;
    		pairPlaysCounter[p.ordinal()] ++;
            plays[Play.Pair.ordinal()] = null;
    	}
    }

    public ArrayList<Stats> getStats(){
    	return stats; 
    }
    
	public String toString(){
        StringBuilder sb = new StringBuilder();
       /* for (int i = plays.length-1; i >= 0; i--) {
            if(plays[i] != null) {
            	
            		sb.append(plays[i] + " -> " + ((int)Math.floor((playsCounter[i]*100)/combos)) + "%");
            }
            else if (i == Play.Pair.ordinal()) {
        		for (int j = pairPlays.length -1; j >= 0; j--) {
        			if (pairPlays[j] != null) 
        				sb.append(pairPlays[j] + " (" + PairType.getFromInt(j).toString() + ") -> " + ((int)Math.floor((pairPlaysCounter[j]*100)/combos)) + "%");
        			
        		}
            }
        }*/
        return sb.toString();
    }

}
