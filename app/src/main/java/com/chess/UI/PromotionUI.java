//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This class is the component added to view when a pawn is ready for promotion, which has specific
// attributes
package com.chess.UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PromotionUI extends JPanel {

    private final JPanel[] innerPanels;
    private JLabel pieceLabel;
    private CustomImage image;
    private GameEventListener promotionPieceClicked;

    public PromotionUI(boolean darkPieces) { //Sets general JPanel settings
        setSize(388, 97);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
        innerPanels = new JPanel[4];
        if (darkPieces)
            generateDarkPromotionUI();
        else
            generateLightPromotionUI();
    }

    private void generateLightPromotionUI() { //Generates a panel with light team images
        for (int i = 0; i < 4; i++) {
            innerPanels[i] = new JPanel();
            innerPanels[i].setPreferredSize(new Dimension(97, 97));
            innerPanels[i].setOpaque(false);
            switch (i) {
                case 0 -> pieceLabel = new JLabel(image = new CustomImage("/QueenW.png", 95, 95));
                case 1 -> pieceLabel = new JLabel(image = new CustomImage("/KnightW.png", 95, 95));
                case 2 -> pieceLabel = new JLabel(image = new CustomImage("/RookW.png", 95, 95));
                case 3 -> pieceLabel = new JLabel(image = new CustomImage("/BishopW.png", 95, 95));
                default -> {}
            }
            generatePromotionUI(i);
        }
    }

    private void generatePromotionUI(int i) {
        pieceLabel.setSize(95, 95);
        innerPanels[i].setLayout(new GridLayout());
        innerPanels[i].add(pieceLabel);
        innerPanels[i].setName(i + "");
        innerPanels[i].addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { //Adds clicked listeners to the individual panels where
                // the implementation is in ChessBoardUI
                if (image.getColorAtPoint(evt.getX(), evt.getY()).getAlpha() != 0)
                    promotionPieceClicked.gameAction(evt, 0);
            }
        });
        add(innerPanels[i]);
    }

    private void generateDarkPromotionUI() { //Generates a panel with dark team images
        for (int i = 0; i < 4; i++) {
            innerPanels[i] = new JPanel();
            innerPanels[i].setPreferredSize(new Dimension(97, 97));
            innerPanels[i].setOpaque(false);
            switch (i) {
                case 0 -> pieceLabel = new JLabel(image = new CustomImage("/QueenB.png", 95, 95));
                case 1 -> pieceLabel = new JLabel(image = new CustomImage("/KnightB.png", 95, 95));
                case 2 -> pieceLabel = new JLabel(image = new CustomImage("/RookB.png", 95, 95));
                case 3 -> pieceLabel = new JLabel(image = new CustomImage("/BishopB.png", 95, 95));
                default -> {
                }
            }
            generatePromotionUI(i);
        }
    }

    public void setGameEventPromotionPieceClicked(GameEventListener gel) { //Setter for the GameEventListener
        this.promotionPieceClicked = gel;
    }
}
