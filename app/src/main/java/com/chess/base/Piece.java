//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Parent class for all Pieces, this class holds all functions and attributes all Pieces have 
//allowing for easy polymorphism latter on
package com.chess.base;

import java.util.Arrays;

public class Piece {
    protected String name; //Piece name stores Piece kind i.e. "Knight" assigned automatically once specific object
    // is created as well as starting location and number of Piece which is assigned through parameters
    protected String color; //Piece color or "team"
    //Each "row" represents a x y transformation that a Piece can make from current location Each Piece has a set of
    // "possible" movements, meaning there is somewhere on the board where that piece can make that
    //movement, however, the piece won't always be able to use all movements at any location
    protected int[][] MOVEMENTS;
    protected int pieceID;    //ID is used to match specific piece to graphical image using a int
    protected boolean hasMoved; //This stores a boolean specifying if a Piece has moved

    public String getName() {
        return name;
    }

    public String getStarterColumnAndRowIndex() //returns 2 space String with (x,y) cord of Piece in matrix index
    // form ((column,row) starting the origin in the top left and moving positive to the right and down)
    {                                           //when used for a matrix row must be the first input and column will
        // be second input in the Array or ArrayList method
        return switch (name.charAt(name.length() - 4)) {
            case 'A' -> "" + 0 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'B' -> "" + 1 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'C' -> "" + 2 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'D' -> "" + 3 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'E' -> "" + 4 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'F' -> "" + 5 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'G' -> "" + 6 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            case 'H' -> "" + 7 + (8 - Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)));
            default -> "Spot Code Can't Be Represented";
        };
    }

    public String getColor() {
        return color;
    }

    public int[][] getMovements() {
        return MOVEMENTS;
    }

    public int getID() {
        return pieceID;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setMovements(int[][] movements) {
        MOVEMENTS = movements;
    }

    @SuppressWarnings("unused")
    public void setName(String n) {
        name = n;
    }

    public void setColumnAttribute(char columnLetter) { //Allows the column attribute of Piece to be set
        name = name.substring(0, name.length() - 4) + columnLetter + name.substring(name.length() - 3);
    }

    public void setColor(String c) {
        color = c;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean legalMove(int[][] move) //Checks to see if move is possible for piece object that method is evoked
    // on
    {
        for (int[] movement : MOVEMENTS)
            if (movement[0] == move[0][0] && movement[1] == move[0][1])
                return true;
        return false;
    }

    public String toString() //String representation of a Piece
    {
        StringBuilder output = new StringBuilder();
        for (int[] row : MOVEMENTS)
            output.append(Arrays.toString(row)).append(", ");
        return "(Piece Tag: #" + name + "_" + color + "#), (Piece Moves: -" + MOVEMENTS.length + " total- " +
            output.substring(0, output.length() - 2) + "), (Piece ID: " + pieceID + ")";
    }
}
