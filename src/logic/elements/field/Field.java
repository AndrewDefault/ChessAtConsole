package logic.elements.field;

import logic.elements.Cell;
import logic.elements.Figure;

import java.util.ArrayList;

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

    public int getCurrentTurnCount() {
        return currentTurnCount;
    }

    private void setupFigures() {
        for (int x = 0; x < 8; x++) {
            // cells[1][x].addFigure(new Figure(Figure.Color.WHITE, Figure.Type.PAWN));
            //cells[6][x].addFigure(new Figure(Figure.Color.BLACK, Figure.Type.PAWN));
        }

        var c = Figure.Color.WHITE;
//        for (int i = 0; i < 10; i += 7) {
//            cells[i][0].addFigure(new Figure(c, Figure.Type.ROOK));
//            cells[i][1].addFigure(new Figure(c, Figure.Type.HORSE));
//            cells[i][2].addFigure(new Figure(c, Figure.Type.BISHOP));
//            cells[i][3].addFigure(new Figure(c, Figure.Type.QUEEN));
//            cells[i][4].addFigure(new Figure(c, Figure.Type.KING));
//            cells[i][5].addFigure(new Figure(c, Figure.Type.BISHOP));
//            cells[i][6].addFigure(new Figure(c, Figure.Type.HORSE));
//            cells[i][7].addFigure(new Figure(c, Figure.Type.ROOK));
//            c = Figure.Color.BLACK;
//        }


        cellAt(4, 0).addFigure(new Figure(Figure.Color.WHITE, Figure.Type.KING));
        cellAt(4, 7).addFigure(new Figure(Figure.Color.BLACK, Figure.Type.ROOK));
        cellAt(0, 3).addFigure(new Figure(Figure.Color.WHITE, Figure.Type.QUEEN));


        var a = cellsForCorrectMoves(0, 3);


        for (var s : a) {
            s.addFigure(new Figure(Figure.Color.BLACK, Figure.Type.KING));
        }


    }


    public Cell cellAt(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            return null;

        return cells[y][x];
    }

    public ArrayList<Cell> cellsForCorrectMoves(int startX, int startY) {

        return ChessMovesAnalyzer.get(this, cellAt(startX, startY)).PossibleCellsForMoves();
    }


    public void moveFigure(int startX, int startY, int endX, int endY) {
        Cell start = cells[startY][startX];
        Cell end = cells[endY][endX];

        end.addFigure(start.removeFigure());
        end.getFigure().movePerformed(currentTurnCount++);

        ChessMovesAnalyzer.get(this, end).passantMove().roqueMove();
    }


    @Override
    public String toString() {
        boolean isBlack = false;
        StringBuilder str = new StringBuilder("  ┌────┬────┬────┬────┬────┬────┬────┬────┐\n");
        for (int y = 7; y >= 0; y--) {
            str.append(y + 1).append(" │");

            for (int x = 0; x <= 7; x++) {
                String filler = isBlack ? "■" : " ";

                str.append(filler);
                if (!cells[y][x].hasFigure()) {
                    str.append(filler).append(filler);
                } else {
                    Figure f = cells[y][x].getFigure();
                    String color = switch (f.getColor()) {
                        case WHITE -> "W";
                        case BLACK -> "B";
                    };
                    String type = switch (f.getType()) {
                        case KING -> "K";
                        case ROOK -> "R";
                        case PAWN -> "P";
                        case HORSE -> "H";
                        case QUEEN -> "Q";
                        case BISHOP -> "B";
                    };
                    str.append(color).append(type);
                }

                str.append(filler).append("│");
                isBlack = !isBlack;
            }
            str.append("\n  ").append((y == 0) ? "└────┴────┴────┴────┴────┴────┴────┴────┘\n"
                    : "├────┼────┼────┼────┼────┼────┼────┼────┤\n");

            isBlack = !isBlack;
        }
        str.append("    A    B    C    D    E    F    G    H   \n");
        return str.toString();
    }
}
