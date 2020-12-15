package com.perilandachess.player;

import com.perilandachess.board.Board;
import com.perilandachess.board.Move;

/**
 * This class represents new state on the board before some move is actually played
 */
public class MoveTransition {
    // new position on the board
    private final Board transitionBoard;
    // move that leads to that state
    private final Move move;

    // status of a move
    private final MoveStatus moveStatus;

    /**
     * Constructor for MoveTransitions
     * @param transitionBoard chess board
     * @param move made move
     * @param moveStatus move status
     */
    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus){
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    /**
     * This method returns move status
     * @return status of a made move, DONE if move is made, ILLEGAL if it is impossible to make such move
     */
    public MoveStatus getMoveStatus(){
        return moveStatus;
    }

    /**
     * This method returns transition board
     * Transition board is a position on the board after the move is played
     * @return transition board
     */
    public Board getTransitionBoard(){
        return transitionBoard;
    }

}
