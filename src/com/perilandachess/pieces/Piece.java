package com.perilandachess.pieces;

import com.perilandachess.Alliance;
import com.perilandachess.board.Board;
import com.perilandachess.board.Move;

import java.util.Collection;

/**
 * This class is abstract for one piece on the board
 */
public abstract class Piece {

    // position of the piece on the board
    protected final int piecePosition;

    // alliance of the piece (green or orange)
    protected final Alliance pieceAlliance;

    //type of the piece (Advancer, Chief, Excel, Tercel or Trident)
    protected final PieceType pieceType;

    /**
     * Constructor for piece class
     * @param piecePosition position of the piece from the board
     * @param pieceAlliance color of the piece
     * @param pieceType type of the piece
     */
    public Piece(final int piecePosition, final Alliance pieceAlliance, PieceType pieceType){
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType = pieceType;
    }

    /**
     * Getter for the position
     * @return position of the piece from the board
     */
    public int getPiecePosition(){
        return piecePosition;
    }

    /**
     * Getter for color of the piece
     * @return piece color
     */
    public Alliance getPieceAlliance(){
        return pieceAlliance;
    }

    /**
     * Getter for piece type
     * @return return type of the piece
     */
    public PieceType getPieceType(){
        return pieceType;
    }

    /**
     * This method takes piece value from piece type
     * @return value of the piece
     */
    public int getPieceValue(){
        return pieceType.getPieceValue();
    }

    /**
     * This method checks if two pieces are same
     * Pieces are same if they are on same position, they are same type and they have same alliance
     * @param other piece to comper to
     * @return true if pieces are same, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        Piece otherPiece = (Piece)other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.pieceType && pieceAlliance == otherPiece.pieceAlliance;
    }

    @Override
    public int hashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;

        return result;
    }

    /**
     * This method will move the piece
     * @param move Move that piece does
     * @return new piece placed on new position
     */
    public abstract Piece movePiece(Move move);

    /**
     * This method will calculate all possible legal moves for this piece
     * @param board current state on the board
     * @return collection of all possible moves
     */
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    /**
     * This enum represent piece types
     * Each piece will have name and value
     * Types of pieces are: Advancer, Chief, Excel, Tercel and Trident
     */
    public enum PieceType{
        ADVANCER("A", 1){
            @Override
            public boolean isChief() {
                return false;
            }
        },
        CHIEF("C", 10) {
            @Override
            public boolean isChief() {
                return true;
            }
        },
        EXCEL("E", 5) {
            @Override
            public boolean isChief() {
                return false;
            }
        },
        TERCEL("T", 3) {
            @Override
            public boolean isChief() {
                return false;
            }
        },
        TRIDENT("R", 2) {
            @Override
            public boolean isChief() {
                return false;
            }
        };

        // name of the piece
        private String pieceName;

        //value of the piece
        private int pieceValue;

        /**
         * Constructor for piece type
         * @param pieceName name of the piece
         * @param pieceValue value of the piece
         */
        PieceType(String pieceName, int pieceValue){
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        /**
         * String representation of piece type
         * @return piece type as a string
         */
        @Override
        public String toString(){
            return this.pieceName;
        }

        /**
         * Checks if piece is Chief
         * @return true if piece is chief, false otherwise
         */
        public abstract boolean isChief();

        /**
         * This method returns piece value
         * @return actual piece value based on the type
         */
        public int getPieceValue(){
            return pieceValue;
        }

    }
}
