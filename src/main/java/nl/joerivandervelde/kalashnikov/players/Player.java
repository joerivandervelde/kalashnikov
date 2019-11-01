package nl.joerivandervelde.kalashnikov.players;

import nl.joerivandervelde.kalashnikov.Kalashnikov;
import nl.joerivandervelde.kalashnikov.logic.Hand;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A player is a participant in a series of games that starts with a number
 * of health points and an AI instance. During games, the player hand (the
 * cards which the player is holding) is stored as well as so-called
 * useless shelf draws which may accumulate to a certain maximum.
 */
public class Player {

    // Local variables.
    private AI ai;
    private AtomicInteger health;
    private Hand hand;
    private AtomicInteger consecUselessUnknownShelfDraws;

    /**
     * Constructor for default AI and health.
     */
    public Player() {
        this.ai = new AI();
        this.health = new AtomicInteger(Kalashnikov.PLAYER_HEALTH);
        this.consecUselessUnknownShelfDraws = new AtomicInteger(0);
    }

    /**
     * Constructor for custom AI, default health.
     * @param ai
     */
    public Player(AI ai) {
        this.ai = ai;
        this.health = new AtomicInteger(Kalashnikov.PLAYER_HEALTH);
        this.consecUselessUnknownShelfDraws = new AtomicInteger(0);
    }

    /**
     * Constructor for custom AI and health.
     * @param ai
     * @param health
     */
    public Player(AI ai, AtomicInteger health) {
        this.ai = ai;
        this.health = health;
        this.consecUselessUnknownShelfDraws = new AtomicInteger(0);
    }

    /**
     * Get the number of consecutive draws of unknown cards from shelf that
     * turned out to be useless to the player.
     *
     * @return
     * @throws Exception
     */
    public AtomicInteger getConsecUselessUnknownShelfDraws() throws Exception {
        if (consecUselessUnknownShelfDraws.intValue() >
            this.ai.getMaxConsecUselessUnknownShelfDraws()) {
            throw new Exception(
                "Bad state. consecUselessUnknownShelfDraws exceeds this.ai" +
                    ".getMaxConsecUselessUnknownShelfDraws.");
        }
        return consecUselessUnknownShelfDraws;
    }

    /**
     * Getter for AI.
     *
     * @return
     */
    public AI getAi() {
        return ai;
    }

    /**
     * Setter for AI.
     *
     * @param ai
     */
    public void setAi(AI ai) {
        this.ai = ai;
    }

    /**
     * Getter for hand.
     *
     * @return
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Setter for hand.
     *
     * @param hand
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * Getter for health.
     *
     * @return
     */
    public AtomicInteger getHealth() {
        return health;
    }

    /**
     * Check if player is alive.
     *
     * @return
     */
    public boolean isAlive() {
        return getHealth().intValue() > 0;
    }
}
