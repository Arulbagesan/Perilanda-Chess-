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
 * This class represents Excel on the board
 * The Excel can move any number of steps diagonally.
 */
public class Excel extends Piece {

    // -8 represents north-west direction
    // -6 north-east
    // 6 south-west
    // 8 south-east
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -6, 6, 8};

    /**
     * Constructor for the excel
     * @param piecePosition position of the excel
     * @param pieceAlliance color of the excel
     */
    public Excel(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.EXCEL);
    }

    /**
     * This method will move the piece, it creates a new excel on destination position of move
     * @param move Move that piece does
     * @return new piece placed on new position
     */
    @Override
    public Excel movePiece(Move move) {
        return new Excel(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    /**
     * This method will calculate all possible legal moves for this piece
     * @param board current state on the board
     * @return collection of all possible moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        // list of legal moves
        final List<Move> legalMoves = new ArrayList<>();

        // for each of possible offset
        for(int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;

            // while we can go in that direction
            // because we want to go as far as possible
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                // check for exclusion
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || isSeventhColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }

                // calculate destination coordinate
                candidateDestinationCoordinate += candidateCoordinateOffset;

                //check if it is valid coordinate
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                    // take destination tile
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    // check if destination tile is occupied
                    // if it is not occupied add the major move to the list of legal moves
                    if(!(candidateDestinationTile.isTileOccupied())){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    // tile is occupied
                    else {
                        // take the piece from occupied tile
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();

                        // take its color
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        // if destination piece color is different then this piece color add attack move
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        // break to not go further, because excel can not skip over pieces
                        break;
                    }
                }
            }
        }

        //return the list of the legal moves
        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * This method checks if excel is on the first column and possible offsets are -8 and 6
     * Piece can't go north-west or south-west from first row
     * @param currentPosition current piece position
     * @param candidateOffset possible offset
     * @return true if excel is on the first column and possible offsets are -8 and 6
     */
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -8 || candidateOffset == 6);
    }

    /**
     * This method checks if excel is on the first column and possible offsets are 8 and -6
     * Piece can't go north-east or south-east from seventh row
     * @param currentPosition current piece position
     * @param candidateOffset possible offset
     * @return true if excel is on the first column and possible offsets are 8 and -6
     */
    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == 8 || candidateOffset == -6);
    }

    /**
     * String representation of excel
     * this method calls PieceType.Excel toString method
     * @return String representation of excel
     */
    @Override
    public String toString(){
        return PieceType.EXCEL.toString();
    }
}
