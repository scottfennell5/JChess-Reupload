package com.chess.UI;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class VisualPanelTest {

    @Test
    public void testSquareIDtoInt() {
        VisualPanel tester = new VisualPanel();
        int[] testCoords = tester.squareIDtoInt("00");
        int[] expectedCoords = new int[]{10, 10};
        assertTrue(tester.compareCoords(expectedCoords,testCoords));
    }

    @Test
    public void testCompareCoords() {
        VisualPanel tester = new VisualPanel();
        int[] testCoords1 = new int[]{100,50};
        int[] testCoords2 = new int[]{100,50};
        assertTrue(tester.compareCoords(testCoords1,testCoords2));

    }
}