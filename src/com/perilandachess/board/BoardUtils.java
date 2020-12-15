package com.perilandachess.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains useful constants and methods to other classes
 */
public class BoardUtils {

    /**
     * Number of the tiles on the board
     */
    public static final int NUM_TILES = 49;

    /**
     * Number of the tiles in one row
     */
    public static final int NUM_TILES_PER_ROW = 7;

    /**
     * Array that has first column coordinate set to true
     */
    public static final boolean[] FIRST_COLUMN = initColumn(0);

    /**
     * Array that has seventh column coordinate set to true
     */
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);

    /**
     * Array that has first row coordinate set to true
     */
    public static final boolean[] FIRST_ROW = initRow(0);

    /**
     * Array that has seventh row coordinate set to true
     */
    public static final boolean[] SEVENTH_ROW = initRow(42);

    /**
     * PGN notation
     */
    public static final String[] ALGEBREIC_NOTATION = initializeAlgebraicNotation();

    /**
     * Map position in PGN to coordinate
     */
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinate();

    /**
     * This method creates a new array of booleans and sets elements that represent columnNumber column to true
     * @param columnNumber number of the column
     * @return created array
     */
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[49];
        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }while(columnNumber < NUM_TILES);
        return column;
    }

    /**
     * This method creates a new array of booleans and sets elements that represent columnNumber row to true
     * @param rowNumber number of the row
     * @return created array
     */
    private static boolean[] initRow(int rowNumber) {
        boolean[] row = new boolean[NUM_TILES];
        for(int i = 0; i < row.length; i++){
            row[i] = false;
        }
        do{
            row[rowNumber] = true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private BoardUtils(){
        throw new RuntimeException("You can not instantiate this.");
    }

    /**
     * This method checks if given coordinate is valid coordinate
     * @param candidateDestinationCoordinate coordinate to be checked
     * @return true if given coordinate is between 0 and 49
     */
    public static boolean isValidTileCoordinate(int candidateDestinationCoordinate) {
        return candidateDestinationCoordinate >= 0 && candidateDestinationCoordinate < NUM_TILES;
    }

    /**
     * Creates notation for the tiles
     * @return String array with notation
     */
    private static String[] initializeAlgebraicNotation() {
        return new String[]{
                "a7", "b7", "c7", "d7", "e7", "f7", "g7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1"
        };
    }

    /**
     * This method will map positions in the notation form to the actual coordinate
     * @return created map
     */
    private static Map<String, Integer> initializePositionToCoordinate() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for(int i = 0; i < NUM_TILES; i++){
            positionToCoordinate.put(ALGEBREIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    /**
     * This method gives notation position for given coordinate
     * @param coordinate coordinate that we want to find out notation
     * @return notation of the coordinate
     */
    public static String getPositionAtCoordinate(final int coordinate){
        return ALGEBREIC_NOTATION[coordinate];
    }
}
