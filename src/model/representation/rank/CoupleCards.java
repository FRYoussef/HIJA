package model.representation.rank;

import model.representation.Card;

public class CoupleCards implements Comparable{

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
        return offSuited ? hl + "o" : hl + "s";
    }
}
