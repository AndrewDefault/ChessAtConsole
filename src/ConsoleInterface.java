import logic.ChessGame;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class with simple console interface for playing chess game.
 */
public class ConsoleInterface {

    private ChessGame game;
    StringBuffer log = new StringBuffer();

    public static void main(String[] args) {

        new ConsoleInterface().startNewGame(); //tem line

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Hello!
                    Type "quit" to exit the game
                    "start" to start a new game
                    """);
            var s = in.nextLine();
            switch (s) {
                case "quit":
                    return;
                case "start":
                    new ConsoleInterface().startNewGame();


                default:
            }
        }
    }

    /**
     * Creates new copy of a game and starts to play
     */
    void startNewGame() {
        game = new ChessGame();
        log = new StringBuffer();

        Scanner in = new Scanner(System.in);
        System.out.println(game.getField());
        while (game.isRunning()) {
            System.out.print(game.whoMovesNow().toString().toLowerCase() + "'s turn: ");

            String turn = in.nextLine();

            if (Pattern.matches("([a-hA-H][1-8]\\s+[a-hA-H][1-8])|quit", turn)) {
                if (turn.equals("quit")) {
                    System.out.println("\nGame ended!\n");

                    return;
                }

                int[] args = transferCoordinates(turn);

                if (game.cellContainsCorrectFigureForMove(args[0], args[1]).isEmpty()) {
                    System.out.println(game.getField());
                    System.out.println("Wrong figure for this turn!");
                    continue;
                }
                if (!game.cellIsSuitableForMove(args[0], args[1], args[2], args[3])) {
                    System.out.println(game.getField());
                    System.out.println("Wrong target!");
                    continue;
                }
                game.performMove(args[0], args[1], args[2], args[3]);
                System.out.println(game.getField());
            }
        }
        congratsTheWinner();
    }

    /**
     * Transfer input coordinates in chess "format" into digital
     *
     * @param stringCoords coordinates in chess "format"
     * @return array with integers - digital coordinates
     */
    int[] transferCoordinates(String stringCoords) {
        String[] parsed = stringCoords.toLowerCase().split("\\s+");
        return new int[]{
                parsed[0].charAt(0) - 'a',
                parsed[0].charAt(1) - '1',
                parsed[1].charAt(0) - 'a',
                parsed[1].charAt(1) - '1'
        };
    }

    /**
     * Method to show winner
     */
    void congratsTheWinner() {
        System.out.println(game.whoIsWinner() + """
                 is a winner!
                Congrats!

                """);
    }
}
