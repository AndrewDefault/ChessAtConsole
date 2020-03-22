package logic.game;

import logic.elements.Cell;
import logic.elements.Field;
import logic.elements.Figure;

import java.util.ArrayList;

/**
 * Provides class with static methods to perform main chess moves or to get cells for move
 */
public class ChessRules {
    static int TurnsCount = 0;

    /**
     * perform turn and analyze it
     *
     * @param field field of game
     * @param start start cell (with figure)
     * @param end   end cell
     * @return ChessTurn object (with information about action figure, type of move, result of this turn)
     */
    public static ChessTurn performChessTurn(Field field, Cell start, Cell end) {
        ChessTurn turn = new ChessTurn(start, end);

        turn.setFigure(start.getFigure());
        turn.setType(performTurn(field, start, end));
        turn.setTurnResult(getResult(field, end));
        turn.setPromotion(isTurnPromotional(end));

        return turn;
    }

    /**
     * Static method for getting cells that can be reached from this cell
     *
     * @param field           field for analysis
     * @param cellForAnalysis cell from which analysis is started (mandatory: cell must contain Figure)
     * @return List of Cells, that can be reached from cellForAnalysis
     */
    public static ArrayList<Cell> correctMovesFromCell(Field field, Cell cellForAnalysis) {
        return new FieldAnalyzer(field, cellForAnalysis).possibleCellsForMoves();
    }

    /**
     * method to handle promotion of the pawn
     * and update turn and result of the turn (possible pat, mate or check)
     *
     * @param field        field where action is performed
     * @param turn         turn, on which action was performed
     * @param figureLetter string that contains letter of new figure for promotion
     */
    public static void addPromotionalFigure(Field field, ChessTurn turn, String figureLetter) {
        Cell end = turn.getDestinationCell();

        end.addFigure(new Figure(end.getFigure().getColor(),
                switch (figureLetter) {
                    case "q", "Q" -> Figure.Type.QUEEN;
                    case "r", "R" -> Figure.Type.ROOK;
                    case "b", "B" -> Figure.Type.BISHOP;
                    case "h", "H" -> Figure.Type.HORSE;
                    case "p", "P" -> Figure.Type.PAWN;
                    default -> throw new IllegalStateException("Unexpected value: " + figureLetter);
                }
        ));
        turn.setPromotionFigure(end.getFigure());
        turn.setTurnResult(getResult(field, end));
    }


    //*******************************************************************************

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
