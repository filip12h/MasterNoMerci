public class TestDepthLimit {
    public static void main(String[] args) {
        int numofPl = 3;
        int numberOfRounds = 5;
        int numOfCards = 33;
        int playersChips = 11;
        int[][] hundredGames = new int[100][3];
        int[] avg = new int[3];
        for (int limit = 0; limit < 100; limit++) {
            long startTime = System.nanoTime();
            for (int i = 0; i < 1; i++) {
                Game game = new Game(numofPl, numberOfRounds, numOfCards, playersChips);
                hundredGames[i] = game.playGame(limit);
                for (int j = 0; j < 3; j++) {
                    avg[j] += hundredGames[i][j];
                }
                //System.out.println(hundredGames[i][0] + "..." + hundredGames[i][1] + "..." + hundredGames[i][2]);
                Game.memo.clear();
            }
            //System.out.println("average scores after 100 games");
            //System.out.println("player 0: " + avg[0] + "\nplayer 1: " + avg[1] + "\nplayer 2: " + avg[2]);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println(timeElapsed / 1000000);
        }
    }
}
