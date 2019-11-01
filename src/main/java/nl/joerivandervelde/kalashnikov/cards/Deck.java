package nl.joerivandervelde.kalashnikov.cards;

import java.util.*;

/**
 * This class has been borrowed/adapted from:
 * <p>
 * https://gist.github.com/johnmastro/644409b2555e1029f50806c044392b7d
 * Class representing a deck of cards.
 * <p>
 * Credits & thanks to John Mastro.
 */
public class Deck implements Iterable<Card> {
    private final Card[] cards;
    public int top;

    /**
     * Construct a deck. The cards will start out in an unspecified but
     * deterministic order - you must call shuffle() yourself.
     */
    public Deck() {
        cards = new Card[Card.getSuits().length *
            (Card.getMaxRank() - Card.getMinRank() + 1)];
        refresh();
    }

    /**
     * Returns how many cards are left in the deck
     *
     * @return
     */
    public int getTop() {
        return top;
    }

    /**
     * Repopulate the deck with a full set of cards.
     */
    public void refresh() {
        Card.Suit[] suits = Card.getSuits();
        int min_rank = Card.getMinRank();
        int max_rank = Card.getMaxRank();
        int i = 0;
        for (Card.Suit suit : suits)
            for (int rank = min_rank; rank <= max_rank; rank++)
                cards[i++] = new Card(rank, suit);

        top = cards.length - 1;
        assert cards[top] != null;
    }

    /**
     * Shuffle the deck, leaving the cards in a random order.
     */
    public void shuffle() {
        Random rng = new Random();
        for (int i = cards.length - 1; i > 0; i--) {
            // Swap the i-th card with a random one
            int j = rng.nextInt(i + 1);
            Card tmp = cards[j];
            cards[j] = cards[i];
            cards[i] = tmp;
        }
    }

    /**
     * Return true if the deck is empty.
     */
    public boolean empty() {
        return top < 0;
    }

    /**
     * Take a card from the deck and return it.
     */
    public Card takeCard() {
        if (empty()) {
            throw new IllegalStateException("Can't deal from an empty deck.");
        }
        return cards[top--];
    }

    /**
     * Take multiple cards from the deck and return them.
     */
    public List<Card> takeCards(int amount) {
        List<Card> res = new ArrayList<Card>(amount);
        for (int i = 0; i < amount; i++) {
            Card c = takeCard();
            res.add(c);
        }
        return res;
    }

    /**
     * Print the current state of the deck.
     */
    public void print() {
        if (empty()) {
            System.out.println("The deck is empty.");
            return;
        }

        System.out.println("The current deck:");
        for (Card card : this)
            System.out.println("  " + card);
    }

    /**
     * Return an iterator of the deck's cards.
     * <p>
     * The behavior is unspecified if you modify the deck (including taking a
     * card) during the lifetime of an iterator.
     */
    public Iterator<Card> iterator() {
        return new Iterator<Card>() {
            private int cursor = top;

            public boolean hasNext() {
                return cursor >= 0;
            }

            public Card next() {
                if (hasNext()) {
                    return cards[cursor--];
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}