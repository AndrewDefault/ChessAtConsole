package logic.elements;

public class Cell {

    Figure figureAtCell;
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        figureAtCell = null;
    }

    public Cell(int x, int y, Figure f) {
        this(x, y);
        figureAtCell = f;
    }

    public boolean hasFigure() {
        return figureAtCell != null;
    }

    public Figure getFigure() {
        return figureAtCell;
    }

    public boolean removeFigure() {
        if (figureAtCell != null) {
            figureAtCell = null;
            return true;
        }
        return false;
    }

    public Figure addFigure(Figure fig) {
        Figure temp = figureAtCell;
        figureAtCell = fig;
        return temp;
    }



}
