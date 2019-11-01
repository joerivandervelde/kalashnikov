package nl.joerivandervelde.kalashnikov.analysis;

import nl.joerivandervelde.kalashnikov.Kalashnikov;

/**
 * Let 2 to 13 random AI players play 1,000 series and report how many games
 * had to be played to reach a winner.
 */
public class NrOfGamesVsNrOfPlayers {
    public static void main(String[] args) throws Exception {
        System.out.println("players" + "\t" + "games");
        for (int players = 2; players <= 13; players++) {
            for (int i = 0; i < 1000; i++) {
                Kalashnikov series = new Kalashnikov(players, false);
                series.start();
                System.out
                    .println(players + "\t" + series.getNrOfGamesPlayed());
            }
        }
    }
}
