package nl.joerivandervelde.kalashnikov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestKalasnikov {

    @Test
    void put() throws Exception {
        Kalashnikov k = new Kalashnikov(2, false);
        k.start();
        int winner = k.getWinner();
        assertTrue(0 <= winner);
        assertTrue(2 >= winner);
    }

}
