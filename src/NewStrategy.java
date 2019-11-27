import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class NewStrategy {
    private ArrayList<Integer> cards;
    private ArrayList<Integer> cards0, cards1, cards2;
    private int chips0, chips1, chips2, numberOfRounds;
    NewStrategy(ArrayList<Player> players, ArrayList<Integer> cards, int numberOfRounds){
        cards0 = players.get(0).getCards();
        cards0 = players.get(0).getCards();
        cards0 = players.get(0).getCards();
        chips0 = players.get(0).getChips();
        chips0 = players.get(0).getChips();
        chips0 = players.get(0).getChips();
        this.cards = cards;
        this.numberOfRounds = numberOfRounds;
    }

    int myPosition(ArrayList<Integer> c0, ArrayList<Integer> c1, ArrayList<Integer> c2, int ch0, int ch1, int ch2){
        Player player0 = new Player(ch0, c0);
        Player player1 = new Player(ch1, c1);
        Player player2 = new Player(ch2, c2);
        if (player0.howManyPoints(c0, ch0)>=player1.howManyPoints(c1, ch1)&&player0.howManyPoints(c0, ch0)>=player2.howManyPoints(c2, ch2)) return 1;
        if (player0.howManyPoints(c0, ch0)<player1.howManyPoints(c1, ch1)&&player0.howManyPoints(c0, ch0)<player2.howManyPoints(c2, ch2)) return 3;
        return 2;
    }

    ArrayList<Integer> addCardToList(ArrayList<Integer> cardsList, int card) {
        Set<Integer> set = new HashSet<Integer>(cardsList);
        set.add(card);
        return new ArrayList<>(set);
    }

    int whatPlace(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips) {
        switch (currentPlayer) {
            case 0: {
                if (round == numberOfRounds) return myPosition(cards0, cards1, cards2, chips0, chips1, chips2);
                if (chips >= cards.get(round) || chips0 == 0)
                    return whatPlace(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)), cards1, cards2, chips0 + chips, chips1, chips2, 0);
                return (whatPlace(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)), cards1, cards2, chips0 + chips, chips1, chips2, 0) +
                        whatPlace((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0 - 1, chips1, chips2, chips + 1)) / 2;
            }
            case 1: {
                if (round == numberOfRounds) return myPosition(cards1, cards0, cards2, chips1, chips0, chips2);
                if (chips >= cards.get(round) || chips1 == 0)
                    return whatPlace(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)), cards2, chips0, chips1 + chips, chips2, 0);
                return (whatPlace(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)), cards2, chips0, chips1 + chips, chips2, 0) +
                        whatPlace((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0, chips1 - 1, chips2, chips + 1)) / 2;
            }
            case 2: {
                if (round == numberOfRounds) return myPosition(cards2, cards1, cards0, chips2, chips1, chips0);
                if (chips >= cards.get(round) || chips2 == 0)
                    return whatPlace(currentPlayer, round + 1, cards0, cards1, addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0);
                return (whatPlace(currentPlayer, round + 1, cards0, cards1, addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0) +
                        whatPlace((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0, chips1, chips2 - 1, chips + 1)) / 2;
            }
        }
        return 3;
    }

    boolean whatMove(int currentPlayer, int round, ArrayList<Integer> cards0, ArrayList<Integer> cards1, ArrayList<Integer> cards2, int chips0, int chips1, int chips2, int chips){
        //if number of players chips is 0, they have to take card, they also would like to take card if there is at least that many chips as it is value of card
        switch (currentPlayer){
            case 0: {
                if (chips >= cards.get(round) || chips0 == 0) return true;
                float tookCard = whatPlace(currentPlayer, round + 1, addCardToList(cards0, cards.get(round)), cards1, cards2, chips0 + chips, chips1, chips2, 0);
                float notTook = whatPlace((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0 - 1, chips1, chips2, chips + 1);
                return tookCard < notTook;
            }
            case 1: {
                if (chips >= cards.get(round) || chips1 == 0) return true;
                float tookCard = whatPlace(currentPlayer, round + 1, cards0, addCardToList(cards1, cards.get(round)), cards2, chips0, chips1 + chips, chips2, 0);
                float notTook = whatPlace((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0, chips1 - 1, chips2, chips + 1);
                return tookCard < notTook;
            }
            case 2: {
                if (chips >= cards.get(round) || chips2 == 0) return true;
                float tookCard = whatPlace(currentPlayer, round + 1, cards0, cards1, addCardToList(cards2, cards.get(round)), chips0, chips1, chips2 + chips, 0);
                float notTook = whatPlace((currentPlayer + 1) % 3, round, cards0, cards1, cards2, chips0, chips1, chips2 - 1, chips + 1);
                return tookCard < notTook;
            }
        }
        return true;
    }



}
