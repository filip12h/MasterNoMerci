import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

abstract class Strategy{
    double whatPlace(int player, int[] points) {
        /*v pripade ze hrac skonci na tom istom mieste nahladiac na to ci kartu zoberie, bude ho zaujimat konecne skore
        * cim ma vacsi naskok pred inymi, tym viac od vysledku odpocitame, no ak bude hrac zaostavat tak k vysledku pridame*/
        double difference = points[player] - points[(player+1)%3] - points[(player+2)%3];
        if (points[player] > points[(player + 1) % 3] && points[player] > points[(player + 2) % 3]) return 1 - difference/100;
        if (points[player] < points[(player + 1) % 3] && points[player] < points[(player + 2) % 3]) return 3 - difference/100;
        if ((points[player] > points[(player + 1) % 3] && points[player] == points[(player + 2) % 3]) ||
                (points[player] == points[(player + 1) % 3] && points[player] > points[(player + 2) % 3])) return 1.5 - difference/100;
        if ((points[player] < points[(player + 1) % 3] && points[player] == points[(player + 2) % 3]) ||
                (points[player] == points[(player + 1) % 3] && points[player] < points[(player + 2) % 3])) return 2.5 - difference/100;
        return 2 - difference/100;
    }
    ArrayList<Integer> addCardToList(ArrayList<Integer> cardsList, int card) {
        Set<Integer> set = new HashSet<>(cardsList);
        set.add(card);
        return new ArrayList<>(set);
    }
    private int howManyPoints(ArrayList<Integer> cards, int chips) {
        int counter = 0;
        int previous = 0;
        Collections.sort(cards);
        for (Integer i : cards) {
            if (i != previous + 1) {
                counter += i;
            }
            previous = i;
        }
        return chips - counter;
    }
    int[] currentPoints(ArrayList<Integer> c0, ArrayList<Integer> c1, ArrayList<Integer> c2, int ch0, int ch1, int ch2) {
        int[] points = new int[3];
        points[0] = howManyPoints(c0, ch0);
        points[1] = howManyPoints(c1, ch1);
        points[2] = howManyPoints(c2, ch2);
        return points;
    }
    abstract int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips, int depth);
    abstract boolean decide(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips);
}
/*uplne prehladavanie. Pouzitelne max. 6-7 kol dopredu.
* */
class RoughStrategy extends Strategy {
    private ArrayList<Integer> cards;
    private int numberOfRounds;
    //konštruktorom si inicializujeme poradie kariet prichádzajúcich do hry a počet kôl
    RoughStrategy(ArrayList<Integer> cards, int numberOfRounds) {
        this.cards = cards;
        this.numberOfRounds = numberOfRounds;
    }

    public boolean decide(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1,
                            ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips){
        int[] pTookCard;
        int[] pNotTook;
        if (currentPlayer==0){
            pTookCard = whatMove(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)),
                    cards1, cards2, chips0 + chips, chips1, chips2, 0, 0);
            pNotTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                    chips0 - 1, chips1, chips2, chips + 1, 0);
            return whatPlace(0, pTookCard)<=whatPlace(0, pNotTook);
        } else if(currentPlayer==1){
            pTookCard = whatMove(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)),
                    cards2, chips0, chips1 + chips, chips2, 0, 0);
            pNotTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0,
                    chips1 - 1, chips2, chips + 1, 0);
            return whatPlace(1, pTookCard)<=whatPlace(0, pNotTook);
        } else {
            pTookCard = whatMove(currentPlayer, round + 1, cards0, cards1,
                    addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0, 0);
            pNotTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                    chips0, chips1, chips2 - 1, chips + 1, 0);
            return whatPlace(2, pTookCard)<=whatPlace(0, pNotTook);
        }
    }
    //prechádza hráčove karty od najmenšej karty po najväčšiu - ak nie je prezeraná karta o 1 väčšia ako previous, zväčší counter o i, prezeranú kartu si následne uložíme do previous
    //vrátime hodnotu chips - counter
    public int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1,
                          ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips, int depth) {
        switch (currentPlayer) {
            case 0: {
                if (round == numberOfRounds) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)),
                        cards1, cards2, chips0 + chips, chips1, chips2, 0, 0);
                if (chips >= cards.get(round) || chips0 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                        chips0 - 1, chips1, chips2, chips + 1, 0);
                if ((whatPlace(0, tookCard) < whatPlace(0, notTook))||(whatPlace(0, tookCard)==1))
                    return tookCard;
                else return notTook;
            }
            case 1: {
                if (round == numberOfRounds) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)),
                        cards2, chips0, chips1 + chips, chips2, 0, 0);
                if (chips >= cards.get(round) || chips1 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0,
                        chips1 - 1, chips2, chips + 1, 0);
                if (whatPlace(1, tookCard) < whatPlace(1, notTook)||(whatPlace(1, tookCard)==1))
                    return tookCard;
                else return notTook;
            }
            case 2: {
                if (round == numberOfRounds) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, cards0, cards1,
                        addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0, 0);
                if (chips >= cards.get(round) || chips2 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                        chips0, chips1, chips2 - 1, chips + 1, 0);
                if (whatPlace(2, tookCard) < whatPlace(2, notTook)||(whatPlace(2, tookCard)==1))
                    return tookCard;
                else return notTook;
            }
        }
        //tuto sa nikdy nedostaneme, ale syntax nepustí
        return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
    }
}

/*absolutne najdebilnejsie riesenie - kartu zoberieme s 50% sancou nehladiac na jej hodnotu.
* */
class NoStrategy extends Strategy {
    NoStrategy(){}
    @Override
    int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips, int depth) {
        return new int[3];
    }
    @Override
    boolean decide(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips) {
        return Math.random()>Math.random();
    }
}

/*zakladna myslienka kde sa hrac bude rozhodovat ci zobrat kartu iba na zaklade toho aka bude bodova situacia o X tahov.
* t.j.je to ako uplne prehladavanie, ale iba do urcitej hlbky
* */
class BasicStrategy extends Strategy{
    private ArrayList<Integer> cards;
    private int numberOfRounds;
    private int maxdepth;
    BasicStrategy(ArrayList<Integer> cards, int numberOfRounds, int maxdepth) {
        this.cards = cards;
        this.numberOfRounds = numberOfRounds;
        this.maxdepth = maxdepth;
    }

    public boolean decide(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1,
                          ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips){
        int[] pTookCard;
        int[] pNotTook;
        if (currentPlayer==0){
            pTookCard = whatMove(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)),
                    cards1, cards2, chips0 + chips, chips1, chips2, 0, 0);
            pNotTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                    chips0 - 1, chips1, chips2, chips + 1, 0);
            return whatPlace(0, pTookCard)<=whatPlace(0, pNotTook);
        } else if(currentPlayer==1){
            pTookCard = whatMove(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)),
                    cards2, chips0, chips1 + chips, chips2, 0, 0);
            pNotTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0,
                    chips1 - 1, chips2, chips + 1, 0);
            return whatPlace(1, pTookCard)<=whatPlace(1, pNotTook);
        } else {
            pTookCard = whatMove(currentPlayer, round + 1, cards0, cards1,
                    addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0, 0);
            pNotTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                    chips0, chips1, chips2 - 1, chips + 1, 0);
            return whatPlace(2, pTookCard)<=whatPlace(2, pNotTook);
        }
    }
    //prechádza hráčove karty od najmenšej karty po najväčšiu - ak nie je prezeraná karta o 1 väčšia ako previous, zväčší counter o i, prezeranú kartu si následne uložíme do previous
    //vrátime hodnotu chips - counter
    public int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1,
                          ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips, int depth) {
        switch (currentPlayer) {
            case 0: {
                if ((depth == maxdepth)||(round == numberOfRounds)) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)),
                        cards1, cards2, chips0 + chips, chips1, chips2, 0, depth+1);
                if (chips >= cards.get(round) || chips0 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                        chips0 - 1, chips1, chips2, chips + 1, depth+1);
                if ((whatPlace(0, tookCard) < whatPlace(0, notTook))||(whatPlace(0, tookCard)==1))
                    return tookCard;
                else return notTook;
            }
            case 1: {
                if ((depth == maxdepth)||(round == numberOfRounds)) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)),
                        cards2, chips0, chips1 + chips, chips2, 0, depth+1);
                if (chips >= cards.get(round) || chips1 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0,
                        chips1 - 1, chips2, chips + 1, depth+1);
                if (whatPlace(1, tookCard) < whatPlace(1, notTook)||(whatPlace(1, tookCard)==1))
                    return tookCard;
                else return notTook;
            }
            case 2: {
                if ((depth == maxdepth)||(round == numberOfRounds)) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, cards0, cards1,
                        addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0, depth+1);
                if (chips >= cards.get(round) || chips2 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2,
                        chips0, chips1, chips2 - 1, chips + 1, depth+1);
                if (whatPlace(2, tookCard) < whatPlace(2, notTook)||(whatPlace(2, tookCard)==1))
                    return tookCard;
                else return notTook;
            }
        }
        //tuto sa nikdy nedostaneme, ale syntax nepustí
        return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
    }
}

/*podla nasledovnej strategie hrac zoberie kartu ak sa na nej nachadza aspo c/n zetonov, pricom n je dlzka postupnosti
* ktoru nou vie hrac este vytvorit a c je hodnota danej karty z postupnosti
* */
class SequenceStrategy extends Strategy{

    private ArrayList<Integer> cards;
    private int numberOfRounds;
    //konštruktorom si inicializujeme poradie kariet prichádzajúcich do hry a počet kôl
    SequenceStrategy(ArrayList<Integer> cards, int numberOfRounds) {
        this.cards = cards;
        this.numberOfRounds = numberOfRounds;
    }

    @Override
    int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips, int depth) {
        return new int[0];
    }

    @Override
    boolean decide(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips) {
        ArrayList<Integer> possibleCards = new ArrayList<>();
        if (currentPlayer==0){
            possibleCards.addAll(cards0);
        } else if (currentPlayer==1){
            possibleCards.addAll(cards1);
        } else if (currentPlayer==2){
            possibleCards.addAll(cards2);
        }
        for (int i = round; i < numberOfRounds; i++) {
            possibleCards.add(cards.get(i));
        }

        int indexl = cards.get(round);
        while (possibleCards.contains(indexl))
            indexl--;
        int indexr = cards.get(round);
        while (possibleCards.contains(indexr))
            indexr++;
        int n = indexr-indexl-1;

        System.out.println(possibleCards+" "+cards.get(round)+" "+n);
        return chips>cards.get(round)/n;
        //mozno namiesto hodnoty karty by bolo lepsie davat hodnotu najmensej karty z postupnosti
    }
}

/*toto je nastavenie kedy hra uzivatel
* */

class NotARobot extends Strategy{

    @Override
    int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips, int depth) {
        return new int[0];
    }

    @Override
    boolean decide(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips) {
        return false;
    }
}