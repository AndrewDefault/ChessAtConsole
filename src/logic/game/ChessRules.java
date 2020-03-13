package logic.game;

import logic.elements.Cell;
import logic.elements.Field;
import logic.elements.Figure;

import java.util.ArrayList;
import java.util.HashSet;
// TODO: 13.03.2020 class shouldn't change objects, that are sent to it

/**
 * Provides class that analyze field and cell to calculate right moves.
 */
public class ChessRules {
    Field field;

    Figure.Type fType;
    Figure.Color figureColor;

    Cell cell;
    int thisX;
    int thisY;

    /**
     * Private constructor. Creates new copy of analyzer.
     *
     * @param field field for analysis
     * @param cellForAnalysis  cell from which analysis is started (mandatory: cell must contain Figure)
     */
    private ChessRules(Field field, Cell cellForAnalysis) {
        this.field = field;
        this.cell = cellForAnalysis;
        this.thisX = cellForAnalysis.getX();
        this.thisY = cellForAnalysis.getY();
        figureColor = cellForAnalysis.getFigure().getColor();
        fType = cellForAnalysis.getFigure().getType();
    }

    /**
     * Static method for creating a new copy of analyzer with specified parameters
     *
     * @param field     field for analysis
     * @param cellForAnalysis cell from which analysis is started (mandatory: cell must contain Figure)
     * @return new copy of ChessMovesAnalyzer
     */
    public static ChessRules analysis(Field field, Cell cellForAnalysis) {
        return new ChessRules(field, cellForAnalysis);
    }

    /**
     * Method for creating list of cells that are right for move from current cell.
     */
    public ArrayList<Cell> PossibleCellsForMoves() {
        var returningCells = switch (fType) {
            case HORSE -> horseMove();
            case PAWN -> pawnMove();
            case ROOK -> rookMove();
            case KING -> kingMove();
            case QUEEN -> queenMove();
            case BISHOP -> bishopMove();
        };
        returningCells.removeIf(tCell -> !isMoveSafeForTheKing(tCell));
        return returningCells;
    }


    private ArrayList<Cell> PossibleCellsForSaveMoves() {
        return switch (fType) {
            case HORSE -> horseMove();
            case PAWN -> pawnMove();
            case ROOK -> rookMove();
            case KING -> kingMove();
            case QUEEN -> queenMove();
            case BISHOP -> bishopMove();
        };
    }

    /**
     * Returns list of cells which are dangerous to king of figureColor
     * @return
     */
    private ArrayList<Cell> cellsWithDangerToKing() {
        var ret = new HashSet<Cell>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field.cellAt(j, i).hasFigure() && field.cellAt(j, i).getFigure().getColor() != figureColor) {
                    ret.addAll(ChessRules.analysis(field, field.cellAt(j, i)).PossibleCellsForSaveMoves());
                }
            }
        }
        return new ArrayList<>(ret);

    }

    private ArrayList<Cell> bishopMove() {
        return new ArrayList<>() {{
            addAll(cellsToDirection(Horizontal.RIGHT, Vertical.TOP));
            addAll(cellsToDirection(Horizontal.LEFT, Vertical.TOP));
            addAll(cellsToDirection(Horizontal.LEFT, Vertical.BOT));
            addAll(cellsToDirection(Horizontal.RIGHT, Vertical.BOT));
        }};
    }

    private ArrayList<Cell> queenMove() {
        return new ArrayList<>() {{
            addAll(cellsToDirection(Horizontal.RIGHT, Vertical.NO_DIRECTION));
            addAll(cellsToDirection(Horizontal.RIGHT, Vertical.TOP));
            addAll(cellsToDirection(Horizontal.NO_DIRECTION, Vertical.TOP));
            addAll(cellsToDirection(Horizontal.LEFT, Vertical.TOP));
            addAll(cellsToDirection(Horizontal.LEFT, Vertical.NO_DIRECTION));
            addAll(cellsToDirection(Horizontal.LEFT, Vertical.BOT));
            addAll(cellsToDirection(Horizontal.NO_DIRECTION, Vertical.BOT));
            addAll(cellsToDirection(Horizontal.RIGHT, Vertical.BOT));
        }};
    }

    private ArrayList<Cell> horseMove() {
        var ret = new ArrayList<Cell>();

        for (int x = thisX - 1; x <= thisX + 1; x += 2) {
            for (int y = thisY - 2; y <= thisY + 2; y += 4) {
                Cell temp = field.cellAt(x, y);
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != figureColor))
                    ret.add(temp);
            }
        }
        for (int x = thisX - 2; x <= thisX + 2; x += 4) {
            for (int y = thisY - 1; y <= thisY + 1; y += 2) {
                Cell temp = field.cellAt(x, y);
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != figureColor))
                    ret.add(temp);
            }
        }
        return ret;


    }

    private ArrayList<Cell> rookMove() {
        return new ArrayList<>() {{
            addAll(cellsToDirection(Horizontal.RIGHT, Vertical.NO_DIRECTION));
            addAll(cellsToDirection(Horizontal.NO_DIRECTION, Vertical.TOP));
            addAll(cellsToDirection(Horizontal.LEFT, Vertical.NO_DIRECTION));
            addAll(cellsToDirection(Horizontal.NO_DIRECTION, Vertical.BOT));
        }};
    }

    private ArrayList<Cell> pawnMove() {
        var attackMoves = new ArrayList<Cell>();
        int xOffset = -1;

        int yOffset = figureColor == Figure.Color.WHITE ? 1 : -1;
        int enPassant = figureColor == Figure.Color.WHITE ? 4 : 3;

        Figure.Color opColor = figureColor.getOppositeColor();

        for (int i = 0; i < 2; i++) {
            Cell c = field.cellAt(thisX + xOffset, thisY + yOffset);
            if (c != null
                    && c.hasFigure()
                    && c.getFigure().getColor() == opColor)
                attackMoves.add(c);

            Cell b = field.cellAt(thisX + xOffset, thisY);
            if (thisY == enPassant
                    && b.hasFigure()
                    && b.getFigure().getMovesCount() == 1
                    && b.getFigure().getColor() == opColor
                    && b.getFigure().getType() == Figure.Type.PAWN
                    && b.getFigure().getLastTurn() == field.getCurrentTurnCount() - 1) {
                attackMoves.add(c);
            }

            xOffset = 1;
        }

        var classicMoves = cellsToDirection(Horizontal.NO_DIRECTION,
                figureColor == Figure.Color.WHITE ? Vertical.TOP : Vertical.BOT);

        if (!classicMoves.isEmpty() && classicMoves.get(classicMoves.size() - 1).hasFigure())
            classicMoves.remove(classicMoves.size() - 1);

        if (classicMoves.size() > 2)
            classicMoves = new ArrayList<>(classicMoves.subList(0, 2));

        if (!classicMoves.isEmpty() && cell.getFigure().getMovesCount() != 0)
            classicMoves = new ArrayList<>(classicMoves.subList(0, 1));

        attackMoves.addAll(classicMoves);
        return attackMoves;
    }

    private ArrayList<Cell> kingMove() {
        var ret = new ArrayList<Cell>();

        var right = cellsToDirection(Horizontal.RIGHT, Vertical.NO_DIRECTION);
        var left = cellsToDirection(Horizontal.LEFT, Vertical.NO_DIRECTION);

        int yOffset = 0;
        for (int i = 0; i < 2; i++) {
            if (right.size() == 2
                    && cell.getFigure().getMovesCount() == 0
                    && field.cellAt(7, yOffset).hasFigure()
                    && field.cellAt(7, yOffset).getFigure().getMovesCount() == 0)
                ret.add(field.cellAt(thisX + 2, thisY));

            if (left.size() == 3
                    && cell.getFigure().getMovesCount() == 0
                    && field.cellAt(0, yOffset).hasFigure()
                    && field.cellAt(0, yOffset).getFigure().getMovesCount() == 0)
                ret.add(field.cellAt(thisX - 2, thisY));

            yOffset = 7;
        }

        for (int x = thisX - 1; x <= thisX + 1; x++) {
            for (int y = thisY - 1; y <= thisY + 1; y++) {
                Cell temp = field.cellAt(x, y);
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != figureColor))
                    ret.add(temp);
            }
        }


        return ret;

    }


    public void performSpecialMoves() {
        passantMove();
        roqueMove();
    }


    private void passantMove() {
        if (thisY == 5
                && field.cellAt(thisX, thisY).getFigure().getType() == Figure.Type.PAWN
                && field.cellAt(thisX, thisY).getFigure().getColor() == Figure.Color.WHITE
                && field.cellAt(thisX, thisY - 1).hasFigure()
                && field.cellAt(thisX, thisY - 1).getFigure().getType() == Figure.Type.PAWN
                && field.cellAt(thisX, thisY - 1).getFigure().getColor() == Figure.Color.BLACK
                && field.cellAt(thisX, thisY - 1).getFigure().getLastTurn() == field.cellAt(thisX, thisY).getFigure().getLastTurn() - 1) {
            field.cellAt(thisX, thisY - 1).removeFigure();
        } else if (thisY == 2
                && field.cellAt(thisX, thisY).getFigure().getType() == Figure.Type.PAWN
                && field.cellAt(thisX, thisY).getFigure().getColor() == Figure.Color.BLACK
                && field.cellAt(thisX, thisY + 1).hasFigure()
                && field.cellAt(thisX, thisY + 1).getFigure().getType() == Figure.Type.PAWN
                && field.cellAt(thisX, thisY + 1).getFigure().getColor() == Figure.Color.WHITE
                && field.cellAt(thisX, thisY + 1).getFigure().getLastTurn() == field.cellAt(thisX, thisY).getFigure().getLastTurn() - 1) {
            field.cellAt(thisX, thisY + 1).removeFigure();
        }
    }

    private void roqueMove() {
        int yOffset = figureColor == Figure.Color.WHITE ? 0 : 7;

        if (fType == Figure.Type.KING && thisX == 2
                && cell.getFigure().getMovesCount() == 1)
            field.moveFigure(0, yOffset, 3, yOffset);
        if (fType == Figure.Type.KING && thisX == 6
                && cell.getFigure().getMovesCount() == 1)
            field.moveFigure(7, yOffset, 5, yOffset);
    }

    /**
     * @param end f
     */
    public boolean isMoveSafeForTheKing(Cell end) {
        Figure tempDeletedFig = null;
        if (end.hasFigure()) {
            tempDeletedFig = end.removeFigure();
        }
        end.addFigure(cell.removeFigure());

        Cell King = detectKing();

        if (cellsWithDangerToKing().contains(King)) {
            cell.addFigure(end.removeFigure());
            end.addFigure(tempDeletedFig);
            return false;
        }

        cell.addFigure(end.removeFigure());
        end.addFigure(tempDeletedFig);
        return true;
    }

    private Cell detectKing() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (field.cellAt(j, i).hasFigure() &&
                        field.cellAt(j, i).getFigure().getType() == Figure.Type.KING
                        && field.cellAt(j, i).getFigure().getColor() == figureColor)
                    return field.cellAt(j, i);

        return null;
    }

    public boolean isCheckmate() {
        figureColor = figureColor.getOppositeColor();

        var cellsWithDanger = cellsWithDangerToKing();
        Cell King = detectKing();

        var cellsThatSaveTheKing = new HashSet<Cell>();

        if (cellsWithDanger.contains(King)) {
            System.out.println("King is under danger!!!");
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (field.cellAt(j, i).hasFigure()
                            && field.cellAt(j, i).getFigure().getColor() == figureColor)
                        cellsThatSaveTheKing.addAll(new ChessRules(field, field.cellAt(j, i)).PossibleCellsForMoves());

            return cellsThatSaveTheKing.isEmpty();
        }

        return false;
    }

    private ArrayList<Cell> cellsToDirection(Horizontal hD, Vertical vD) {
        var ret = new ArrayList<Cell>();
        for (int i = 1; i < 8; i++) {
            Cell temp = field.cellAt(thisX + i * hD.direction, thisY + i * vD.direction);
            if (temp != null) {
                if (temp.hasFigure()) {
                    if (temp.getFigure().getColor() == figureColor)
                        return ret;
                    ret.add(temp);
                    return ret;
                }
                ret.add(temp);
            }
        }
        return ret;

    }

    private enum Horizontal {
        RIGHT(1), LEFT(-1), NO_DIRECTION(0);
        private int direction;
        Horizontal(int i) {
            direction = i;
        }
    }

    private enum Vertical {
        TOP(1), BOT(-1), NO_DIRECTION(0);
        private int direction;
        Vertical(int i) {
            direction = i;
        }
    }
}
