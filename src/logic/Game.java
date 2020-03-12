package logic;

import logic.elements.Cell;
import logic.elements.Figure;
import logic.elements.field.Field;

import java.util.ArrayList;

/**
 * Class that provides interface for chess game.
 */
public class Game {
    Figure.Color winnerOfGame;
    private boolean isGameRunning;
    private Figure.Color whoMoves;
    private Field field;

    /**
     * Constructs new game and field
     */
    public Game() {
        isGameRunning = true;
        whoMoves = Figure.Color.WHITE;
        winnerOfGame = Figure.Color.WHITE;
        field = new Field();
    }

    /**
     * @return color of player, who moves this turn
     */
    public Figure.Color whoMovesNow() {
        return whoMoves;
    }

    /**
     * @return true if game is running, false if game ended.
     */
    public boolean isRunning() {
        return isGameRunning;
    }

    public Field getField() {
        return field;
    }

    /**
     * Moves figure from startX startY to lastX lastY
     * Starting coordinates must refer to cell with figure with current turn color.
     * Target's coordinates must refer to only correct cells (no same color figure or unreachable cells).
     */
    public void performMove(int startX, int startY, int lastX, int lastY) {
        boolean isCheck = field.moveFigure(startX, startY, lastX, lastY);

        if (!isCheck)
            whoMoves = whoMoves == Figure.Color.WHITE
                    ? Figure.Color.BLACK
                    : Figure.Color.WHITE;
        else
            endGame();

    }

    /**
     * Turns off the game and defines the winner of game.
     */
    private void endGame() {
        isGameRunning = false;
        winnerOfGame = whoMoves;
    }

    /**
     * @return winner of ended game.
     */
    public Figure.Color whoIsWinner() {
            return  winnerOfGame;
    }

    /**
     * Defines if cell contains correct figure for this turn's move
     * @return array with cells, which are reachable from current position.
     */
    public ArrayList<Cell> cellContainsCorrectFigureForMove(int startX, int startY) {
        if (field.cellAt(startX, startY).hasFigure()
                && field.cellAt(startX, startY).getFigure().getColor() == whoMoves)
            return field.cellsForCorrectMoves(startX, startY);
        return null;
    }

    /**
     * Defines if target cell is reachable from current position.
     * @param startX current coordinate
     * @param startY current coordinate
     * @param targetX target coordinate
     * @param targetY target coordinate
     * @return true if target cell is reachable from current position
     */
    public boolean cellIsSuitableForMove(int startX, int startY, int targetX, int targetY) {
        var r = field.cellsForCorrectMoves(startX, startY);
        return r.contains(field.cellAt(targetX, targetY));
    }
}
