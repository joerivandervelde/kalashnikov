package nl.joerivandervelde.kalashnikov.players;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.cards.CardComparator;
import nl.joerivandervelde.kalashnikov.cards.Deck;
import nl.joerivandervelde.kalashnikov.logic.Hand;
import nl.joerivandervelde.kalashnikov.logic.Shelf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static nl.joerivandervelde.kalashnikov.players.Decision.Action.*;

/**
 * Core game logic where decisions are made based on cards and AI settings.
 */
public class Consider {

    // Local variables.
    private Random rng;
    private HashMap<Integer, Player> players;
    private Shelf shelf;
    private Deck scrap;

    /**
     * Constructor which requires players, shelf and scrap.
     *
     * @param players
     * @param shelf
     * @param scrap
     * @param rng
     */
    public Consider(HashMap<Integer, Player> players, Shelf shelf, Deck scrap
        , Random rng) {
        this.players = players;
        this.shelf = shelf;
        this.scrap = scrap;
        this.rng = rng;
    }

    /**
     * Decide what to discard. The considerations, in order of priority, are:
     * (1) if player has only useless cards, get rid of one. (2) if player
     * has only duplicates, get rid of one, 'least suit' preferred. Note that
     * '1' may be bypassed with an AI setting. (3) we have gun, but decide to
     * wait and must shelf a gun card to pick it up later. (4) no decision
     * could be reached and error is thrown.
     *
     * @return
     */
    public Decision whatToDiscard(int player) throws Exception {
        Hand h = this.players.get(player).getHand();
        double prioUselessLikelihood =
            this.players.get(player).getAi()
                .getDiscardDuplicatesBeforeUselessCards();
        boolean prioUseless = rng.nextDouble() > prioUselessLikelihood;
        boolean hasUseless = h.hasUselessCard();
        boolean hasDuplicateOfAnyGunPart = h.hasDuplicateOfAnyGunPart();
        if (hasUseless && hasDuplicateOfAnyGunPart) {
            if (prioUseless) {
                Card uselessCard = h.getUselessCard();
                return new Decision(DISCARD, uselessCard);
            } else {
                Card dup = h.getLeastSuitDuplicateOfAnyGunPart();
                return new Decision(PUT_ON_SHELF, dup);
            }
        }
        if (hasUseless) {
            Card uselessCard = h.getUselessCard();
            return new Decision(DISCARD, uselessCard);
        }
        if (hasDuplicateOfAnyGunPart) {
            Card dup = h.getLeastSuitDuplicateOfAnyGunPart();
            return new Decision(PUT_ON_SHELF, dup);
        }
        if (h.isGun()) {
            Collections.sort(h, new CardComparator());
            return new Decision(PUT_ON_SHELF, h.get(h.size() - 1));
        } else {
            throw new Exception("Bad state. Decision could not be reached!");
        }
    }


    /**
     * Decide what to draw. The considerations, in order of priority, are:
     * (1) if no cards on shelf, but cards in scrap, take from scrap. (2) if
     * shelf has useful cards known to player, take the best one. (3) if shelf
     * has unknown cards, and drawing unknown cards before has not been too
     * unsuccessful, draw unknown shelf card. (4) if shelf is not good
     * option, take from scrap. Note that '3' may be bypassed with an AI
     * setting. (5) if scrap is empty and shelf contains cards that are not
     * useful, grab a different card from the one that was just shelved in
     * this turn, not great but game continues. (6) out of options: player
     * cannot draw from deck or shelf, except perhaps drawing the card that
     * was JUST put on shelf but if go down that alley, we probably reach
     * MAX_ROUNDS anyway. Game ends.
     *
     * @return
     * @throws Exception
     */
    public Decision whatToDraw(int player) throws Exception {
        if (shelf.isEmpty() && !scrap.empty()) {
            return new Decision(TAKE_FROM_SCRAP, scrap.takeCard());
        }
        if (shelf.hasUsefulKnownCards(player,
            this.players.get(player).getHand())) {
            return new Decision(TAKE_KNOWN_FROM_SHELF,
                shelf.getPreferredKnownCard(player,
                    players.get(player).getHand()));
        }
        boolean mayDrawUnknownCardFromShelf =
            shelf.hasUnknownCard(player) &&
                !(players.get(player).getConsecUselessUnknownShelfDraws()
                    .intValue() == players.get(player).getAi()
                    .getMaxConsecUselessUnknownShelfDraws());
        double pickFromScrapInsteadShelfLLH =
            this.players.get(player).getAi()
                .getChooseScrapOverUnknownShelf();
        boolean ignoreUnknownShelf =
            rng.nextDouble() < pickFromScrapInsteadShelfLLH;
        if (mayDrawUnknownCardFromShelf && ignoreUnknownShelf &&
            !scrap.empty()) {
            return new Decision(TAKE_FROM_SCRAP, scrap.takeCard());
        }
        if (mayDrawUnknownCardFromShelf) {
            return new Decision(TAKE_UNKNOWN_FROM_SHELF,
                shelf.drawUnknownCard(player));
        }
        if (!scrap.empty()) {
            return new Decision(TAKE_FROM_SCRAP, scrap.takeCard());
        }
        if (shelf.get(player).size() > 1) {
            return new Decision(TAKE_KNOWN_FROM_SHELF,
                shelf.drawKnownDuplicateCard(player));
        }
        return new Decision(GAME_ENDS);
    }

    /**
     * Decide whether it is time to attack, or not. If player has golden gun,
     * always attack.
     *
     * @param player
     * @return
     * @throws Exception
     */
    public Decision toAttackOrNot(int player) throws Exception {

        Hand hand = this.players.get(player).getHand();
        if (hand.isGun()) {
            if (hand.isGoldenGun()) {
                return new Decision(ATTACK);
            }
            double goldenStrat =
                this.players.get(player).getAi().getWaitForGoldenGun();
            boolean waitForGolden = rng.nextDouble() < goldenStrat;
            if (waitForGolden) {
                return new Decision(POSTPONE_ATTACK);
            } else {
                return new Decision(ATTACK);
            }
        }
        return new Decision(CANNOT_ATTACK);
    }

    /**
     * Count how many times an unknown shelf draw resulted in disappointment.
     * If this happens more than a maximum determined by AI, player refrains
     * from shelf draw.
     *
     * @param draw
     * @param player
     * @throws Exception
     */
    public void trackUselessShelfDraws(Decision draw, int player)
        throws Exception {
        Hand hand = this.players.get(player).getHand();
        if (draw.getAction() == TAKE_UNKNOWN_FROM_SHELF &&
            !hand.isUsefulAddition(draw.getCard())) {
            players.get(player).getConsecUselessUnknownShelfDraws()
                .getAndIncrement();
        } else if (draw.getAction() != TAKE_UNKNOWN_FROM_SHELF &&
            players.get(player).getConsecUselessUnknownShelfDraws().intValue() >
                0) {
            players.get(player).getConsecUselessUnknownShelfDraws().set(0);
        }
    }
}
