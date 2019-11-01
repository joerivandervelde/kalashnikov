package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.logic.Hand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TestHand {


    @Test
    void duplicateRanks() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(1, Card.Suit.SPADES));
        hand.add(new Card(2, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(4, Card.Suit.SPADES));
        assertEquals(new HashSet<>(), hand.getDuplicateRanks());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertEquals(new HashSet<>(Arrays.asList(6)), hand.getDuplicateRanks());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        assertEquals(new HashSet<>(Arrays.asList(13)),
            hand.getDuplicateRanks());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertEquals(new HashSet<>(Arrays.asList(6, 13)),
            hand.getDuplicateRanks());
    }

    @Test
    void uniqueRanks() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertEquals(new HashSet<>(Arrays.asList(13, 12, 3, 6)),
            hand.getUniqueRanks());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.DIAMONDS));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.CLUBS));
        hand.add(new Card(13, Card.Suit.HEARTS));
        assertEquals(new HashSet<>(Arrays.asList(13)), hand.getUniqueRanks());
    }

    @Test
    void hasAK47duplicates() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertFalse(hand.hasDuplicateOfAnyGunPart());

        hand = new Hand();
        hand.add(new Card(8, Card.Suit.SPADES));
        hand.add(new Card(8, Card.Suit.SPADES));
        hand.add(new Card(9, Card.Suit.SPADES));
        hand.add(new Card(9, Card.Suit.SPADES));
        assertFalse(hand.hasDuplicateOfAnyGunPart());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.DIAMONDS));
        hand.add(new Card(12, Card.Suit.DIAMONDS));
        hand.add(new Card(12, Card.Suit.DIAMONDS));
        hand.add(new Card(6, Card.Suit.DIAMONDS));
        assertTrue(hand.hasDuplicateOfAnyGunPart());

        hand = new Hand();
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertTrue(hand.hasDuplicateOfAnyGunPart());
    }

    @Test
    void hasTrash() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(13, Card.Suit.CLUBS));
        hand.add(new Card(12, Card.Suit.CLUBS));
        hand.add(new Card(3, Card.Suit.CLUBS));
        hand.add(new Card(6, Card.Suit.CLUBS));
        assertEquals(false, hand.hasUselessCard());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(11, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertEquals(true, hand.hasUselessCard());

        hand = new Hand();
        hand.add(new Card(1, Card.Suit.SPADES));
        hand.add(new Card(2, Card.Suit.SPADES));
        hand.add(new Card(4, Card.Suit.SPADES));
        hand.add(new Card(5, Card.Suit.SPADES));
        assertEquals(true, hand.hasUselessCard());
    }

    @Test
    void isTrash() throws Exception {
        Hand hand = new Hand();
        assertTrue(hand.isUseless(new Card(1, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(2, Card.Suit.SPADES)));
        assertFalse(hand.isUseless(new Card(3, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(4, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(5, Card.Suit.SPADES)));
        assertFalse(hand.isUseless(new Card(6, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(7, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(8, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(9, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(10, Card.Suit.SPADES)));
        assertTrue(hand.isUseless(new Card(11, Card.Suit.SPADES)));
        assertFalse(hand.isUseless(new Card(12, Card.Suit.SPADES)));
        assertFalse(hand.isUseless(new Card(13, Card.Suit.SPADES)));
    }


    @Test
    void getTrash() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(12, Card.Suit.HEARTS));
        Assertions.assertThrows(Exception.class, hand::getUselessCard);
        hand.add(new Card(5, Card.Suit.HEARTS));
        assertEquals(new Card(5, Card.Suit.HEARTS), hand.getUselessCard());
        hand.add(new Card(6, Card.Suit.HEARTS));
        assertEquals(new Card(5, Card.Suit.HEARTS), hand.getUselessCard());
        hand.add(new Card(7, Card.Suit.HEARTS));
        assertEquals(new Card(7, Card.Suit.HEARTS), hand.getUselessCard());
        hand.add(new Card(7, Card.Suit.SPADES));
        assertEquals(new Card(7, Card.Suit.SPADES), hand.getUselessCard());
    }

    @Test
    void getLeastSuitAK47duplicate() throws Exception {

        //TODO:
        // Exception in thread "main" java.lang.Exception: Expected 2 or 4
        // cards in duplicate gun parts, instead found: [ACE of SPADES, ACE
        // of CLUBS, ACE of HEARTS]. Perhaps identical cards?

        // case: no duplicates (exception)
        final Hand hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        Assertions.assertThrows(Exception.class,
            hand::getLeastSuitDuplicateOfAnyGunPart);

        // case: invalid hand (exception)
        hand.clear();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        Assertions.assertThrows(Exception.class,
            hand::getLeastSuitDuplicateOfAnyGunPart);

        // case: 1 duplicate pair with clear winner
        hand.clear();
        hand.add(new Card(13, Card.Suit.HEARTS));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        assertEquals(new Card(13, Card.Suit.HEARTS),
            hand.getLeastSuitDuplicateOfAnyGunPart());

        // case: 1 duplicate pair with slight winner
        hand.clear();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.HEARTS));
        hand.add(new Card(3, Card.Suit.CLUBS));
        assertEquals(new Card(13, Card.Suit.HEARTS),
            hand.getLeastSuitDuplicateOfAnyGunPart());

        // case: 1 duplicate pair with no winner
        // sorted by CardComparator, 13 of SPADES is highest
        hand.clear();
        hand.add(new Card(13, Card.Suit.HEARTS));
        hand.add(new Card(12, Card.Suit.HEARTS));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        assertEquals(new Card(13, Card.Suit.SPADES),
            hand.getLeastSuitDuplicateOfAnyGunPart());

        // case: 2 duplicate pairs with no winner amongst 2 cards
        // sorted by CardComparator, 13 of CLUBS is highest
        hand.clear();
        hand.add(new Card(12, Card.Suit.DIAMONDS));
        hand.add(new Card(12, Card.Suit.HEARTS));
        hand.add(new Card(13, Card.Suit.CLUBS));
        hand.add(new Card(13, Card.Suit.DIAMONDS));
        assertEquals(new Card(13, Card.Suit.CLUBS),
            hand.getLeastSuitDuplicateOfAnyGunPart());

        // case: triplicate pairs with no winner
        // sorted by CardComparator, 6 of HEARTS is highest
        hand.clear();
        hand.add(new Card(6, Card.Suit.DIAMONDS));
        hand.add(new Card(6, Card.Suit.HEARTS));
        hand.add(new Card(6, Card.Suit.CLUBS));
        hand.add(new Card(3, Card.Suit.DIAMONDS));
        assertEquals(new Card(6, Card.Suit.HEARTS),
            hand.getLeastSuitDuplicateOfAnyGunPart());

        // case: 2 duplicate pairs with no winner amongst 4 cards
        // sorted by CardComparator, 13 of CLUBS is highest
        hand.clear();
        hand.add(new Card(12, Card.Suit.HEARTS));
        hand.add(new Card(13, Card.Suit.DIAMONDS));
        hand.add(new Card(12, Card.Suit.DIAMONDS));
        hand.add(new Card(13, Card.Suit.HEARTS));
        assertEquals(new Card(13, Card.Suit.HEARTS),
            hand.getLeastSuitDuplicateOfAnyGunPart());

        // case: all different suits with no winner amongst 2 cards
        // sorted by CardComparator, 13 of HEARTS is highest
        hand.clear();
        hand.add(new Card(5, Card.Suit.DIAMONDS));
        hand.add(new Card(13, Card.Suit.HEARTS));
        hand.add(new Card(13, Card.Suit.CLUBS));
        hand.add(new Card(5, Card.Suit.SPADES));
        assertEquals(new Card(13, Card.Suit.HEARTS),
            hand.getLeastSuitDuplicateOfAnyGunPart());

    }

    @Test
    void drawRandomCard() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(1, Card.Suit.DIAMONDS));
        hand.add(new Card(2, Card.Suit.DIAMONDS));
        hand.add(new Card(3, Card.Suit.DIAMONDS));
        hand.add(new Card(4, Card.Suit.DIAMONDS));
        Card random = hand.drawRandomAttackCard(new Random());
        assertTrue(hand.contains(random));
    }

//    @Test
//    void needsCard() {
//        Hand hand = new Hand();
//        hand.add(new Card(1, Card.Suit.DIAMONDS));
//        hand.add(new Card(2, Card.Suit.DIAMONDS));
//        hand.add(new Card(3, Card.Suit.DIAMONDS));
//        assertTrue(hand.needsCard());
//
//        hand = new Hand();
//        hand.add(new Card(1, Card.Suit.DIAMONDS));
//        hand.add(new Card(2, Card.Suit.DIAMONDS));
//        hand.add(new Card(3, Card.Suit.DIAMONDS));
//        hand.add(new Card(4, Card.Suit.DIAMONDS));
//        assertFalse(hand.needsCard());
//
//    }

    @Test
    void checkState() {
        Hand hand = new Hand();
        hand.add(new Card(1, Card.Suit.HEARTS));
        hand.add(new Card(2, Card.Suit.HEARTS));
        hand.add(new Card(3, Card.Suit.HEARTS));
        Assertions.assertThrows(Exception.class, hand::checkState);

        hand.add(new Card(4, Card.Suit.HEARTS));
        Assertions.assertDoesNotThrow(hand::checkState);

        hand.add(new Card(5, Card.Suit.HEARTS));
        Assertions.assertThrows(Exception.class, hand::checkState);

    }

    @Test
    void isAK47() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertTrue(hand.isGun());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.HEARTS));
        hand.add(new Card(12, Card.Suit.CLUBS));
        hand.add(new Card(3, Card.Suit.DIAMONDS));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertTrue(hand.isGun());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(7, Card.Suit.SPADES));
        assertFalse(hand.isGun());
    }

    @Test
    void isUseful() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        assertFalse(hand.isUsefulAddition(new Card(13, Card.Suit.HEARTS)));

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertTrue(hand.isUsefulAddition(new Card(3, Card.Suit.SPADES)));
    }


    @Test
    void isGoldenAK47() throws Exception {
        Hand hand = new Hand();
        hand.add(new Card(12, Card.Suit.SPADES));
        hand.add(new Card(13, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(6, Card.Suit.SPADES));
        assertTrue(hand.isGoldenGun());

        hand = new Hand();
        hand.add(new Card(13, Card.Suit.HEARTS));
        hand.add(new Card(12, Card.Suit.CLUBS));
        hand.add(new Card(6, Card.Suit.HEARTS));
        hand.add(new Card(3, Card.Suit.HEARTS));
        assertFalse(hand.isGoldenGun());
    }
}