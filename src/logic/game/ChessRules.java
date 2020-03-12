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

    Figure.Color fColor;
    Cell cell;
    int thisX;
    int thisY;
    Figure figStart;

    /**
     * Private constructor. Creates new copy of analyzer.
     *
     * @param field field for analysis
     * @param from  cell from which analysis is started (mandatory: from.hasFigure() == true)
     */
    private ChessRules(Field field, Cell from) {

        this.field = field;
        this.cell = from;
        this.thisX = from.getX();
        this.thisY = from.getY();
        figStart = from.getFigure();
        fColor = from.getFigure().getColor();
        fType = from.getFigure().getType();

    }

    /**
     * Static method for creating a new copy of analyzer with specified parameters
     *
     * @param field     field for analysis
     * @param startCell cell from which analysis is started (mandatory: from.hasFigure() == true)
     * @return new copy of ChessMovesAnalyzer
     */
    public static ChessRules get(Field field, Cell startCell) {
        return new ChessRules(field, startCell);
    }

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


    private ArrayList<Cell> cellsWithDangerToKing() {
        var ret = new HashSet<Cell>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field.cellAt(j, i).hasFigure() && field.cellAt(j, i).getFigure().getColor() != fColor) {
                    ret.addAll(ChessRules.get(field, field.cellAt(j, i)).PossibleCellsForSaveMoves());
                }
            }
        }
        return new ArrayList<>(ret);

    }

    private ArrayList<Cell> bishopMove() {
        return new ArrayList<>() {{
            addAll(cellsToBotLeft());
            addAll(cellsToTopLeft());
            addAll(cellsToBotRight());
            addAll(cellsToTopRight());
        }};
    }

    private ArrayList<Cell> queenMove() {
        return new ArrayList<>() {{
            addAll(cellsToBotLeft());
            addAll(cellsToTopLeft());
            addAll(cellsToBotRight());
            addAll(cellsToTopRight());
            addAll(cellsToBot());
            addAll(cellsToTop());
            addAll(cellsToLeft());
            addAll(cellsToRight());
        }};
    }

    private ArrayList<Cell> horseMove() {
        var ret = new ArrayList<Cell>();

        for (int x = thisX - 1; x <= thisX + 1; x += 2) {
            for (int y = thisY - 2; y <= thisY + 2; y += 4) {
                Cell temp = field.cellAt(x, y);
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != fColor))
                    ret.add(temp);
            }
        }
        for (int x = thisX - 2; x <= thisX + 2; x += 4) {
            for (int y = thisY - 1; y <= thisY + 1; y += 2) {
                Cell temp = field.cellAt(x, y);
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != fColor))
                    ret.add(temp);
            }
        }
        return ret;


    }

    private ArrayList<Cell> rookMove() {
        return new ArrayList<>() {{
            addAll(cellsToBot());
            addAll(cellsToTop());
            addAll(cellsToLeft());
            addAll(cellsToRight());
        }};
    }

    private ArrayList<Cell> pawnMove() {
        var attackMoves = new ArrayList<Cell>();
        int xOffset = -1;

        int yOffset = fColor == Figure.Color.WHITE ? 1 : -1;
        int enPassant = fColor == Figure.Color.WHITE ? 4 : 3;

        Figure.Color opColor = fColor.getOposeColor();

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

        var classicMoves = fColor == Figure.Color.WHITE ? cellsToTop() : cellsToBot();

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

        var right = cellsToRight();
        var left = cellsToLeft();

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
                if (temp != null && (!temp.hasFigure() || temp.getFigure().getColor() != fColor))
                    ret.add(temp);
            }
        }

        //var danger = cellsWithDangerToKing();

        // ret.removeIf(danger::contains);


        return ret;

    }

    private ArrayList<Cell> cellsToRight() {
        var ret = new ArrayList<Cell>();
        for (int x = thisX + 1; x < 8; x++) {
            if (field.cellAt(x, thisY).hasFigure()) {
                if (field.cellAt(x, thisY).getFigure().getColor() != fColor) {
                    ret.add(field.cellAt(x, thisY));
                    return ret;
                }
                return ret;
            }
            ret.add(field.cellAt(x, thisY));
        }
        return ret;
    }

    private ArrayList<Cell> cellsToLeft() {
        var ret = new ArrayList<Cell>();
        for (int x = thisX - 1; x >= 0; x--) {
            if (field.cellAt(x, thisY).hasFigure()) {
                if (field.cellAt(x, thisY).getFigure().getColor() != fColor) {
                    ret.add(field.cellAt(x, thisY));
                    return ret;
                }
                return ret;
            }
            ret.add(field.cellAt(x, thisY));
        }
        return ret;
    }

    private ArrayList<Cell> cellsToTop() {
        var ret = new ArrayList<Cell>();
        for (int y = thisY + 1; y < 8; y++) {
            if (field.cellAt(thisX, y).hasFigure()) {
                if (field.cellAt(thisX, y).getFigure().getColor() != fColor) {
                    ret.add(field.cellAt(thisX, y));
                    return ret;
                }
                return ret;
            }
            ret.add(field.cellAt(thisX, y));
        }
        return ret;
    }

    private ArrayList<Cell> cellsToBot() {
        var ret = new ArrayList<Cell>();
        for (int y = thisY - 1; y >= 0; y--) {
            if (field.cellAt(thisX, y).hasFigure()) {
                if (field.cellAt(thisX, y).getFigure().getColor() != fColor) {
                    ret.add(field.cellAt(thisX, y));
                    return ret;
                }
                return ret;
            }
            ret.add(field.cellAt(thisX, y));
        }
        return ret;
    }

    private ArrayList<Cell> cellsToTopRight() {
        var ret = new ArrayList<Cell>();
        for (int i = 1; i < 8; i++) {
            Cell temp = field.cellAt(thisX + i, thisY + i);
            if (temp != null) {
                if (temp.hasFigure()) {
                    if (temp.getFigure().getColor() != fColor) {
                        ret.add(temp);
                        return ret;
                    }
                    return ret;
                }
                ret.add(temp);
            }
        }
        return ret;
    }

    private ArrayList<Cell> cellsToTopLeft() {
        var ret = new ArrayList<Cell>();
        for (int i = 1; i < 8; i++) {
            Cell temp = field.cellAt(thisX - i, thisY + i);
            if (temp != null) {
                if (temp.hasFigure()) {
                    if (temp.getFigure().getColor() != fColor) {
                        ret.add(temp);
                        return ret;
                    }
                    return ret;
                }
                ret.add(temp);
            }
        }
        return ret;
    }

    private ArrayList<Cell> cellsToBotRight() {
        var ret = new ArrayList<Cell>();
        for (int i = 1; i < 8; i++) {
            Cell temp = field.cellAt(thisX + i, thisY - i);
            if (temp != null) {
                if (temp.hasFigure()) {
                    if (temp.getFigure().getColor() != fColor) {
                        ret.add(temp);
                        return ret;
                    }
                    return ret;
                }
                ret.add(temp);
            }
        }
        return ret;
    }

    private ArrayList<Cell> cellsToBotLeft() {
        var ret = new ArrayList<Cell>();
        for (int i = 1; i < 8; i++) {
            Cell temp = field.cellAt(thisX - i, thisY - i);
            if (temp != null) {
                if (temp.hasFigure()) {
                    if (temp.getFigure().getColor() != fColor) {
                        ret.add(temp);
                        return ret;
                    }
                    return ret;
                }
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
        int yOffset = fColor == Figure.Color.WHITE ? 0 : 7;

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

    Cell detectKing() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (field.cellAt(j, i).hasFigure() &&
                        field.cellAt(j, i).getFigure().getType() == Figure.Type.KING
                        && field.cellAt(j, i).getFigure().getColor() == fColor)
                    return field.cellAt(j, i);

        return null;
    }

    public boolean isCheckmate() {
        fColor = fColor.getOposeColor();

        var cellsWithDanger = cellsWithDangerToKing();
        Cell King = detectKing();

        var cellsThatSaveTheKing = new HashSet<Cell>();

        if (cellsWithDanger.contains(King)) {
            System.out.println("King is under danger!!!");
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (field.cellAt(j, i).hasFigure()
                            && field.cellAt(j, i).getFigure().getColor() == fColor)
                        cellsThatSaveTheKing.addAll(new ChessRules(field, field.cellAt(j, i)).PossibleCellsForMoves());

            return cellsThatSaveTheKing.isEmpty();
        }

        return false;
    }
}