package logic.elements.field;

import logic.elements.Cell;

import java.util.ArrayList;

public class CellsAnalyzer {
    Field field;

    CellsAnalyzer(Field field) {
        this.field = field;
    }

    ArrayList<Cell> cellsForMove(Cell cell) {
        var ret = new ArrayList<Cell>();


        return ret;
    }

    private ArrayList<Cell> cellsToRight(int x, int y) {
        var ret = new ArrayList<Cell>();
        for (int i = x + 1; i < 8; i++) {
            if (field.cellAt(i, y).hasFigure()) {
                if (field.cellAt(i, y).getFigure().getColor() != field.cellAt(x, y).getFigure().getColor()) {
                    ret.add(field.cellAt(i, y));
                    return ret;
                }
                return ret;
            }
            ret.add(field.cellAt(i, y));
        }
        return ret;
    }


}
