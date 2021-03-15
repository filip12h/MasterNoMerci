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
        Game game = new Game(numofPl, numberOfRounds,numOfCards,playersChips);
        game.playGame();
    }
}