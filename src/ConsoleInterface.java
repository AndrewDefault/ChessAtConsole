import logic.Game;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleInterface {

    private Game game;

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

    void startNewGame() {
        game = new Game();

        Scanner in = new Scanner(System.in);


        testMove("e2 e4");
        testMove("e7 e5");
        testMove("f1 c4");
        testMove("b8 c6");
        testMove("d1 h5");
        testMove("g8 f6");


        while (game.isRunning()) {
            System.out.println(game.getField());
            System.out.print(game.whoMovesNow().toString().toLowerCase() + "'s turn: ");

            String turn = in.nextLine();

            if (Pattern.matches("([a-hA-H][1-8]\\s+[a-hA-H][1-8])|quit", turn)) {
                if(turn.equals("quit")) {
                    congratsTheWinner();
                    return;
                }

                int[] args = transferCoordinates(turn);

                if ( game.cellContainsCorrectFigureForMove(args[0], args[1]).isEmpty()) {
                    System.out.println("Wrong figure for this turn!");
                    continue;
                }
                if (!game.cellIsSuitableForMove(args[0], args[1], args[2], args[3])) {
                    System.out.println("Wrong target!");
                    continue;
                }
                boolean check = game.performMove(args[0], args[1], args[2], args[3]);
                if (check)
                    break;
            }
        }
        congratsTheWinner();
    }

    int[] transferCoordinates(String s) {
        int[] ret = new int[4];
        String[] parsed = s.toLowerCase().split("\\s+");
        ret[0] = parsed[0].charAt(0) - 'a';
        ret[1] = parsed[0].charAt(1) - '1';

        ret[2] = parsed[1].charAt(0) - 'a';
        ret[3] = parsed[1].charAt(1) - '1';

        return ret;
    }


    void testMove(String s) {
        int[] args = transferCoordinates(s);
        game.performMove(args[0], args[1], args[2], args[3]);
    }

    void testGame(String S) {
        String[] str = S.trim().split("\\s+");
        for (var k : str)
            testMove(k);
    }

    void congratsTheWinner() {

        System.out.println(game.whoMovesNow().getOposeColor() + """
                 is a winner!
                Congrats!

                """);
    }


}
