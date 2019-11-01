package nl.joerivandervelde.kalashnikov.cards;

import java.util.Objects;

/**
 * This class has been borrowed/adapted from:
 * <p>
 * https://gist.github.com/johnmastro/644409b2555e1029f50806c044392b7d
 * Class representing a playing card from a standard 52-card deck.
 * <p>
 * Credits & thanks to John Mastro.
 */
public class Card {
    /**
     * Enum representing playing card suits.
     */
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES;
    }

    // The min and max valid card ranks
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 13;

    // This instance's rank and suit
    private int rank;
    private Suit suit;

    /**
     * Construct a Card with a given rank and suit.
     */
    public Card(int rank, Suit suit) {
        setRank(rank);
        setSuit(suit);
    }

    /**
     * Convert rank to name. Not generic, works for 1-13
     */
    public String rankToName(int rank) {
        if (rank > 13 || rank < 1) {
            return ("Bad rank!");
        }
        switch (rank) {
            case 13:
                return "ACE";
            case 12:
                return "KING";
            case 11:
                return "QUEEN";
            case 10:
                return "JACK";
        }
        return rank + 1 + "";
    }

    /**
     * Return the card's rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Set the card's rank, with input validation.
     */
    public void setRank(int rank) {
        if (rank < MIN_RANK || rank > MAX_RANK) {
            throw new RuntimeException(
                String.format(
                    "Invalid rank: %d (must be between %d and %d inclusive)",
                    rank, MIN_RANK, MAX_RANK));
        }
        this.rank = rank;
    }

    /**
     * Return the card's suit.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Set the card's suit, with input validation.
     */
    public void setSuit(Suit suit) {
        if (suit == null) {
            throw new RuntimeException("Suit must be non-null");
        }
        this.suit = suit;
    }

    @Override
    public String toString() {
        return String
            .format("%s of %s", rankToName(getRank()), getSuit().name());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Card)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Card that = (Card) obj;
        return that.getRank() == getRank() && that.getSuit() == getSuit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRank(), getSuit());
    }

    /**
     * Return the minimum allowed rank.
     */
    public static int getMinRank() {
        return MIN_RANK;
    }

    /**
     * Return the maximum allowed rank.
     */
    public static int getMaxRank() {
        return MAX_RANK;
    }

    /**
     * Return an array of the available suits.
     */
    public static Suit[] getSuits() {
        return Suit.values();
    }
}

