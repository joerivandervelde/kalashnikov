package nl.joerivandervelde.kalashnikov.players;

import java.util.Random;

/**
 * Artificial intelligence for Player objects represented by a number of
 * settings that affect decisions in a game of Kalashnikov.
 */
public class AI {

    /**
     * Chance of getting rid of duplicate gun parts before useless cards.
     */
    private double discardDuplicatesBeforeUselessCards;

    /**
     * Chance of waiting for golden gun when completing normal gun.
     */
    private double waitForGoldenGun;

    /**
     * Chance of choosing a scrap card over an unknown shelf card.
     */
    private double chooseScrapOverUnknownShelf;

    /**
     * How many useless shelf draws are accepted before ignoring shelf.
     */
    private int maxConsecUselessUnknownShelfDraws;

    /**
     * Constructor for a default AI that does a pretty good job.
     */
    public AI() {
        this.discardDuplicatesBeforeUselessCards = 0.0;
        this.waitForGoldenGun = 0.0;
        this.chooseScrapOverUnknownShelf = 0.0;
        this.maxConsecUselessUnknownShelfDraws = 2;
    }

    /**
     * Advanced constructor, allowing user to set all AI settings manually.
     *
     * @param discardDuplicatesBeforeUselessCards
     * @param waitForGoldenGun
     * @param chooseScrapOverUnknownShelf
     * @param maxConsecUselessUnknownShelfDraws
     * @throws Exception
     */
    public AI(double discardDuplicatesBeforeUselessCards,
              double waitForGoldenGun,
              double chooseScrapOverUnknownShelf,
              int maxConsecUselessUnknownShelfDraws) throws Exception {
        if (discardDuplicatesBeforeUselessCards < 0 ||
            discardDuplicatesBeforeUselessCards > 1.0) {
            throw new Exception("discardDuplicatesBeforeUselessCards must " +
                "fall between 0 and 1");
        }
        if (waitForGoldenGun < 0 || waitForGoldenGun > 1.0) {
            throw new Exception("waitForGoldenGun must fall between" +
                " 0 and 1");
        }
        if (chooseScrapOverUnknownShelf < 0 ||
            chooseScrapOverUnknownShelf > 1.0) {
            throw new Exception("chooseScrapOverUnknownShelf must " +
                "fall between 0 and 1");
        }
        if (maxConsecUselessUnknownShelfDraws < 1 ||
            maxConsecUselessUnknownShelfDraws > 10) {
            throw new Exception("maxConsecUselessUnknownShelfDraws must fall " +
                "between 1 and 10");

        }
        this.discardDuplicatesBeforeUselessCards =
            discardDuplicatesBeforeUselessCards;
        this.waitForGoldenGun = waitForGoldenGun;
        this.chooseScrapOverUnknownShelf =
            chooseScrapOverUnknownShelf;
        this.maxConsecUselessUnknownShelfDraws =
            maxConsecUselessUnknownShelfDraws;
    }

    /**
     * Helper to generate a randomized AI that can be used to create Player
     * objects.
     *
     * @param rng
     * @return
     * @throws Exception
     */
    public static AI generateRandomAI(Random rng) throws Exception {
        return new AI(rng.nextDouble(), rng.nextDouble(), rng.nextDouble(),
            (rng.nextInt(10) + 1));
    }

    /**
     * Getter for discardDuplicatesBeforeUselessCards
     *
     * @return
     */
    public double getDiscardDuplicatesBeforeUselessCards() {
        return discardDuplicatesBeforeUselessCards;
    }

    /**
     * Getter for waitForGoldenGun
     *
     * @return
     */
    public double getWaitForGoldenGun() {
        return waitForGoldenGun;
    }

    /**
     * Getter for chooseScrapOverUnknownShelf
     *
     * @return
     */
    public double getChooseScrapOverUnknownShelf() {
        return chooseScrapOverUnknownShelf;
    }

    /**
     * Getter for maxConsecUselessUnknownShelfDraws
     *
     * @return
     */
    public int getMaxConsecUselessUnknownShelfDraws() {
        return maxConsecUselessUnknownShelfDraws;
    }
}
