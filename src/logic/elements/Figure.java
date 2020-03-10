package logic.elements;

public class Figure {
    private Color color;
    private Type type;
    private int movesCount;

    public Figure(Color color, Type type) {
        this.color = color;
        this.type = type;
        movesCount = 0;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void movePerformed() {
        movesCount++;
    }

    public enum Color {BLACK, WHITE}

    public enum Type {KING, QUEEN, ROOK, HORSE, BISHOP, PAWN}


}
