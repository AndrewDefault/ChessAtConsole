import logic.ChessGame;
import logic.game.ChessTurn;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class with simple console interface for playing chess game.
 */
public class ConsoleInterface {

    private ChessGame game;

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

        Scanner in = new Scanner(System.in);
        String move;
        ChessTurn turn = null;

        System.out.println(game.getField());

        do {
            System.out.print(game.whoMovesNow() + " figures turn: ");

            move = in.nextLine();

            if (Pattern.matches("([a-hA-H][1-8]\\s+[a-hA-H][1-8])|quit", move)) {
                if (move.equals("quit")) {
                    System.out.println("\nGame ended!\n");
                    return;
                }

                int[] args = transferCoordinates(move);

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
                turn = game.performMove(args[0], args[1], args[2], args[3]);
                analyzeTurn(turn);

                System.out.println(game.getField());
            } else
                System.out.println("Wrong move format! Try again.");

        } while (turn == null || Objects.requireNonNull(turn).getTurnResult() != ChessTurn.Result.CHECKMATE);
        System.out.println("Game ended!\n");


    }

    private void analyzeTurn(ChessTurn turn) {
        if (turn.getPromotion() == ChessTurn.Promotion.YES)
            handlePawnPromotion(turn);

        if (turn.getTurnResult() != ChessTurn.Result.DEFAULT) {
            System.out.println("That's a " + turn.getTurnResult() + " to " + turn.getFigure().getColor() + " figures");
//            game.logGame();
        }
    }

    private void handlePawnPromotion(ChessTurn turn) {
        Scanner in = new Scanner(System.in);
        String move;

        System.out.print(turn.getFigure().getColor() + " can promote pawn\n");
        do {
            System.out.print("Type figure name (q,r,b,h,p): ");
            move = in.nextLine();

            if (Pattern.matches("[qQrRbBhHpP]", move))
                game.addPromotionFigure(turn, move);
            else
                System.out.println("Wrong figure name! Try again.");

        } while (!Pattern.matches("[qQrRbBhHpP]", move));
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

}
