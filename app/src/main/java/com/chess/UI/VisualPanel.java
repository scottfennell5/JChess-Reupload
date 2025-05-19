package com.chess.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Scott - BEGIN ----------------------------------------------------------------------
public class VisualPanel extends JPanel {

    private ArrayList<String> squareIDList;

    public VisualPanel(int x, int y, ArrayList<String> squareIDList) {
        setBounds(24, 124, x, y);
        setOpaque(true);
        this.squareIDList = squareIDList;
    }

    public void paint(Graphics g){
        int[] coords;

        for(int i = 0; i < squareIDList.size(); i++){
            coords = squareIDtoInt(squareIDList.get(i));
            g.fillOval(coords[0],coords[1],75,75);
        }

    }

    //translates the squareID string into the pixel coordinates of the square (storage format: [col,row] or [x,y])
    private int[] squareIDtoInt(String ID){
        int[] coordinates = new int[2];
        int xSquares = Character.getNumericValue(ID.charAt(0));
        int ySquares = Character.getNumericValue(ID.charAt(1));
        coordinates[0] = 10 + (xSquares * 97);
        coordinates[1] = 10 + (ySquares * 97);
        return coordinates;
    }

    // Scott - END ----------------------------------------------------------------------

}
