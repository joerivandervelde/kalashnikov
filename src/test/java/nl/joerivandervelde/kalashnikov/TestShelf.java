package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.logic.Hand;
import nl.joerivandervelde.kalashnikov.logic.Shelf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestShelf {

    // todo : draw sae card that was just shelved ?

    @Test
    void put() throws Exception {
        Shelf s = new Shelf(2);
        assertTrue(s.get(1).isEmpty());
        s.get(1).add(new Card(13, Card.Suit.SPADES));
        assertFalse(s.isEmpty());
        assertEquals(new Card(13, Card.Suit.SPADES), s.get(1).get(0));
    }

    @Test
    void getPreferredKnownCard() throws Exception {

        // we could use: clubs, hearts of diamonds, rank 6
        Hand h = new Hand();
        h.add(new Card(3, Card.Suit.CLUBS));
        h.add(new Card(12, Card.Suit.HEARTS));
        h.add(new Card(13, Card.Suit.DIAMONDS));

        // rank 6 of CLUBS and HEARTS on shelf, based on sorting HEART wins
        Shelf s = new Shelf(2);
        s.get(1).add(new Card(3, Card.Suit.HEARTS));
        s.get(1).add(new Card(6, Card.Suit.CLUBS));
        s.get(1).add(new Card(6, Card.Suit.HEARTS));
        s.get(1).add(new Card(13, Card.Suit.SPADES));
        s.get(2).add(new Card(6, Card.Suit.SPADES));

        assertEquals(new Card(6, Card.Suit.HEARTS),
            s.getPreferredKnownCard(1, h));

        // we could use: clubs or hearts, rank 6 or 13
        h.clear();
        h.add(new Card(3, Card.Suit.CLUBS));
        h.add(new Card(12, Card.Suit.HEARTS));
        h.add(new Card(4, Card.Suit.HEARTS));

        // in shelf: 6 of clubs best choice
        s = new Shelf(2);
        s.get(1).add(new Card(3, Card.Suit.HEARTS));
        s.get(1).add(new Card(6, Card.Suit.CLUBS));
        s.get(1).add(new Card(13, Card.Suit.SPADES));
        s.get(1).add(new Card(6, Card.Suit.SPADES));
        assertEquals(new Card(6, Card.Suit.CLUBS),
            s.getPreferredKnownCard(1, h));

        // we could use: HEARTS best, CLUBS OK, rank 3
        h.clear();
        h.add(new Card(12, Card.Suit.CLUBS));
        h.add(new Card(13, Card.Suit.HEARTS));
        h.add(new Card(6, Card.Suit.HEARTS));

        // shelf offers 3 useful cards, HEARTS best, then CLUBS, then SPADES
        final Shelf fs = new Shelf(2);
        fs.get(1).add(new Card(6, Card.Suit.SPADES));
        fs.get(1).add(new Card(3, Card.Suit.HEARTS));
        fs.get(1).add(new Card(3, Card.Suit.CLUBS));
        fs.get(1).add(new Card(3, Card.Suit.SPADES));
        assertEquals(new Card(3, Card.Suit.HEARTS),
            fs.getPreferredKnownCard(1, h));

        fs.get(1).remove(new Card(3, Card.Suit.HEARTS));
        assertEquals(new Card(3, Card.Suit.CLUBS),
            fs.getPreferredKnownCard(1, h));

        fs.get(1).remove(new Card(3, Card.Suit.CLUBS));
        assertEquals(new Card(3, Card.Suit.SPADES),
            fs.getPreferredKnownCard(1, h));

        // when no useful cards are left, throw error
        fs.get(1).remove(new Card(3, Card.Suit.SPADES));
        Assertions.assertThrows(Exception.class, () -> {
            fs.getPreferredKnownCard(1, h);
        });

    }

}
