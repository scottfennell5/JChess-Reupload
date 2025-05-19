package com.chess.UI;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChessBoardUITest {


    int[][] possibleMoves = new int[][]{{0,1}};
    private ChessBoardUI tester = new ChessBoardUI();

    /**
     * Tests for addMoveToTargetSquareID function
     */
    @Test
    public void testAddMoveToTargetSquareID() {
        //initializing the test StringBuilder
        StringBuilder target = new StringBuilder();
        target = tester.addMoveToTargetSquareID(false,target,"00",possibleMoves,0,0);
        target = tester.addMoveToTargetSquareID(false,target,"00",possibleMoves,0,1);

        //created expected StringBuilder
        StringBuilder expectedTarget = new StringBuilder();
        expectedTarget.append("01");

        //checks if StringBuilders are equal
        assertTrue(tester.isEqualtoStringBuilder(target,expectedTarget));
    }

    /**
     * Tests for isOnBoard function
     * Using the boundary value method
     */
    @Test
    public void testIsOnBoard_Y_Values() {
        assertFalse(tester.isOnBoard("0-1"));
        assertTrue(tester.isOnBoard("00"));
        assertTrue(tester.isOnBoard("04"));
        assertTrue(tester.isOnBoard("07"));
        assertFalse(tester.isOnBoard("08"));
    }

    @Test
    public void testIsOnBoard_X_Values() {
        assertFalse(tester.isOnBoard("-10"));
        assertTrue(tester.isOnBoard("00"));
        assertTrue(tester.isOnBoard("40"));
        assertTrue(tester.isOnBoard("70"));
        assertFalse(tester.isOnBoard("80"));
    }

    @Test
    public void testIsOnBoard_Both_Values() {
        assertFalse(tester.isOnBoard("-1-1"));
        assertTrue(tester.isOnBoard("00"));
        assertTrue(tester.isOnBoard("44"));
        assertTrue(tester.isOnBoard("77"));
        assertFalse(tester.isOnBoard("88"));
    }
}