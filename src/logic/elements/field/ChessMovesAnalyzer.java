package logic.elements.field;

import logic.elements.Cell;
import logic.elements.Figure;

import java.util.ArrayList;

public class ChessMovesAnalyzer {
    Field field;
    Figure.Type fType;

    Figure.Color fColor;
    Cell cell;
    int thisX;
    int thisY;

    private ChessMovesAnalyzer(Field field, Cell c) {

        this.field = field;
        this.cell = c;
        this.thisX = c.getX();
        this.thisY = c.getY();
        fColor = c.getFigure().getColor();
        fType = c.getFigure().getType();
    }

    public static ChessMovesAnalyzer get(Field f, Cell c) {
        return new ChessMovesAnalyzer(f, c);
    }

    ArrayList<Cell> PossibleCellsForMoves() {
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
return kingMove();
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

    ArrayList<Cell> kingMove() {
        var ret = new ArrayList<Cell>();

        var right = cellsToRight();
        var left = cellsToLeft();

        int yOffset = 0;
        for (int i = 0; i < 2; i++) {
            if (right.size() == 2
                    && cell.getFigure().getMovesCount()==0
                    && field.cellAt(7, yOffset).hasFigure()
                    && field.cellAt(7, yOffset).getFigure().getMovesCount()==0 )
                ret.add(field.cellAt(thisX + 2, thisY));

            if (left.size() == 3
                    && cell.getFigure().getMovesCount() == 0
                    && field.cellAt(0, yOffset).hasFigure()
                    && field.cellAt(0, yOffset).getFigure().getMovesCount() == 0)
                ret.add(field.cellAt(thisX - 2, thisY));

            yOffset = 7;
        }
        for(int x = thisX-1; x <= thisX+1; x++){
            for (int y = thisY-1; y <= thisY+1; y++){
                Cell temp = field.cellAt(x,y);
                if(temp != null && (!temp.hasFigure() || temp.getFigure().getColor()== Figure.Color.BLACK))
                    ret.add(temp);
            }
        }
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


    public ChessMovesAnalyzer checkEnPassantMove() {
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
        return this;
    }

    public ChessMovesAnalyzer checkRoqueMove() {
        int yOffset = fColor == Figure.Color.WHITE? 0 : 7;

        if(fType== Figure.Type.KING && thisX == 2
        && cell.getFigure().getMovesCount()==1)
            field.moveFigure(0,yOffset,3,yOffset);
        if(fType== Figure.Type.KING && thisX == 6
                && cell.getFigure().getMovesCount()==1)
            field.moveFigure(7,yOffset,5,yOffset);
        return this;
    }
}
