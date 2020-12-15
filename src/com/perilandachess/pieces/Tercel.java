package com.perilandachess.pieces;

import com.perilandachess.Alliance;
import com.perilandachess.board.Board;
import com.perilandachess.board.BoardUtils;
import com.perilandachess.board.Move;
import com.perilandachess.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class represents Tercel on the board
 * The Tercel can move any number of steps up and down, or left and right.
 */
public class Tercel extends Piece {

    // -7 represents north direction
    // -1 west
    // 1 east
    // 7 south
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-7, -1, 1, 7};

    /**
     * Constructor for the tercel
     * @param piecePosition position of the tercel
     * @param pieceAlliance color of the tercel
     */
    public Tercel(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.EXCEL);
    }

    /**
     * This method will move the piece, it creates a new tercel on destination position of move
     * @param move Move that piece does
     * @return new piece placed on new position
     */
    @Override
    public Tercel movePiece(Move move) {
        return new Tercel(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());

    }

    /**
     * This method will calculate all possible legal moves for this piece
     * @param board current state on the board
     * @return collection of all possible moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        // list of legal mvoes
        final List<Move> legalMoves = new ArrayList<>();

        //for each of possible destination offsets
        for(int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            //while we can go in that direction
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                // check for exclusion
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || isSeventhColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }

                // calculate next destination coordinate
                candidateDestinationCoordinate += candidateCoordinateOffset;

                //check if it is valid
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                    //take the destination tile
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    //if it is not occupied add a major move and proceed
                    if(!(candidateDestinationTile.isTileOccupied())){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    // tile is occupied
                    else {
                        // take the piece from destination coordinate
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();

                        //take the color of the piece
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        // check if destination piece and this piece have different colors
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }

                        // break since tercel can't go over pieces
                        break;
                    }
                }
            }
        }

        // return the list of legal moves
        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * This method checks if tercel is on the first column and possible offsets is -1
     * Piece can't go west from first row
     * @param currentPosition current piece position
     * @param candidateOffset possible offset
     * @return true if excel is on the first column and possible offsets is -1
     */
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == -1;
    }

    /**
     * This method checks if tercel is on the first column and possible offsets is 1
     * Piece can't go east from seventh row
     * @param currentPosition current piece position
     * @param candidateOffset possible offset
     * @return true if excel is on the first column and possible offsets is 1
     */
    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && candidateOffset == 1;
    }

    /**
     * String representation of the tercel
     * This method calls PieceType.Tercel toString method
     * @return String representation of the tercel
     */
    @Override
    public String toString(){
        return PieceType.TERCEL.toString();
    }
}
