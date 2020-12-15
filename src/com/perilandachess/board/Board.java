package com.perilandachess.board;

import com.perilandachess.Alliance;
import com.perilandachess.pieces.*;
import com.perilandachess.player.GreenPlayer;
import com.perilandachess.player.OrangePlayer;
import com.perilandachess.player.Player;

import java.io.*;
import java.util.*;

public class Board {
    // list of tiles represents a board
    private final List<Tile> gameBoard;

    // orange pieces
    private final Collection<Piece> orangePieces;

    // green pieces
    private final Collection<Piece> greenPieces;

    // orange player
    private final OrangePlayer orangePlayer;

    // green player
    private final GreenPlayer greenPlayer;

    // current player
    private final Player currentPlayer;

    /**
     * Constructor for the board class
     * constructor is private and it takes Builder to set up the board
     * @param builder builder for the board
     */
    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.orangePieces = calculateActivePieces(this.gameBoard, Alliance.ORANGE);
        this.greenPieces = calculateActivePieces(this.gameBoard, Alliance.GREEN);
        final Collection<Move> orangeStandardLegalMoves = calculateLegalMoves(this.orangePieces);
        final Collection<Move> greenStandardLegalMoves = calculateLegalMoves(this.greenPieces);
        this.orangePlayer = new OrangePlayer(this, orangeStandardLegalMoves, greenStandardLegalMoves);
        this.greenPlayer = new GreenPlayer(this, orangeStandardLegalMoves, greenStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(orangePlayer, greenPlayer);
    }

    /**
     * This method returns orange pieces
     * @return collection of orange pieces
     */
    public Collection<Piece> getOrangePieces(){
        return orangePieces;
    }

    /**
     * This method returns green pieces
     * @return collection of green pieces
     */
    public Collection<Piece> getGreenPieces(){
        return greenPieces;
    }

    /**
     * This method returns green player
     * @return green player
     */
    public Player getGreenPlayer(){
        return greenPlayer;
    }

    /**
     * This method returns orange player
     * @return orange player
     */
    public Player getOrangePlayer(){
        return orangePlayer;
    }

    /**
     * This method returns current player
     * @return current player
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * This method gets all possible legal moves for both players
     * @return List of moves
     */
    public List<Move> getAllLegalMoves(){
        List<Move> resultList = new ArrayList<>();
        resultList.addAll(orangePlayer.getLegalMoves());
        resultList.addAll(greenPlayer.getLegalMoves());
        return resultList;
    }

    /**
     * This method calculates all active pieces on the board
     * @param gameBoard board with the pieces
     * @param alliance color
     * @return collection of the pieces on the board
     */
    private Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        List<Piece> activePieces = new ArrayList<>();

        // go through list of tiles, and for each tile
        for(Tile tile : gameBoard){
            // if tile is occupied
            if(tile.isTileOccupied()){
                // take a piece from tile
                final Piece piece = tile.getPiece();

                // if it is desired color, add it to list of active pieces
                if(piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }
        return activePieces;
    }

    /**
     * This method will create a list of tiles that represents the position on the board
     * @param builder contains informations about pieces and its position
     * @return List of tiles with pieces
     */
    private static List<Tile> createGameBoard(Builder builder) {
        List<Tile> tiles = new ArrayList<>();
        // create 49 tiles with or without piece on it depending on builder state
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            tiles.add(Tile.createTile(i, builder.boardConfig.get(i)));
        }
        return Collections.unmodifiableList(tiles);
    }

    /**
     * This method gets specific tile
     * @param tileCoordinate coordinate of desired tile, a7 has position 0, b7 has position 1 and so on
     * @return desired tile
     */
    public Tile getTile(int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    /**
     * This method will create a starting position on the board
     * @return board with start position
     */
    public static Board createStandardBoard(){
        final Builder builder = new Builder();


        // set orange pieces
        builder.setPiece(new Tercel(0, Alliance.ORANGE));
        builder.setPiece(new Excel(1, Alliance.ORANGE));
        builder.setPiece(new Trident(2, Alliance.ORANGE));
        builder.setPiece(new Chief(3, Alliance.ORANGE));
        builder.setPiece(new Trident(4, Alliance.ORANGE));
        builder.setPiece(new Excel(5, Alliance.ORANGE));
        builder.setPiece(new Tercel(6, Alliance.ORANGE));
        builder.setPiece(new Advancer(7, Alliance.ORANGE));
        builder.setPiece(new Advancer(8, Alliance.ORANGE));
        builder.setPiece(new Advancer(9, Alliance.ORANGE));
        builder.setPiece(new Advancer(10, Alliance.ORANGE));
        builder.setPiece(new Advancer(11, Alliance.ORANGE));
        builder.setPiece(new Advancer(12, Alliance.ORANGE));
        builder.setPiece(new Advancer(13, Alliance.ORANGE));

        // set green pieces
        builder.setPiece(new Tercel(48, Alliance.GREEN));
        builder.setPiece(new Excel(47, Alliance.GREEN));
        builder.setPiece(new Trident(46, Alliance.GREEN));
        builder.setPiece(new Chief(45, Alliance.GREEN));
        builder.setPiece(new Trident(44, Alliance.GREEN));
        builder.setPiece(new Excel(43, Alliance.GREEN));
        builder.setPiece(new Tercel(42, Alliance.GREEN));
        builder.setPiece(new Advancer(41, Alliance.GREEN));
        builder.setPiece(new Advancer(40, Alliance.GREEN));
        builder.setPiece(new Advancer(39, Alliance.GREEN));
        builder.setPiece(new Advancer(38, Alliance.GREEN));
        builder.setPiece(new Advancer(37, Alliance.GREEN));
        builder.setPiece(new Advancer(36, Alliance.GREEN));
        builder.setPiece(new Advancer(35, Alliance.GREEN));

        // first player to play is green
        builder.setMoveMaker(Alliance.GREEN);

        return builder.build();
    }

    /**
     * Create a board from file with random position
     * @param selectedFile file that contains information about board position
     * @return Board with given position
     * @throws IOException thrown if there is some IO error
     * @throws IllegalFormatException thrown when file format is incorrect
     */
    public static Board createCustomBoard(File selectedFile) throws IOException, IllegalFormatException {
        final Builder builder = new Builder();

        String row = "";
        int rowNumber = 0;
        boolean hasGreenChief = false;
        boolean hasOrangeChief = false;

        // open file to read
        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));

        // read 7 lines or all lines and place pieces on the board
        while(rowNumber < 7 && (row = reader.readLine()) != null){

            // trim line so it has no spaces befor or after, and then split it around whitespaces
            String[] tiles = row.trim().split("\\s+");

            // if there is 7 pieces in a row
            if(tiles.length == 7){
                // take each piece and put it to builder
                for(int i = 0; i < 7; i++){
                    switch (tiles[i]){
                        case "T":
                            builder.setPiece(new Tercel(rowNumber * 7 + i, Alliance.ORANGE));
                            break;
                        case "E":
                            builder.setPiece(new Excel(rowNumber * 7 + i, Alliance.ORANGE));
                            break;
                        case "R":
                            builder.setPiece(new Trident(rowNumber * 7 + i, Alliance.ORANGE));
                            break;
                        case "C":
                            builder.setPiece(new Chief(rowNumber * 7 + i, Alliance.ORANGE));
                            hasOrangeChief = true;
                            break;
                        case "A":
                            builder.setPiece(new Advancer(rowNumber * 7 + i, Alliance.ORANGE));
                            break;
                        case "t":
                            builder.setPiece(new Tercel(rowNumber * 7 + i, Alliance.GREEN));
                            break;
                        case "e":
                            builder.setPiece(new Excel(rowNumber * 7 + i, Alliance.GREEN));
                            break;
                        case "r":
                            builder.setPiece(new Trident(rowNumber * 7 + i, Alliance.GREEN));
                            break;
                        case "c":
                            builder.setPiece(new Chief(rowNumber * 7 + i, Alliance.GREEN));
                            hasGreenChief = true;
                            break;
                        case "a":
                            builder.setPiece(new Advancer(rowNumber * 7 + i, Alliance.GREEN));
                            break;
                    }
                }
                rowNumber++;
            }else{
                // there is no enough information for one row
                throw new IllegalArgumentException("Invalid input file!");
            }
        }

        // if there is no a chief game is over
        if(!hasGreenChief){
            throw new IllegalArgumentException("There is no green chief on saved game!");
        }else if(!hasOrangeChief){
            throw new IllegalArgumentException("There is no orange chief on saved game!");
        }

        // next line represents next move maker
        row = reader.readLine();

        // if there is no next move maker
        if(row == null){
            throw new IllegalArgumentException("There is no next move maker!");
        }

        // if next move maker is G, set the Green
        if("G".equals(row.trim())){
            builder.setMoveMaker(Alliance.GREEN);
        }
        // if next move maker is O, set the Orange
        else if("O".equals(row.trim())){
            builder.setMoveMaker(Alliance.ORANGE);
        }
        // invalid next move maker
        else{
            throw new IllegalArgumentException("Invalid next move maker!");
        }

        // close the file
        reader.close();

        return builder.build();
    }

    /**
     * This method creates a list of all legal moves for given collection
     * @param pieces pieces for is needed to calculate legal moves
     * @return List of legal moves
     */
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces){
        List<Move> legalMoves = new ArrayList<>();

        // calculate legal moves for each pieces and add to the list of legal moves
        for(Piece piece : pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }

        return legalMoves;
    }

    /**
     * String representation of the board
     * @return String representation of the boards
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){
            String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * Builder class for board
     */
    public static class Builder{

        // map of board position, maps coordinate to piece
        Map<Integer, Piece> boardConfig;

        // color of next move maker
        Alliance nextMoveMaker;

        /**
         * Constructor for the builder
         */
        public Builder(){
            boardConfig = new HashMap<>();
        }

        /**
         * This method sets the piece on its position
         * @param piece Piece to be put on board
         * @return builder with given pieces inside
         */
        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        /**
         * This method sets next move maker
         * @param nextMoveMaker next move maker
         * @return builder with changed move maker
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        /**
         * This method returns a new board with position same as in map in builder
         * @return board set up
         */
        public Board build(){
            return new Board(this);
        }
    }
}
