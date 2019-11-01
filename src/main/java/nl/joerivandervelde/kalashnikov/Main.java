package nl.joerivandervelde.kalashnikov;

public class Main {

    /**
     * Runnable main example to start a default series of Kalashnikov games.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Kalashnikov series = new Kalashnikov();
        series.start();
        System.out.println(
            "Series have ended! Player " + series.getWinner() + " has won in " +
                series.getNrOfGamesPlayed() + " games.");
    }

}
