package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.players.AI;
import nl.joerivandervelde.kalashnikov.players.Player;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPlayer {

    @Test
    void constructor() throws Exception {
        AI ai = new AI(0.5, 0.1, 0.2, 2);
        Player p = new Player(ai, new AtomicInteger(20));
        assertEquals(20, p.getHealth().intValue());
    }

    @Test
    void consecUselessUnknownShelfDrawsToString() throws Exception {
        AI ai = new AI(0.5, 0.1, 0.2, 2);
        Player p = new Player(ai, new AtomicInteger(20));
        assertEquals(0, p.getConsecUselessUnknownShelfDraws().intValue());
    }

}
