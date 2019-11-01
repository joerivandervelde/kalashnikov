package nl.joerivandervelde.kalashnikov.logic;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.cards.CardComparator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.joerivandervelde.kalashnikov.Kalashnikov.*;

/**
 * A player holds four cards at all times except when a card has just been
 * discard or shelved and a new card must still be drawn. This class
 * represents a player hand and is an extension of ArrayList with helper
 * functions added.
 */
public class Hand extends ArrayList<Card> {


    /**
     * Constructor.
     */
    public Hand() {
        super();
    }

    /**
     * Get any ranks in this hand of which there are two or more cards.
     *
     * @return
     */
    public Set<Integer> getDuplicateRanks() {
        Set<Integer> ranksSeen = new HashSet<Integer>();
        Set<Integer> dups = new HashSet<Integer>();
        for (Card c : this) {
            if (ranksSeen.contains(c.getRank())) {
                dups.add(c.getRank());
            } else {
                ranksSeen.add(c.getRank());
            }
        }
        return dups;
    }

    /**
     * Instead of just get duplicate ranks, this function finds actual
     * duplicate card pairs and return all those cards.
     *
     * @return
     */
    public Set<Card> getDuplicateGunParts() {
        Set<Integer> ranksSeen = new HashSet<Integer>();
        Set<Integer> dupRanks = new HashSet<Integer>();
        Set<Card> dupCards = new HashSet<Card>();
        for (Card c : this) {
            if (c.getRank() == GP1 || c.getRank() == GP2 ||
                c.getRank() == GP3 ||
                c.getRank() == GP4) {
                if (ranksSeen.contains(c.getRank())) {
                    dupRanks.add(c.getRank());
                } else {
                    ranksSeen.add(c.getRank());
                }
            }
        }
        for (Card c : this) {
            if (dupRanks.contains(c.getRank())) {
                dupCards.add(c);
            }
        }
        return dupCards;
    }

    /**
     * Get all unique ranks currently in this hand.
     *
     * @return
     */
    public Set<Integer> getUniqueRanks() {
        Set<Integer> ranksSeen = new HashSet<Integer>();
        for (Card c : this) {
            ranksSeen.add(c.getRank());
        }
        return ranksSeen;
    }

    /**
     * Check whether there are duplicate cards of any gun parts.
     *
     * @return
     */
    public boolean hasDuplicateOfAnyGunPart() {
        Set<Integer> d = getDuplicateRanks();
        if (d.contains(GP1) || d.contains(GP2) || d.contains(GP3) ||
            d.contains(GP4)) {
            return true;
        }

        return false;
    }

    /**
     * Check if hand has at least one useless card.
     *
     * @return
     */
    public boolean hasUselessCard() {
        for (Card c : this) {
            if (isUseless(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether a card is useless, i.e. not a gun part.
     *
     * @param card
     * @return
     */
    public boolean isUseless(Card card) {
        return (card.getRank() != GP1 && card.getRank() != GP2 &&
            card.getRank() != GP3 && card.getRank() != GP4);
    }

    /**
     * Returns one useless card.
     *
     * @return
     * @throws Exception
     */
    public Card getUselessCard() throws Exception {
        List<Card> useless = new ArrayList<Card>();
        for (Card card : this) {
            if (isUseless(card)) {
                useless.add(card);
            }
        }
        if (useless.size() == 0) {
            throw new Exception(
                "There is no useless card!");
        }
        Collections.sort(useless, new CardComparator());
        return useless.get(useless.size() - 1);
    }

    /**
     * Get rid of duplicate gun part of which there are the least suits of
     * other usable gun parts in hand. This increases chances of getting
     * golden gun. What happens: get all duplicate cards, count all suits in
     * hand, determine least suit, using least suit count, pick card from
     * duplicates, sort results and return top card.
     *
     * @return
     * @throws Exception
     */
    public Card getLeastSuitDuplicateOfAnyGunPart() throws Exception {
        if (!hasDuplicateOfAnyGunPart()) {
            throw new Exception("No duplicates found");
        }
        Set<Card> dupGunParts = this.getDuplicateGunParts();
        if (!(dupGunParts.size() == 2 || dupGunParts.size() == 3 ||
            dupGunParts.size() == 4)) {
            throw new Exception(
                "Expected 2, 3 or 4 cards in duplicate gun parts, instead " +
                    "found: " + dupGunParts.toString() +
                    ". Perhaps identical cards?");
        }
        Map<Card.Suit, AtomicInteger> suitCount =
            new HashMap<Card.Suit, AtomicInteger>();
        for (Card card : this) {
            if (!suitCount.containsKey(card.getSuit())) {
                suitCount.put(card.getSuit(), new AtomicInteger());
            }
            suitCount.get(card.getSuit()).incrementAndGet();
        }
        int leastSuitCount = 5;
        for (Card.Suit suit : suitCount.keySet()) {
            if (suitCount.get(suit).intValue() < leastSuitCount) {
                leastSuitCount = suitCount.get(suit).intValue();
            }
        }
        List<Card> res = new ArrayList<Card>();
        for (Card dupCard : dupGunParts) {
            if (suitCount.get(dupCard.getSuit()).intValue() <= leastSuitCount) {
                res.add(dupCard);
            }
        }
        Collections.sort(res, new CardComparator());
        return res.get(res.size() - 1);
    }

    /**
     * Draw one of the four cards randomly as part of an attack.
     *
     * @param rng
     * @return
     * @throws Exception
     */
    public Card drawRandomAttackCard(Random rng) throws Exception {
        checkState();
        int randomIndex = rng.nextInt(4);
        return this.get(randomIndex);
    }

    /**
     * Sanity check if there are indeed exactly four cards currently in hand.
     *
     * @throws Exception
     */
    public void checkState() throws Exception {
        if (this.size() != 4) {
            throw new Exception(
                "Bad state. Expected to have exactly 4 cards, but found " +
                    this.size() + ".");
        }
    }

    /**
     * Check if the current hand is a gun. If there are no useless cards and no
     * duplicates, it means A-K-4-7 are present.
     *
     * @return
     */
    public boolean isGun() throws Exception {
        checkState();
        return !this.hasUselessCard() && !this.hasDuplicateOfAnyGunPart();
    }

    /**
     * Check whether current hand is a golden gun.
     *
     * @return
     * @throws Exception
     */
    public boolean isGoldenGun() throws Exception {
        if (this.isGun()) {
            Set<Card.Suit> uniqSuits = new HashSet<Card.Suit>();
            for (Card c : this) {
                uniqSuits.add(c.getSuit());
            }
            if (uniqSuits.size() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the presented card is a useful addition to this hand.
     *
     * @param card
     * @return
     */
    public boolean isUsefulAddition(Card card) throws Exception {
        if (this.size() != 3) {
            throw new Exception("Expected to find 3 cards in hand");
        }
        return !this.getUniqueRanks().contains(card.getRank());
    }
}

