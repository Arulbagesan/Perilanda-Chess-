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

import static com.perilandachess.board.Move.*;

/**
 * This class represents the Chief on the board
 * The Chief can only move one step in any direction. The game ends when the Chief is captured by the other side.
 */
public class Chief extends Piece {

    // -8 represents north-west direction
    // -7 north
    // -6 north-east
    // -1 west
    // 1 east
    // 6 south-west
    // 7 south
    // 8 south-east
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-8, -7, -6, -1, 1, 6, 7, 8};

    /**
     * Constructor for the chief
     * @param piecePosition position of the chief
     * @param pieceAlliance color of the chief
     */
    public Chief(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.CHIEF);
    }

    /**
     * This method will move the piece, it creates a new chief on destination position of move
     * @param move Move that piece does
     * @return new piece placed on new position
     */
    @Override
    public Chief movePiece(Move move) {
        return new Chief(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    /**
     * This method will calculate all possible legal moves for this piece
     * @param board current state on the board
     * @return collection of all possible moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        // list of legal moves
        List<Move> legalMoves = new ArrayList<>();
        // check each square around the chief
        for(int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            // take its coordinate
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            // check if destination coordinate is a valid coordinate
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                // if chief is on the first or seventh column we need to calculate possible moves differently
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                // if destination tile is not occupied
                if(!(candidateDestinationTile.isTileOccupied())){
                    // add major move
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
                // destination tile is occupied
                else {
                    // take piece from destination tile
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    // take its color
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    // if color is different from chiefs color, add atack move
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        // return the list of legal moves
        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * This method checks if chief is on the first column and possible offsets are -8, -1 and 7
     * Piece can't go north-west, west or south-west from first row
     * @param currentPosition position of the chief
     * @param candidateOffset coordinate of new chief position
     * @return true if king is on the first column and offsets are -8, -1, 7, false otherwise
     */
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -8 || candidateOffset == -1 || candidateOffset == 7);
    }

    /**
     * This method checks if chief is on the seventh column and possible offsets are 8, 1 and 7
     * Piece can't go north-east, east or south-east from seventh row
     * @param currentPosition current position of the chief
     * @param candidateOffset destination position of the chief
     * @return true if king is on the first column and offsets are 8, 1, -7, false otherwise
     */
    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == 8 || currentPosition == 1 || candidateOffset == -7);
    }

    /**
     * String representation of the chief
     * this method calls PieceType.Chief toString method
     * @return String representation of the chief
     */
    @Override
    public String toString(){
        return PieceType.CHIEF.toString();
    }

}
