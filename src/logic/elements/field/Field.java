package logic.elements.field;

import logic.elements.Cell;
import logic.elements.Figure;

import java.util.ArrayList;

public class Field {

    Cell[][] cells;
    ArrayList<Figure> blackFigures;
    ArrayList<Figure> whiteFigures;

    CellsAnalyzer analyzer;

    public Field() {
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                cells[i][j] = new Cell(j, i);

        setupFigures();
        analyzer = new CellsAnalyzer(this);
    }

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
            c = Figure.Color.BLACK;
        }

        for (var cellLine : cells) {
            for (var cell : cellLine) {
                if (cell.hasFigure()) {
                    if (cell.getFigure().getColor() == Figure.Color.WHITE)
                        whiteFigures.add(cell.getFigure());
                    else
                        blackFigures.add(cell.getFigure());
                }
            }
        }

    }

    public Cell cellAt(int x, int y) {
        return cells[y][x];
    }

    //    public ArrayList<Cell> possibleTurnsForCell(int x, int y) {
//        Figure currentFigure = cells[y][x].getFigure();
//        var ret = new ArrayList<Cell>();
//        switch (currentFigure.getType()) {
//            case ROOK:
//
//
//            default:
//        }
//    }


    public void moveFigure(int startX, int startY, int endX, int endY) {
        Cell start = cells[startY][startX];
        Cell end = cells[endY][endX];
        end.addFigure(start.getFigure());
        start.removeFigure();
        end.getFigure().movePerformed();
    }

    private ArrayList<Cell> cellsToLeft(int x, int y) {
        var ret = new ArrayList<Cell>();
        for (int i = x; i >= 0; i--)
            ret.add(cells[y][i]);
        return ret;
    }

    private ArrayList<Cell> cellsToTop(int x, int y) {
        var ret = new ArrayList<Cell>();
        for (int i = y; i < 8; i++)
            ret.add(cells[i][x]);
        return ret;
    }

    private ArrayList<Cell> cellsToBottom(int x, int y) {
        var ret = new ArrayList<Cell>();
        for (int i = x; i >= 0; i--)
            ret.add(cells[i][x]);
        return ret;
    }


    @Override
    public String toString() {
        boolean isBlack = false;
        StringBuilder str = new StringBuilder("  ┌────┬────┬────┬────┬────┬────┬────┬────┐\n");
        for (int y = 7; y >= 0; y--) {
            str.append((y + 1) + " │");

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
                    str.append(color + type);
                }

                str.append(filler).append("│");
                isBlack = !isBlack;
            }
            str.append("\n  " +
                    ((y == 0) ? "└────┴────┴────┴────┴────┴────┴────┴────┘\n"
                            : "├────┼────┼────┼────┼────┼────┼────┼────┤\n")
            );

            isBlack = !isBlack;
        }
        str.append("    A    B    C    D    E    F    G    H   \n");
        return str.toString();
    }
}
