package logic.game;

import logic.elements.Cell;
import logic.elements.Figure;

/**
 * Class that provides information about performed turn:
 * Turn's figure, result of turn, was it attack or not and etc.
 */
public class ChessTurn {
    private TurnType type = TurnType.SILENT;
    private Result turnResult = Result.DEFAULT;
    private Promotion promotion = Promotion.NO;

    private Figure figure = null;
    private Figure promotionFigure = null;

    private Cell from;
    private Cell to;

    ChessTurn(Cell from, Cell to) {
        this.from = from;
        this.to = to;
    }

    public Figure getFigure() {
        return figure;
    }

    void setFigure(Figure figure) {
        this.figure = figure;
    }

    public TurnType getType() {
        return type;
    }

    void setType(TurnType type) {
        this.type = type;
    }

    public Result getTurnResult() {
        return turnResult;
    }

    void setTurnResult(Result turnResult) {
        this.turnResult = turnResult;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Figure getPromotionFigure() {
        return promotionFigure;
    }

    void setPromotionFigure(Figure promotionFigure) {
        this.promotionFigure = promotionFigure;
    }

    public Cell getDestinationCell() {
        return to;
    }

    public Cell getFromCell(){
        return from;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getFigure().getColor().toString())
                .append(" ")
                .append(getFigure().getType())
                .append(" moves in ")
                .append(getType()).append(" from ")
                .append(getFromCell().letterNumbCoordinates())
                .append(" to ")
                .append(to.letterNumbCoordinates())
                .append(".\n");
        if (getPromotion() == Promotion.YES) {
            str.append("PAWN got promotion and turned into ")
                    .append(getPromotionFigure().getType())
                    .append(".\n");
        }
        if (getTurnResult() != Result.DEFAULT) {
            str.append("Result of turn: ")
                    .append(getTurnResult())
                    .append(" to ")
                    .append(getFigure().getColor().getOppositeColor())
                    .append(" figures.\n");
        }
        str.append("\n");
        return str.toString();
    }

    public enum TurnType {CAPTURE, SILENT, ROQUE, ENPASSANT}

    public enum Result {CHECK, CHECKMATE, PAT, DEFAULT}

    public enum Promotion {YES, NO}
}
