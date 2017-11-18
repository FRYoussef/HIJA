package test;

import model.representation.Card;
import model.representation.rank.CoupleCards;
import model.representation.rank.Range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class RankTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//A9s, A8s, A7o, 54o, 53o, 52s, ATs, TTs, KKo, AAs, QQo

        while (true){
            String lin;
            String cards [];
            ArrayList<CoupleCards> cp = new ArrayList<>(10);
            ArrayList<Range> ranges = new ArrayList<>(10);
            System.out.print("Enter couples of cards: ");
            lin = sc.nextLine();
            cards = lin.split(", ");

            //parsing
            for (String c: cards){
                boolean o = c.charAt(2) == 'o' ? true : false;
                CoupleCards cop = new CoupleCards(Card.charToValue(c.charAt(0)), Card.charToValue(c.charAt(1)), o);
                cp.add(cop);
            }
            Collections.sort(cp, Collections.reverseOrder());
            Range r = new Range(cp.get(0));
            for(int i = 1; i < cp.size(); i++){
                Range aux = Range.union(r, cp.get(i));
                if(aux != null){
                    r = aux;
                }
                else{
                    ranges.add(r);
                    r = new Range(cp.get(i));
                }
                aux = null;
            }
            if(r != null)
                ranges.add(r);
            System.out.print("Your rank is:  ");
            for(Range ran: ranges){
                System.out.print(ran + ", ");
            }
            System.out.println();
        }


    }

}
