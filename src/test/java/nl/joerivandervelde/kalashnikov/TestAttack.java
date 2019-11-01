package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.logic.Attack;
import nl.joerivandervelde.kalashnikov.logic.Hand;
import nl.joerivandervelde.kalashnikov.players.AI;
import nl.joerivandervelde.kalashnikov.players.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TestAttack {

    @Test
    void getDamage() throws Exception {

        // use reflection to access private method
        Method method =
            Attack.class.getDeclaredMethod("getDamage", Hand.class, Card.class);
        method.setAccessible(true);

        // case: all different ranks
        Hand hand = new Hand();
        hand.add(new Card(1, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.HEARTS));
        hand.add(new Card(5, Card.Suit.DIAMONDS));
        hand.add(new Card(8, Card.Suit.CLUBS));

        int dmg =
            (int) method.invoke(null, hand, new Card(1, Card.Suit.SPADES));
        assertEquals(1, dmg);

        dmg = (int) method.invoke(null, hand, new Card(3, Card.Suit.HEARTS));
        assertEquals(2, dmg);

        dmg = (int) method.invoke(null, hand, new Card(5, Card.Suit.DIAMONDS));
        assertEquals(3, dmg);

        dmg = (int) method.invoke(null, hand, new Card(8, Card.Suit.CLUBS));
        assertEquals(4, dmg);

        // case: all the same ranks
        hand = new Hand();
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(3, Card.Suit.HEARTS));
        hand.add(new Card(3, Card.Suit.DIAMONDS));
        hand.add(new Card(3, Card.Suit.CLUBS));

        dmg = (int) method.invoke(null, hand, new Card(3, Card.Suit.SPADES));
        assertEquals(4, dmg);

        dmg = (int) method.invoke(null, hand, new Card(3, Card.Suit.HEARTS));
        assertEquals(4, dmg);

        dmg = (int) method.invoke(null, hand, new Card(3, Card.Suit.DIAMONDS));
        assertEquals(4, dmg);

        dmg = (int) method.invoke(null, hand, new Card(3, Card.Suit.CLUBS));
        assertEquals(4, dmg);

        // case: same ranks in middle
        hand = new Hand();
        hand.add(new Card(3, Card.Suit.SPADES));
        hand.add(new Card(5, Card.Suit.HEARTS));
        hand.add(new Card(5, Card.Suit.DIAMONDS));
        hand.add(new Card(8, Card.Suit.CLUBS));

        dmg = (int) method.invoke(null, hand, new Card(3, Card.Suit.SPADES));
        assertEquals(1, dmg);

        dmg = (int) method.invoke(null, hand, new Card(5, Card.Suit.HEARTS));
        assertEquals(3, dmg);

        dmg = (int) method.invoke(null, hand, new Card(5, Card.Suit.DIAMONDS));
        assertEquals(3, dmg);

        dmg = (int) method.invoke(null, hand, new Card(8, Card.Suit.CLUBS));
        assertEquals(4, dmg);

        // case: same ranks high and low
        hand = new Hand();
        hand.add(new Card(7, Card.Suit.SPADES));
        hand.add(new Card(7, Card.Suit.HEARTS));
        hand.add(new Card(8, Card.Suit.DIAMONDS));
        hand.add(new Card(8, Card.Suit.HEARTS));

        dmg = (int) method.invoke(null, hand, new Card(7, Card.Suit.SPADES));
        assertEquals(2, dmg);

        dmg = (int) method.invoke(null, hand, new Card(7, Card.Suit.HEARTS));
        assertEquals(2, dmg);

        dmg = (int) method.invoke(null, hand, new Card(8, Card.Suit.DIAMONDS));
        assertEquals(4, dmg);

        dmg = (int) method.invoke(null, hand, new Card(8, Card.Suit.HEARTS));
        assertEquals(4, dmg);
    }


    static AI ai;
    static int startHealth;
    static Player p1;
    static Player p2;
    static Hand p1hand;
    static Hand p2hand;
    static HashMap<Integer, Player> players;

    @BeforeAll
    static void setup() throws Exception {
        p1hand = new Hand();
        startHealth = 20;
        p1 = new Player(new AI(), new AtomicInteger(startHealth));
        p2 = new Player(new AI(), new AtomicInteger(startHealth));
        players = new HashMap<Integer, Player>();
        players.put(1, p1);
        players.put(2, p2);
    }

    @BeforeEach
    void refresh() {
        p1hand = new Hand();
        p2hand = new Hand();
        p1.setHand(p1hand);
        p2.setHand(p2hand);
        for (int player : players.keySet()) {
            players.get(player).getHealth().set(startHealth);
        }
    }

    @Test
    void twoPlayersOneNormalGun() throws Exception {
        p1hand.add(new Card(3, Card.Suit.HEARTS));
        p1hand.add(new Card(6, Card.Suit.CLUBS));
        p1hand.add(new Card(12, Card.Suit.SPADES));
        p1hand.add(new Card(13, Card.Suit.DIAMONDS));
        assertTrue(p1hand.isGun());
        assertFalse(p1hand.isGoldenGun());

        p2hand.add(new Card(1, Card.Suit.HEARTS));
        p2hand.add(new Card(2, Card.Suit.CLUBS));
        p2hand.add(new Card(3, Card.Suit.SPADES));
        p2hand.add(new Card(4, Card.Suit.DIAMONDS));
        assertFalse(p2hand.isGun());
        assertFalse(p2hand.isGoldenGun());

        Attack attack = new Attack(players);
        attack.shoot(new Random(), false);

        // p2 has lost 1-4 health while p1 has not
        assertEquals(startHealth, p1.getHealth().intValue());
        assertTrue(startHealth - 1 >= p2.getHealth().intValue());
        assertTrue(startHealth - 4 <= p2.getHealth().intValue());
    }


    @Test
    void twoPlayersTwoNormalGuns() throws Exception {
        p1hand.add(new Card(3, Card.Suit.DIAMONDS));
        p1hand.add(new Card(6, Card.Suit.HEARTS));
        p1hand.add(new Card(12, Card.Suit.CLUBS));
        p1hand.add(new Card(13, Card.Suit.SPADES));
        assertTrue(p1hand.isGun());
        assertFalse(p1hand.isGoldenGun());

        p2hand.add(new Card(3, Card.Suit.HEARTS));
        p2hand.add(new Card(6, Card.Suit.HEARTS));
        p2hand.add(new Card(12, Card.Suit.SPADES));
        p2hand.add(new Card(13, Card.Suit.HEARTS));
        assertTrue(p2hand.isGun());
        assertFalse(p2hand.isGoldenGun());

        Attack attack = new Attack(players);
        attack.shoot(new Random(), false);

        // both players will have lost 1-4 health
        assertTrue(startHealth - 1 >= p1.getHealth().intValue());
        assertTrue(startHealth - 4 <= p1.getHealth().intValue());
        assertTrue(startHealth - 1 >= p2.getHealth().intValue());
        assertTrue(startHealth - 4 <= p2.getHealth().intValue());
    }

    @Test
    void twoPlayersOneGoldenGun() throws Exception {
        p1hand.add(new Card(3, Card.Suit.DIAMONDS));
        p1hand.add(new Card(6, Card.Suit.DIAMONDS));
        p1hand.add(new Card(12, Card.Suit.DIAMONDS));
        p1hand.add(new Card(13, Card.Suit.DIAMONDS));
        assertTrue(p1hand.isGun());
        assertTrue(p1hand.isGoldenGun());

        p2hand.add(new Card(3, Card.Suit.CLUBS));
        p2hand.add(new Card(6, Card.Suit.CLUBS));
        p2hand.add(new Card(5, Card.Suit.SPADES));
        p2hand.add(new Card(4, Card.Suit.CLUBS));
        assertFalse(p2hand.isGun());
        assertFalse(p2hand.isGoldenGun());

        Attack attack = new Attack(players);
        attack.shoot(new Random(), false);

        // p2 should have lost 8 life
        assertEquals(startHealth - 8, p2.getHealth().intValue());
    }

    @Test
    void twoPlayersOneNormalGunOneGoldenGun() throws Exception {
        p1hand.add(new Card(3, Card.Suit.SPADES));
        p1hand.add(new Card(6, Card.Suit.SPADES));
        p1hand.add(new Card(12, Card.Suit.SPADES));
        p1hand.add(new Card(13, Card.Suit.SPADES));
        assertTrue(p1hand.isGun());
        assertTrue(p1hand.isGoldenGun());

        p2hand.add(new Card(3, Card.Suit.CLUBS));
        p2hand.add(new Card(6, Card.Suit.CLUBS));
        p2hand.add(new Card(12, Card.Suit.HEARTS));
        p2hand.add(new Card(13, Card.Suit.CLUBS));
        assertTrue(p2hand.isGun());
        assertFalse(p2hand.isGoldenGun());

        Attack attack = new Attack(players);
        attack.shoot(new Random(), false);

        // p2 should have lost 8 life and p1 between 1 and 4
        assertTrue(startHealth - 1 >= p1.getHealth().intValue());
        assertTrue(startHealth - 4 <= p1.getHealth().intValue());
        assertEquals(startHealth - 8, p2.getHealth().intValue());
    }

    @AfterAll
    static void threePlayersOneNormalGunOneGoldenGun() throws Exception {
        p1hand.clear();
        p2hand.clear();
        for (int player : players.keySet()) {
            players.get(player).getHealth().set(startHealth);
        }

        // p1 has no gun
        p1hand.add(new Card(1, Card.Suit.SPADES));
        p1hand.add(new Card(2, Card.Suit.HEARTS));
        p1hand.add(new Card(3, Card.Suit.CLUBS));
        p1hand.add(new Card(4, Card.Suit.SPADES));
        assertFalse(p1hand.isGun());
        assertFalse(p1hand.isGoldenGun());

        // p2 has regular gun
        p2hand.add(new Card(3, Card.Suit.HEARTS));
        p2hand.add(new Card(6, Card.Suit.SPADES));
        p2hand.add(new Card(12, Card.Suit.CLUBS));
        p2hand.add(new Card(13, Card.Suit.CLUBS));
        assertTrue(p2hand.isGun());
        assertFalse(p2hand.isGoldenGun());

        // add third player
        // p3 has golden gun
        Player p3 = new Player(ai, new AtomicInteger(startHealth));
        players.put(3, p3);
        Hand p3hand = new Hand();
        p3hand.add(new Card(3, Card.Suit.CLUBS));
        p3hand.add(new Card(6, Card.Suit.CLUBS));
        p3hand.add(new Card(12, Card.Suit.CLUBS));
        p3hand.add(new Card(13, Card.Suit.CLUBS));
        assertTrue(p3hand.isGun());
        assertTrue(p3hand.isGoldenGun());
        p3.setHand(p3hand);

        Attack attack = new Attack(players);
        attack.shoot(new Random(), false);

        // p1 has been shot by p2 and p3, loses 9-12 life
        assertTrue(startHealth - (8 + 1) >= p1.getHealth().intValue());
        assertTrue(startHealth - (8 + 4) <= p1.getHealth().intValue());

        // p1 has been shot by p3, loses 8 life
        assertEquals(startHealth - 8, p2.getHealth().intValue());

        // p3 has been shot by p2, loses 1-4 life
        assertTrue(startHealth - 1 >= p3.getHealth().intValue());
        assertTrue(startHealth - 4 <= p3.getHealth().intValue());
    }

}
