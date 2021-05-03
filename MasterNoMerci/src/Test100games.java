public class Test100games {

    // tuto porovnavame 3 strategie a pre kazdu z nich spocitame kolkokrát skoncila 1., 2. alebo 3.

    public static void main(String[] args) {
        int numofPl = 3;
        int numberOfRounds = 24;
        int numOfCards = 33;
        int playersChips = 11;
        int numOfGames = 100;
        int[][] scoreStats = new int[numOfGames][3];
        for (int i = 0; i < numOfCards; i++) {
            Game game = new Game(numofPl, numberOfRounds,numOfCards,playersChips);
            scoreStats[i] = game.playGame(0);
        }
        /*vypis priemerne skore a pocet akych umiestneni*/
        for (int i = 0; i < 3; i++) {
            int totalScore = 0;
            int[] positions = new int[3];
            for (int j = 0; j < numOfCards; j++) {
                totalScore += scoreStats[j][i];
                if ((scoreStats[j][i]>=scoreStats[j][(i+1)%3])&&(scoreStats[j][i]>=scoreStats[j][(i+2)%3]))
                    positions[0]++;
                else
                if ((scoreStats[j][i]<scoreStats[j][(i+1)%3])&&(scoreStats[j][i]<scoreStats[j][(i+2)%3]))
                    positions[2]++;
                else
                    positions[1]++;
            }
            System.out.println("Player "+i+":\n"+totalScore/numOfGames);
            System.out.println(positions[0]+" "+positions[1]+" "+positions[2]);
        }
    }
}