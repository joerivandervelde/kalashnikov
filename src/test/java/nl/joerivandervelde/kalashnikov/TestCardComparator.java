package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.cards.CardComparator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCardComparator {

    @Test
    void sortCards() throws Exception {

        List<Card> cards = new ArrayList<Card>();
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(12, Card.Suit.HEARTS));
        cards.add(new Card(2, Card.Suit.HEARTS));

        Collections.sort(cards, new CardComparator());
        assertEquals(cards.get(0).getRank(), 2);
        assertEquals(cards.get(1).getRank(), 8);
        assertEquals(cards.get(2).getRank(), 10);
        assertEquals(cards.get(3).getRank(), 12);

        cards = new ArrayList<Card>();
        cards.add(new Card(10, Card.Suit.SPADES));
        cards.add(new Card(10, Card.Suit.DIAMONDS));
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(10, Card.Suit.CLUBS));

        Collections.sort(cards, new CardComparator());
        assertEquals(cards.get(0).getSuit(), Card.Suit.CLUBS);
        assertEquals(cards.get(1).getSuit(), Card.Suit.DIAMONDS);
        assertEquals(cards.get(2).getSuit(), Card.Suit.HEARTS);
        assertEquals(cards.get(3).getSuit(), Card.Suit.SPADES);

        cards = new ArrayList<Card>();
        cards.add(new Card(5, Card.Suit.CLUBS));
        cards.add(new Card(1, Card.Suit.DIAMONDS));
        cards.add(new Card(1, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.SPADES));

        Collections.sort(cards, new CardComparator());
        assertEquals(cards.get(0).getRank(), 1);
        assertEquals(cards.get(0).getSuit(), Card.Suit.DIAMONDS);
        assertEquals(cards.get(1).getRank(), 1);
        assertEquals(cards.get(1).getSuit(), Card.Suit.HEARTS);
        assertEquals(cards.get(2).getRank(), 3);
        assertEquals(cards.get(2).getSuit(), Card.Suit.SPADES);
        assertEquals(cards.get(3).getRank(), 5);
        assertEquals(cards.get(3).getSuit(), Card.Suit.CLUBS);

    }
}
