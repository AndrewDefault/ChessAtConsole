package logic.elements.field;

import logic.elements.Cell;
import logic.elements.Figure;

import java.util.ArrayList;

public class CellsAnalyzer {
    Field field;
    Figure.Type fType;
    ;
    Figure.Color fColor;
    Cell cell;
    int thisX;
    int thisY;

    private CellsAnalyzer(Field field, Cell c) {

        this.field = field;
        this.cell = c;
        this.thisX = c.getX();
        this.thisY = c.getY();
        fColor = c.getFigure().getColor();
        fType = c.getFigure().getType();
    }

    public static CellsAnalyzer get(Field f, Cell c) {
        return new CellsAnalyzer(f, c);
    }

    ArrayList<Cell> cellsForMove() {
        var ret = new ArrayList<Cell>();

        switch (fType) {
            case BISHOP -> {

            }
            case QUEEN -> {

            }
            case HORSE -> {

            }
            case PAWN -> {
                return pawnMove();

            }
            case ROOK -> {
                return rookMove();
            }

            case KING -> {

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

        Figure.Color opColor = fColor == Figure.Color.WHITE ? Figure.Color.BLACK : Figure.Color.WHITE;

        for (int i = 0; i < 2; i++) {
            Cell c = field.cellAt(thisX + xOffset, thisY + yOffset);
            if (c != null
                    && c.hasFigure()
                    && c.getFigure().getColor() == opColor)
                attackMoves.add(c);

            Cell b = field.cellAt(thisX + xOffset, thisY);
            if (thisY == enPassant
                    && b.hasFigure()
                    && b.getFigure().isTouched()
                    && b.getFigure().getColor() == opColor
                    && b.getFigure().getType() == Figure.Type.PAWN
                    && b.getFigure().getLastTurn() == field.getCurrentTurnCount()-1) {
                attackMoves.add(c);
            }

            xOffset = 1;
        }

        var classicMoves = fColor == Figure.Color.WHITE ? cellsToTop() : cellsToBot();

        if (!classicMoves.isEmpty() && classicMoves.get(classicMoves.size() - 1).hasFigure())
            classicMoves.remove(classicMoves.size() - 1);

        if (classicMoves.size() > 2)
            classicMoves = new ArrayList<>(classicMoves.subList(0, 2));

        if (!classicMoves.isEmpty() && cell.getFigure().isTouched())
            classicMoves = new ArrayList<>(classicMoves.subList(0, 1));

        attackMoves.addAll(classicMoves);
        return attackMoves;
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


}
