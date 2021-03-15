public class Test {

    public static void main(String[] args) {
        int numofPl = 3;
        int numberOfRounds = 24;
        int numOfCards = 33;
        int playersChips = 11;
        int[][] scoreStats = new int[100][3];
        for (int i = 0; i < 100; i++) {
            Game game = new Game(numofPl, numberOfRounds,numOfCards,playersChips);
            scoreStats[i] = game.playGame();
        }
        /*vypis priemerne skore a pocet akych umiestneni*/
        for (int i = 0; i < 3; i++) {
            int totalScore = 0;
            int[] positions = new int[3];
            for (int j = 0; j < 100; j++) {
                totalScore += scoreStats[j][i];
                if ((scoreStats[j][i]>=scoreStats[j][(i+1)%3])&&(scoreStats[j][i]>=scoreStats[j][(i+2)%3]))
                    positions[0]++;
                else
                if ((scoreStats[j][i]<scoreStats[j][(i+1)%3])&&(scoreStats[j][i]<scoreStats[j][(i+2)%3]))
                    positions[2]++;
                else
                    positions[1]++;
            }
            System.out.println("Player "+i+":\n"+totalScore/100);
            System.out.println(positions[0]+" "+positions[1]+" "+positions[2]);
        }
    }
}