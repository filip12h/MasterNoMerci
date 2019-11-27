import java.util.ArrayList;
import java.util.Collections;

class Player {

    private int chips;
    private boolean[] cardsBool;
    private ArrayList<Integer> cardsList;
    private String name;
    private int points;
    private Player nextPlayer;
    private int id;

    Player(int chips, ArrayList<Integer> cardsList){
        this.chips = chips;
        this.cardsList = cardsList;
    }

    Player(String name, int id){
        this.name = name;
        cardsBool = new boolean[37];
        for (boolean k : cardsBool) {k = false;}
        cardsList = new ArrayList<>();
        points = 5;
        chips = 5; //TODO change to 11 for original game
        this.id = id;
    }

    int getId(){return id;}

    void setNextPlayer(Player player){ nextPlayer = player; }

    Player getNextPlayer(){ return nextPlayer; }

    int howManyPoints(ArrayList<Integer> cards, int chips){
        int counter = 0;
        int previous = 0;
        Collections.sort(cards);
        for (Integer i: cards){
            if (i!=previous+1){
                counter += i;
                previous = i;
            }
        }
        return chips - counter;
    }

    private void updatesPoints(int newCard){
        if (!cardsBool[newCard-1])
            points -= newCard;
        if (cardsBool[newCard+1])
            points += newCard+1;
    }

    //players turn is true if he took card. If he didnt, it is false
    int playersTurn(boolean tookCard, int card, int chips) {
        //playChip - 'no, merci'
        if (!tookCard) {
            if (this.chips > 0)
                this.chips--;
            points--;
            return this.chips;
        } else {
            cardsBool[card] = true;
            this.chips += chips;
            updatesPoints(card);
            cardsList.add(card);
            points += chips;
            return -1;
        }
    }

    void gainChips(int n){
        chips += n;
    }

    int getPoints() {
        return points;
    }

    int getChips() {
        return chips;
    }

    ArrayList<Integer> getCards() { return cardsList; }

    void takeCard(int card){ cardsList.add(card);}

    String getName() {
        return name;
    }
    void removeLastCard(){ cardsList.remove(cardsList.size()-1);}


}