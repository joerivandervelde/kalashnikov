package nl.joerivandervelde.kalashnikov.players;

import nl.joerivandervelde.kalashnikov.cards.Card;

import java.util.Objects;

/**
 * Data structures for expressing and comparing player decisions.
 */
public class Decision {

    /**
     * Different actions that players can decide to take.
     */
    public enum Action {
        DISCARD,
        PUT_ON_SHELF,
        TAKE_FROM_SCRAP,
        TAKE_KNOWN_FROM_SHELF,
        TAKE_UNKNOWN_FROM_SHELF,
        ATTACK,
        CANNOT_ATTACK,
        POSTPONE_ATTACK,
        GAME_ENDS
    }

    // Local variables.
    private Card card;
    private Action action;

    /**
     * Constructor with Action and Card.
     *
     * @param a
     * @param c
     */
    public Decision(Action a, Card c) {
        this.action = a;
        this.card = c;
    }

    /**
     * Constructor with only Action.
     *
     * @param a
     */
    public Decision(Action a) {
        this.action = a;
    }

    /**
     * Getter for card.
     * @return
     */
    public Card getCard() {
        return card;
    }

    /**
     * Getter for action.
     * @return
     */
    public Action getAction() {
        return action;
    }

    /**
     * Override toString.
     *
     * @return
     */
    @Override
    public String toString() {
        return action + (card != null ? " the " + card : "");
    }

    /**
     * Override equals that needs to take a few things into account.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Decision)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Decision that = (Decision) obj;
        if (this.getCard() != null && that.getCard() != null) {
            return that.getCard().getRank() == getCard().getRank() &&
                that.getCard().getSuit() == getCard().getSuit() &&
                that.getAction() == getAction();
        } else if (this.getCard() == null && that.getCard() == null) {
            return that.getAction() == getAction();
        } else {
            return false;
        }
    }

    /**
     * Override hashCode.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects
            .hash(getCard().getRank(), getCard().getSuit(), getAction());
    }

}
