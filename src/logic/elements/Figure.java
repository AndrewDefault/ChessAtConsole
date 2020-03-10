package logic.elements;

public class Figure {
    private Color color;
    private Type type;
    private boolean isTouched;
    private  int lastTurn;

    public Figure(Color color, Type type) {
        this.color = color;
        this.type = type;
        isTouched = false;
        lastTurn = 0;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void movePerformed(int k) {
        lastTurn = k;
        isTouched = true;
    }

    public int getLastTurn() {
        return lastTurn;
    }

    public enum Color {BLACK, WHITE}

    public enum Type {KING, QUEEN, ROOK, HORSE, BISHOP, PAWN}


}
