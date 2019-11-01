package nl.joerivandervelde.kalashnikov.logic;

import nl.joerivandervelde.kalashnikov.players.Player;

import java.util.HashMap;

/**
 * Contains functions to determine whether the series should continue, and
 * who has won.
 */
public class Win {

    /**
     * After each game we check if there are more than 1 players alive. If
     * so, the next game starts. If not, we can determine who has won.
     *
     * @param players
     * @return
     */
    public static boolean moreThanOnePlayerAlive(
        HashMap<Integer, Player> players) {
        int alivePlayers = 0;
        for (int player : players.keySet()) {
            if (players.get(player).getHealth().intValue() > 0) {
                alivePlayers++;
            }
        }
        return alivePlayers > 1;
    }

    /**
     * Return the first player with positive health. This assumes there are
     * no two or more players with positive health left in the series. If it
     * turns out that all players killed each other, return 0 and game ends
     * in a draw.
     *
     * @param players
     * @return
     * @throws Exception
     */
    public static int winner(HashMap<Integer, Player> players) {
        for (int player : players.keySet()) {
            if (players.get(player).getHealth().intValue() > 0) {
                return player;
            }
        }
        return 0;
    }
}
