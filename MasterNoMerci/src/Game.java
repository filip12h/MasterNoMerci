import java.util.*;

/*
    táto klasa obsahuje všetky informácie o hre ktorá sa hrá - počet hráčov, žetónov na stole,
    poradie kariet ako budú prichádzať do hry, zoznam hráčov a hráč na ťahu
 */
//todo: memoizovať si výsledky zo stavov do hašovacej tabuľky
public class Game {
    private ArrayList<Player> players;
    private ArrayList<Strategy> strategies;
    private ArrayList<Integer> cards;
    private int numberOfRounds;
    private int numOfPl;
    private int chips;
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

    public static HashMap<String, int[]> memo = new HashMap<>();

    public Game(int numOfPl, int numberOfRounds, int numOfCards, int playersChips) {
        chips = 0;
        players = new ArrayList<>();
        this.numberOfRounds = numberOfRounds;
        this.numOfPl = numOfPl;

        for (int i = 0; i < numOfPl; i++)
            players.add(new Player("Player " + (i + 1), i, playersChips));



        cards = new ArrayList<>();
/*
        cards.add(14);
        cards.add(18);
        cards.add(24);
        cards.add(34);
        cards.add(25);
        cards.add(5);
        cards.add(9);
        cards.add(11);
        cards.add(33);
        cards.add(35);
        cards.add(31);
        cards.add(32);
        cards.add(7);
        cards.add(21);
        cards.add(13);
        cards.add(22);
        cards.add(3);
        cards.add(29);
        cards.add(10);
        cards.add(15);
        cards.add(28);
        cards.add(12);
        cards.add(17);
        cards.add(4);
        cards.add(8);
        cards.add(23);
        cards.add(19);
        cards.add(27);
        cards.add(20);
        cards.add(6);
        cards.add(26);
        cards.add(30);
        cards.add(16);
*/
        for (int i = 3; i < numOfCards + 3; i++) cards.add(i);
        Collections.shuffle(cards);
    }
    public int[] playGame(int limit){
        strategies = new ArrayList<>();
        strategies.add(new BasicStrategy(cards, numberOfRounds, 2));
        strategies.add(new BasicStrategy(cards, numberOfRounds, 16));
        strategies.add(new BasicStrategy(cards, numberOfRounds, 9));
        Random random = new Random();
        Player currentPlayer = players.get(random.nextInt(numOfPl));

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
        //previousTurns is string which consists 0s and 1s in order which tells if card was taken or not
        String previousTurns = "";
        while (round<numberOfRounds){
            //long startTime = System.nanoTime();
            //System.out.println(cards);
            //System.out.println("ROUND "+round+"\nCurrent card:"+ cards.get(round)+". "+ currentPlayer.getName()+", would you like to take it?(y/n). There is also "+ chips +" chips.");
            String answer;
            if (currentPlayer.getChips()==0){
                System.out.println(currentPlayer.getName()+" has no chips, therefore they have to take the card.");
                answer = "y";
            } else if (strategies.get(currentPlayer.getId()).getClass()==NotARobot.class){
                answer = input.next();
            } else {
                if (strategies.get(currentPlayer.getId()).decide(currentPlayer.getId(), round, players.get(0).getCards(),
                        players.get(1).getCards(), players.get(2).getCards(), players.get(0).getChips(),
                        players.get(1).getChips(), players.get(2).getChips(), chips, previousTurns))
                    answer = "y";
                else
                    answer = "n";
            }
            while(true) {
                if (answer.equals("y")) {
                    previousTurns += "1";
                    currentPlayer.takeCard(cards.get(round), chips);
                    round++;
                    chips = 0;
                    break;
                }
                else if (answer.equals("n")) {
                    previousTurns += "0";
                    currentPlayer.notTake();
                    currentPlayer = players.get((currentPlayer.getId()+1)%numOfPl);
                    chips++;
                    break;
                }
            //    System.out.println("Please, answer \'y\' or \'n\'");
                answer = input.next();
            }
            //System.out.println(gameScore());
            //long endTime = System.nanoTime();
            //long timeElapsed = endTime - startTime;
            //System.out.println("turn lasted: " + timeElapsed / 1000000);
        }
        int[] finalScore = new int[3];
        finalScore[0] = players.get(0).getPoints();
        finalScore[1] = players.get(1).getPoints();
        finalScore[2] = players.get(2).getPoints();
        return finalScore;
    }
}