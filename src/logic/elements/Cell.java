package logic.elements;

import java.util.Comparator;

/**
 * Class that provides cell for field
 */
public class Cell {
    Figure figureAtCell;
    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        figureAtCell = null;
    }

    /**
     * @return true if cell has figure;
     */
    public boolean hasFigure() {
        return figureAtCell != null;
    }

    /**
     * @return Figure in this cell or null if there is no figure in cell
     */
    public Figure getFigure() {
        return figureAtCell;
    }

    /**
     * Removes figure from  cell
     *
     * @return Figure, that was in cell
     */
    public Figure removeFigure() {
        var ret = figureAtCell;
        figureAtCell = null;
        return ret;
    }

    /**
     * Add Figure to cell
     *
     * @param fig figure
     */
    public void addFigure(Figure fig) {
        figureAtCell = fig;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @return simple text view of cell
     */
    @Override
    public String toString() {
        String filler = (x + y) % 2 == 0 ? "■" : " ";

        return filler + (hasFigure() ? figureAtCell : filler + filler) + filler;
    }

    public String letterNumbCoordinates() {
        return (char) ('A' + getX()) + String.valueOf(getY() + 1);
    }
}
