package nl.joerivandervelde.kalashnikov.logic;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.cards.CardComparator;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class representing the shelf. Cards put in by a player are known only by
 * that player but are unknown to other players. This implementation works for
 * any number of players.
 */
public class Shelf extends HashMap<Integer, List<Card>> {

    /**
     * Constructor. Pre-fills the map with a key entry and empty list for each
     * player.
     *
     * @param nrOfPlayers
     */
    public Shelf(int nrOfPlayers) {
        super();
        for (int i = 1; i <= nrOfPlayers; i++) {
            this.put(i, new ArrayList<>());
        }
    }

    /**
     * Check if shelf is totally empty or not.
     *
     * @return
     */
    public boolean isEmpty() {
        for (int player : this.keySet()) {
            if (!this.get(player).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Draw the oldest card put in by player. When using this method it is
     * assumed there are no other options but to draw a known duplicate,
     * although not the card that was most recently put in. It should
     * therefore only be used when there are at least two cards present in
     * player shelf.
     *
     * @param player
     * @return
     */
    public Card drawKnownDuplicateCard(int player) throws Exception {
        Card c = this.get(player).get(0);
        this.get(player).remove(0);
        return c;
    }

    /**
     * Draw a card that is NOT put in by the player, i.e. an unknown card.
     *
     * @param activePlayer
     * @return
     * @throws Exception
     */
    public Card drawUnknownCard(int activePlayer) throws Exception {
        for (int otherPlayer : this.keySet()) {
            if (otherPlayer != activePlayer &&
                this.get(otherPlayer).size() > 0) {
                return this.get(otherPlayer).get(0);
            }
        }
        throw new Exception("Failed to draw unknown card from shelf");
    }

    /**
     * Check whether unknown cards are available on the shelf to the player.
     *
     * @param activePlayer
     * @return
     */
    public boolean hasUnknownCard(int activePlayer) {
        for (int otherPlayer : this.keySet()) {
            if (otherPlayer != activePlayer &&
                this.get(otherPlayer).size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine which unique card ranks are currently shelved by this player.
     *
     * @param player
     * @return
     */
    public Set<Integer> getUniqueRanks(int player) {
        Set<Integer> ranksSeen = new HashSet<Integer>();
        for (Card c : this.get(player)) {
            ranksSeen.add(c.getRank());
        }
        return ranksSeen;
    }

    /**
     * Check whether the player has useful known cards (i.e. their own)
     * available on the shelf. Usefulness depends on the current hand.
     *
     * @param player
     * @param hand
     * @return
     */
    public boolean hasUsefulKnownCards(int player, Hand hand) {
        Set<Integer> uniqueRanksInHand = hand.getUniqueRanks();
        Set<Integer> uniqueRanksInShelf = this.getUniqueRanks(player);
        for (int i : uniqueRanksInShelf) {
            if (!uniqueRanksInHand.contains(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * From known player cards on shelf, get preferred option based on
     * current suits in hand and available suits in shelf. First, consider
     * which cards are useful. Then, get all cards matching needed parts from
     * shelf, count suits of useful cards in hand, prioritize useful cards
     * into candidates of equal rank, and finally sort the output and return.
     *
     * @param player
     * @param hand
     * @return
     */
    public Card getPreferredKnownCard(int player, Hand hand) throws Exception {
        if (hand.size() != 3) {
            throw new Exception("Expected to get a hand with 3 cards");
        }
        Set<Integer> uRanks = hand.getUniqueRanks();
        Set<Integer> needParts = new HashSet<Integer>();
        needParts.addAll(Arrays.asList(new Integer[]{3, 6, 12, 13}));
        needParts.removeAll(uRanks);
        List<Card> usefulCards = new ArrayList<Card>();
        for (Card shelfCard : this.get(player)) {
            if (needParts.contains(shelfCard.getRank())) {
                usefulCards.add(shelfCard);
            }
        }
        if (usefulCards.size() == 0) {
            throw new Exception("No useful cards in shelf");
        }
        Map<Card.Suit, AtomicInteger> suitCount =
            new HashMap<Card.Suit, AtomicInteger>();
        for (Card c : hand) {
            if (!hand.isUseless(c)) {
                if (!suitCount.containsKey(c.getSuit())) {
                    suitCount.put(c.getSuit(), new AtomicInteger());
                }
                suitCount.get(c.getSuit()).incrementAndGet();
            }
        }
        List<Card> candidateCards = new ArrayList<Card>();
        int highestSuitCount = -1;
        for (Card c : usefulCards) {
            int cardSuitCount = suitCount.get(c.getSuit()) != null ?
                suitCount.get(c.getSuit()).intValue() : 0;
            if (cardSuitCount > highestSuitCount) {
                candidateCards.clear();
                candidateCards.add(c);
                highestSuitCount = cardSuitCount;
            } else if (cardSuitCount == highestSuitCount) {
                candidateCards.add(c);
            }
        }
        Collections.sort(candidateCards, new CardComparator());
        return candidateCards.get(candidateCards.size() - 1);
    }

    /**
     * Remove a specific card from shelf, regardless who holds it.
     *
     * @param card
     */
    public void remove(Card card) {
        for (int player : this.keySet()) {
            if (this.get(player).contains(card)) {
                this.get(player).remove(card);
            }
        }
    }
}