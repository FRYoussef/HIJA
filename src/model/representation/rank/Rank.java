package model.representation.rank;

import model.representation.Card;

public class Rank {

    private CoupleCards coupleCards1 = null;
    private CoupleCards coupleCards2 = null;

    private boolean rank = false; /** like A9s-A7s */
    private boolean highRank = false; /** like JJ+ */

    public Rank() { }

    public Rank(CoupleCards coupleCards1) {
        this.coupleCards1 = coupleCards1;
    }

    public Rank(CoupleCards coupleCards1, CoupleCards coupleCards2, boolean rank, boolean highRank) {
        this.coupleCards1 = coupleCards1;
        this.coupleCards2 = coupleCards2;
        this.rank = rank;
        this.highRank = highRank;
    }

    public CoupleCards getCoupleCards1() {
        return coupleCards1;
    }

    public CoupleCards getCoupleCards2() {
        return coupleCards2;
    }

    public boolean isRank() {
        return rank;
    }

    public boolean isHighRank() {
        return highRank;
    }

    public void setCoupleCards1(CoupleCards coupleCards1) {
        this.coupleCards1 = coupleCards1;
    }

    public void setCoupleCards2(CoupleCards coupleCards2) {
        this.coupleCards2 = coupleCards2;
    }

    public void setRank(boolean rank) {
        this.rank = rank;
    }

    public void setHighRank(boolean highRank) {
        this.highRank = highRank;
    }

    /**
     * It creates a rank from two couples of cards.
     * @param c1 first couple of cards
     * @param c2 second couple of cards
     * @return null if there is no union, the rank otherwise
     */
    public static Rank union(CoupleCards c1, CoupleCards c2){
        CoupleCards cp1 = c1.compareTo(c2) >= 0 ? c1 : c2;
        CoupleCards cp2 = c1.compareTo(c2) < 0 ? c1 : c2;

        if(c1.isPair() || c2.isPair()){
            if(cp1.isPair() && cp2.isPair()){
                if(cp1.getHiggerValue() == Card.NUM_CARDS-1 && cp2.getHiggerValue() == Card.NUM_CARDS-2)
                    return new Rank(cp1, null, false, true);

                return null;
            }
            return null;
        }
        /** 97o, 86s */
        if(c1.getHiggerValue() != c2.getHiggerValue() || c1.isOffSuited() != c2.isOffSuited())
            return null;

        if(Math.abs(c1.getLowerValue() - c2.getLowerValue()) == 1){
            /** 54o-53o => 53o+  */
            if(Math.abs(cp1.getHiggerValue() - cp1.getLowerValue()) == 1)
                return new Rank(cp2, null, false, true);

            return new Rank(c1, c2, true, false);
        }

        return null;
    }

    /**
     * It creates a rank from a couples of cards and from a rank.
     * @param r the rank
     * @param c first couple of cards
     * @return null if there is no union, the new rank otherwise
     */
    public static Rank union(Rank r, CoupleCards c){
        CoupleCards cp1 = r.getCoupleCards1().compareTo(c) >= 0 ? r.getCoupleCards1() : c;
        CoupleCards cp2 = r.getCoupleCards1().compareTo(c) < 0 ? r.getCoupleCards1() : c;

        if(r.getCoupleCards1().isPair() || c.isPair()){
            if(r.getCoupleCards1().isPair() && c.isPair()){
                if(r.isHighRank() && Math.abs(r.getCoupleCards1().getHiggerValue() - c.getHiggerValue()) == 1){
                    r.setCoupleCards1(c);
                    return r;
                }
                else if(cp1.getHiggerValue() == Card.NUM_CARDS-1
                        && Math.abs(cp1.getHiggerValue() - cp2.getHiggerValue()) == 1)
                {
                    r.setHighRank(true);
                    r.setCoupleCards1(cp2);
                    return r;
                }
                else
                    return null;
            }
            return null;
        }
        /** 97o, 86s */
        if(r.getCoupleCards1().getHiggerValue() != c.getHiggerValue()
                || r.getCoupleCards1().isOffSuited() != c.isOffSuited())
            return null;

        if(!r.isHighRank() && !r.isRank() && Math.abs(r.getCoupleCards1().getLowerValue() - c.getLowerValue()) == 1){
            if(Math.abs(cp1.getHiggerValue() - cp1.getLowerValue()) == 1){
                r.setCoupleCards1(cp2);
                r.setCoupleCards2(null);
                r.setRank(false);
                r.setHighRank(true);
                return r;
            }

            r.setCoupleCards2(cp2);
            r.setRank(true);
            return r;
        }

        if(r.isRank()){
            /** the couple of cards are above the rank */
            if(Math.abs(r.getCoupleCards1().getLowerValue() - c.getLowerValue()) == 1){
                if(Math.abs(c.getHiggerValue()-c.getLowerValue()) == 1){
                    r.setCoupleCards1(r.getCoupleCards2());
                    r.setCoupleCards2(null);
                    r.setRank(false);
                    r.setHighRank(true);
                    return r;
                }
                r.setCoupleCards1(c);
                return r;
            }
            /** the couple of cards are below the rank */
            else if(Math.abs(r.getCoupleCards2().getLowerValue() - c.getLowerValue()) == 1){
                r.setCoupleCards2(c);
                return r;
            }
            return null;
        }

        if(r.isHighRank()){
            if(Math.abs(r.getCoupleCards1().getLowerValue() - c.getLowerValue()) == 1){
                r.setCoupleCards1(c);
                return r;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(coupleCards1.toString());
        if(rank && coupleCards2 != null)
            sb.append("-" + coupleCards2.toString());
        else if(highRank)
            sb.append("+");
        return sb.toString();
    }
}
