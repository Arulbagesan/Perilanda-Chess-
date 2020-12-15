package com.perilandachess.board;

import com.perilandachess.pieces.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.perilandachess.board.BoardUtils.NUM_TILES;

/**
 * This method represents one square on the board
 */
public abstract class Tile {
    // coordinate of the tile
    private final int tileCoordinate;
    // creates 49 empty tiles since often most of the board will be empty and then there are a lot of empty tiles
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    /**
     * Constructor for the tile
     * @param tileCoordinate coordinate of the tile
     */
    private Tile(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    /**
     * This method creates all possible empty tiles
     * @return Map of empty tiles
     */
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        for(int i = 0; i < NUM_TILES; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }
        return Collections.unmodifiableMap(emptyTileMap);
    }

    /**
     * This method will create a new Tile
     * @param tileCoordinate coordinate of the tile to be created
     * @param piece piece that will be on the tile, or null otherwise
     * @return created tile, OccupiedTile if piece is not null, or EmptyTile otherwise
     */
    public static Tile createTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    /**
     * This method will check if tile is occupied
     * @return true if tile is occupied or false if tile is not occupied
     */
    public abstract boolean isTileOccupied();

    /**
     * This method will return the piece from the tile
     * @return piece on tile
     */
    public abstract Piece getPiece();

    public int getTileCoordinate(){
        return tileCoordinate;
    }

    @Override
    public String toString(){
        return "-";
    }

    /**
     * This class represents an empty square
     */
    public static final class EmptyTile extends Tile{
        /**
         * Constructor for empty tile
         * @param tileCoordinate coordinate of the empty tile
         */
        private EmptyTile(final int tileCoordinate){
            super(tileCoordinate);
        }

        /**
         * This method checks if tile is occupied
         * @return false
         */
        @Override
        public boolean isTileOccupied() {
            return false;
        }

        /**
         * This method returns the piece from the tile
         * @return null, there is no piece on empty tile
         */
        @Override
        public Piece getPiece() {
            return null;
        }
    }

    /**
     * This class represents occupied tile
     */
    public static final class OccupiedTile extends Tile{
        // piece on the tile
        private final Piece pieceOnTile;

        /**
         * Constructor for the occupied tile
         * @param tileCoordinate tile coordinate
         * @param pieceOnTile piece on tile
         */
        private OccupiedTile(final int tileCoordinate, Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        /**
         * This method checks if tile is occupied
         * @return true
         */
        @Override
        public boolean isTileOccupied() {
            return true;
        }

        /**
         * This method returns the piece from the tile
         * @return piece on tile
         */
        @Override
        public Piece getPiece() {
            return pieceOnTile;
        }

        /**
         * This method returns a string representation of tile
         * It returns piece name lowec case for green, and upper case for orange
         * @return
         */
        @Override
        public String toString(){
            return pieceOnTile.getPieceAlliance().isGreen() ? pieceOnTile.toString().toLowerCase() : pieceOnTile.toString();
        }
    }
}
