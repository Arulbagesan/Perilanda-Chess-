package com.perilandachess.player;

import com.perilandachess.Alliance;
import com.perilandachess.board.Board;
import com.perilandachess.board.Move;
import com.perilandachess.pieces.Piece;

import java.util.Collection;

/**
 * This class represents Green Player
 */
public class GreenPlayer extends Player{
    private static int moveNum = 0;

    /**
     * Constructor for the green player
     * @param board chess board
     * @param orangeStandardLegalMoves legal moves for orange player
     * @param greenStandardLegalMoves legal moves for green player
     */
    public GreenPlayer(Board board, Collection<Move> orangeStandardLegalMoves, Collection<Move> greenStandardLegalMoves) {
        // note that we first send greenStandardLegalMoves and then orangeStandardLegalMoves
        super(board, greenStandardLegalMoves, orangeStandardLegalMoves);
    }

    /**
     * This method get all active pieces of the player
     * @return Collection of the pieces
     */
    @Override
    public Collection<Piece> getActivePieces() {
        return board.getGreenPieces();
    }

    /**
     * This method return Alliance of the player
     * @return players alliance
     */
    @Override
    public Alliance getAlliance() {
        return Alliance.GREEN;
    }

    /**
     * This method returns other player
     * @return opponent player
     */
    @Override
    public Player getOpponent() {
        return board.getOrangePlayer();
    }

    /**
     * This method makes move for the player
     * @param move move to be played
     * @return Move transition
     */
    @Override
    public MoveTransition makeMove(Move move) {
        // check if move is legal
        if(!isMoveLegal(move)){
            // return move transition with same board and status illegal
            return new MoveTransition(board, move, MoveStatus.ILLEGAL_MOVE);
        }
        moveNum++;
        // make the move and get transition board
        final Board transitionBoard = move.execute(moveNum);
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
}
