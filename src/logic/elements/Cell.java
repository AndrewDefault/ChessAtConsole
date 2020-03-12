package logic.elements;

// TODO: 11.03.2020 Asserts are needed!
public class Cell {

    Figure figureAtCell;
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        figureAtCell = null;
    }

    public boolean hasFigure() {
        return figureAtCell != null;
    }

    public Figure getFigure() {
        return figureAtCell;
    }

    public Figure removeFigure() {
        var ret = figureAtCell;
        figureAtCell = null;
        return ret;
    }

    public Figure addFigure(Figure fig) {
        Figure temp = figureAtCell;
        figureAtCell = fig;
        return temp;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");

        String filler = (x + y) % 2 == 0 ? "■" : " ";
        str.append(filler);

        if (!hasFigure())
            str.append(filler + filler);
        else
            str.append(figureAtCell);

        str.append(filler);
        return str.toString();
    }
}
