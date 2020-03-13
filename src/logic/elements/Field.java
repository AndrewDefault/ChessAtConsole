package logic.elements;

import logic.game.ChessRules;

import java.util.ArrayList;

/**
 * Class with field for chess game.
 */
public class Field {

    Cell[][] cells;
    int currentTurnCount;


    public Field() {
        currentTurnCount = 0;
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                cells[i][j] = new Cell(j, i);

        setupFigures();
    }

    /**
     * @return how many moves were already made
     */
    public int getCurrentTurnCount() {
        return currentTurnCount;
    }

    /**
     * setup chess field according to rules
     */
    private void setupFigures() {
        for (int x = 0; x < 8; x++) {
            cells[1][x].addFigure(new Figure(Figure.Color.WHITE, Figure.Type.PAWN));
            cells[6][x].addFigure(new Figure(Figure.Color.BLACK, Figure.Type.PAWN));
        }

        var c = Figure.Color.WHITE;
        for (int i = 0; i < 10; i += 7) {
            cells[i][0].addFigure(new Figure(c, Figure.Type.ROOK));
            cells[i][1].addFigure(new Figure(c, Figure.Type.HORSE));
            cells[i][2].addFigure(new Figure(c, Figure.Type.BISHOP));
            cells[i][3].addFigure(new Figure(c, Figure.Type.QUEEN));
            cells[i][4].addFigure(new Figure(c, Figure.Type.KING));
            cells[i][5].addFigure(new Figure(c, Figure.Type.BISHOP));
            cells[i][6].addFigure(new Figure(c, Figure.Type.HORSE));
            cells[i][7].addFigure(new Figure(c, Figure.Type.ROOK));
            c = c.getOppositeColor();
        }
    }

    /**
     * @param x from 0 to 7
     * @param y from 0 to 7
     * @return cell at given coordinates or null if coordinates are out of range
     */
    public Cell cellAt(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            return null;

        return cells[y][x];
    }

    /**
     *Coordinates must refer to cell with figure!
     * @return cells which are reachable from current position
     */
    public ArrayList<Cell> cellsForCorrectMoves(int startX, int startY) {
        return ChessRules.get(this, cellAt(startX, startY)).PossibleCellsForMoves();
    }
    // TODO: 12.03.2020 pawn at end of board
    // TODO: 12.03.2020 logging all moves

    /**
     * Move figure from start to end and make special moves (en passent and roque)
     * start cell must contain figure. Target cell must be reachable from start cell.
     * @return true if after move there is a checkmate
     */
    public boolean moveFigure(int startX, int startY, int endX, int endY) {
        Cell start = cells[startY][startX];
        Cell end = cells[endY][endX];

        end.addFigure(start.removeFigure());
        end.getFigure().movePerformed(currentTurnCount++);

        var analyzer = ChessRules.get(this, end);
        analyzer.performSpecialMoves();
        return analyzer.isCheckmate();
    }

    /**
     * @return text view of chess board
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("  ┌────┬────┬────┬────┬────┬────┬────┬────┐\n");
        for (int y = 7; y >= 0; y--) {
            str.append(y + 1).append(" │");

            for (int x = 0; x <= 7; x++) {
                str.append(cells[y][x]);
                str.append("│");
            }
            str.append("\n  ").append((y == 0) ? "└────┴────┴────┴────┴────┴────┴────┴────┘\n"
                    : "├────┼────┼────┼────┼────┼────┼────┼────┤\n");

        }
        str.append("    A    B    C    D    E    F    G    H   \n");
        return str.toString();
    }
}
