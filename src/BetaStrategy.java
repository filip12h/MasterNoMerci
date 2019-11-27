/*
this java class completely sucks...
I rather try make another one
 */

import java.util.ArrayList;

class BetaStrategy {
    private int myID;
    private int chips;
    private int round;
    private ArrayList<Integer> cards; //cards in play
    private ArrayList<Player> players; //here is all needed informations about me and other players

    BetaStrategy(int myID, int chips, ArrayList<Integer> cards, ArrayList<Player> players, int round){
        this.myID = myID;
        this.chips = chips;
        this.cards = cards;
        this.players = players;
        this.round = round;
    }

    private float maximizePlaceWhenTake(int myID, int playerOnTurn, int chips, int round, ArrayList<Player> p) {
        ArrayList<Player> players = (ArrayList<Player>) p.clone();
        players.get(playerOnTurn).gainChips(chips);
        players.get(playerOnTurn).takeCard(cards.get(round));
        return maximizePlace(myID, playerOnTurn, 0, round+1, players);
    }

    private float maximizePlaceNotTake(int myID, int playerOnTurn, int chips, int round, ArrayList<Player> p){
        ArrayList<Player> players = (ArrayList<Player>) p.clone();
        players.get(playerOnTurn).gainChips(-1);
        return maximizePlace(myID, (playerOnTurn+1)%3, chips+1, round, players);
    }

    private float maximizePlace(int myID, int playerOnTurn, int chips, int round, ArrayList<Player> p){
        ArrayList<Player> players = (ArrayList<Player>) p.clone();
        if (round==23) {
            if ((pointsAfterTakeCard(playerOnTurn, players, round) >= players.get((myID + 1) % 3).getPoints()) && (pointsAfterTakeCard(playerOnTurn, players, round) >= players.get((myID + 2) % 3).getPoints()))
                return 1;
            if ((pointsAfterTakeCard(playerOnTurn, players, round) < players.get((myID + 1) % 3).getPoints()) && (pointsAfterTakeCard(playerOnTurn, players, round) < players.get((myID + 2) % 3).getPoints()))
                return 3;
            return 2;
        }
        if ((chips>=cards.get(round))||(players.get(myID).getChips()==0)){
            players.get(playerOnTurn).playersTurn(true, cards.get(round), chips);
            return maximizePlace(myID, playerOnTurn, 0, round+1, players);
        }

        return (maximizePlaceWhenTake(myID, playerOnTurn, chips, round, players)
                + maximizePlaceNotTake(myID, playerOnTurn, chips, round, players)) / 2;

    }

    boolean iWouldTake(int myID, int chips, int round, ArrayList<Player> players){
        return (maximizePlaceWhenTake(myID, myID, chips, round, players) <= maximizePlaceNotTake(myID, myID, chips, round, players));
    }

    private int pointsAfterTakeCard(int playerOnTurn, ArrayList<Player> players, int round){
        int points = players.get(playerOnTurn).getPoints();
        if (!players.get(playerOnTurn).getCards().contains(cards.get(round)-1))
            points -= cards.get(round);
        if (players.get(playerOnTurn).getCards().contains(cards.get(round)+1))
            points += cards.get(round)+1;
        return points;
    }

}
