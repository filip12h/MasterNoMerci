import java.util.ArrayList;

class CopyPlayers {
    private ArrayList<Player> players;
    CopyPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    ArrayList<Player> getPlayers(){return players;}
}
class Strategy {
    private ArrayList<Integer> cards;
    private ArrayList<Player> players;
    Strategy(ArrayList<Player> players, ArrayList<Integer> cards){
        this.players = players;
        this.cards = cards;
    }
    ArrayList<Player> getPlayers(){return players;}

    int myPosition(int cp, ArrayList<Player> players){
        if (players.get(cp).getPoints()>=players.get((cp+1)%3).getPoints()&&players.get(cp).getPoints()>=players.get((cp+2)%3).getPoints()) return 1;
        if (players.get(cp).getPoints()<players.get((cp+1)%3).getPoints()&&players.get(cp).getPoints()<players.get((cp+2)%3).getPoints()) return 3;
        return 2;
    }

    int whatPlace(int currentPlayer, int round, ArrayList<Player> players, int chips){
        if (round==24) return myPosition(currentPlayer, players);
        if (chips >= cards.get(round) || players.get(currentPlayer).getChips() == 0) {
            players.get(currentPlayer).takeCard(cards.get(round));
            players.get(currentPlayer).gainChips(chips);
            return whatPlace(currentPlayer, round + 1, players, 0);
        }
        CopyPlayers altPlayers = new CopyPlayers(players);
        CopyPlayers alt2Players = new CopyPlayers(players);
        altPlayers.getPlayers().get(currentPlayer).takeCard(cards.get(round));
        altPlayers.getPlayers().get(currentPlayer).gainChips(chips);
        alt2Players.getPlayers().get(currentPlayer).gainChips(-1);
        return (whatPlace(currentPlayer, round + 1, altPlayers.getPlayers(), 0) + whatPlace((currentPlayer+1)%3, round, alt2Players.getPlayers(), chips+1))/2;
    }

    boolean whatMove(int currentPlayer, int round, ArrayList<Player> players, int chips){
        //if number of players chips is 0, they have to take card, they also would like to take card if there is at least that many chips as it is value of card
        if (chips >= cards.get(round) || players.get(currentPlayer).getChips() == 0) return true;
        //if player takes the card
        players.get(currentPlayer).takeCard(cards.get(round));
        players.get(currentPlayer).gainChips(chips);
        float tookCard = whatPlace(currentPlayer, round+1, players, 0);
        players.get(currentPlayer).removeLastCard();
        players.get(currentPlayer).gainChips(-chips-1);
        float notTook = whatPlace((currentPlayer+1)%3, round, players, chips);
        //we encount probability of winning in case of taking card and not taking card by recursive function whatPlace(), then we choose better option
        return tookCard>notTook;
    }



}
