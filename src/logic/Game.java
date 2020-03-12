package logic;


import logic.elements.Cell;
import logic.elements.Figure;
import logic.elements.field.Field;

import java.util.ArrayList;

public class Game {
    private boolean isGameRunning;
    private Figure.Color whoMoves;

    private Field field;

    public Game() {
        isGameRunning = true;
        whoMoves = Figure.Color.WHITE;
        field = new Field();

        System.out.println(field);
    }

    public Figure.Color whoMovesNow() {
        return whoMoves;
    }

    public boolean isRunning() {
        return isGameRunning;
    }

    public void performMove(int startX, int startY, int lastX, int lastY) {
        field.moveFigure(startX, startY, lastX, lastY);

        whoMoves = whoMoves == Figure.Color.WHITE
                ? Figure.Color.BLACK
                : Figure.Color.WHITE;

        System.out.println(field);
    }


    public ArrayList<Cell> cellContainsCorrectFigureForMove(int startX, int startY) {
        if(field.cellAt(startX, startY).hasFigure()
                && field.cellAt(startX, startY).getFigure().getColor() == whoMoves)
        return field.cellsForCorrectMoves(startX, startY);
        return null;
    }

    public boolean cellIsSuitableForMove(int startX, int startY, int endX, int endY) {
        var r = field.cellsForCorrectMoves(startX, startY);

        return  r.contains(field.cellAt(endX, endY));

    }


}
