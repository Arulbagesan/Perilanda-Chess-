package com.perilandachess.player;

import com.perilandachess.Alliance;
import com.perilandachess.board.Board;
import com.perilandachess.board.Move;
import com.perilandachess.pieces.Chief;
import com.perilandachess.pieces.Piece;

import java.util.Collection;

/**
 * This is abstract class that represents one player
 */
public abstract class Player {
    protected final Board board;
    protected final Chief playerChief;
    protected final Collection<Move> legalMoves;

    /**
     * Constructor for the player
     * @param board chess board
     * @param legalMoves list of legal moves for the player
     * @param opponentMoves list of opponents legal moves
     */
    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves){
        this.board = board;
        this.playerChief = establishChief();
        this.legalMoves = legalMoves;
    }

    /**
     * This method returns all possible legal moves for this player
     * @return List of legal moves
     */
    public Collection<Move> getLegalMoves(){
        return legalMoves;
    }

    /**
     * This method looks if there is chief on the board
     * @return chief or null if there is no one
     */
    private Chief establishChief() {
        for(Piece piece : getActivePieces()){
            if(piece.getPieceType().isChief()){
                return (Chief)piece;
            }
        }
        return null;
    }

    /**
     * This method get all active pieces of the player
     * @return Collection of the pieces
     */
    public abstract Collection<Piece> getActivePieces();

    /**
     * This method return Alliance of the player
     * @return players alliance
     */
    public abstract Alliance getAlliance();

    /**
     * This method returns other player
     * @return opponent player
     */
    public abstract Player getOpponent();

    /**
     * This method check if given move is legal
     * @param move move to be check can this player play
     * @return true if move is legal, false otherwise
     */
    public boolean isMoveLegal(Move move){
        return legalMoves.contains(move);
    }

    /**
     * This method checks if the chief is captured
     * @return Chief or null if it is captured
     */
    public boolean isChiefCaptured(){
        Chief chief = establishChief();
        return chief == null;
    }

    /**
     * This method makes move for the player
     * @param move move to be played
     * @return Move transition
     */
    public abstract MoveTransition makeMove(Move move);
}
