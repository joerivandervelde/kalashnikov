package nl.joerivandervelde.kalashnikov.analysis;

import nl.joerivandervelde.kalashnikov.Kalashnikov;

/**
 * Let 2 to 9 random AI players play a 1,000 series of games and report the
 * increased odds of player 1 winning relative to expected odds. Observe each
 * combination 100 times to get a distribution. From 10 players on this
 * becomes time consuming due to the high number of games per series.
 */
public class WinBiasVsNrOfPlayers {
    public static void main(String[] args) throws Exception {
        System.out.println("players" + "\t" + "percBetterWinningOdds");
        int seriesPlayed = 1000;
        for (int players = 2; players <= 9; players++) {
            for (int obs = 0; obs < 100; obs++) {
                int p1wins = 0;
                for (int i = 0; i < seriesPlayed; i++) {
                    Kalashnikov series = new Kalashnikov(players, false);
                    series.start();
                    if (series.getWinner() == 1) {
                        p1wins++;
                    }
                }
                double odds = ((double) p1wins) / ((double) seriesPlayed);
                double frac = (1.0 / (double) players);
                double betterOdds = ((odds - frac) / frac) * 100.0;
                double roundBetterOdds =
                    Math.round(betterOdds * 1000.0) / 1000.0;
                System.out.println(players + "\t" + roundBetterOdds);
            }
        }
    }
}
