package logic.game;

import logic.elements.Cell;
import logic.elements.Figure;

public class ChessTurn {
    TurnType type = TurnType.SILENT;
    Result turnResult = Result.DEFAULT;
    Promotion promotion = Promotion.NO;

    Figure figure = null;
    Figure promotionFigure = null;

    Cell from;
    Cell to;

    ChessTurn(Cell from, Cell to) {
        this.from = from;
        this.to = to;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public TurnType getType() {
        return type;
    }

    public void setType(TurnType type) {
        this.type = type;
    }

    public Result getTurnResult() {
        return turnResult;
    }

    public void setTurnResult(Result turnResult) {
        this.turnResult = turnResult;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Figure getPromotionFigure() {
        return promotionFigure;
    }

    public void setPromotionFigure(Figure promotionFigure) {
        this.promotionFigure = promotionFigure;
    }

    public Cell getDestinationCell() {
        return to;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(figure.getColor().toString()).append(" ").append(figure.getType()).append(" moves in ")
                .append(type).append(" from ").append(from.letterNumbCoordinates()).
                append(" to ").append(to.letterNumbCoordinates()).append(".\n");
        if (promotion == Promotion.YES) {
            str.append("PAWN got promotion and turned into ").append(promotionFigure.getType()).append(".\n");
        }
        if (turnResult != Result.DEFAULT) {
            str.append("Result of turn: ").append(turnResult).append(" to ").append(figure.getColor().getOppositeColor())
                    .append(" figures.\n");
        }
        str.append("\n");
        return str.toString();

    }

    public enum TurnType {CAPTURE, SILENT, ROQUE, ENPASSANT}

    public enum Result {CHECK, CHECKMATE, PAT, DEFAULT}

    public enum Promotion {YES, NO}
}
