package com.perilandachess.pieces;

import com.perilandachess.Alliance;
import com.perilandachess.board.Board;
import com.perilandachess.board.BoardUtils;
import com.perilandachess.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class is representation of advancer on the board
 * The Advancer can only move 1 or 2 steps forward each time, but when it reaches the other edge of the board, it turns around and heads back in the opposite direction.
 */
public class Advancer extends Piece {

    // 7 represents one step at the time
    // 14 represents two steps
    private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 14};

    // direction of the piece, 1 for normal direction -1 for opposite direction
    private int direction;


    /**
     * Constructor for Advancer
     * @param piecePosition position of the advancer
     * @param pieceAlliance alliance of the advancer
     */
    public Advancer(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.ADVANCER);
        this.direction = 1;
    }

    /**
     * This method will move the piece, it creates a new advancer on destination position of move
     * @param move Move that piece does
     * @return new piece placed on new position
     */
    @Override
    public Advancer movePiece(Move move) {
        Advancer newAdvancer = new Advancer(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
        newAdvancer.direction = this.direction;
        return newAdvancer;
    }

    /**
     * Getter for the direction
     * @return direction of the piece 1 normal direction, 0 opposite direction
     */
    public int getDirection(){
        return direction;
    }

    /**
     * This method changes direction
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
        // list of legal moves
        final List<Move> legalMoves = new ArrayList<>();

        // for each candidate offset, it can be 1 or 2 tiles at the time
        for(int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            // calculate possible destination
            int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset * this.getPieceAlliance().getDirection() * direction);

            // check if destination coordinate is valid coordinate
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            //if it is one step at the time
            if(currentCandidateOffset == 7){
                //if destination tile is occupied and color of piece on that tile is different add attack move
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    //take the piece from candidate destination
                    final Piece candidatePiece = board.getTile(candidateDestinationCoordinate).getPiece();
                    // if the candidate piece is different alliance, we can attack it, add AttackMove to the list
                    if(candidatePiece.getPieceAlliance() != this.pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, candidatePiece));
                    }
                }
                // add regular major move
                else {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
            }
            //if it is two step at the time
            else {
                // position between current and destination coordinate
                final int behindCandidateOffsetPosition = this.piecePosition + (this.getPieceAlliance().getDirection() * 7 * direction);

                // check if position between destination and current position is valid coordinate
                if(!BoardUtils.isValidTileCoordinate(behindCandidateOffsetPosition)){
                    continue;
                }

                // check if there is a piece between current and destination tile
                if(!board.getTile(behindCandidateOffsetPosition).isTileOccupied()){
                    // check if there is a piece on destination tile
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        // take the piece from the destination tile
                        final Piece candidatePiece = board.getTile(candidateDestinationCoordinate).getPiece();
                        // check if candidate piece is different color then this piece
                        // if so add attack move
                        if(candidatePiece.getPieceAlliance() != this.pieceAlliance){
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, candidatePiece));
                        }
                    }
                    // add regular move
                    else {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }

        // return list of legal moves
        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * String representation of advancer
     * This method calls PieceType.Advancer toString method
     * @return String representation of advancer
     */
    @Override
    public String toString(){
        return PieceType.ADVANCER.toString();
    }
}
