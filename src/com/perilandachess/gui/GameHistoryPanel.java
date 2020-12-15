package com.perilandachess.gui;

import com.perilandachess.board.Board;
import com.perilandachess.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel will contain a table of played moves
 */
public class GameHistoryPanel extends JPanel {
    // model for the table
    private final DataModel model;
    private final JScrollPane scrollPane;

    // table of taken moves
    private final JTable table;

    // dimension of the panel
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(120, 400);

    /**
     * Constructor for game history panel
     */
    public GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * This method will add new move in table
     * @param board chess board
     * @param moveLog moves played
     */
    void redo(final Board board, final Table.MoveLog moveLog){
        // current row
        int currentRow = 0;

        // clear all from model
        this.model.clear();

        // for each move from move log
        for(final Move move : moveLog.getMoves()){
            // take the move text
            final String moveText = move.toString();

            // if move is played by green add to first column
            if(move.getMovedPiece().getPieceAlliance().isGreen()){
                this.model.setValueAt(moveText, currentRow, 0);
            }
            // if the move is played by orange add to second colunm
            else if(move.getMovedPiece().getPieceAlliance().isOrange()){
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        // add scroll bar if there is a lot of moves
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    /**
     * This method will clear the table
     */
    public void reset(){
        this.model.clear();
        model.setRowCount(0);
        table.revalidate();
    }

    /**
     * This class represents Data model for the table
     */
    private static class DataModel extends DefaultTableModel{
        // list of rows
        private final List<Row> values;

        // header for the table
        private static final String[] NAMES = {"Green", "Orange"};

        /**
         * Constructor for the data model
         */
        DataModel(){
            this.values = new ArrayList<>();
        }

        /**
         * This method will clear data model
         */
        public void clear(){
            values.clear();
            setRowCount(0);
        }

        /**
         * This method will return row count
         * @return
         */
        @Override
        public int getRowCount(){
            if(values == null){
                return 0;
            }
            return values.size();
        }


        /**
         * This method will return column count
         * @return number of columns (2)
         */
        @Override
        public int getColumnCount(){
            return NAMES.length;
        }


        /**
         * This method will return the object of position row column
         * @param row row number
         * @param column column number
         * @return object on given position
         */
        @Override
        public Object getValueAt(final int row, final int column){
            final Row currentRow = this.values.get(row);
            if(column == 0){
                return currentRow.getGreenMove();
            }else if(column == 1){
                return currentRow.getOrangeMove();
            }
            return null;
        }

        /**
         * This method will set object of given position
         * @param aValue object to be placed on given position
         * @param row row number
         * @param column column number
         */
        @Override
        public void setValueAt(final Object aValue, final int row, final int column){
            final Row currentRow;
            // if we need to ad new row
            if(this.values.size() <= row){
                currentRow = new Row();
                this.values.add(currentRow);
            }
            // we can add it in current row
            else{
                currentRow = this.values.get(row);
            }

            // if it is first column
            if(column == 0){
                currentRow.setGreenMove((String)aValue);
                fireTableRowsInserted(row, row);
            }
            // if it is second column
            else if(column == 1){
                currentRow.setOrangeMove((String)aValue);
                fireTableCellUpdated(row, column);
            }

        }

        @Override
        public Class<?> getColumnClass(final int column){
            return Move.class;
        }

        @Override
        public String getColumnName(final int column){
            return NAMES[column];
        }
    }

    /**
     * This class represents a row in a table
     */
    private static class Row{

        // text of green move
        private String greenMove;

        // text of orange move
        private String orangeMove;

        /**
         * Constructor for the row class
         */
        Row(){
        }

        /**
         * This method gets green move
         * @return green move
         */
        public String getGreenMove(){
            return greenMove;
        }

        /**
         * This method gets orange move
         * @return orange move
         */
        public String getOrangeMove(){
            return orangeMove;
        }

        /**
         * This method sets the green move with a given text
         * @param greenMove text for green move
         */
        public void setGreenMove(String greenMove){
            this.greenMove = greenMove;
        }

        /**
         * This method sets the orange move with a given text
         * @param orangeMove text for orange move
         */
        public void setOrangeMove(String orangeMove){
            this.orangeMove = orangeMove;
        }
    }
}
