package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.logic.Win;
import nl.joerivandervelde.kalashnikov.players.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestWin {

    static Player p1;
    static Player p2;
    static Player p3;
    static HashMap<Integer, Player> players;

    /**
     * use 3 players to show that it works for any number
     *
     * @throws Exception
     */
    @BeforeAll
    static void setup() throws Exception {
        p1 = new Player();
        p2 = new Player();
        p3 = new Player();
        players = new HashMap<Integer, Player>();
        players.put(1, p1);
        players.put(2, p2);
        players.put(3, p3);
    }


    @Test
    void moreThanOnePlayerAlive() throws Exception {
        p1.getHealth().set(1);
        p2.getHealth().set(1);
        p3.getHealth().set(1);
        assertEquals(true, Win.moreThanOnePlayerAlive(players));

        p1.getHealth().set(1);
        p2.getHealth().set(0);
        p3.getHealth().set(1);
        assertEquals(true, Win.moreThanOnePlayerAlive(players));

        p1.getHealth().set(0);
        p2.getHealth().set(0);
        p3.getHealth().set(1);
        assertEquals(false, Win.moreThanOnePlayerAlive(players));

        p1.getHealth().set(0);
        p2.getHealth().set(0);
        p3.getHealth().set(0);
        assertEquals(false, Win.moreThanOnePlayerAlive(players));
    }

    @Test
    void winner() throws Exception {
        p1.getHealth().set(1);
        p2.getHealth().set(0);
        p3.getHealth().set(0);
        assertEquals(1, Win.winner(players));

        p1.getHealth().set(0);
        p2.getHealth().set(0);
        p3.getHealth().set(1);
        assertEquals(3, Win.winner(players));

        p1.getHealth().set(-2);
        p2.getHealth().set(1);
        p3.getHealth().set(-3);
        assertEquals(2, Win.winner(players));

        // draw!
        p1.getHealth().set(0);
        p2.getHealth().set(0);
        p3.getHealth().set(0);
        assertEquals(0, Win.winner(players));

        // draw!
        p1.getHealth().set(-4);
        p2.getHealth().set(0);
        p3.getHealth().set(-2);
        assertEquals(0, Win.winner(players));
    }


}
