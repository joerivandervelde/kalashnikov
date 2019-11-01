package nl.joerivandervelde.kalashnikov.logic;

import nl.joerivandervelde.kalashnikov.Kalashnikov;
import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.players.Player;

import java.util.HashMap;
import java.util.Random;

/**
 * This class handles the shooting and damage calculations.
 */
public class Attack {

    // Local variables.
    private HashMap<Integer, Player> players;

    /**
     * Prepare an attack involving current players.
     *
     * @param players
     * @throws Exception
     */
    public Attack(HashMap<Integer, Player> players) throws Exception {
        this.players = players;
    }

    /**
     * Start shooting. Since each player with a gun can be either an attacker
     * or retaliating defender, it does not matter who initiated the attack.
     *
     * @param verbose
     * @throws Exception
     */
    public void shoot(Random rng, boolean verbose) throws Exception {
        for (int attacker : players.keySet()) {
            if (players.get(attacker).getHand().isGun()) {
                boolean attackerHasGoldenGun =
                    players.get(attacker).getHand().isGoldenGun();
                for (int victim : players.keySet()) {
                    if (attacker != victim) {
                        int dmg = 0;
                        if (attackerHasGoldenGun) {
                            dmg = Kalashnikov.GOLDEN_GUN_DMG;
                        } else {
                            Hand vs = players.get(victim).getHand();
                            dmg = getDamage(vs, vs.drawRandomAttackCard(rng));
                        }
                        players.get(victim).getHealth().addAndGet(-dmg);
                        if (players.get(victim).getHealth().intValue() < 0) {
                            players.get(victim).getHealth().set(0);
                        }
                        if (verbose) {
                            System.out.println(
                                "Player " + attacker +
                                    " shouts 'Калашников сука!' and shoots " +
                                    "player " + victim + " with a " +
                                    (attackerHasGoldenGun ? "golden gun" :
                                        "normal gun") + " for " + dmg +
                                    " damage.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Determine the damage done based on a card that was drawn, and the the
     * four cards of the hand it was drawn from. Damage is between 1-4
     * depending on card rank, for duplicate cards the damage is counted
     * towards the highest card. For more explanation see Life of Boris:
     * https://www.youtube.com/watch?v=IiRk-yGfAjc
     *
     * @param hand
     * @param drawnCard
     * @return
     * @throws Exception
     */
    private static int getDamage(Hand hand, Card drawnCard) throws Exception {
        int dmg = 4;
        int otherCards = 0;
        for (Card otherCard : hand) {
            if (!otherCard.equals(drawnCard)) {
                otherCards++;
                if (otherCard.getRank() > drawnCard.getRank()) {
                    dmg--;
                }
            }
        }
        if (otherCards != 3) {
            throw new Exception(
                "Bad state. Not considered exactly 3 other cards in attacking" +
                    ". Perhaps drawn card not in hand?");
        }
        return dmg;
    }
}
