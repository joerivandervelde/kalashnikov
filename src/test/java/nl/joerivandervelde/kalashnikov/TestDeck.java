package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestDeck {

    @Test
    void refresh() throws Exception {
        Deck d = new Deck();
        assertEquals(51, d.getTop());
        d.takeCard();
        assertEquals(50, d.getTop());
        d.refresh();
        assertEquals(51, d.getTop());
    }

}
