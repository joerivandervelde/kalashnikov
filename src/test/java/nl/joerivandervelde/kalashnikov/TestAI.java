package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.players.AI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAI {

    @Test
    void constructor() throws Exception {
        AI ai = new AI(0.5, 0.1, 0.3, 2);
        assertEquals(0.5, ai.getDiscardDuplicatesBeforeUselessCards());
        assertEquals(0.1, ai.getWaitForGoldenGun());
        assertEquals(0.3, ai.getChooseScrapOverUnknownShelf());
        assertEquals(2, ai.getMaxConsecUselessUnknownShelfDraws());
    }

}
