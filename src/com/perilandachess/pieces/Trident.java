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
 * This class represents Trident on the board
 * The Trident can move any number of steps to the left or right, but only 1 step forward. When it reaches the opposing side of the board, it will turn around.
 */
public class Trident extends Piece {
    // -1 represents west direction
    // 1 east
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-1, 1};
    // 7 represents south (or north based on direction and alliance)
    private final static int CANDIDATE_MOVE_COORDINATE = 7;

    // direction of the trident, 1 for normal direction -1 for the opposite direction
    private int direction;

    /**
     * Constructor for the trident
     * @param piecePosition position of the trident
     * @param pieceAlliance color of the trident
     */
    public Trident(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.TRIDENT);
        this.direction = 1;
    }

    /**
     * This method gets direction of the trident
     * @return direction of the trident, 1 for normal direction, -1 for opposite
     */
    public int getDirection(){
        return direction;
    }

    /**
     * This method will move the piece, it creates a new trident on destination position of move
     * @param move Move that piece does
     * @return new piece placed on new position
     */
    @Override
    public Trident movePiece(Move move) {
        Trident newTrident = new Trident(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
        newTrident.direction = this.direction;
        return newTrident;
    }

    /**
     * This method change direction of the trident movement
     */
    public void changeDirection(){
        direction *= -1;
    }

    /**
     * This method will calculate all possible legal moves for this piece
     * @param board current state on the board
     * @return collection of all possible moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        // check for east and west direction
        for(int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            // while destination coordinate is valid coordinate
            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                // check for exclusion
                if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || isSeventhColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }

                // calculate destination coordinate
                candidateDestinationCoordinate += candidateCoordinateOffset;

                // check if it is valid coordinate
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    //check if destination tile is not occupied and add a major move
                    if(!(candidateDestinationTile.isTileOccupied())){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    // destination tile is occupied
                    else {
                        // take the piece from the destination tile
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();

                        // take the color of the piece
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        // if they have different colors add attack move
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        // break since trident can't go over other pieces
                        break;
                    }
                }
            }
        }

        // check for one step move
        int candidateDestinationCoordinate = this.piecePosition + (CANDIDATE_MOVE_COORDINATE * this.getPieceAlliance().getDirection() * direction);

        //check if it is valid coordinate
        if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
            final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            // check if destination tile is occupied,
            if (!(candidateDestinationTile.isTileOccupied())) {
                // destination tile is not occupied add a major move
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            }
            // destination tile is occupied
            else {
                // take the piece from the destination tile
                final Piece pieceAtDestination = candidateDestinationTile.getPiece();

                //take the color of the destination piece
                final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                // check if colors of the destination piece and this piece are different and add a atack move
                if (this.pieceAlliance != pieceAlliance) {
                    legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                }
            }

        }

        // return the list of legal moves
        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * This method checks if trident is on the first column and possible offsets is -1
     * Piece can't go west from first row
     * @param currentPosition current piece position
     * @param candidateOffset possible offset
     * @return true if excel is on the first column and possible offsets is -1
     */
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == -1;
    }

    /**
     * This method checks if trident is on the first column and possible offsets is 1
     * Piece can't go east from seventh row
     * @param currentPosition current piece position
     * @param candidateOffset possible offset
     * @return true if excel is on the first column and possible offsets is 1
     */
    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && candidateOffset == 1;
    }

    /**
     * This method returns string representation of the trident
     * @return String representation of the trident
     */
    @Override
    public String toString(){
        return PieceType.TRIDENT.toString();
    }
}
