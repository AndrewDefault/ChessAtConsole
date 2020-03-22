package logic;

import logic.elements.Cell;
import logic.elements.Field;
import logic.elements.Figure;
import logic.game.ChessTurn;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that provides interface for chess game.
 */
public class ChessGame {
    private Figure.Color whoMoves;
    private Field field;
    private ArrayList<ChessTurn> turns;

    /**
     * Constructs new game and field
     */
    public ChessGame() {
        whoMoves = Figure.Color.WHITE;
        field = new Field();
        turns = new ArrayList<>();
    }

    /**
     * @return color of player, who moves this turn
     */
    public Figure.Color whoMovesNow() {
        return whoMoves;
    }

    public Field getField() {
        return field;
    }

    /**
     * Moves figure from startX startY to lastX lastY
     * Starting coordinates must refer to cell with figure with current turn color.
     * Target's coordinates must refer to only correct cells (no same color figure or unreachable cells).
     */
    public ChessTurn performMove(int startX, int startY, int lastX, int lastY) {
        var turn = field.makeTurn(startX, startY, lastX, lastY);
        turns.add(turn);
        whoMoves = whoMoves.getOppositeColor();
        return turn;
    }

    public void logGame() {
        Date d = new Date();

        try (FileWriter file = new FileWriter("Log game [" + d.getTime() + "]")) {
            for (ChessTurn turn : turns)
                file.append(turn.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Defines if cell contains correct figure for this turn's move
     *
     * @return array with cells, which are reachable from current position.
     */
    public ArrayList<Cell> cellContainsCorrectFigureForMove(int startX, int startY) {
        if (field.cellAt(startX, startY).hasFigure()
                && field.cellAt(startX, startY).getFigure().getColor() == whoMoves)
            return field.reachableCellsFromPosition(startX, startY);
        return new ArrayList<>();
    }

    /**
     * Defines if target cell is reachable from current position.
     *
     * @param startX  current coordinate
     * @param startY  current coordinate
     * @param targetX target coordinate
     * @param targetY target coordinate
     * @return true if target cell is reachable from current position
     */
    public boolean cellIsSuitableForMove(int startX, int startY, int targetX, int targetY) {
        var r = field.reachableCellsFromPosition(startX, startY);
        return r.contains(field.cellAt(targetX, targetY));
    }

    public void setPromotionFigure(ChessTurn turn, String move) {
        field.addPromotionFigure(turn, move);

    }
}
