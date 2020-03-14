package logic.game;

import logic.elements.Cell;
import logic.elements.Field;
import logic.elements.Figure;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Provides class that analyze field and cell to calculate right moves.
 */
public class ChessRules {
    private static int TurnsCount = 0;
    Field field;
    Figure.Type fType;
    Figure.Color figureColor;
    Cell cell;
    int thisX;
    int thisY;

    /**
     * Private constructor. Creates new copy of analyzer.
     *
     * @param field           field for analysis
     * @param cellForAnalysis cell from which analysis is started (mandatory: cell must contain Figure)
     */
    private ChessRules(Field field, Cell cellForAnalysis) {
        this.field = field;
        this.cell = cellForAnalysis;
        this.thisX = cellForAnalysis.getX();
        this.thisY = cellForAnalysis.getY();
        figureColor = cellForAnalysis.getFigure().getColor();
        fType = cellForAnalysis.getFigure().getType();
    }

    private ChessRules(Field field, Figure.Color col) {
        this.field = field;
        this.figureColor = col;
    }

    /**
     * Static method for creating a new copy of analyzer with specified parameters
     *
     * @param field           field for analysis
     * @param cellForAnalysis cell from which analysis is started (mandatory: cell must contain Figure)
     * @return new copy of ChessMovesAnalyzer
     */
    public static ArrayList<Cell> correctMovesFromCell(Field field, Cell cellForAnalysis) {
        return new ChessRules(field, cellForAnalysis).possibleCellsForMoves();
    }

    public static boolean isCheckmate(Field field, Figure.Color colorOfLastTurnAttacker) {
        return new ChessRules(field, colorOfLastTurnAttacker.getOppositeColor()).isCheckmate();
    }

    public static void moveFiguresCorrectly(Field field, Cell start, Cell end) {
        performMove(start, end);
        passantMove(field, end);
        roqueMove(field, end);
    }

    private static void performMove(Cell start, Cell end) {
        end.addFigure(start.removeFigure());
        end.getFigure().movePerformed(TurnsCount++);
    }

    private static void passantMove(Field field, Cell cellTarget) {
        var thisY = cellTarget.getY();
        var thisX = cellTarget.getX();

        if (thisY == 5 || thisY == 2) {
            var dY = thisY == 5 ? -1 : 1;
            if (field.cellAt(thisX, thisY).getFigure().getType() == Figure.Type.PAWN
                    && field.cellAt(thisX, thisY).getFigure().getMovesCount() > 2
                    && field.cellAt(thisX, thisY + dY).hasFigure()
                    && field.cellAt(thisX, thisY + dY).getFigure().getType() == Figure.Type.PAWN
                    && field.cellAt(thisX, thisY + dY).getFigure().getMovesCount() == 1
                    && field.cellAt(thisX, thisY + dY).getFigure().getLastTurn() == cellTarget.getFigure().getLastTurn() - 1) {
                field.cellAt(thisX, thisY + dY).removeFigure();
            }

        }
    }

    private static void roqueMove(Field field, Cell targetCell) {
        int yOffset = targetCell.getFigure().getColor() == Figure.Color.WHITE ? 0 : 7;

        if (targetCell.getFigure().getType() == Figure.Type.KING && targetCell.getX() == 2
                && targetCell.getFigure().getMovesCount() == 1)
            performMove(field.cellAt(0, yOffset), field.cellAt(3, yOffset));

        if (targetCell.getFigure().getType() == Figure.Type.KING && targetCell.getX() == 6
                && targetCell.getFigure().getMovesCount() == 1)
            performMove(field.cellAt(7, yOffset), field.cellAt(7, yOffset));
    }

    private boolean isCheckmate() {
        var cellsWithDanger = cellsWithDangerToKing();
        Cell King = detectKing();

        var cellsThatSaveTheKing = new HashSet<Cell>();

        if (cellsWithDanger.contains(King)) {
            System.out.println("King is under danger!!!");
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (field.cellAt(j, i).hasFigure()
                            && field.cellAt(j, i).getFigure().getColor() == figureColor)
                        cellsThatSaveTheKing.addAll(new ChessRules(field, field.cellAt(j, i)).possibleCellsForMoves());

            return cellsThatSaveTheKing.isEmpty();
        }

        return false;
    }

    /**
     * Method for creating list of cells that are right for move from current cell.
     */
    private ArrayList<Cell> possibleCellsForMoves() {
        var returningCells = switch (fType) {
            case HORSE -> horseMove();
            case PAWN -> pawnMove();
            case ROOK -> rookMove();
            case KING -> kingMove();
            case QUEEN -> queenMove();
            case BISHOP -> bishopMove();
        };
        returningCells.removeIf(this::isMoveDangerousForKing);
        return returningCells;
    }

    private ArrayList<Cell> allCellsMoves() {
        return switch (fType) {
            case HORSE -> horseMove();
            case PAWN -> pawnMove();
            case ROOK -> rookMove();
            case KING -> kingMove();
            case QUEEN -> queenMove();
            case BISHOP -> bishopMove();
        };
    }

    private ArrayList<Cell> cellsWithDangerToKing() {
        var ret = new HashSet<Cell>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field.cellAt(j, i).hasFigure() && field.cellAt(j, i).getFigure().getColor() != figureColor) {
                    ret.addAll(new ChessRules(field, field.cellAt(j, i)).allCellsMoves());
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

        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++)
                if ((i * i + j * j) == 5) {
                    Cell temp = field.cellAt(thisX + i, thisY + j);
                    if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != figureColor))
                        ret.add(temp);
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

        int yOffset = figureColor == Figure.Color.WHITE ? 1 : -1;
        int enPassant = figureColor == Figure.Color.WHITE ? 4 : 3;

        Figure.Color opColor = figureColor.getOppositeColor();

        int xOffset = -1;
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
                    && b.getFigure().getLastTurn() == TurnsCount - 1) {
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

        int yOffset = figureColor == Figure.Color.WHITE ? 0 : 7;

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


        for (int x = thisX - 1; x <= thisX + 1; x++) {
            for (int y = thisY - 1; y <= thisY + 1; y++) {
                Cell temp = field.cellAt(x, y);
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != figureColor))
                    ret.add(temp);
            }
        }

        return ret;
    }

    private boolean isMoveDangerousForKing(Cell end) {
        Figure tempDeletedFig = end.removeFigure();
        end.addFigure(cell.removeFigure());

        Cell King = detectKing();
        var danger = cellsWithDangerToKing();

        cell.addFigure(end.removeFigure());
        end.addFigure(tempDeletedFig);

        return danger.contains(King);
    }

    private Cell detectKing() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (field.cellAt(j, i).hasFigure()
                        && field.cellAt(j, i).getFigure().getType() == Figure.Type.KING
                        && field.cellAt(j, i).getFigure().getColor() == figureColor)
                    return field.cellAt(j, i);

        return null;
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
