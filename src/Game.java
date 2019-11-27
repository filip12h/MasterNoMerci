import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private int chips; //number of chips in the centre
    private ArrayList<Integer> cards; //list of cards in the game
    private int numOfPl;
    private ArrayList<Player> players;
    private Random random = new Random();
    private Player currentPlayer;
    private int currentCard;

    boolean endOfGame() {return cards.isEmpty(); }

    ArrayList<Player> getPlayers(){ return players; }

    int getChips(){ return chips; }

    int addChip(){
        chips++;
        return chips;
    }

    public ArrayList<Integer> getCards(){ return cards; }


    Player firstPlayer(){
        currentPlayer = players.get(random.nextInt(numOfPl));
        return currentPlayer;
    }

    Player currentPlayer(){
        return currentPlayer;
    }

    Player nextPlayer(){
        currentPlayer = currentPlayer.getNextPlayer();
        return currentPlayer;
    }

    int currentCard(){
        return currentCard;
    }

    int getNextCard(){
        currentCard = cards.get(random.nextInt(cards.size()));
        cards.remove(cards.indexOf(currentCard));
        chips = 0;
        return currentCard;
    }

    ArrayList getcards(){
        return cards;
    }

    String gameScore(){
        return getPlayers().get(0).getName()+"\n - chips: "+getPlayers().get(0).getChips()+"\n - cards: "+getPlayers().get(0).getCards()+"\n - score: "+getPlayers().get(0).getPoints()+"\n"
                +getPlayers().get(1).getName()+"\n - chips: "+getPlayers().get(1).getChips()+"\n - cards: "+getPlayers().get(1).getCards()+"\n - score: "+getPlayers().get(1).getPoints()+"\n"
                +getPlayers().get(2).getName()+"\n - chips: "+getPlayers().get(2).getChips()+"\n - cards: "+getPlayers().get(2).getCards()+"\n - score: "+getPlayers().get(2).getPoints()+"\n";
    }

    int getWinner(){
        if (getPlayers().get(0).getPoints()>getPlayers().get(1).getPoints()){
            if (getPlayers().get(0).getPoints()>getPlayers().get(2).getPoints()){
                return 0;
            } else return 2;
        } if (getPlayers().get(1).getPoints()>getPlayers().get(2).getPoints()) return 1;
        else return 2;
    }

    public Game( int numOfPl){
        chips = 0;
        this.numOfPl = numOfPl;
        players = new ArrayList<>();

        for (int i = 0; i<numOfPl; i++)
            players.add(new Player("Player" + (i + 1), i));

        firstPlayer();

        //setting next players. TODO:probably doesnt work for setting first player as next to last...
        for (int i = 0; i<numOfPl; i++)
            players.get(i).setNextPlayer(players.get((i+1)%3));

        int numberOfRounds = 5; //TODO change to 24 for full game

        //we remove 10 cards at the beginning
        cards = new ArrayList<>();
        for (int i = 3; i<10; i++) cards.add(i); //TODO 10 change to 36 for full game
        Collections.shuffle(cards);
        NewStrategy strat = new NewStrategy(players, cards, numberOfRounds);
        for (int i = 0; i<numberOfRounds; i++){
            currentCard = cards.get(i);
            System.out.println(cards);
            System.out.println("ROUND "+i+"\nCurrent card:"+currentCard+". "+currentPlayer.getName()+", would you like to take it?(y/n). There is also "+chips+" chips.");
            if (currentPlayer.getId()==0){
                if (strat.whatMove(currentPlayer.getId(), i, players.get(0).getCards(), players.get(1).getCards(), players.get(2).getCards(), players.get(0).getChips(), players.get(1).getChips(), players.get(2).getChips(), chips))
                    System.out.println("TAKE IT!!!");
                else System.out.println("DONT TAKE IT");
            }
            String answer = "";
            if (currentPlayer.getChips()==0){
                System.out.println(currentPlayer.getName()+"has no chips, therefore they have to take the card.");
                answer = "y";
            } else {
                Scanner input = new Scanner(System.in);
                answer = input.next();
            }
            if (currentPlayer.playersTurn(answer.equals("y"), currentCard, chips)!=-1) {
                nextPlayer();
                chips++;
                i--;
            } else {
                //currentPlayer.gainChips(chips);
                chips = 0;
            }
            System.out.println(gameScore());
        }
    }
}