package com.perilandachess.board;

import com.perilandachess.Alliance;
import com.perilandachess.pieces.*;

/**
 * This class will represent a move that is happening on the game
 */
public abstract class Move {

    // chess board
    final Board board;

    // piece that is moving
    final Piece movedPiece;

    // destination coordinate of moving piece
    final int destinationCoordinate;

    // NULL MOVE
    public static final Move NULL_MOVE = new NullMove();

    /**
     * Constructor for move class
     * @param board chess board
     * @param movedPiece moving piece
     * @param destinationCoordinate destination coordinate of moving piece
     */
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    /**
     * This method will return coordinate of the piece that is moving
     * @return source coordinate
     */
    public int getCurrentCoordinate(){
        return movedPiece.getPiecePosition();
    }

    /**
     * This method will return the destination coordinate
     * @return destination coordinate
     */
    public int getDestinationCoordinate(){
        return destinationCoordinate;
    }

    /**
     * This method will return the piece that is moving
     * @return moving piece
     */
    public Piece getMovedPiece(){
        return movedPiece;
    }

    /**
     * This method checks if it is attack move
     * @return false
     */
    public boolean isAttack(){
        return false;
    }

    /**
     * This method will return attacked piece
     * @return null
     */
    public Piece getAttackedPiece(){
        return null;
    }

    @Override
    public int hashCode(){
        int result = 1;
        result = 31 * result + destinationCoordinate;
        result = 31 * result + movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }

        Move otherMove = (Move)other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove.getMovedPiece());
    }

    /**
     * This method actually moves the piece and take care of the transform of the pieces
     * @param moveNum number of the move (that player takes)
     * @return new board with the moved piece
     */
    public Board execute(int moveNum) {
        // create a board
        final Board.Builder builder = new Board.Builder();
        // if it is 3rd, 6th ... transform all Excels to Tercels and all Tercels to Excels
        if((moveNum % 3) == 0){
            // leave all the pieces of current player on same place, except the moving piece and perform transform
            for(final Piece piece : board.getCurrentPlayer().getActivePieces()){
                if(!movedPiece.equals(piece)){
                    if(piece instanceof Excel){
                        builder.setPiece(new Tercel(piece.getPiecePosition(), piece.getPieceAlliance()));
                    }else if(piece instanceof Tercel){
                        builder.setPiece(new Excel(piece.getPiecePosition(), piece.getPieceAlliance()));
                    }else{
                        builder.setPiece(piece);
                    }
                }
            }
        }
        // ordinary move
        else {
            // leave all pieces of current player on same square, except moving piece
            for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
                if (!movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
        }

        // leave all opponents pieces on the same square
        for(final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }

        // move the piece
        Piece newMovedPiece = movedPiece.movePiece(this);

        //if moving piece was excel and moveNum is product of 3 then transform it to tercel
        if(newMovedPiece instanceof Excel && (moveNum % 3) == 0){
            builder.setPiece(new Tercel(newMovedPiece.getPiecePosition(), newMovedPiece.getPieceAlliance()));
        }
        // if moving piece was tercel and moveNum is product of 3 then transform it to excel
        else if(newMovedPiece instanceof Tercel && (moveNum % 3) == 0){
            builder.setPiece(new Excel(newMovedPiece.getPiecePosition(), newMovedPiece.getPieceAlliance()));
        }
        // if piece is advancer
        else if(newMovedPiece instanceof Advancer){
            Advancer advancer = (Advancer)newMovedPiece;
            // check if it is at the end of the board and it need to change direction
            if((advancer.getPieceAlliance() == Alliance.GREEN && BoardUtils.FIRST_ROW[advancer.getPiecePosition()] && advancer.getDirection() == 1) || (advancer.getPieceAlliance() == Alliance.GREEN && BoardUtils.SEVENTH_ROW[advancer.getPiecePosition()] && advancer.getDirection() == -1)){
                advancer.changeDirection();
            }else if((advancer.getPieceAlliance() == Alliance.ORANGE && BoardUtils.SEVENTH_ROW[advancer.getPiecePosition()] && advancer.getDirection() == 1) || advancer.getPieceAlliance() == Alliance.ORANGE && BoardUtils.FIRST_ROW[advancer.getPiecePosition()] && advancer.getDirection() == -1){
                advancer.changeDirection();
            }
            builder.setPiece(advancer);
        }
        // if moving piece is trident
        else if(newMovedPiece instanceof Trident){
            Trident trident = (Trident)newMovedPiece;
            // check if it is at the end of the board and it need to change direction
            if((trident.getPieceAlliance() == Alliance.GREEN && BoardUtils.FIRST_ROW[trident.getPiecePosition()] && trident.getDirection() == 1) || (trident.getPieceAlliance() == Alliance.GREEN && BoardUtils.SEVENTH_ROW[trident.getPiecePosition()] && trident.getDirection() == -1)){
                trident.changeDirection();
            }else if((trident.getPieceAlliance() == Alliance.ORANGE && BoardUtils.SEVENTH_ROW[trident.getPiecePosition()] && trident.getDirection() == 1) || trident.getPieceAlliance() == Alliance.ORANGE && BoardUtils.FIRST_ROW[trident.getPiecePosition()] && trident.getDirection() == -1){
                trident.changeDirection();
            }
            builder.setPiece(trident);
        }else {
            builder.setPiece(movedPiece.movePiece(this));
        }
        // make the current player opponent player
        builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());

        // return the board with the moved piece
        return builder.build();
    }

    /**
     * This class represents regular move for some piece that is not attack move
     */
    public static final class MajorMove extends Move{

        /**
         * Constructor for major move
         * @param board chess board
         * @param movedPiece moving piece
         * @param destinationCoordinate destination coordinate of moving piece
         */
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        /**
         * String representation of major move
         * @return String representation of major move
         */
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

    }

    /**
     * This class represents the attack move for some piece
     */
    public static final class AttackMove extends Move{

        // piece that is attacked with move
        final Piece attackedPiece;


        /**
         * Constructor for attack move
         * @param board chess board
         * @param movedPiece moving piece
         * @param destinationCoordinate destination coordinate of moving piece
         * @param attackedPiece piece on destination coordinate
         */
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        /**
         * This method checks if move is attack move
         * @return true
         */
        @Override
        public boolean isAttack(){
            return true;
        }

        /**
         * This method gets attacked piece
         * @return attacked piece
         */
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public int hashCode(){
                return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            AttackMove otherAttackMove = (AttackMove)other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        /**
         * String representation of the move, it has notation "This piece" "x" "destination coordinate"
         * @return String representation of the move
         */
        @Override
        public String toString(){
            return movedPiece + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    /**
     * This method represents imposible move
     */
    public static final class NullMove extends Move{
        /**
         * Constructor for null move
         */
        public NullMove(){
           super(null, null, -1);
        }

        /**
         * execute method for null move, throws RuntimeException since this is illegal move and it is not suppose to happen
         * @param moveNum number of the move (that player takes)
         * @return
         */
        @Override
        public Board execute(int moveNum){
            throw new RuntimeException("Not execute the null move!");
        }

        /**
         * this move has no coordinate
         * @return
         */
        @Override
        public int getCurrentCoordinate(){
            return -1;
        }
    }

    /**
     * This class is factory class for move class
     */
    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }

        /**
         * This method creates new move
         * @param board current board
         * @param currentCoordinate current position of the piece
         * @param destinationCoordinate destination position of the piece
         * @return new move it is legal move, or NULL_MOVE if it is illegal move
         */
        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate){
            for(final Move move : board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
