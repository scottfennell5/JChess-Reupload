package com.chess.base;

public class ValidMove extends Piece {

    private static final int[][] MOVEMENTS = new int[1][1];

    public ValidMove()
    {
        name = "validMove";
        color = "none";
        pieceID = 12;
        setMovements(MOVEMENTS);
    }


}