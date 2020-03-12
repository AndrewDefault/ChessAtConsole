package logic.elements;

/**
 * Class that provides cell for field
 */
public class Cell {

    Figure figureAtCell;
    private int x;
    private int y;

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
     * @return Figure, that was in cell
     */
    public Figure removeFigure() {
        var ret = figureAtCell;
        figureAtCell = null;
        return ret;
    }

    /**
     * Add Figure to cell
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
        StringBuilder str = new StringBuilder();

        String filler = (x + y) % 2 == 0 ? "■" : " ";
        str.append(filler);

        if (!hasFigure())
            str.append(filler).append(filler);
        else
            str.append(figureAtCell);

        str.append(filler);
        return str.toString();
    }
}
