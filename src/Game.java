import java.util.*;

/*
    táto klasa obsahuje všetky informácie o hre ktorá sa hrá - počet hráčov, žetónov na stole,
    poradie kariet ako budú prichádzať do hry, zoznam hráčov a hráč na ťahu
 */
//todo: memoizovať si výsledky zo stavov do hašovacej tabuľky
class Game {
    private ArrayList<Player> players;
    //funkcia na vypísanie stavu hry vo formáte - hráč, žetóny, karty, skóre
    private String gameScore(){
        return players.get(0).getName()+"\n - chips: "+players.get(0).getChips()+"\n - cards: "+players.get(0).getCards()+"\n - score: "+players.get(0).getPoints()+"\n"
                +players.get(1).getName()+"\n - chips: "+players.get(1).getChips()+"\n - cards: "+players.get(1).getCards()+"\n - score: "+players.get(1).getPoints()+"\n"
                +players.get(2).getName()+"\n - chips: "+players.get(2).getChips()+"\n - cards: "+players.get(2).getCards()+"\n - score: "+players.get(2).getPoints()+"\n";
    }
    //vráti to miesto hráča podľa počtu bodov, pričom pri rovnakom počte bodov s iným hráčov sa vráti stred intervalu umiestnenia, t.j. ak som skončil na 2.-3.mieste, vráti sa 2.5
    private double whatPlace(int player, int[] points){
        if (points[player] > points[(player + 1) % 3] && points[player] > points[(player + 2) % 3]) return 1;
        if (points[player] < points[(player + 1) % 3] && points[player] < points[(player + 2) % 3]) return 3;
        if ((points[player] > points[(player + 1) % 3] && points[player] == points[(player + 2) % 3]) ||
                (points[player] == points[(player + 1) % 3] && points[player] > points[(player + 2) % 3])) return 1.5;
        if ((points[player] < points[(player + 1) % 3] && points[player] == points[(player + 2) % 3]) ||
                (points[player] == points[(player + 1) % 3] && points[player] < points[(player + 2) % 3])) return 2.5;
        return 2;
    }
    //funkcia ako vstup dostane zoznam čísel a nové číslo a vráti zjednotenie zoznamu s číslom
    private ArrayList<Integer> addCardToList(ArrayList<Integer> cardsList, int card) {
        Set<Integer> set = new HashSet<>(cardsList);
        set.add(card);
        return new ArrayList<>(set);
    }
    /*konštruktor rovno spúšťa hru - vstupný parameter je počet hráčov, počet kôl a počet kariet (kôl musí byť nanajvýš toľko, koľko kariet)
        počet žetónov nastavíme na 0, vytvoríme daný počet hráčov - mená hráčov sú od "Player 1" po "Player n", avšak id číslujeme od 0, teda hráči majú meno o 1 väčšie ako id
        náhodne nastavíme currentPlayer, do zoznamu pridáme karty a náhodne ich zamiešame, nastavíme kolo 0
        vytvoríme FinalStrategy a vo while cykle prebieha hra pokým je round menšie ako numOfRounds
    */
    Game(int numOfPl, int numberOfRounds, int numOfCards){
        int chips = 0;
        players = new ArrayList<>();

        for (int i = 0; i<numOfPl; i++)
            players.add(new Player("Player " + (i + 1), i));

        Random random = new Random();
        Player currentPlayer = players.get(random.nextInt(numOfPl));

        ArrayList<Integer> cards = new ArrayList<>();
        for (int i = 3; i<numOfCards+3; i++) cards.add(i);
        Collections.shuffle(cards);

        FinalStrategy strategy = new FinalStrategy(cards, numberOfRounds);
        int round = 0;
        Scanner input = new Scanner(System.in);
        /*
            na začiatku vypíšeme stav hry - kolo, aktuálna karta, hráč na ťahu a počet žetónov v strede
            ak má aktuuálny hráč 0 žetónov, jeho odpoveď bude automaticky "y"
            TODO:nasledovný úsek je fixne spravený pre hru 3 hráčov
            zistíme ktorý hráč je na ťahu - spýtame sa ako by hra dopadla ak by kartu zobral (pTookCard) alebo nezobral (pNotTook), pričom predpokladáme že všetci hrajú optimálne
            vyberieme si tú možnosť kde je whatPlace menšie, t.j. ten prípad, kde sa umiestnime na lepšej(nižšej) pozícii
            následne hru patrične upravíme podľa toho ako sa hráč rozhodol
        */
        while (round<numberOfRounds){
            System.out.println(cards);
            System.out.println("ROUND "+round+"\nCurrent card:"+ cards.get(round)+". "+ currentPlayer.getName()+", would you like to take it?(y/n). There is also "+ chips +" chips.");
            String answer = "";
            //if (currentPlayer.getId()>0) {
            if (currentPlayer.getChips()==0){
                System.out.println(currentPlayer.getName()+" has no chips, therefore they have to take the card.");
                answer = "y";
            } else {
                int[] pTookCard;
                int[] pNotTook;
                if (currentPlayer.getId()==0){
                    pTookCard = strategy.whatMove(0, round + 1, addCardToList(players.get(0).getCards(), cards.get(round)), players.get(1).getCards(), players.get(2).getCards(), players.get(0).getChips() + chips, players.get(1).getChips(), players.get(2).getChips(), 0);
                    pNotTook = strategy.whatMove(1, round, players.get(0).getCards(), players.get(1).getCards(), players.get(2).getCards(), players.get(0).getChips() - 1, players.get(1).getChips(), players.get(2).getChips(), chips + 1);
                } else if(currentPlayer.getId()==1){
                    pTookCard = strategy.whatMove(1, round + 1, players.get(0).getCards(), addCardToList(players.get(1).getCards(), cards.get(round)), players.get(2).getCards(), players.get(0).getChips(), players.get(1).getChips() + chips, players.get(2).getChips(), 0);
                    pNotTook = strategy.whatMove(2, round, players.get(0).getCards(), players.get(1).getCards(), players.get(2).getCards(), players.get(0).getChips(), players.get(1).getChips() - 1, players.get(2).getChips(), chips + 1);
                } else {
                    pTookCard = strategy.whatMove(2, round + 1, players.get(0).getCards(), players.get(1).getCards(), addCardToList(players.get(2).getCards(), cards.get(round)), players.get(0).getChips(), players.get(1).getChips(), players.get(2).getChips() + chips, 0);
                    pNotTook = strategy.whatMove(0, round, players.get(0).getCards(), players.get(1).getCards(), players.get(2).getCards(), players.get(0).getChips(), players.get(1).getChips(), players.get(2).getChips() - 1, chips + 1);
                }
                //System.out.println("took=" + pTookCard[0] + " " + pTookCard[1] + " " + pTookCard[2]);
                //System.out.println("not=" + pNotTook[0] + " " + pNotTook[1] + " " + pNotTook[2]);
                if (whatPlace(currentPlayer.getId(), pTookCard) < whatPlace(currentPlayer.getId(), pNotTook)) {
                    System.out.println(currentPlayer.getName()+" took card "+cards.get(round));
                    answer = "y";
                } else {
                    System.out.println(currentPlayer.getName()+" did not take card "+cards.get(round));
                    answer = "n";
                }
            }
            //}
            //else if (currentPlayer.getId()==0) answer = input.next();
            while(true) {
                if (answer.equals("y")) {
                    currentPlayer.takeCard(cards.get(round), chips);
                    round++;
                    chips = 0;
                    break;
                }
                else if (answer.equals("n")) {
                    currentPlayer.notTake();
                    currentPlayer = players.get((currentPlayer.getId()+1)%numOfPl);
                    chips++;
                    break;
                }
                System.out.println("Please, answer \'y\' or \'n\'");
                answer = input.next();
            }
            System.out.println(gameScore());
        }
    }
}