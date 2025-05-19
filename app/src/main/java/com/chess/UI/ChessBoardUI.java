//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: This is the graphical ChessBoard object which sets and updates the view and listens for user input
//Some important functions include updating the graphical board by looking at the the "actual" chess board, and 
//implementation of Mouse Events on PieceUI objects, which is done in lambdas. This class is the main user
// interaction with the ChessBoard thus
//this class acts as the house upon the ChessBoard foundation "coupling" the classes closely together
//This class is quite important probably just as impotent as the Chess Board itself
package com.chess.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chess.base.*;

@SuppressWarnings({"unused"})
public class ChessBoardUI extends JLayeredPane {
    private final JPanel gameFoundation;
    private final JPanel boardFoundation;
    private final JPanel topLayer;
    private VisualPanel visualMoveLayer;
    //otherView is used when split screen is active and each instance of this same class need to communicate about
	// updates to the view
    private ChessBoardUI otherView;
    private PromotionUI currentPromotionComponent;
    private final ArrayList<ArrayList<PieceUI>> pieceUITempStorage;
    //Each index corresponds to a Piece ID and each int inside each index corresponds to how many Pieces of that id
	// there needs to be including promotion pieces
    private final int[] NUMBER_OF_PIECES = {10, 10, 10, 9, 1, 8, 10, 10, 10, 9, 1, 8};
    private final boolean darkPerspective;
    private final ChessBoard gameInstance;
    private BoardSquareUI currentHeldSquare;
    private Piece currentHeldActualPiece;
    private GameEventListener dragged;
    private GameEventListener released;
    private GameEventListener promotionPieceClicked;
    private JPanel currentHeldPiece;
    private JPanel startingSquare;
    private int x, y;
    private boolean block;
    private String squareID;
    private String name;
    private int draggedEventInteration;
    private boolean alreadyCalled;
    private boolean visualizeMoves;

    public ChessBoardUI(ChessBoard gameInstance, ChessBoardUI otherView, boolean visualizeMoves) {
        this(gameInstance, otherView, false, false, visualizeMoves);
    }

    //Constructs the Graphical chess board by setting the actual image of the board and building the various layers
	// of JPanels
    public ChessBoardUI(ChessBoard gameInstance, ChessBoardUI otherView, boolean darkPerspective, boolean reverse, boolean visualizeMoves) {
        this.darkPerspective = darkPerspective;
        this.gameInstance = gameInstance;
        this.otherView = otherView;
        System.out.println(gameInstance);
        DeveloperModePane.printText(gameInstance.toString());
        System.out.println(gameInstance.toStringDangerBoard());
        DeveloperModePane.printText(gameInstance.toStringDangerBoard());
        System.out.println(gameInstance.printSpotDangerList());
        DeveloperModePane.printText(gameInstance.printSpotDangerList());
        setPreferredSize(new Dimension(825, 922));
        //setPreferredSize(new Dimension(500, 600));

        pieceUITempStorage = new ArrayList<>(12);
        for (int i = 0; i < 13; i++) {
            pieceUITempStorage.add(new ArrayList<>());
        }

        gameFoundation = new JPanel();
        gameFoundation.setSize(825, 922);
        gameFoundation.setBackground(Color.BLACK);
        gameFoundation.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel chessBoardImageLabel;
        if (reverse)
            chessBoardImageLabel = new JLabel(new CustomImage("/Chess_Board_Reverse.png", 825, 825));
        else
            chessBoardImageLabel = new JLabel(new CustomImage("/Chess_Board.png", 825, 825));
        chessBoardImageLabel.setBounds(0, 97, 825, 825);

        boardFoundation = new JPanel();
        boardFoundation.setBounds(24, 124, 777, 776);
        boardFoundation.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        boardFoundation.setOpaque(false);

        topLayer = new JPanel();
        topLayer.setBounds(0, 97, 825, 825);
        topLayer.setOpaque(false);

        alreadyCalled = false;
        this.visualizeMoves = visualizeMoves;

        generateChessBoardPanelGrid();

        add(gameFoundation);
        add(chessBoardImageLabel, 0);
        add(boardFoundation, 0);
        add(topLayer, 0);



        //Once setup view is completed the "running view" is entered through the run method
        run();
    }

    private void generateChessBoardPanelGrid() { //Generates a Grid of BoardSquareUIs 8 x 8 on top of boardFoundation
        int squareStart63 = 63;
        int squareStart0 = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (darkPerspective) {
                    currentHeldSquare = new BoardSquareUI((7 - j) + "" + (7 - i));
                    currentHeldSquare.setSquareNumber(squareStart63--);
                    boardFoundation.add(currentHeldSquare);
                } else {
                    currentHeldSquare = new BoardSquareUI(j + "" + i);
                    currentHeldSquare.setSquareNumber(squareStart0++);
                    boardFoundation.add(currentHeldSquare);
                }
            }
        }
    }

    private void loadUIPieces() { //Loads PieceUIs needed to play standard chess
        PieceUI pieceUI;
        for (int i = 0; i < 12; i++)
            for (int j = 0; j < NUMBER_OF_PIECES[i]; j++) {
                pieceUI = switch (i) {
                    case 0 -> new PieceUI("/RookW.png", 0);
                    case 1 -> new PieceUI("/KnightW.png", 1);
                    case 2 -> new PieceUI("/BishopW.png", 2);
                    case 3 -> new PieceUI("/QueenW.png", 3);
                    case 4 -> new PieceUI("/KingW.png", 4);
                    case 6 -> new PieceUI("/RookB.png", 6);
                    case 7 -> new PieceUI("/KnightB.png", 7);
                    case 8 -> new PieceUI("/BishopB.png", 8);
                    case 9 -> new PieceUI("/QueenB.png", 9);
                    case 10 -> new PieceUI("/KingB.png", 10);
                    case 11 -> new PieceUI("/PawnB.png", 11);
                    default -> new PieceUI("/PawnW.png", 5);
                };
                //Sets the PieceUI's GameEventListers with the implementations of the GameEventListener interface
                if (dragged != null)
                    pieceUI.setGameEventDragged(dragged);
                if (released != null)
                    pieceUI.setGameEventReleased(released);
                pieceUITempStorage.get(i).add(pieceUI);
            }
    }

    public void updateGraphicalBoard(ChessBoardUI instance) { //Updates the graphical board by looking at the actual
		// board instance, this acts a refresh to account for changes to the board
        //that are not always automatically accounted for
        int square = 0;
        int decInc = 1;
        PieceUI current;

        for (int i = 0; i < 8; i++)  //Removes all PieceUIs
            for (int j = 0; j < 8; j++) {
                if (((BoardSquareUI) instance.boardFoundation.getComponent(square)).getComponentCount() == 1)
                    instance.pieceUITempStorage.get(((PieceUI) ((BoardSquareUI) instance.boardFoundation.getComponent(square)).getComponent(0))
                        .getID()).add(((PieceUI) ((BoardSquareUI) instance.boardFoundation.getComponent(square)).getComponent(0)));
                square++;
            }
        if (instance.darkPerspective) {
            square = 63;
            decInc = -1;
        } else
            square = 0;
        for (int i = 0; i < 8; i++)  //Looks at the actual board and grabs and adds PieceUIs to the right
			// BoardSquareUIs accordingly
            for (int j = 0; j < 8; j++) {
                if (((JPanel) (instance.boardFoundation.getComponent(square))).getComponentCount() != 0)
                    ((JPanel) (instance.boardFoundation.getComponent(square))).removeAll();
                for (int k = 0; k < 12; k++) {
                    StatStorage.increment(12);
                    if (gameInstance.getChessBoard()[i][j] != null && gameInstance.getChessBoard()[i][j].getID() == k) {
                        current = instance.pieceUITempStorage.get(k).remove(0);
                        current.setName(gameInstance.getChessBoard()[i][j].getName());
                        ((JPanel) (instance.boardFoundation.getComponent(square))).add(current);
                        k = 12;
                    }
                }
                square += decInc;
            }
        instance.repaint();
        instance.revalidate();
    }

    private void run() { //This method maintains the graphical board by implementation of game events and is really
		// where the entire
        //game is "played," in all event implementations the graphical board is being updated
        dragged = (evt, errorCode) -> { //Implements PieceUI dragged events
            if (draggedEventInteration == 0) {
                currentHeldPiece = (JPanel) evt.getSource();
                startingSquare = (JPanel) currentHeldPiece.getParent();

                if (!block && (gameInstance.isLightsTurn() && ((PieceUI) evt.getSource()).getID() < 6) || (!gameInstance.isLightsTurn() && ((PieceUI) evt.getSource()).getID() > 5)) {
                    x = ((JPanel) evt.getSource()).getParent().getX() + 24;
                    y = ((JPanel) evt.getSource()).getParent().getY() + 27;
                    topLayer.add(currentHeldPiece);
                    currentHeldPiece.setLocation(x, y);
                    repaint();
                } else {
                    startingSquare.add(currentHeldPiece);
                }

                //new code for move visualization
                if(!alreadyCalled && visualizeMoves){
                    squareID = ((BoardSquareUI) boardFoundation.getComponentAt(x + 24, y + 27)).getID();
                    visualizeMoves(currentHeldPiece, squareID);
                    System.out.println("SquareID: "+squareID);
                    alreadyCalled = true;
                }
                //

            }
            int newX = currentHeldPiece.getX() + evt.getX() - 48, newY = currentHeldPiece.getY() + evt.getY() - 48;
            if (currentHeldPiece.getParent() == topLayer)
                currentHeldPiece.setLocation(newX, newY);
            else {
                startingSquare.add(currentHeldPiece);
                draggedEventInteration = -1;
            }
            repaint();
            x = newX;
            y = newY;
            System.out.println("Dragged to " + newX + ", " + newY);
            DeveloperModePane.printText("Dragged to " + newX + ", " + newY);
            draggedEventInteration++;
        };
        released = (evt, errorCode) -> { //Implements PieceUI released events, and then in turn calls a lot of the
			// ChessBoard logic methods to check for move legality as well as game updates
            revalidate();
            draggedEventInteration = 0;
            name = ((PieceUI) evt.getSource()).getName();

            //move visualization code
            if(visualizeMoves){
                clearVisualMoves();
                squareID = ((BoardSquareUI) boardFoundation.getComponentAt(x + 24, y + 27)).getID();
                System.out.println("SquareID: "+squareID);
                alreadyCalled = false;
            }
            //

            if (boardFoundation.getComponentAt(x + 24, y + 27) != null && boardFoundation.getComponentAt(x + 24, y + 27) != startingSquare && errorCode != -1) {
                squareID = ((BoardSquareUI) boardFoundation.getComponentAt(x + 24, y + 27)).getID();
                System.out.println(name);
                System.out.println(squareID);
                if (gameInstance.legalMove(name, squareID)) {
                    System.out.println(squareID);
                    if (gameInstance.isCapture(name, squareID)) {
                        if (gameInstance.getEnPassant()) {
                            x = gameInstance.getEnPassantInt()[0] * 100;
                            y = gameInstance.getEnPassantInt()[1] * 100;
                            if (darkPerspective) {
                                x = 700 - x;
                                y = 700 - y;
                            }
                            gameInstance.addCapture(gameInstance.getAndRemovePieceAtLocation(gameInstance.getEnPassantXY()));
                        } else
                            gameInstance.addCapture(gameInstance.getAndRemovePieceAtLocation(squareID));
                        pieceUITempStorage.get(((PieceUI) ((JPanel) boardFoundation.getComponentAt(x + 24, y + 27)).getComponent(0)).getID()).add(((PieceUI) ((JPanel) boardFoundation.getComponentAt(x + 24, y + 27)).getComponent(0)));
                        ((JPanel) boardFoundation.getComponentAt(x + 24, y + 27)).removeAll();
                    }
                    gameInstance.movePieceToLocation(name, squareID, true);
                    ((JPanel) boardFoundation.getComponentAt(x + 24, y + 27)).add(((JPanel) evt.getSource()));

                    //This and following else if checks to see if a PromotionUI needs to be added to view if pawn is
					// ready for promotion
                    if (gameInstance.isLightsTurn() && gameInstance.isLightPawnReadyForPromotion()) {
                        currentPromotionComponent = new PromotionUI(false);
                        currentPromotionComponent.setGameEventPromotionPieceClicked(promotionPieceClicked);
                        gameFoundation.add(currentPromotionComponent);
                        block = true;
                        if (otherView != null)
                            otherView.block = true;
                        repaint();
                        revalidate();
                    } else if (!gameInstance.isLightsTurn() && gameInstance.isDarkPawnReadyForPromotion()) {
                        currentPromotionComponent = new PromotionUI(true);
                        currentPromotionComponent.setGameEventPromotionPieceClicked(promotionPieceClicked);
                        gameFoundation.add(currentPromotionComponent);
                        block = true;
                        if (otherView != null)
                            otherView.block = true;
                        repaint();
                        revalidate();
                    } else
                        afterTurn();
                } else if (startingSquare != null)
                    startingSquare.add(((JPanel) evt.getSource()));
            } else if (startingSquare != null) {
                startingSquare.add(((JPanel) evt.getSource()));
            }
            currentHeldSquare = null;
            currentHeldPiece = null;
            startingSquare = null;
            name = null;
            repaint();
            revalidate();
        };
        promotionPieceClicked = (promotionEvt, promotionErrorCode) -> { //Implementation for PromotionUI events,
			// which is used to replace pawn with a newly picked piece
            System.out.println(((JPanel) promotionEvt.getSource()).getName());
            DeveloperModePane.printText(((JPanel) promotionEvt.getSource()).getName());
            gameInstance.getAndRemovePieceAtLocation(squareID);
            if (gameInstance.isLightsTurn())
                currentHeldActualPiece = gameInstance.getLightPromotionPieceArray(Integer.parseInt(((JPanel) promotionEvt.getSource()).getName())).get(0);
            else
                currentHeldActualPiece = gameInstance.getDarkPromotionPieceArray(Integer.parseInt(((JPanel) promotionEvt.getSource()).getName())).get(0);
            currentHeldActualPiece.setColumnAttribute((char) ('A' + Integer.parseInt(squareID.substring(0, 1))));
            System.out.println(squareID);
            gameInstance.placePieceAtLocation(currentHeldActualPiece, squareID);
            gameFoundation.remove(0);
            block = false;
            if (otherView != null)
                otherView.block = false;
            afterTurn();
        };

        loadUIPieces(); //When first Ran the pieces get created
        updateGraphicalBoard(this);
    }

    //places temporary ValidMove pieces to represent all legal moves of the currently held piece
    public void visualizeMoves(JPanel currentPiece, String currentLocationID){
        System.out.println("---------------------------------");
        System.out.println("Visualizing moves....");
        System.out.println("---------------------------------");

        //temporary variable for testing purposes only
        boolean runCheck = true;

        Piece currentActualPiece = gameInstance.getPiece(currentPiece.getName());
        int[][] possibleMoves = currentActualPiece.getMovements();

        System.out.println("visualizing moves for: "+currentActualPiece.getName());

        Boolean isLightsTurn = gameInstance.isLightsTurn();
        JPanel visualPiece;

        ArrayList<String> squareIDList = new ArrayList<String>();

        StringBuilder targetSquareID = new StringBuilder(new String());
        for (int i = 0; i < possibleMoves.length; i++) {
            targetSquareID = new StringBuilder();
            System.out.println("Iteration number: "+i);
            for (int j = 0; j < possibleMoves[i].length; j++) {
                //adds a number to the targetSquareID string, runs twice to form the target square to test for a valid move
                targetSquareID = addMoveToTargetSquareID(isLightsTurn, targetSquareID,
                        currentLocationID, possibleMoves, i, j);
            }

            //if the formed targetSquareID is a valid spot on the board, add a circle if it is a valid move
            if(isOnBoard(targetSquareID.toString())) {
                if(runCheck){
                    if (gameInstance.legalMove(currentActualPiece.getName(), targetSquareID.toString())) {
                        System.out.println("Move " + i + " ID: " + targetSquareID);
                        System.out.println("This is a valid move^");
                        squareIDList.add(targetSquareID.toString());
                    }
                }
                System.out.println("this is on the board: "+targetSquareID);

            }

        }

        addVisualLayerPanel(squareIDList);

        System.out.println("----------------------");
        System.out.println("Done!");
        System.out.println("----------------------");


    }

    public void addVisualLayerPanel(ArrayList<String> squareIDList){
        visualMoveLayer = new VisualPanel(777,776, squareIDList);

        add(visualMoveLayer, 0);
        add(topLayer,0);
    }

    //determines the new target square based on possible moves, one row/column at a time
    public StringBuilder addMoveToTargetSquareID(boolean isLightsTurn, StringBuilder targetSquareID, String currentLocationID, int[][] possibleMoves, int i, int j){
        if(darkPerspective){
            if(isLightsTurn){
                targetSquareID.append(Character.getNumericValue(currentLocationID.charAt(j)) + possibleMoves[i][j]);
            } else {
                targetSquareID.append(Character.getNumericValue(currentLocationID.charAt(j)) - possibleMoves[i][j]);
            }
        } else {
            if(isLightsTurn){
                targetSquareID.append(Character.getNumericValue(currentLocationID.charAt(j)) - possibleMoves[i][j]);
            } else {
                targetSquareID.append(Character.getNumericValue(currentLocationID.charAt(j)) + possibleMoves[i][j]);
            }
        }
        return targetSquareID;
    }

    public boolean isOnBoard(String targetSquareID){
        if(targetSquareID.length() != 2){
            return false;
        } else {
            int row = Character.getNumericValue(targetSquareID.charAt(0));
            int col = Character.getNumericValue(targetSquareID.charAt(1));
            if(row > 7 || col > 7) {
                return false;
            } else {
                return true;
            }
        }

    }

    //scans through the board and removes all temporary visual move pieces
    public void clearVisualMoves(){
        System.out.println("---------------------------------");
        System.out.println("Clearing Visual Moves....");
        System.out.println("---------------------------------");

        remove(visualMoveLayer);

    }

    public void afterTurn() { //This is called after a released event has resulted in a piece being moved to new location and thus a turn has been taken
        //Update both graphical instances
        updateGraphicalBoard(this);
        if (otherView != null)
            updateGraphicalBoard(otherView);
        repaint();
        revalidate();
        System.out.println("Released");
        DeveloperModePane.printText("Released");
        System.out.println(gameInstance);
        DeveloperModePane.printText(gameInstance.toString());
        System.out.println(gameInstance.toStringDangerBoard());
        DeveloperModePane.printText(gameInstance.toStringDangerBoard());
        System.out.println(gameInstance.printSpotDangerList());
        DeveloperModePane.printText(gameInstance.printSpotDangerList());

        //Calls logical afterTurnUpdate on gameInstance and displays GAME OVER Prompt if necessary
        String team = "Dark";
        if (gameInstance.isLightsTurn()) {
            team = "Light";
            StatStorage.increment(0);
        } else
            StatStorage.increment(1);
        String gameOverMsg = "Checkmate! " + team + " Team Wins!";
        switch (gameInstance.afterTurnUpdate()) {
            case 0:
                break;
            case 2:
                gameOverMsg = "Stalemate! The Game Is A Draw!";
            default:
                System.out.println("GAME OVER");
                DeveloperModePane.printText("GAME OVER");
                StatStorage.updateCalculations();
                //Game Over simple JOptionPane, shows stats and gives option to play again or quit
                int input = JOptionPane.showConfirmDialog(this.getParent(), gameOverMsg + " Do you want to play again?\nSTATS::\n" + StatStorage.staticToString(), "GAME OVER", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new CustomImage("/GameOverChessArt.jpg", 420, 379));
                if (input == JOptionPane.YES_OPTION) {
                    ((MainFrame) SwingUtilities.getRoot(this)).dispose();
                    new MainControllerUI();
                } else if (input == JOptionPane.NO_OPTION)
                    System.exit(0);
        }
    }

    public ChessBoardUI getOtherView() { //returns the other view
        return otherView;
    }

    public void setOtherView(ChessBoardUI otherView) { //sets the other view
        this.otherView = otherView;
    }

}











