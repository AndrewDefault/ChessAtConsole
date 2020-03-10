import logic.Game;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleInterface {

    private Game game;

    public static void main(String[] args) {

        new ConsoleInterface().startNewGame(); //tem line

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Hello!\n" +
                    "Type \"quit\" to exit the game\n" +
                    "\"start\" to start a new game");
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

        while (game.isRunning()) {
            System.out.print(game.whoMovesNow().toString().toLowerCase() + "'s turn: ");

            String turn = in.nextLine();

            if (Pattern.matches("[a-hA-H][1-8]\\s+[a-hA-H][1-8]", turn)) {
                int[] args = transferCoodrd(turn);
                if (!game.cellContainsCorrectFigureForMove(args[0], args[1])) {
                    System.out.println("Wrong figure for this turn!");
                } else if (!game.cellIsSuitableForMove(args[2], args[3])) {
                    System.out.println("Wrong target!");
                } else
                    game.performMove(args[0], args[1], args[2], args[3]);
            }
        }
        congratsTheWinner();
    }

    int[] transferCoodrd(String s) {
        int[] ret = new int[4];
        String[] parsed = s.toLowerCase().split("\\s+");
        ret[0] = parsed[0].charAt(0) - 'a';
        ret[1] = parsed[0].charAt(1) - '1';

        ret[2] = parsed[1].charAt(0) - 'a';
        ret[3] = parsed[1].charAt(1) - '1';

        return ret;
    }


    void congratsTheWinner() {

    }


}
