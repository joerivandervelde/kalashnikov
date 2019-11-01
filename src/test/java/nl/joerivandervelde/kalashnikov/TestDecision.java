package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.players.Decision;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestDecision {

    @Test
    void shelfCard() throws Exception {
        Decision d = new Decision(Decision.Action.PUT_ON_SHELF,
            new Card(13, Card.Suit.SPADES));
        assertEquals("PUT_ON_SHELF the ACE of SPADES", d.toString());
    }

    @Test
    void attack() throws Exception {
        Decision d = new Decision(Decision.Action.ATTACK);
        assertEquals("ATTACK", d.toString());
    }

}
