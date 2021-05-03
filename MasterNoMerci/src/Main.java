/*
    purpose of this class is only to create instance of class Game - i.e. start the game
 */

public class Main {
    // 2.parameter zmeň na 24, 3.par.na 36 a 4.par. na 11 pre originál hru

    public static void main(String[] args) {
        int numofPl = 3;
        int numberOfRounds = 24;
        int numOfCards = 33;
        int playersChips = 11;
        int[][] hundredGames = new int[100][3];
        int[] avg = new int[3];
        long startTime = System.nanoTime();
        for (int i = 0; i < 1; i++) {
            Game game = new Game(numofPl, numberOfRounds, numOfCards, playersChips);
            hundredGames[i] = game.playGame(10);
            for (int j = 0; j < 3; j++) {
                avg[j] += hundredGames[i][j];
            }
            //System.out.println(hundredGames[i][0] + "..." + hundredGames[i][1] + "..." + hundredGames[i][2]);
            Game.memo.clear();
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println(timeElapsed / 1000000);
    }
}