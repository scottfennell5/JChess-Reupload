//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Main Chess "logic" class, this is where the actual game is "played," rules, game status, game actions, etc. are all done through this class it is
//the entire chess board object and all of its properties and rules associated with interacting with it, with main functions such as holding Piece objects
//in specific positions, moving pieces, checking for spots that are in danger, and checking legality of moves

package com.chess.base;

import java.util.ArrayList;

import com.chess.UI.DeveloperModePane;

@SuppressWarnings({"FieldMayBeFinal"})
public class ChessBoard {
    // 2D board that can hold Pieces (8 x 8)
    private Piece[][] board;
    // 3D board that matches board but doesn't hold Pieces and instead holds ints,
    // the amount in each spot is the amount of Pieces putting that location in
    // danger (it is 3D because each "spot" on the board is actually 2 elements in
    // danger board, one for light team danger and the other dark team danger), (8 x 8 x 2)
    private int[][][] dangerBoard;
    //Holds list of Pieces putting certain spots in dangerBoard in danger
    private ArrayList<ArrayList<String>> spotDangerList;
    // Holds current light Pieces
    private ArrayList<Piece> lightPieces;
    // Holds current dark Pieces
    private ArrayList<Piece> darkPieces;
    // Holds Pieces captured by light
    private ArrayList<Piece> lightCapturedPieces;
    // Holds Pieces captured by dark
    private ArrayList<Piece> darkCapturedPieces;
    //Holds pieces that light pawns can be promoted to, 8 of each kind
    private ArrayList<ArrayList<Piece>> lightPromotionPieces;
    //Holds pieces that dark pawns can be promoted to, 8 of each kind
    private ArrayList<ArrayList<Piece>> darkPromotionPieces;
    // Holds current status of kings being in check or not
    private boolean lightKingInCheck, darkKingInCheck;
    // Holds boolean for if game is from light perspective i.e. true, set to false if
    // it is dark's perspective
    private boolean lightPerspective;
    // Holds current turn (true for light false for dark)
    private boolean lightsTurn;
    //Holds status of light pawn being at the last row respective to the player
    private boolean lightPawnAtLastRank;
    //Holds status of dark pawn being at the last row respective to the player
    private boolean darkPawnAtLastRank;
    //Stores name of king based on perspective (used for check mate and still in check methods)
    private String lightKingName = "king_E1_1", darkKingName = "king_E8_1";
    //Holds boolean for if the current move is an En Passant
    private boolean enPassant;
    //Holds String coordinate of the capture during En Passant
    private String enPassantXY= "";
    //Holds int coordinate of the capture during En Passant
    private int[] enPassantXYInt = {0,0};

    /** Constructor of the ChessBoard and main game methods (player1Color is team color from "set perspective") */
    public ChessBoard(String player1Color) {
        StatStorage.startTurnClock();
        System.out.println("Chess Board Created from " + player1Color + " perspective");
        DeveloperModePane.printText("Chess Board Created from " + player1Color + " perspective");
        board = new Piece[8][8];
        dangerBoard = new int[8][8][2];
        lightKingInCheck = false;
        darkKingInCheck = false;
        lightPerspective = true;
        lightsTurn = true;
        enPassant = false;
        if (player1Color.equals("dark"))
            lightPerspective = false;
        if (!lightPerspective) {
            lightKingName = "king_D8_1";
            darkKingName = "king_D1_1";
        }
        lightCapturedPieces = new ArrayList<>();
        darkCapturedPieces = new ArrayList<>();
        spotDangerList = new ArrayList<>(64);
        lightPromotionPieces = new ArrayList<>(4);
        darkPromotionPieces = new ArrayList<>(4);
        for(int i = 0; i < 64; i++)
            spotDangerList.add(new ArrayList<>());
        for(int i = 0; i < 4; i++) {
            lightPromotionPieces.add(new ArrayList<>());
            darkPromotionPieces.add(new ArrayList<>());
        }
        buildPieces();
        placePiecesAtStartingPositions();
        updateDangerBoard();
    }

    /**
     * Place all created Pieces at standard starting locations on the board.
     */
    private void placePiecesAtStartingPositions() {
        for (int i = 0; i < 16; i++) {
            board[convertToIntXOrY(lightPieces.get(i).getStarterColumnAndRowIndex(), 'y')][convertToIntXOrY(
                lightPieces.get(i).getStarterColumnAndRowIndex(), 'x')] = lightPieces.get(i);
            board[convertToIntXOrY(darkPieces.get(i).getStarterColumnAndRowIndex(), 'y')][convertToIntXOrY(
                darkPieces.get(i).getStarterColumnAndRowIndex(), 'x')] = darkPieces.get(i);
        }
    }

    /**
     * Fill the lightPieces, darkPieces, and promotionPieces ArrayLists with all Piece objects needed to play standard
     * chess. These lists do not change throughout the instance and their elements are just merely used to be copied for
     * other method and lists.
     */
    private void buildPieces() {
        // All this and the proceeding if statement are for	if board is being set up for dark perspective
        int lightDefault0 = 1, darkDefault0 = 8, lightDefault1 = 2, darkDefault1 = 7, letterIncDecDefault = 0;
        if (!lightPerspective) {
            lightDefault0 = 8;
            darkDefault0 = 1;
            lightDefault1 = 7;
            darkDefault1 = 2;
            letterIncDecDefault = 1;
        }
        char letter = 'A';
        lightPieces = new ArrayList<>(16);
        darkPieces = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            lightPieces.add(null);
            darkPieces.add(null);
        }
        for (int i = 0; i < 8; i++) {
            letter = (char) (letter + i);
            lightPieces.set(i, new Pawn(letter, lightDefault1, (i + 1), "light"));
            darkPieces.set(i, new Pawn(letter, darkDefault1, (i + 1), "dark"));
            switch (i) {
                case 0 -> {
                    lightPieces.set(8, new Rook(letter, lightDefault0, 1, "light"));
                    darkPieces.set(8, new Rook(letter, darkDefault0, 1, "dark"));
                }
                case 1 -> {
                    lightPieces.set(9, new Knight(letter, lightDefault0, 1, "light"));
                    darkPieces.set(9, new Knight(letter, darkDefault0, 1, "dark"));
                }
                case 2 -> {
                    lightPieces.set(10, new Bishop(letter, lightDefault0, 1, "light"));
                    darkPieces.set(10, new Bishop(letter, darkDefault0, 1, "dark"));
                }
                case 3 -> {
                    lightPieces.set(11, new Queen((char) (letter + letterIncDecDefault), lightDefault0, 1, "light"));
                    darkPieces.set(11, new Queen((char) (letter + letterIncDecDefault), darkDefault0, 1, "dark"));
                }
                case 4 -> {
                    lightPieces.set(12, new King((char) (letter - letterIncDecDefault), lightDefault0, 1, "light"));
                    darkPieces.set(12, new King((char) (letter - letterIncDecDefault), darkDefault0, 1, "dark"));
                }
                case 5 -> {
                    lightPieces.set(13, new Bishop(letter, lightDefault0, 2, "light"));
                    darkPieces.set(13, new Bishop(letter, darkDefault0, 2, "dark"));
                }
                case 6 -> {
                    lightPieces.set(14, new Knight(letter, lightDefault0, 2, "light"));
                    darkPieces.set(14, new Knight(letter, darkDefault0, 2, "dark"));
                }
                case 7 -> {
                    lightPieces.set(15, new Rook(letter, lightDefault0, 2, "light"));
                    darkPieces.set(15, new Rook(letter, darkDefault0, 2, "dark"));
                }
                default -> {
                    lightPieces.set(15, null);
                    darkPieces.set(15, null);
                }
            }
            letter = 'A';
        }

        // lightDefault0 and darkDefault0 are switched because the promotion pieces are created at the opposite end from
        // the respective team. Numbers are added to respective piece "piece number" attribute to account for the
        // default created pieces. A is used as the default column for every piece and will be changed once piece
        // actually enters the game
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 8; j++) {
                switch (i) {
                    case 0 -> {
                        lightPromotionPieces.get(i).add(new Queen('A', darkDefault0, j + 2, "light"));
                        darkPromotionPieces.get(i).add(new Queen('A', lightDefault0, j + 2, "dark"));
                    }
                    case 1 -> {
                        lightPromotionPieces.get(i).add(new Knight('A', darkDefault0, j + 3, "light"));
                        darkPromotionPieces.get(i).add(new Knight('A', lightDefault0, j + 3, "dark"));
                    }
                    case 2 -> {
                        lightPromotionPieces.get(i).add(new Rook('A', darkDefault0, j + 3, "light"));
                        darkPromotionPieces.get(i).add(new Rook('A', lightDefault0, j + 3, "dark"));
                    }
                    case 3 -> {
                        lightPromotionPieces.get(i).add(new Bishop('A', darkDefault0, j + 3, "light"));
                        darkPromotionPieces.get(i).add(new Bishop('A', lightDefault0, j + 3, "dark"));
                    }
                    default -> {}
                }
            }
        }
    }

    /**
     * Updates dangerBoard by placing in each location the amount of Pieces that is
     * putting that specific spot in danger (negatives for dark team danger and
     * positives for light team danger)
     */
    public void updateDangerBoard() {
        String xy;
        int set, exclude;
        // Resets dangerBoard, so it holds only 0s, resets spotDangerList as well
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                dangerBoard[i][j][0] = 0;
                dangerBoard[i][j][1] = 0;
                spotDangerList.get((i * 8) + j).clear();
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    set = board[i][j].getName().contains("pawn") ? 2 : 0;
                    exclude = board[i][j].getName().contains("king") ? 2 : 0;
                    for (; set < board[i][j].getMovements().length - exclude; set++) {
                        StatStorage.increment(5);
                        if (!moveBlocked(board[i][j].getName(),
                            xy = convertToGoToXY(board[i][j].getName(), board[i][j].getMovements()[set]), false)
                                && specialCaseChecker(board[i][j].getName(), xy, board[i][j].getMovements()[set], true)
                                && !spotDangerList.get((convertToIntXOrY(xy, 'y') * 8) +
                                    convertToIntXOrY(xy, 'x')).contains(board[i][j].getName())
                        ) {
                            if (board[i][j].getColor().equals("dark")) {
                                dangerBoard[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')][1]--;
                            } else {
                                dangerBoard[convertToIntXOrY(xy, 'y')][convertToIntXOrY(xy, 'x')][0]++;
                            }
                            spotDangerList.get((convertToIntXOrY(xy, 'y') * 8) + convertToIntXOrY(xy, 'x')).add(board[i][j].getName());
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates danger board and checks if a king is in check/checkmate.
     */
    public int afterTurnUpdate() {
        updateDangerBoard();
        if (lightsTurn) {
            StatStorage.endTurnClockFor(0);
            lightsTurn = false;
        } else {
            lightsTurn = true;
            StatStorage.endTurnClockFor(1);
        }
        if (inCheckOrDanger(lightKingName, getPieceLocation(lightKingName))) {
            lightKingInCheck = true;
            StatStorage.increment(3);
            System.out.println("Light King In Check");
            DeveloperModePane.printText("Light King In Check");
        } else {
            lightKingInCheck = false;
        }
        if (inCheckOrDanger(darkKingName, getPieceLocation(darkKingName))) {
            darkKingInCheck = true;
            StatStorage.increment(4);
            System.out.println("Dark King In Check");
            DeveloperModePane.printText("Dark King In Check");
        } else {
            darkKingInCheck = false;
        }
        clearJustMoved2();
        enPassant = false;
        StatStorage.startTurnClock();
        return checkmate();
    }

    /**
     * Converts an array of two ints (x, y) transformation and Piece "name" (needed to find current location) into
     * "goToXY" cord once transformation is added to piece location.
     */
    private String convertToGoToXY(String name, int[] transformation)
    {
        String xy = "";
        if ((getPiece(name).getColor().contains("light") && lightPerspective) || (getPiece(name).getColor().contains("dark") && !lightPerspective)) {
            xy += convertToIntXOrY(getPieceLocation(name), 'x') + transformation[0];
            xy += convertToIntXOrY(getPieceLocation(name), 'y') - transformation[1];
        } else {
            xy += convertToIntXOrY(getPieceLocation(name), 'x') - transformation[0];
            xy += convertToIntXOrY(getPieceLocation(name), 'y') + transformation[1];
        }
        return xy;
    }

    /**
     * Adds Piece to a "captured" ArrayList based on Piece color (does not automatically remove Piece from where it is
     * being passed from)
     */
    public void addCapture(Piece piece) {
        (piece.getColor().equals("light") ? darkCapturedPieces : lightCapturedPieces).add(piece);
    }

    /**
     * Inner loop used by {@link ChessBoard#getCaptured}.
     * @param name The name of the piece to consider
     * @param isLight True if we are evaluating the light pieces, false for dark pieces.
     * @return The piece to be promoted.
     */
    private Piece innerGetCaptured(String name, boolean isLight) {
        var pieceList = isLight ? lightPieces : darkPieces;
        var promotionPieces = isLight ? lightPromotionPieces : darkPromotionPieces;
        var otherSide = isLight ? darkCapturedPieces : lightCapturedPieces;
        for (var piece : pieceList) {
            for (var promoList : promotionPieces) {
                for (var promotionPiece : promoList) {
                    if (piece.getName().equals(name) || promotionPiece.getName().equals(name)) {
                        for (int i = 0; i < otherSide.size(); i++) {
                            if (otherSide.get(i).getName().equals(name)) {
                                return otherSide.remove(i);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns and removes Piece with "name" from a capturedPieces ArrayList determined by its team color which is
     * checked by the first for loops.
     */
    public Piece getCaptured(String name) {
        Piece light = innerGetCaptured(name, true);
        return light != null ? light : innerGetCaptured(name, false);
    }

    /**
     * Returns an ArrayList of captured pieces dependent on the color.
     * @param color The color to check.
     * @return The ArrayList of captured pieces.
     */
    @SuppressWarnings("unused")
    public ArrayList<Piece> getCapturedList(String color) {
        return color.equals("light") ? darkCapturedPieces : lightCapturedPieces;
    }

    /**
     * Get the specific array which holds the light promotion at index {@param i}.
     * @param i The index to get.
     * @return The array or null.
     */
    public ArrayList<Piece> getLightPromotionPieceArray(int i) {
        return lightPromotionPieces.get(i);
    }

    /**
     * Get the specific array which holds the dark promotion at index {@param i}.
     * @param i The index to get.
     * @return The array or null.
     */
    public ArrayList<Piece> getDarkPromotionPieceArray(int i) {
        return darkPromotionPieces.get(i);
    }

    /**
     * @return Whether a light pawn is ready for a promotion.
     */
    public boolean isLightPawnReadyForPromotion() {
        return lightPawnAtLastRank;
    }

    /**
     * @return Whether a dark pawn is ready for a promotion.
     */
    public boolean isDarkPawnReadyForPromotion() {
        return darkPawnAtLastRank;
    }

    /**
     * Place a Piece at the specified coordinate.
     * @param piece The Piece to place
     * @param xy The coordinate to place the piece at
     */
    public void placePieceAtLocation(Piece piece, String xy) {
        int x = convertToIntXOrY(xy, 'y');
        int y = convertToIntXOrY(xy, 'x');
        board[x][y] = piece;
    }

    /**
     * Remove and return the piece at (x, y).
     * @param xy The position string.
     * @return The piece.
     */
    public Piece getAndRemovePieceAtLocation(String xy) {
        int x = convertToIntXOrY(xy, 'y');
        int y = convertToIntXOrY(xy, 'x');
        Piece grabbed = board[x][y];
        board[x][y] = null;
        return grabbed;
    }

    /**
     * Returns the location of the piece; searches by {@param name}.
     * @param name The name of the piece to get the location for.
     * @return String with location, or empty string if not found.
     */
    public String getPieceLocation(String name) {
        StatStorage.increment(6);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (board[i][j] != null && board[i][j].getName().equals(name))
                    return "%d%d".formatted(j, i);
        return "";
    }

    /**
     * Returns the piece object that is currently on the board with {@param name} without removing it.
     * @param name The name of the piece to get.
     * @return The piece, or null if something goes horribly wrong.
     */
    public Piece getPiece(String name) {
        int x = convertToIntXOrY(getPieceLocation(name), 'y');
        int y = convertToIntXOrY(getPieceLocation(name), 'x');
        return board[x][y];
    }

    /**
     * @return True if the current turn is for light pieces.
     */
    public boolean isLightsTurn() {
        return lightsTurn;
    }

    /**
     * @return A deep clone of the current board.
     */
    public Piece[][] getChessBoard() {
        Piece[][] copy;
        copy = board;
        return copy;
    }

    /**
     * Searches for the Piece specified by {@param name} and moves it to the specified location on the board as
     * indicated by {@param goToXY}. If {@param actualMove} is true and the piece is a pawn, we use
     * {@link Pawn#setJustMoved2} to ensure that the pawn can't move two spaces a second time.
     *
     * @param name The name of the piece to move
     * @param goToXY The proposed position
     * @param actualMove If the piece is a pawn, it will prevent the pawn from being moved two spaces a second time.
     */
    public void movePieceToLocation(String name, String goToXY, boolean actualMove) {
        //This a special switch that is used to set the justMoved2 flag for pawns that are moving up two spaces on this turn
        int absY = Math.abs(convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y'));
        if (actualMove && name.contains("pawn") && (absY > 1)) ((Pawn)getPiece(name)).setJustMoved2(true);
        int goToX = convertToIntXOrY(goToXY, 'y');
        int goToY = convertToIntXOrY(goToXY, 'x');
        board[goToX][goToY] = getAndRemovePieceAtLocation(getPieceLocation(name));
        // This a special switch that is used to "block" pawns 0,2 transformation once pawn has been moved one time
        if (actualMove && !getPiece(name).getHasMoved()) getPiece(name).setHasMoved(true);
        // This is used in order to update pawn ready for promotion flags before turn is over
        updatePawnReadyForPromotionBooleans();
    }

    /**
     * Determines if a piece specified by {@param name} can move to the specified location {@param goToXY} legally from
     * the current position.
     *
     * @param name The piece to attempt to move.
     * @param goToXY The proposed position.
     * @return True if the move is permitted by the rules, false otherwise.
     */
    public boolean legalMove(String name, String goToXY) {
        boolean legalMove, moveNotBlocked, specialCase, kingNotInCheck, output;
        int[][] transformation = new int[1][2];
        if (Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)) < 3) {
            transformation[0][0] = convertToIntXOrY(goToXY, 'x') - convertToIntXOrY(getPieceLocation(name), 'x');
            transformation[0][1] = -(convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y'));
        }
        else {
            transformation[0][0] = -(convertToIntXOrY(goToXY, 'x') - convertToIntXOrY(getPieceLocation(name), 'x'));
            transformation[0][1] = convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y');
        }
        legalMove =
            board[convertToIntXOrY(getPieceLocation(name), 'y')][convertToIntXOrY(getPieceLocation(name), 'x')]
                .legalMove(transformation);
        moveNotBlocked = !moveBlocked(name, goToXY, true);
        specialCase = specialCaseChecker(name, goToXY, transformation[0], false);
        kingNotInCheck = !kingStillInCheck(name, goToXY);
        output = legalMove && moveNotBlocked && specialCase && kingNotInCheck;
        String debugText = "Legal Move: " +
            legalMove +
            "\nMove Not Blocked: " +
            moveNotBlocked +
            "\nMove Special Case Checker: " +
            specialCase +
            "\nKing Not In Check: " +
            kingNotInCheck;
        System.out.println(debugText);
        DeveloperModePane.printText(debugText);
        return output;
    }

    /**
     * Checks if a piece is in the way of a movement from the current spot to the proposed position {@param goToXY}.
     * It ignores knights, and if {@param checkCapture} is false, it won't check if the spot is blocked by an allied
     * piece.
     *
     * @param name The piece to move.
     * @param goToXY The proposed position.
     * @param checkCapture Whether to check for piece capture.
     * @return True if another piece is blocking the move, false otherwise.
     */
    @SuppressWarnings({"StatementWithEmptyBody", "BooleanMethodIsAlwaysInverted"})
    private boolean moveBlocked(String name, String goToXY, boolean checkCapture) {
        try { // Catches goToXY that are outside the board returning true as in "move is blocked"
            if (board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')] == null);
        } catch (Exception e) {
            return true;
        }
        int x1 = convertToIntXOrY(getPieceLocation(name), 'x'), x2 = convertToIntXOrY(goToXY, 'x'),
            y1 = convertToIntXOrY(getPieceLocation(name), 'y'), y2 = convertToIntXOrY(goToXY, 'y'), xSign = 0,
            ySign = 0, iIncrements = Math.abs(y2 - y1), jIncrements = Math.abs(x2 - x1);
        if (!name.contains("knight")) { //need to check if knight can move based on if something is already there
            if (x2 - x1 != 0)
                xSign = (x2 - x1) / (Math.abs(x2 - x1));
            else
                jIncrements = 1;
            if (y2 - y1 != 0)
                ySign = (y2 - y1) / (Math.abs(y2 - y1));
            else
                iIncrements = 1;
            for (int i = 0, y = y1 + ySign; i < iIncrements; i++, y += ySign)
                for (int j = 0, x = x1 + xSign; j < jIncrements; j++, x += xSign) {
                    StatStorage.increment(7);
                    if (board[y][x] != null && ((i != iIncrements - 1 || j != jIncrements - 1)
                        || ((i == iIncrements - 1 || j == jIncrements - 1) && checkCapture
                        && !isCapture(name, goToXY))))
                        return true;
                    if(iIncrements == jIncrements) {
                        y += ySign;
                        i++;
                    }
                }
        } else {
            return checkCapture && board[convertToIntXOrY(goToXY, 'y')][convertToIntXOrY(goToXY, 'x')] != null && !isCapture(name, goToXY);
        }
        return false;
    }

    /**
     * This is used for four special rules; if the rules aren't met by the move, then it returns false. The rules are
     * as follows:
     * <p>
     *     <ul>
     *         <li>Pawns can't move diagonally unless they are capturing.</li>
     *         <li>Pawns can only perform transformation (0, 2) on that pawn's first move.</li>
     *         <li>Pawns can't capture with a (0, 1) transformation.</li>
     *         <li>
     *             Kings can perform special "castling" in very strict circumstances.
     *             These circumstances are checked, and if permitted, this method automatically moves the respective
     *             rook to its respective position.
     *         </li>
     *     </ul>
     * </p>
     * @param name The piece name to move
     * @param goToXY The proposed position
     * @param transformation The transform array
     * @param skipPawnCheck Whether to skip checking rules 1 through 3.
     * @return True when the rules permit the move.
     */
    private boolean specialCaseChecker(String name, String goToXY, int[] transformation, boolean skipPawnCheck)
    {
        String lightLeftRook = lightPerspective ? "rook_A1_1" : "rook_H8_2";
        String lightRightRook = lightPerspective ? "rook_H1_2" : "rook_A8_1";
        String darkLeftRook = lightPerspective ? "rook_H8_2" : "rook_A1_1";
        String darkRightRook = lightPerspective ? "rook_A8_1" : "rook_H1_2";

        Piece piece = getPiece(name);
        String location = getPieceLocation(name);
        int incDec = 1;
        int pieceStartingRow = 0;
        if (name.charAt(name.length()-3) == '1') pieceStartingRow = 7;
        if (piece.getColor().contains("dark")) incDec = -1;
        if (!lightPerspective) incDec = -incDec;

        int loc = convertToIntXOrY(location, 'x');
        String format = "%d%c";
        String[] rightSideSpots = {
            format.formatted(loc + incDec, location.charAt(1)),
            format.formatted(loc + (incDec * 2), location.charAt(1)),
            format.formatted(loc + (incDec * 3), location.charAt(1))
        };
        String[] leftSideSpots = {
            format.formatted(loc - incDec, location.charAt(1)),
            format.formatted(loc - (incDec * 2), location.charAt(1)),
            format.formatted(loc - (incDec * 3), location.charAt(1))
        };

        int x = convertToIntXOrY(goToXY, 'y');
        int y = convertToIntXOrY(goToXY, 'x');

        if (!skipPawnCheck &&
            name.startsWith("pawn") &&
            (
                (transformation[0] != 0 && board[x][y] == null && !enPassant(name, goToXY)) ||
                (transformation[1] == 2 && piece.getHasMoved()) ||
                (transformation[0] == 0 && isCapture(name, goToXY))
            )
        ) return false;

        //The Rest of method is special case move "Castling"
        if (name.startsWith("king") && (transformation[0] == 2 || transformation[0] == -2)) {
            if (convertToIntXOrY(location, 'y') == pieceStartingRow && !inCheckOrDanger(name,goToXY)) {
                if (piece.getColor().contains("light") && !lightKingInCheck) {
                    if (
                        transformation[0] == 2 &&
                            convertToIntXOrY(getPieceLocation(lightRightRook), 'y') == pieceStartingRow &&
                            !piece.getHasMoved() &&
                            !getPiece(lightRightRook).getHasMoved() &&
                            !inCheckOrDanger(name,rightSideSpots[0])
                    ) {
                        movePieceToLocation(lightRightRook, rightSideSpots[0], true);
                        return true;
                    } else if (
                        transformation[0] == -2 &&
                            convertToIntXOrY(getPieceLocation(lightLeftRook), 'y') == pieceStartingRow &&
                            !piece.getHasMoved() &&
                            !getPiece(lightLeftRook).getHasMoved() &&
                            !inCheckOrDanger(name,leftSideSpots[0]) &&
                            board[convertToIntXOrY(leftSideSpots[2], 'y')][convertToIntXOrY(leftSideSpots[2], 'x')] == null
                    ) {
                        movePieceToLocation(lightLeftRook, leftSideSpots[0], true);
                        return true;
                    }
                    return false;
                } else if (piece.getColor().contains("dark") && !darkKingInCheck) {
                    if(
                        transformation[0] == 2 &&
                            convertToIntXOrY(getPieceLocation(darkRightRook), 'y') == pieceStartingRow &&
                            !piece.getHasMoved() &&
                            !getPiece(darkRightRook).getHasMoved() &&
                            !inCheckOrDanger(name,rightSideSpots[0]) &&
                            board[convertToIntXOrY(rightSideSpots[2], 'y')][convertToIntXOrY(rightSideSpots[2], 'x')] == null
                    ) {
                        movePieceToLocation(darkRightRook, rightSideSpots[0], true);
                        return true;
                    } else if (
                        transformation[0] == -2 &&
                            convertToIntXOrY(getPieceLocation(darkLeftRook), 'y') == pieceStartingRow &&
                            !piece.getHasMoved() &&
                            !getPiece(darkLeftRook).getHasMoved() &&
                            !inCheckOrDanger(name,leftSideSpots[0])
                    ) {
                        movePieceToLocation(darkLeftRook, leftSideSpots[0], true);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return true;
    }

    /**
     * Determines if moving {@param name} to {@param xy} is dangerous by looking at dangerBoard.
     *
     * @param name The piece name
     * @param xy The proposed new position
     * @return Whether the move is dangerous
     */
    private boolean inCheckOrDanger(String name, String xy) {
        StatStorage.increment(11);
        int x = convertToIntXOrY(xy, 'y');
        int y = convertToIntXOrY(xy, 'x');
        int[] it = dangerBoard[x][y];
        return (getPiece(name).getColor().equals("light")) ? it[1] < 0 : it[0] > 0;
    }

    /**
     * The king, still in check, plays out the specified move, updates the danger board, and then determines whether
     * the king is still in check, returns true or false accordingly, and then undoes the specified move no matter what.
     * This function acts as a "simulated move" in order to check threats to the king in special situations.
     *
     * @param name Piece name
     * @param goToXY The new position
     * @return Whether the king is still in check.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean kingStillInCheck(String name, String goToXY) {
        String pastLocation = getPieceLocation(name);
        boolean isCapture = isCapture(name, goToXY);
        String capturedName = "";
        String condition = enPassant ? enPassantXY : goToXY;
        int x = convertToIntXOrY(condition, 'y');
        int y = convertToIntXOrY(condition, 'x');
        if (isCapture) {
            capturedName = board[x][y].getName();
            addCapture(getAndRemovePieceAtLocation(condition));
        }
        movePieceToLocation(name, goToXY, false);
        updateDangerBoard();
        boolean result = (lightsTurn && inCheckOrDanger(lightKingName, getPieceLocation(lightKingName))) ||
            (!lightsTurn && inCheckOrDanger(darkKingName, getPieceLocation(darkKingName)));
        movePieceToLocation(name, pastLocation, false);
        if (isCapture) placePieceAtLocation(getCaptured(capturedName), condition);
        updateDangerBoard();
        return result;
    }

    /**
     * Check to see if one of the kings are in checkmate or stalemate using a similar algorithm to
     * {@link ChessBoard#updateDangerBoard}.
     * @return 0 for neither, 1 for checkmate, 2 for stalemate.
     */
    public int checkmate() {
        int exclude;
        String xy;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (
                    board[i][j] != null &&
                        ((!lightsTurn && board[i][j].getColor().contains("dark")) ||
                            (lightsTurn && board[i][j].getColor().contains("light")))
                ) {
                    exclude = (board[i][j].getName().contains("king")) ? 2 : 0;
                    for (int set = 0; set < board[i][j].getMovements().length - exclude; set++) {
                        StatStorage.increment(8);
                        if (
                            !moveBlocked(board[i][j].getName(),
                            xy = convertToGoToXY(board[i][j].getName(),
                            board[i][j].getMovements()[set]), true) &&
                            specialCaseChecker(board[i][j].getName(), xy, board[i][j].getMovements()[set], false) &&
                            !kingStillInCheck(board[i][j].getName(), xy)
                        ) return 0;
                    }
                }
            }
        }
        if (!darkKingInCheck && !lightKingInCheck) return 2;
        return 1;
    }

    /**
     * Looks to see if a pawn is in position for a promotion, and updates the respective attributes accordingly.
     */
    private void updatePawnReadyForPromotionBooleans() {
        int lastRankRespectiveToLight = lightPerspective ? 0 : 7;
        int lastRankRespectiveToDark = lightPerspective ? 7 : 0;
        for (int i = 0; i < 8; i++) {
            if (board[lastRankRespectiveToLight][i] != null && board[lastRankRespectiveToLight][i].getName().contains("pawn")) {
                lightPawnAtLastRank = true;
                break;
            } else {
                lightPawnAtLastRank = false;
            }
            if (board[lastRankRespectiveToDark][i] != null && board[lastRankRespectiveToDark][i].getName().contains("pawn")) {
                darkPawnAtLastRank = true;
                break;
            } else {
                darkPawnAtLastRank = false;
            }
        }
    }

    /**
     * Reset the Pawn attribute justMoved2 for all Pawns of the player whose turn is about to begin.
     */
    private void clearJustMoved2() {
        Piece current;
        var pieces = lightsTurn ? lightPieces : darkPieces;
        for (Piece piece : pieces) {
            current = piece;
            if (current.name.contains("pawn")) ((Pawn) current).setJustMoved2(false);
        }
    }

    /**
     * Check for a special case for which the En Passant move can be played, and return true if all rules are met.
     * @param name The name of the piece
     * @param goToXY The attempted position
     * @return True if the move can be played, false otherwise
     */
    private boolean enPassant(String name, String goToXY) {
        int x = convertToIntXOrY(getPieceLocation(name), 'y');
        int y = convertToIntXOrY(goToXY, 'x');
        Piece piece = board[x][y];
        if (
            piece != null &&
            piece.name.contains("pawn") &&
            ((lightsTurn && piece.getColor().contains("dark")) ||
            (!lightsTurn && piece.getColor().contains("light"))) && ((Pawn)piece).getJustMoved2()
        ) {
            enPassant = true;
            enPassantXY = "%d%d".formatted(y, x);
            enPassantXYInt[0] = y;
            enPassantXYInt[1] = x;
            return true;
        }
        return false;
    }

    /**
     * @return Whether we're in en passant
     */
    public boolean getEnPassant() {
        return enPassant;
    }

    /**
     * @return The current en passant index string
     */
    public String getEnPassantXY() {
        return enPassantXY;
    }

    /**
     * @return The en passant integer array
     */
    public int[] getEnPassantInt() {
        return enPassantXYInt;
    }

    /**
     * Take in the name of a Piece and check xy to see if the Piece there is on the opposite team.
     * @param name The name of the Piece
     * @param xy The position value
     * @return True if the piece is on the opposite team, false otherwise
     */
    public boolean isCapture(String name, String xy) {
        if(enPassant(name, xy)) return true;
        int x = convertToIntXOrY(xy, 'y');
        int y = convertToIntXOrY(xy, 'x');
        if(board[x][y] != null) return !getPiece(name).getColor().equals(board[x][y].getColor());
        return false;
    }

    /**
     * Helper method for location conversation.
     * @param xy The value to check
     * @param xOrY What values to return
     * @return The integer value parsed from {@param xy}
     */
    private int convertToIntXOrY(String xy, char xOrY) {
        StatStorage.increment(9);
        if (xOrY == 'x')
            return Integer.parseInt(xy.substring(0, 1));
        else
            return Integer.parseInt(xy.substring(1, 2));
    }

    /**
     * @return a string representation of the board with all Piece locations
     */
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece == null)
                    boardString.append("#PositionEmpty_").append(piece).append("#\t");
                else
                    boardString
                        .append(piece.toString(), piece.toString().indexOf('#'), piece.toString().lastIndexOf('#') + 1)
                        .append("\t");
            }
            boardString.append("\n");
        }
        return boardString + "\n\n";
    }

    /**
     * @return a string representation of dangerBoard in its current state with all "danger spots" numbered.
     */
    public String toStringDangerBoard() {
        StringBuilder dangerBoardString = new StringBuilder();
        for (int[][] rowAndCol : dangerBoard) {
            for (int[] z : rowAndCol) {
                for (int num : z) dangerBoardString.append(num).append(" ");
                dangerBoardString.append("\t");
            }
            dangerBoardString.append("\n");
        }
        return dangerBoardString + "\n\n";
    }

    /**
     * @return a string of each piece that is putting a specific spot in danger.
     */
    public String printSpotDangerList() {
        StringBuilder output = new StringBuilder();
        int count = 0;
        for(ArrayList<String> spots : spotDangerList) {
            output.append("::::Danger to spot ").append(count).append("::::\n");
            for(String name : spots) output.append(name).append("\n");
            output.append("\n");
            count++;
        }
        return output.toString();
    }

    /**  used exclusively for visual move pieces
     *   same function as legalMove(), only the print statements are removed
    */
    public boolean legalMoveSuppressed(String name, String goToXY) // Returns t/f if Piece specified by "name" can move to specified location "goToXY" legally from current position
    {
        boolean legalMove = false, moveNotBlocked = false, specialCase = false, kingNotInCheck = false, output = false;
        int[][] transformation = new int[1][2];
        if (Integer.parseInt(name.substring(name.length() - 3, name.length() - 2)) < 3) {
            transformation[0][0] = convertToIntXOrY(goToXY, 'x') - convertToIntXOrY(getPieceLocation(name), 'x');
            transformation[0][1] = -(convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y'));
        }
        else {
            transformation[0][0] = -(convertToIntXOrY(goToXY, 'x') - convertToIntXOrY(getPieceLocation(name), 'x'));
            transformation[0][1] = convertToIntXOrY(goToXY, 'y') - convertToIntXOrY(getPieceLocation(name), 'y');
        }
        output = (legalMove = board[convertToIntXOrY(getPieceLocation(name), 'y')][convertToIntXOrY(getPieceLocation(name), 'x')]
                .legalMove(transformation)) && (moveNotBlocked = !moveBlocked(name, goToXY, true))
                && (specialCase = specialCaseChecker(name, goToXY, transformation[0], false)) && (kingNotInCheck = !kingStillInCheck(name, goToXY));
        return output;
    }

    public void clearVisualPieces(){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] != null){
                    if(board[i][j].name.contains("validMove")){
                        board[i][j] = null;
                    }
                }
            }
        }
    }
}