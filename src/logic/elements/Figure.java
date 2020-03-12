package logic.elements;

public class Figure {
    private Color color;
    private Type type;
    private int movesCount;
    private int lastTurn;

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

    @Override
    public String toString() {
        String sColor = switch (color) {
            case WHITE -> "W";
            case BLACK -> "B";
        };

        String sType = switch (type) {
            case KING -> "K";
            case ROOK -> "R";
            case PAWN -> "P";
            case HORSE -> "H";
            case QUEEN -> "Q";
            case BISHOP -> "B";
        };
        return sColor.concat(sType);
    }

    public enum Color {BLACK, WHITE;
    public Color getOposeColor(){
        return this == BLACK ? WHITE : BLACK;
    }}

    public enum Type {KING, QUEEN, ROOK, HORSE, BISHOP, PAWN}
}
