package logic.elements;

/**
 * Provides class for different chess figures
 */
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

    /**
     * increases number of moves and set id of last turn with this figure
     * @param idOfLastTurn id of last turn
     */
    public void movePerformed(int idOfLastTurn) {
        lastTurn = idOfLastTurn;
        movesCount++;
    }

    /**
     * @return text view of figure (with type and color)
     */
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

    /**
     * Enum for figure color
     */
    public enum Color {BLACK, WHITE;

        /**
         * @return opposite color
         */
    public Color getOposeColor(){
        return this == BLACK ? WHITE : BLACK;
    }}

    /**
     * Enum for different figure types
     */
    public enum Type {KING, QUEEN, ROOK, HORSE, BISHOP, PAWN}
}
