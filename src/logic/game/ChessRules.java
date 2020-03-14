package logic.game;

import logic.elements.Cell;
import logic.elements.Field;
import logic.elements.Figure;

import java.util.ArrayList;

/**
 * Provides class that analyze field and cell to calculate right moves.
 */
public class ChessRules {
    static int TurnsCount = 0;


    public static ChessTurn performChessTurn(Field field, Cell start, Cell end) {
        ChessTurn turn = new ChessTurn(start, end);

        turn.setFigure(start.getFigure());
        turn.setType(performTurn(field, start, end));
        turn.setTurnResult(getResult(field, end));
        turn.setPromotion(isTurnPromotional(end));

        return turn;
    }

    /**
     * Static method for creating a new copy of analyzer with specified parameters
     *
     * @param field           field for analysis
     * @param cellForAnalysis cell from which analysis is started (mandatory: cell must contain Figure)
     * @return new copy of ChessMovesAnalyzer
     */
    public static ArrayList<Cell> correctMovesFromCell(Field field, Cell cellForAnalysis) {
        return new FieldAnalyzer(field, cellForAnalysis).possibleCellsForMoves();
    }

    public static void addPromotionalFigure(Field field, ChessTurn turn, String move) {
        Cell end = turn.getDestinationCell();

        end.addFigure(new Figure(end.getFigure().getColor(),
                switch (move) {
                    case "q", "Q" -> Figure.Type.QUEEN;
                    case "r", "R" -> Figure.Type.ROOK;
                    case "b", "B" -> Figure.Type.BISHOP;
                    case "h", "H" -> Figure.Type.HORSE;
                    case "p", "P" -> Figure.Type.PAWN;
                    default -> throw new IllegalStateException("Unexpected value: " + move);
                }
        ));
        turn.setPromotionFigure(end.getFigure());
        turn.setTurnResult(getResult(field,end));
    }

    private static ChessTurn.Promotion isTurnPromotional(Cell end) {
        if ((end.getY() == 0 || end.getY() == 7) && end.getFigure().getType() == Figure.Type.PAWN)
            return ChessTurn.Promotion.YES;
        return ChessTurn.Promotion.NO;
    }

    private static ChessTurn.TurnType performTurn(Field field, Cell start, Cell end) {
        if (simpleMove(start, end))
            return ChessTurn.TurnType.CAPTURE;
        if (passantMove(field, end))
            return ChessTurn.TurnType.ENPASSANT;
        if (roqueMove(field, end))
            return ChessTurn.TurnType.ROQUE;
        return ChessTurn.TurnType.SILENT;
    }

    private static ChessTurn.Result getResult(Field field, Cell end) {
        var checkCheck = new FieldAnalyzer(field, end.getFigure().getColor().getOppositeColor());

        if (checkCheck.isCheckmate())
            return ChessTurn.Result.CHECKMATE;
        if (checkCheck.isCheck())
            return ChessTurn.Result.CHECK;
        if (checkCheck.isPat())
            return ChessTurn.Result.PAT;

        return ChessTurn.Result.DEFAULT;
    }

    private static boolean simpleMove(Cell start, Cell end) {
        var ret = end.removeFigure();
        end.addFigure(start.removeFigure());
        end.getFigure().movePerformed(TurnsCount++);
        return ret != null;
    }

    private static boolean passantMove(Field field, Cell cellTarget) {
        var thisY = cellTarget.getY();
        var thisX = cellTarget.getX();

        if (thisY == 5 || thisY == 2) {
            var dY = thisY == 5 ? -1 : 1;
            if (field.cellAt(thisX, thisY).getFigure().getType() == Figure.Type.PAWN
                    && field.cellAt(thisX, thisY).getFigure().getMovesCount() > 2
                    && field.cellAt(thisX, thisY + dY).hasFigure()
                    && field.cellAt(thisX, thisY + dY).getFigure().getType() == Figure.Type.PAWN
                    && field.cellAt(thisX, thisY + dY).getFigure().getMovesCount() == 1
                    && field.cellAt(thisX, thisY + dY).getFigure().getLastTurn() == cellTarget.getFigure().getLastTurn() - 1) {
                field.cellAt(thisX, thisY + dY).removeFigure();
                return true;
            }
        }
        return false;
    }

    private static boolean roqueMove(Field field, Cell targetCell) {
        int yOffset = targetCell.getFigure().getColor() == Figure.Color.WHITE ? 0 : 7;

        if (targetCell.getFigure().getType() == Figure.Type.KING && targetCell.getX() == 2
                && targetCell.getFigure().getMovesCount() == 1) {
            simpleMove(field.cellAt(0, yOffset), field.cellAt(3, yOffset));
            return true;
        }
        if (targetCell.getFigure().getType() == Figure.Type.KING && targetCell.getX() == 6
                && targetCell.getFigure().getMovesCount() == 1) {
            simpleMove(field.cellAt(7, yOffset), field.cellAt(7, yOffset));
            return true;
        }
        return false;
    }
}
