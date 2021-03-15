import java.util.ArrayList;
import java.util.Collections;

/*
    táto trieda udržuje informácie o danom hráčovi - žetóny, karty, meno, body, poradie
 */

class Player {

    private int chips;
    private ArrayList<Integer> cardsList;
    private String name;
    private int points;
    private int id;
    //hlavný konštruktor hráča používaný pri vytváraní reálnej hry
    Player(String name, int id, int chips){
        this.name = name;
        cardsList = new ArrayList<>();
        points = 11;
        this.chips = chips;
        this.id = id;
    }
    //id hráča slúži určuje poradie. Na to aby sme zistili ktorý hráč nasleduje po tomto hráčovi, id zväčšíme o 1
    int getId(){return id;}
    //táto metóda je zavolaná po tom ako hráč zoberie kartu
    //ak sa v zozname hráčových kariet nenachádza karta o 1 menšia, skóre sa zmenší o hodnotu novej karty
    //ak sa v zozname hráčových kariet nachádza karta o 1 väčšia, skóre sa zväčší o hodnotu novej karty + 1
    private void updatesPoints(int newCard){
        if (!cardsList.contains(newCard-1))
            points -= newCard;
        if (cardsList.contains(newCard+1))
            points += newCard+1;
    }
    //hráč zoberie kartu -> pridá si žetóny, kartu do svojho zoznamu a aktualizuje body o žetóny zo stredu a pomocou funkcie updatesPoints
    void takeCard(int card, int chips){
        this.chips += chips;
        cardsList.add(card);
        updatesPoints(card);
        points += chips;
    }
    //keď hráč nezoberie kartu, jeho počet žetónov aj skóre sa zmenší o 1
    void notTake(){
        chips--;
        points--;
    }
    //vráti skóre hráča
    int getPoints() {
        return points;
    }
    //vráti počet žetónov hráča
    int getChips() {
        return chips;
    }
    //vráti hráčove karty
    ArrayList<Integer> getCards() { return cardsList; }
    //vráti hráčove meno - pre potreby pekného výpisu
    String getName() {
        return name;
    }
}