package model.representation.rank;

import javafx.util.Pair;
import model.representation.Card;

import java.util.ArrayList;
import java.util.HashSet;

public class CoupleCards implements Comparable{

    public static final int NUM_COUPLE_CARDS = 169;

    /** the value of the cards */
    private int higgerValue;
    private int lowerValue;
    /** it represents if the cards suit are the same */
    private boolean offSuited = true;
    private boolean pair = false;

    public CoupleCards(int higgerValue, int lowerValue) {
        this.higgerValue = higgerValue;
        this.lowerValue = lowerValue;
        if(lowerValue == higgerValue)
            pair = true;
    }

    public CoupleCards(int higgerValue, int lowerValue, boolean offSuited) {
        this.higgerValue = higgerValue;
        this.lowerValue = lowerValue;
        this.offSuited = offSuited;
        if(lowerValue == higgerValue)
            pair = true;
    }

    public int getHiggerValue() {
        return higgerValue;
    }

    public int getLowerValue() {
        return lowerValue;
    }

    public boolean isOffSuited() {
        return offSuited;
    }

    public boolean isPair() {
        return pair;
    }


    /**
     * It creates an array of a numbers from to couple cards
     * @param cps
     * @return array of pair int
     */
    public static ArrayList<Pair<Integer, Integer>> coupleCardsToMatrix(ArrayList<CoupleCards> cps){
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>(cps.size());

        for (CoupleCards cp: cps){
            if(cp.isPair())
                pairs.add(new Pair<>(Math.abs(cp.getHiggerValue()-(Card.NUM_CARDS-1)), Math.abs(cp.getLowerValue()-(Card.NUM_CARDS-1))));
            else if(!cp.isOffSuited())
                pairs.add(new Pair<>(Math.abs(cp.getHiggerValue()-(Card.NUM_CARDS-1)), Math.abs(cp.getLowerValue()-(Card.NUM_CARDS-1))));
            else
                pairs.add(new Pair<>(Math.abs(cp.getLowerValue()-(Card.NUM_CARDS-1)), Math.abs(cp.getHiggerValue()-(Card.NUM_CARDS-1))));
        }
        return pairs;
    }

    public static HashSet<CoupleCards> toCoupleCards(HashSet<String> hs){
        HashSet<CoupleCards> cps = new HashSet<>(hs.size());
        for(String s: hs){
            if(s.length() == 2)
                cps.add(new CoupleCards(Card.charToValue(s.charAt(0)), Card.charToValue(s.charAt(1))));
            else if(s.charAt(2) == 's')
                cps.add(new CoupleCards(Card.charToValue(s.charAt(0)), Card.charToValue(s.charAt(1)), false));
            else
                cps.add(new CoupleCards(Card.charToValue(s.charAt(0)), Card.charToValue(s.charAt(1))));
        }
        return cps;
    }

    @Override
    public int compareTo(Object obj) {
        CoupleCards r = (CoupleCards) obj;
        if(offSuited != r.isOffSuited())
            return offSuited ? -1 : 1;
        int high = higgerValue - r.getHiggerValue();
        int low = lowerValue - r.getLowerValue();
        if(isPair() || r.isPair()){
            if(isPair() && r.isPair())
                return high;
            else if(r.isPair())
                return -1;
            else if(isPair())
                return 1;
        }
        else
            return high == 0 ? low : high;
        return 0;
    }

    @Override
    public String toString() {
        char h = Card.lut[higgerValue];
        char l = Card.lut[lowerValue];
        String hl = h + "" + l;
        if(pair)
            return hl;
        return offSuited ? hl + "o" : hl + "s";
    }
}
