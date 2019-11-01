package nl.joerivandervelde.kalashnikov;

import nl.joerivandervelde.kalashnikov.cards.Card;
import nl.joerivandervelde.kalashnikov.cards.Deck;
import nl.joerivandervelde.kalashnikov.logic.Attack;
import nl.joerivandervelde.kalashnikov.logic.Hand;
import nl.joerivandervelde.kalashnikov.logic.Shelf;
import nl.joerivandervelde.kalashnikov.logic.Win;
import nl.joerivandervelde.kalashnikov.players.AI;
import nl.joerivandervelde.kalashnikov.players.Consider;
import nl.joerivandervelde.kalashnikov.players.Decision;
import nl.joerivandervelde.kalashnikov.players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.joerivandervelde.kalashnikov.players.Decision.Action.*;

/**
 * The Kalashnikov card game by Life of Boris (see: https://www.youtube
 * .com/watch?v=IiRk-yGfAjc)
 * <p>
 * This implementations works for anywhere between 2 and 13 players, although
 * 2-4 is advised. Currently the game is automated with AI players playing
 * against each other. In games with more than two players, shooting hits all
 * other players to keep it fair. In this implementations we assume players
 * to make logical decisions but no 'card counting'.
 * <p>
 * How it works: Kalashnikov is played as a 'series' of games. Each series
 * starts with 2-13 players each with PLAYER_HEALTH starting health. Each
 * game ends with one player having assembled a gun and shooting the others
 * for 1-8 damage per player. Players also holding a gun shoot back. A game
 * can also end without shooting when players can no longer draw cards from
 * scrap or shelf, excluding the card they shelved in the same turn. A game
 * also ends when MAX_ROUNDS is reached. A 'round' is a full cycle of all
 * players who are still alive have taken their turns. Lastly, a 'turn' is an
 * individual player taking and ditching cards, deciding to shoot, etc. The
 * series end when there is only one player left with positive health, who is
 * declared the winner, or when when all players have 0 health (e.g. shot
 * each other), ending in a draw.
 */
public class Kalashnikov {

    // Static variables.
    public static int MAX_NR_OF_PLAYERS = 13;
    public static int PLAYER_HEALTH = 20;
    public static int MAX_ROUNDS = 100;
    public static int GOLDEN_GUN_DMG = 8;

    // Card ranks that are gun parts.
    public static final int GP1 = 3;
    public static final int GP2 = 6;
    public static final int GP3 = 12;
    public static final int GP4 = 13;

    // Local variables.
    private Deck scrap;
    private HashMap<Integer, Player> players;
    private List<Card> discard;
    private Shelf shelf;
    private Consider consider;
    private Integer winner;
    private int nrOfGamesPlayed;
    private boolean verbose;
    private boolean gameInProgress;
    private Random rng;

    /**
     * Constructor for a default verbose game with two random AI players.
     *
     * @throws Exception
     */
    public Kalashnikov() throws Exception {
        this(2, true);
    }

    /**
     * Constructor for a game with 2 to MAX_NR_OF_PLAYERS players and
     * selectable verbosity.
     *
     * @param nrOfPlayers
     * @param verbose
     * @throws Exception
     */
    public Kalashnikov(int nrOfPlayers, boolean verbose) throws Exception {
        if (nrOfPlayers < 2 || nrOfPlayers > MAX_NR_OF_PLAYERS) {
            throw new Exception(
                "Select between 2 and " + MAX_NR_OF_PLAYERS + " players.");
        }
        Random rng = new Random();
        this.rng = rng;
        HashMap<Integer, Player> players = new HashMap<>();
        for (int player = 1; player <= nrOfPlayers; player++) {
            players.put(player, new Player(AI.generateRandomAI(rng),
                new AtomicInteger(PLAYER_HEALTH)));
        }
        this.verbose = verbose;
        this.players = players;
    }

    /**
     * Constructor for a game with predefined players and selectable
     * verbosity. For advanced use only. User is responsible for state of
     * player objects.
     *
     * @param players
     * @param verbose
     */
    public Kalashnikov(HashMap<Integer, Player> players, boolean verbose)
        throws Exception {
        if (players.keySet().size() < 2 ||
            players.keySet().size() > MAX_NR_OF_PLAYERS) {
            throw new Exception(
                "Select between 2 and " + MAX_NR_OF_PLAYERS + " players.");
        }
        Random rng = new Random();
        this.rng = rng;
        this.verbose = verbose;
        this.players = players;
    }

    /**
     * Use after constructing to kick-off a series of games. Creates new
     * games until a winner emerges.
     *
     * @throws Exception
     */
    public void start() throws Exception {
        while (Win.moreThanOnePlayerAlive(players)) {
            this.newGame(players.keySet().size());
        }
        this.winner = Win.winner(players);
    }

    /**
     * Start a new game of Kalashnikov by shuffling scrap (i.e. deck),
     * creating discard pile and shelf, and setting other variables. Then
     * deal cards into the hands of players and let players take turns in
     * rounds. Should MAX_ROUNDS be reached, a draw is called.
     *
     * @param nrOfPlayers
     * @throws Exception
     */
    private void newGame(int nrOfPlayers) throws Exception {
        scrap = new Deck();
        scrap.shuffle();
        discard = new ArrayList<Card>();
        shelf = new Shelf(nrOfPlayers);
        consider = new Consider(players, shelf, scrap, rng);
        nrOfGamesPlayed++;
        if (verbose) {
            System.out.println("-- Begin of game " + nrOfGamesPlayed + " --");
        }
        for (int player = 1; player <= nrOfPlayers; player++) {
            Hand hand = new Hand();
            hand.addAll(scrap.takeCards(4));
            players.get(player).setHand(hand);
            if (verbose) {
                System.out.println("Player " + player + " " +
                    "receives hand: " + hand.toString());
            }
        }
        int round = 1;
        gameInProgress = true;
        while (gameInProgress) {
            for (int player = 1; player <= nrOfPlayers; player++) {
                if (players.get(player).isAlive() && gameInProgress) {
                    if (verbose) {
                        System.out.println(
                            "-- Game " + nrOfGamesPlayed + ", " + "round " +
                                round + ", turn of player " + player + " --");
                    }
                    turn(player);
                }
            }
            if (round++ == MAX_ROUNDS) {
                if (verbose) {
                    System.out.println(
                        "-- Reached " + MAX_ROUNDS + " rounds, no one wins --");
                }
                gameInProgress = false;
            }
        }
    }

    /**
     * A player's turn where actions and decisions are taken. Player considers
     * attacking first because the dealt or current hand may be a gun. Then
     * player considers what to discard, what to draw, after again may
     * consider to attack after drawing.
     *
     * @param player
     * @throws Exception
     */
    private void turn(int player) throws Exception {
        Hand hand = players.get(player).getHand();
        considerAttack(player);
        if (!gameInProgress) {
            return;
        }
        Decision d = consider.whatToDiscard(player);
        if (verbose) {
            System.out.println(
                "Player " + player + " has decided to: " + d.toString());
        }
        if (d.getAction() == DISCARD) {
            discard.add(d.getCard());
            hand.remove(d.getCard());
        } else if (d.getAction() ==
            PUT_ON_SHELF) {
            shelf.get(player).add(d.getCard());
            hand.remove(d.getCard());
        }
        Decision draw = consider.whatToDraw(player);
        if (draw.getAction() == GAME_ENDS) {
            gameInProgress = false;
            return;
        }
        consider.trackUselessShelfDraws(draw, player);
        hand.add(draw.getCard());
        shelf.remove(draw.getCard());
        if (verbose) {
            System.out.println(
                "Player " + player + " needs new card, decides to " +
                    draw.toString());
        }
        considerAttack(player);
        if (gameInProgress) {
            if (verbose) {
                System.out
                    .println("Player " + player + " has: " + hand.toString());
                System.out.println("Shelf contains: " + shelf.toString());
                System.out.println("Discard contains: " + discard.toString());
            }
        }
    }

    /**
     * Let a player consider to start an attack. If so, the shooting starts
     * after which the game ends.
     *
     * @param attackingPlayer
     * @throws Exception
     */
    private void considerAttack(int attackingPlayer) throws Exception {
        Decision decision = consider.toAttackOrNot(attackingPlayer);
        if (decision.getAction() == ATTACK) {
            new Attack(players).shoot(rng, verbose);
            if (verbose) {
                for (int player : players.keySet()) {
                    System.out.println("Player " + player + " has " +
                        players.get(player).getHealth() + "  health.");
                }
                System.out
                    .println("-- End of game " + nrOfGamesPlayed + " --\n");
            }
            gameInProgress = false;
        }
    }


    /**
     * Return the player number of the winning player.
     * If the series has not been started, will throw an error.
     *
     * @return
     */
    public int getWinner()
        throws Exception {
        if (this.winner == null) {
            throw new Exception("Series have not been played. Use start().");
        }
        return winner.intValue();
    }

    /**
     * Get the number of games played in this series.
     *
     * @return
     */
    public int getNrOfGamesPlayed() {
        return nrOfGamesPlayed;
    }
}
