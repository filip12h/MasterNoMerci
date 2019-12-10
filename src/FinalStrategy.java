import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class FinalStrategy {
    private ArrayList<Integer> cards;
    private int numberOfRounds;
    //konštruktorom si inicializujeme poradie kariet prichádzajúcich do hry a počet kôl
    FinalStrategy(ArrayList<Integer> cards, int numberOfRounds) {
        this.cards = cards;
        this.numberOfRounds = numberOfRounds;
    }
    //identická metóda ako v klase Game
    private double whatPlace(int player, int[] points) {
        if (points[player] > points[(player + 1) % 3] && points[player] > points[(player + 2) % 3]) return 1;
        if (points[player] < points[(player + 1) % 3] && points[player] < points[(player + 2) % 3]) return 3;
        if ((points[player] > points[(player + 1) % 3] && points[player] == points[(player + 2) % 3]) ||
                (points[player] == points[(player + 1) % 3] && points[player] > points[(player + 2) % 3])) return 1.5;
        if ((points[player] < points[(player + 1) % 3] && points[player] == points[(player + 2) % 3]) ||
                (points[player] == points[(player + 1) % 3] && points[player] < points[(player + 2) % 3])) return 2.5;
        return 2;
    }
    //identická metóda ako v klase Game
    private ArrayList<Integer> addCardToList(ArrayList<Integer> cardsList, int card) {
        Set<Integer> set = new HashSet<>(cardsList);
        set.add(card);
        return new ArrayList<>(set);
    }
    //prechádza hráčove karty od najmenšej karty po najväčšiu - ak nie je prezeraná karta o 1 väčšia ako previous, zväčší counter o i, prezeranú kartu si následne uložíme do previous
    //vrátime hodnotu chips - counter
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
    //pomocou metódy howManyPoints zistíme aktuálne skóre všetkých 3 hráčov
    private int[] currentPoints(ArrayList<Integer> c0, ArrayList<Integer> c1, ArrayList<Integer> c2, int ch0, int ch1, int ch2) {
        int[] points = new int[3];
        points[0] = howManyPoints(c0, ch0);
        points[1] = howManyPoints(c1, ch1);
        points[2] = howManyPoints(c2, ch2);
        return points;
    }
    /*
        rekurzívna funkcia - zistí ktorý hráč je momentálne na rade, ak už skončila hra (rounds==numberOfRounds) tak vrátime currentPoints
        zistíme si ako by skončila hra v prípade že by hráč kartu zobral a ak by ju nezobral - zavoláme whatMove() s patrične upravenými parametrami
        ak je žetónov viac ako je hodnota karty alebo daný hráč už nemá žetóny, tak vrátime tookCard - teda ako by skončila hra v prípade že by hráč zobral kartu
        zistíme pre ktorú možnosť (zobrať alebo nezobrať) skončí daný hráč na lepšej pozícii a vrátime stav bodov tej situácie ktorú si vyberieme
    */
    int[] whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips) {
        switch (currentPlayer) {
            case 0: {
                if (round == numberOfRounds) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)), cards1, cards2, chips0 + chips, chips1, chips2, 0);
                if (chips >= cards.get(round) || chips0 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0 - 1, chips1, chips2, chips + 1);
                if (whatPlace(0, tookCard) < whatPlace(0, notTook))
                    return tookCard;
                else return notTook;
            }
            case 1: {
                if (round == numberOfRounds) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)), cards2, chips0, chips1 + chips, chips2, 0);
                if (chips >= cards.get(round) || chips1 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0, chips1 - 1, chips2, chips + 1);
                if (whatPlace(1, tookCard) < whatPlace(1, notTook))
                    return tookCard;
                else return notTook;
            }
            case 2: {
                if (round == numberOfRounds) {
                    return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
                }
                int[] tookCard = whatMove(currentPlayer, round + 1, cards0, cards1, addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0);
                if (chips >= cards.get(round) || chips2 == 0)
                    return tookCard;
                int[] notTook = whatMove((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0, chips1, chips2 - 1, chips + 1);
                if (whatPlace(2, tookCard) < whatPlace(2, notTook))
                    return tookCard;
                else return notTook;
            }
        }
        //tuto sa nikdy nedostaneme, ale syntax nepustí
        return currentPoints(cards0, cards1, cards2, chips0, chips1, chips2);
    }
}