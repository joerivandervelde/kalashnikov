package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.cards.Deck;
import nl.joerivandervelde.kalashnikov.logic.Hand;
import nl.joerivandervelde.kalashnikov.logic.Shelf;
import nl.joerivandervelde.kalashnikov.players.AI;
import nl.joerivandervelde.kalashnikov.players.Consider;
import nl.joerivandervelde.kalashnikov.players.Decision;
import nl.joerivandervelde.kalashnikov.players.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestConsider {

    static AI ai;
    static int startHealth;
    static Player p1;
    static Player p2;
    static Hand p1hand;
    static HashMap<Integer, Player> players;
    static Shelf shelf;
    static Deck deck;
    static Consider consider;

    @BeforeAll
    static void setup() throws Exception {
        ai = new AI();
        p1hand = new Hand();
        startHealth = 20;
        p1 = new Player(ai, new AtomicInteger(startHealth));
        p2 = new Player(ai, new AtomicInteger(startHealth));
        p1.setHand(p1hand);
        players = new HashMap<Integer, Player>();
        players.put(1, p1);
        players.put(2, p2);
        deck = new Deck();
        shelf = new Shelf(players.size());
        consider = new Consider(players, shelf, deck, new Random());
    }

    @Test
    void considerHand() throws Exception {

        // get rid of trash before duplicates
        p1hand.add(new Card(1, Card.Suit.HEARTS));
        p1hand.add(new Card(2, Card.Suit.CLUBS));
        p1hand.add(new Card(12, Card.Suit.SPADES));
        p1hand.add(new Card(12, Card.Suit.DIAMONDS));

        // AI priorize getting rid of trash first
        p1.setAi(new AI(0.0, 0.0, 0.0, 2));
        Decision decision = consider.whatToDiscard(1);
        assertEquals(
            new Decision(Decision.Action.DISCARD, new Card(2, Card.Suit.CLUBS)),
            decision);

        // AI priorize getting rid of duplicates first
        p1.setAi(new AI(1.0, 0.0, 0.0, 2));
        decision = consider.whatToDiscard(1);
        assertEquals(new Decision(Decision.Action.PUT_ON_SHELF,
            new Card(12, Card.Suit.SPADES)), decision);

        p1hand.clear();
        p1hand.add(new Card(3, Card.Suit.HEARTS));
        p1hand.add(new Card(6, Card.Suit.CLUBS));
        p1hand.add(new Card(12, Card.Suit.SPADES));
        p1hand.add(new Card(13, Card.Suit.DIAMONDS));

        // this AI does not go for golden gun and always attacks when normal
        // gun is complete
        p1.setAi(new AI(0.0, 0.0, 0.0, 2));
        decision = consider.toAttackOrNot(1);
        assertEquals(new Decision(Decision.Action.ATTACK), decision);

        // this AI always waits for golden gun
        p1.setAi(new AI(0.0, 1.0, 0.0, 2));
        decision = consider.toAttackOrNot(1);
        assertEquals(new Decision(Decision.Action.POSTPONE_ATTACK), decision);

        // TODO: test bypass shelf


    }

}
