package logic.elements;

public class Figure {
    private Color color;
    private Type type;
    private int movesCount;
    private  int lastTurn;

    public Figure(Color color, Type type) {
        this.color = color;
        this.type = type;
        movesCount = 0;
        lastTurn = 0;
    }

    public int getLastTurn() {
        return lastTurn;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void movePerformed(int k) {
        lastTurn = k;
        movesCount++;
    }

    public enum Color {BLACK, WHITE}

    public enum Type {KING, QUEEN, ROOK, HORSE, BISHOP, PAWN}


}
