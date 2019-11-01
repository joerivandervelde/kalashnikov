package nl.joerivandervelde.kalashnikov.analysis;

import nl.joerivandervelde.kalashnikov.Kalashnikov;
import nl.joerivandervelde.kalashnikov.players.AI;
import nl.joerivandervelde.kalashnikov.players.Player;

import java.util.HashMap;
import java.util.Random;

/**
 * Let 1,000 random player 1 AIs play against 1,000 other random players 2
 * AIs, for a total of one million series of games. Report the AI settings of
 * player 1 and how many of the 1,000 series were won. Alternate P1 and P2
 * positions to prevent win bias.
 */
public class WinChanceOfRandomAIs {
    public static void main(String[] args) throws Exception {
        Random rng = new Random();
        System.out.println("discardDuplicatesBeforeUselessCards" + "\t" +
            "waitForGoldenGun" + "\t" + "chooseScrapOverUnknownShelf"
            + "\t" + "maxConsecUselessUnknownShelfDraws" + "\t" + "wins");
        for (int i = 0; i < 1000; i++) {
            AI p1ai = AI.generateRandomAI(rng);
            int p1wins = 0;
            for (int j = 0; j < 1000; j++) {
                HashMap<Integer, Player> players = new HashMap<>();
                Player p1 = new Player(p1ai);
                Player p2 = new Player(AI.generateRandomAI(rng));
                if (j % 2 == 0) {
                    players.put(1, p1);
                    players.put(2, p2);
                    Kalashnikov series = new Kalashnikov(players, false);
                    series.start();
                    if (series.getWinner() == 1) {
                        p1wins++;
                    }
                } else {
                    players.put(1, p2);
                    players.put(2, p1);
                    Kalashnikov series = new Kalashnikov(players, false);
                    series.start();
                    if (series.getWinner() == 2) {
                        p1wins++;
                    }
                }
            }
            double a =
                Math.round(p1ai.getDiscardDuplicatesBeforeUselessCards() * 1000.0) /
                    1000.0;
            double b = Math.round(p1ai.getWaitForGoldenGun() * 1000.0) / 1000.0;
            double c =
                Math.round(p1ai.getChooseScrapOverUnknownShelf() * 1000.0) /
                    1000.0;
            int d = p1ai.getMaxConsecUselessUnknownShelfDraws();
            System.out.println(a + "\t" + b + "\t" + c + "\t" + d +
                "\t" + p1wins);
        }
    }
}
