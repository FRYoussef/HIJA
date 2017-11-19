package model.representation.game;

import model.representation.Card;

import java.util.ArrayList;
import java.util.List;

public class HandScore implements Comparable<HandScore> {

    public static final int NUM_HAND_PLAY = 5;
    private String playerId;
	private Play handValue = Play.HighCard;
	private List<Card> handPlay = null;
	private Card[] playerCards = null;

	public HandScore(String playerId,Play handValue, List<Card> handPlay,
			Card[] playerCards){
		this.playerId = playerId;
        this.handValue = handValue;
        this.handPlay = handPlay;
        this.playerCards = playerCards;
	}
    public HandScore(Play handValue, List<Card> handPlay, Card[] playerCards) {
    	this.playerId = "";
        this.handValue = handValue;
        this.handPlay = handPlay;
        this.playerCards = playerCards;
    }

    public HandScore(Card ... playerCards) {
        handPlay = new ArrayList<Card>(NUM_HAND_PLAY);
        this.playerCards = playerCards;
    }

    public HandScore() {
        handPlay = new ArrayList<Card>(NUM_HAND_PLAY);
    }

    public void setHandValue(Play handValue) {
        this.handValue = handValue;
    }
    
    public boolean contains (Card c) {
    	return handPlay.contains(c);
    }

    //---
    public void setPlayerId(String id){
    	this.playerId = id;
    }

    public String getPlayerId(){
    	return this.playerId;
    }
    public Play getHandValue() {
        return handValue;
    }

    public Card[] getPlayerCards() {
        return playerCards;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("\n- " + this.handValue.toString() + "( ");
        for (Card card : handPlay)
            stringBuilder.append(card.toString());
        stringBuilder.append(" )");
        return stringBuilder.toString();
    }

    /**
     * Compare HandScore
     * @param other
     * @return >0 if the left HandScore is higher, <0 if the rigth HandScore is higher
     *          , and 0 if it is the same HandScore
     */
    @Override
	public int compareTo(HandScore other) {
        //compare the rank of the play
        if(handValue.ordinal() == other.getHandValue().ordinal()){
            //compare the play
            int same = 0;

            for(int i = 0; i < Play.CARDS_PER_PLAY; i++){
                same = handPlay.get(i).compareTo(other.handPlay.get(i));
                if(same != 0)
                    return same;
            }
        }

        return handValue.ordinal() - other.getHandValue().ordinal();
	}
}
