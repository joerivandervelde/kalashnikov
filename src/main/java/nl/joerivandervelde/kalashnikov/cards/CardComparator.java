package nl.joerivandervelde.kalashnikov.cards;

import java.util.Comparator;
import java.util.HashMap;

/**
 * A card sorter based on both rank and suit. There are many instances where
 * choice of cards is arbitrary, but for testing purposes we need predictable
 * results. Sorting the cards allows us to easily test for expected outcomes.
 */
public class CardComparator implements Comparator<Card> {

    /**
     * Give each Card.Suit an arbitrary integer value that can be sorted on.
     * Here the value is based on alphabetical order.
     */
    private static final HashMap<Card.Suit, Integer> suitOrder =
        new HashMap<Card.Suit, Integer>() {{
            put(Card.Suit.CLUBS, 1);
            put(Card.Suit.DIAMONDS, 2);
            put(Card.Suit.HEARTS, 3);
            put(Card.Suit.SPADES, 4);
        }};

    /**
     * Comparator that first sorts on card rank and then on card suit. First,
     * sort ascending on card rank. If ranks are equal, sort ascending
     * alphabetically on suit name as defined by suitOrder.
     *
     * @param a
     * @param b
     * @return
     */
    public int compare(Card a, Card b) {
        if (a.getRank() != b.getRank()) {
            return a.getRank() - b.getRank();
        } else {
            return suitOrder.get(a.getSuit()) - suitOrder.get(b.getSuit());
        }
    }
}

