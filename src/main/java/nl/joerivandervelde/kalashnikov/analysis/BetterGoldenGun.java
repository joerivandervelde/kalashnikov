package nl.joerivandervelde.kalashnikov.analysis;

import nl.joerivandervelde.kalashnikov.Kalashnikov;
import nl.joerivandervelde.kalashnikov.players.AI;
import nl.joerivandervelde.kalashnikov.players.Player;

import java.util.HashMap;

/**
 * Find out if more damage increase the win chance for golden gun strategy.
 * Also take into account the likelihood of waiting for golden gun.
 * Create 25 observations for each condition. Swap players to prevent P1
 * winning bias. Play against AI that never goes for golden gun.
 */
public class BetterGoldenGun {
    public static void main(String[] args) throws Exception {
        System.out.println("goldenStrat" + "\t" + "goldenGunDmg" + "\t" +
            "wins");
        for (int ggLikelihood = 5; ggLikelihood <= 10; ggLikelihood++) {
            double goldenStrat = ((double) ggLikelihood) / 10.0;
            for (int goldenGunDmg = 1; goldenGunDmg <= 20; goldenGunDmg++) {
                for (int obs = 0; obs < 25; obs++) {
                    int goldenStratWins = 0;
                    for (int iSeries = 0; iSeries < 1000; iSeries++) {
                        HashMap<Integer, Player> players = new HashMap<>();
                        Player goldenStratPlayer =
                            new Player(new AI(0.0, goldenStrat,
                                0.0, 2));
                        Player normalStratPlayer = new Player(new AI(0.0, 0.0,
                            0.0, 2));
                        if (iSeries % 2 == 0) {
                            players.put(1, goldenStratPlayer);
                            players.put(2, normalStratPlayer);
                            Kalashnikov series =
                                new Kalashnikov(players, false);
                            series.GOLDEN_GUN_DMG = goldenGunDmg;
                            series.start();
                            if (series.getWinner() == 1) {
                                goldenStratWins++;
                            }
                        } else {
                            players.put(2, goldenStratPlayer);
                            players.put(1, normalStratPlayer);
                            Kalashnikov series =
                                new Kalashnikov(players, false);
                            series.GOLDEN_GUN_DMG = goldenGunDmg;
                            series.start();
                            if (series.getWinner() == 2) {
                                goldenStratWins++;
                            }
                        }
                    }
                    System.out.println(
                        goldenStrat + "\t" + goldenGunDmg + "\t" +
                            goldenStratWins);
                }
            }
        }
    }
}
