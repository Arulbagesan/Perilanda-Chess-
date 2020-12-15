package com.perilandachess.gui;

import com.perilandachess.Alliance;
import com.perilandachess.board.Board;
import com.perilandachess.board.BoardUtils;
import com.perilandachess.board.Move;
import com.perilandachess.board.Tile;
import com.perilandachess.pieces.Advancer;
import com.perilandachess.pieces.Piece;
import com.perilandachess.pieces.Trident;
import com.perilandachess.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    // game window
    private final JFrame gameFrame;

    // constructed panels
    private GameHistoryPanel gameHistoryPanel;
    private TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private NextMovePanel nextMovePanel;

    // chess board
    private Board chessBoard;

    // move log with played moves
    private MoveLog moveLog;

    // direction of the board
    private BoardDirection boardDirection;

    // source and destination tile for moving pieces
    private Tile sourceTile;
    private Tile destinationTile;

    // moving piece
    private Piece movedPiece;

    // check if need to highlight legal move
    private boolean highlightLegalMoves;


    // directory for the images
    private static String defaultPieceImagesPath = "art" + File.separator;

    /**
     * Constructor for table class
     */
    public Table(){
        // set window name
        this.gameFrame = new JFrame("Perilanda chess");
        this.gameFrame.setLayout(new BorderLayout());

        // create standard position on the chess board
        this.chessBoard = Board.createStandardBoard();

        //create panels
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.nextMovePanel = new NextMovePanel();

        // set next mover to green
        nextMovePanel.redo(Alliance.GREEN);

        // set the direction to normal
        boardDirection = BoardDirection.NORMAL;

        // add menu bar
        JMenuBar tableMenuBar =  createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar );
        gameFrame.setSize(new Dimension(1000, 800));

        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();

        // add constructed panels
        this.gameFrame.add(boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(nextMovePanel, BorderLayout.NORTH);

        // highlight legal moves by default
        highlightLegalMoves = true;

        gameFrame.setVisible(true);

        // exit on X button
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * This method will create table menu bar
     * @return created table menu bar
     */
    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        // add two menus
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    /**
     * This method creates file menu
     * @return created JMenu
     */
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // create first item
        JMenuItem newGame = new JMenuItem("New game");

        // add listener to the first item to start a new game
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewGame();
            }
        });
        // add it to fileMenu
        fileMenu.add(newGame);

        // create new item to load saved game
        JMenuItem openPGN = new JMenuItem("Load game");

        // add listener for load game item
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show file chooser
                JFileChooser jfc = new JFileChooser(System.getProperty("user.home"));

                int returnValue = jfc.showOpenDialog(null);

                // if we have a file
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // get path to the file
                    File selectedFile = jfc.getSelectedFile();
                    try{
                        // create a position on the board with information from file
                        chessBoard = Board.createCustomBoard(selectedFile);

                        // reset movelog and two panels
                        moveLog = new MoveLog();
                        takenPiecesPanel.reset();
                        gameHistoryPanel.reset();
                    }catch (IllegalArgumentException ex){
                        // if there was some error while reading file
                        JOptionPane.showMessageDialog(gameFrame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        // if there was some other error
                        ex.printStackTrace();
                    }
                }

                // check if we need to flip the board
                if(chessBoard.getCurrentPlayer().getAlliance() == Alliance.ORANGE){
                    boardDirection = boardDirection.opposite();
                }
                // redraw the board
                boardPanel.drawBoard(chessBoard);
            }
        });
        fileMenu.add(openPGN);

        // save game item
        JMenuItem saveMenuItem = new JMenuItem("Save");

        // add listener to save game item
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show file chooser
                JFileChooser jfc = new JFileChooser(System.getProperty("user.home"));

                int returnValue = jfc.showOpenDialog(null);

                // if we have a file
                if (returnValue == JFileChooser.APPROVE_OPTION){
                    // open file to print into
                    try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(jfc.getSelectedFile())))){
                        // print the chess board
                        writer.print(chessBoard);

                        //print the next move maker
                        if(chessBoard.getCurrentPlayer().getAlliance() == Alliance.GREEN){
                            writer.print("G");
                        }else{
                            writer.print("O");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        // add save menu item
        fileMenu.add(saveMenuItem);

        // create exit item
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        // add listener
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // terminate the program
                System.exit(0);
            }
        });

        // add exit item
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }


    /**
     * This method will create a new game, standard position on the board and clears all panels
     */
    private void createNewGame(){
        // create a standard position on the board
        chessBoard = Board.createStandardBoard();

        // set next mvoe maker to green
        nextMovePanel.redo(Alliance.GREEN);

        // clear both panels
        takenPiecesPanel.reset();
        gameHistoryPanel.reset();

        // set direction to normal
        boardDirection = BoardDirection.NORMAL;

        //clear move log
        moveLog = new MoveLog();

        // redraw board
        boardPanel.drawBoard(chessBoard);
    }

    /**
     * This class represents the chess board
     */
    private class BoardPanel extends JPanel{
        // list of tiles represent the board
        List<TilePanel> boardTiles;

        /**
         * Constructor for board panel
         */
        BoardPanel(){
            super(new GridLayout(7, 7));
            boardTiles = new ArrayList<>();

            // create 49 new TilePanels
            for(int i = 0; i < BoardUtils.NUM_TILES; i++){
                TilePanel tilePanel = new TilePanel(this, i);
                boardTiles.add(tilePanel);
                add(tilePanel);
            }
            // set the size
            setPreferredSize(new Dimension(400, 350));
            validate();
        }

        /**
         * This method will draw the board
         * @param board
         */
        public void drawBoard(final Board board){
            //clear the board
            removeAll();

            // draw all tiles
            for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }

            // set next move maker
            nextMovePanel.redo(chessBoard.getCurrentPlayer().getAlliance());
            validate();
            repaint();
        }

    }

    /**
     * This class will contain all moves that been played in a current game
     */
    public static class MoveLog{

        // list of moves
        private final List<Move> moves;

        /**
         * Constructor for move log
         */
        MoveLog(){
            moves = new ArrayList<>();
        }

        /**
         * This method returns all played moves in a game
         * @return list of played moves
         */
        public List<Move> getMoves(){
            return moves;
        }


        /**
         * This method adds new move in a move log
         * @param move new move
         */
        public void addMove(final Move move){
            moves.add(move);
        }
    }

    /**
     * This class represents one tile on the board
     */
    private class TilePanel extends JPanel{
        // id of the tile
        private int tileId;

        /**
         * Constructor for the tile
         * @param boardPanel board
         * @param tileId id of the tile
         */
        TilePanel(BoardPanel boardPanel, int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(new Dimension(15, 15));
            showBorders(this);

            // assigns icon to tile if there is a piece
            assignTilePieceIcon(chessBoard);

            // add listener to mouse events
            addMouseListener(new MouseListener() {
                @Override
                // if mouse is clicked
                public void mouseClicked(MouseEvent e) {
                    // if it is right mouse button
                    if(isRightMouseButton(e)){
                        // leave the piece
                        sourceTile = null;
                        destinationTile = null;
                        movedPiece = null;
                        // redraw the board to clear possible moves
                        boardPanel.drawBoard(chessBoard);
                    }
                    // if it is left mouse clock
                    else if(isLeftMouseButton(e)){
                        // if it is first click
                        // click on source tile
                        if(sourceTile == null){
                            // gest source tile
                            sourceTile = chessBoard.getTile(tileId);
                            // get moved piece if any
                            movedPiece = sourceTile.getPiece();

                            // if there is no piece, nothing to do
                            if(movedPiece == null){
                                sourceTile = null;
                            }
                        }
                        // if it is second click
                        // click on destination tile
                        else{
                            // get destination tile
                            destinationTile = chessBoard.getTile(tileId);

                            // make a move
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());

                            // get transition
                            final MoveTransition moveTransition = chessBoard.getCurrentPlayer().makeMove(move);

                            // if it is possible move
                            if(moveTransition.getMoveStatus().isDone()){
                                // make the move
                                chessBoard = moveTransition.getTransitionBoard();
                                // add it to move log
                                moveLog.addMove(move);

                                // flip the board
                                boardDirection = boardDirection.opposite();

                                // redraw the board
                                boardPanel.drawBoard(chessBoard);

                                // swich color on next move panel
                                nextMovePanel.redo(chessBoard.getCurrentPlayer().getAlliance());

                                // if chief is captured game over
                                if(chessBoard.getCurrentPlayer().isChiefCaptured()){
                                    // if current player is a green, then green chief was captured
                                    if(chessBoard.getCurrentPlayer().getAlliance() == Alliance.GREEN) {
                                        // print a message that orange is a winner
                                        JOptionPane.showMessageDialog(gameFrame, "Orange is winner!", "Game over", JOptionPane.INFORMATION_MESSAGE);

                                        // ask if he want play again
                                        int n = JOptionPane.showConfirmDialog(gameFrame, "Would you like to start a new game?", "New game", JOptionPane.YES_NO_OPTION);

                                        // he wants play again start new game
                                        if(n == 0){
                                            createNewGame();
                                        }
                                        // he doesn't want to play again, close the app
                                        else{
                                            System.exit(0);
                                        }
                                    }
                                    // // if current player is a orange, then orange chief was captured
                                    else{
                                        // print the message that green is the winner
                                        JOptionPane.showMessageDialog(gameFrame, "Green is winner!", "Game over", JOptionPane.INFORMATION_MESSAGE);

                                        // ask if he want to play again
                                        int n = JOptionPane.showConfirmDialog(gameFrame, "Would you like to start a new game?", "New game", JOptionPane.YES_NO_OPTION);
                                        if(n == 0){
                                            createNewGame();
                                        }else{
                                            System.exit(0);
                                        }
                                    }
                                }
                            }
                            sourceTile = null;
                            destinationTile = null;
                            movedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                // add move to game history panel
                                gameHistoryPanel.redo(chessBoard, moveLog);

                                // add taken pieces to its panel
                                takenPiecesPanel.redo(moveLog);

                                // redraw the board
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        /**
         * This method draws a tile
         * @param board chess board
         */
        private void drawTile(final Board board){
            // show border for tile
            showBorders(this);

            // assign icon for tile
            assignTilePieceIcon(board);

            // highlight legal moves
            highlightLegals(board);
            validate();
            repaint();
        }


        /**
         * Assign icon for the piece
         * @param board chess board
         */
        private void assignTilePieceIcon(final Board board){
            // remove all from tile
            this.removeAll();

            // if tile is occupied we need to put the piece image on it
            if(board.getTile(tileId).isTileOccupied()){
                try {
                    // if direction is normal
                    if(boardDirection == BoardDirection.NORMAL) {
                        // check if advancer is going in opposite direction, put the advancer image in opposite way
                        if(board.getTile(tileId).getPiece() instanceof Advancer && ((Advancer)board.getTile(tileId).getPiece()).getDirection() == -1) {
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + "F.png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                        // check if trident is going in opposite direction, put the trident image in opposite way
                        else if(board.getTile(tileId).getPiece() instanceof Trident && ((Trident)board.getTile(tileId).getPiece()).getDirection() == -1){
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + "F.png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                        // put the piece image on the tile in normal direction
                        else{
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + ".png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                    }
                    // if board is flipped
                    else{
                        // check if advancer goes in opposite direction, and put advancer in normal position
                        if(board.getTile(tileId).getPiece() instanceof Advancer && ((Advancer)board.getTile(tileId).getPiece()).getDirection() == -1) {
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + ".png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                        // check if trident goes in opposite direction, and put the trident in normal position
                        else if(board.getTile(tileId).getPiece() instanceof Trident && ((Trident)board.getTile(tileId).getPiece()).getDirection() == -1){
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + ".png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                        // check if piece is trident and put the flipped image
                        else if(board.getTile(tileId).getPiece() instanceof Advancer || board.getTile(tileId).getPiece() instanceof Trident) {
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + "F.png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                        // add a piece image on the tile
                        else{
                            final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + ".png"));
                            add(new JLabel(new ImageIcon(image)));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegals(final Board board){
            // if highlight is enabled
            if(highlightLegalMoves){
                // for each legal move
                for(final Move move : pieceLegalMoves(board)){
                    // if this is destination tile
                    if(move.getDestinationCoordinate() == this.tileId){
                        try{
                            // if it is attack move put the piece with dot on it
                            if(move.isAttack()){
                                // remove all from tile
                                removeAll();

                                // get attacked piece
                                Piece attackPiece = move.getAttackedPiece();

                                // if board direction is normal
                                if(boardDirection == BoardDirection.NORMAL){

                                    //check if advancer is going in opposite way and put the flipped image
                                    if(attackPiece instanceof Advancer && ((Advancer)attackPiece).getDirection() == -1){
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "Fred_dot.png")))));
                                    }
                                    // check if trident is going in opposite way and put the flipped image
                                    else if(attackPiece instanceof Trident && ((Trident)attackPiece).getDirection() == -1){
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "Fred_dot.png")))));
                                    }
                                    // put the piece icon in normal way
                                    else{
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "red_dot.png")))));
                                    }
                                }
                                // board is flipped
                                else{
                                    // check if piece is advancer and it goes in opposite way, then put the image in normal way
                                    if(attackPiece instanceof Advancer && ((Advancer)attackPiece).getDirection() == -1){
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "red_dot.png")))));
                                    }
                                    // check if piece is trident and it goes in opposite way, then put the image in normal way
                                    else if(attackPiece instanceof Trident && ((Trident)attackPiece).getDirection() == -1){
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "red_dot.png")))));
                                    }
                                    // check if piece is trident or advancer and put the image in flipped way
                                    else if(attackPiece instanceof Advancer || attackPiece instanceof Trident){
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "Fred_dot.png")))));
                                    }
                                    // put the piece image
                                    else{
                                        add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + attackPiece.getPieceAlliance().toString().substring(0, 1) + attackPiece.toString() + "red_dot.png")))));
                                    }
                                }
                            }else {
                                add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + "red_dot.png")))));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        /**
         * This method get the legal moves for moveing piece
         * @param board
         * @return
         */
        private Collection<Move> pieceLegalMoves(final Board board){
            // if that we found moving piece
            if(movedPiece != null && movedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()){
                return movedPiece.calculateLegalMoves(board);
            }

            // there is no moving piece, return empty list
            return Collections.emptyList();
        }

        /**
         * This method shows border for a given tile
         * @param tilePanel tile panel to set the borders
         */
        private void showBorders(TilePanel tilePanel) {
            tilePanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

            // if board direction is normal
            if(boardDirection == BoardDirection.NORMAL) {
                // if tile is in first row and in first column, upper left tile
                if (BoardUtils.FIRST_ROW[tileId] && BoardUtils.FIRST_COLUMN[tileId]) {
                    tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                // if tile is in first row, upper tiles
                else if (BoardUtils.FIRST_ROW[tileId]) {
                    tilePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK));
                }
                // if tile is in first column, left most tiles
                else if (BoardUtils.FIRST_COLUMN[tileId]) {
                    tilePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
                }
                // other tiles
                else {
                    tilePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                }
            }
            // board is flipped
            else{
                // if tile is in seventh row and in seventh column, lower right tile
                // when board is flipped this is top left
                if(BoardUtils.SEVENTH_ROW[tileId] && BoardUtils.SEVENTH_COLUMN[tileId]){
                    tilePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                // if tile is in seventh row, lowest tiles
                // when board is flipped this is upper tiles
                else if(BoardUtils.SEVENTH_ROW[tileId]){
                    tilePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK));
                }
                // if tile is in seventh column, right most
                // when board is flipped this is left most tiles
                else if (BoardUtils.SEVENTH_COLUMN[tileId]) {
                    tilePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));
                }
                // other tiles
                else {
                    tilePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                }
            }
        }
    }

    /**
     * This method creates preferences menu
     * @return created preferences menu
     */
    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");

        // flip boar option
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");

        // add listener to flip option
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set direction to opposite
                boardDirection = boardDirection.opposite();

                //redraw the board
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();

        // highlight checker
        final JCheckBoxMenuItem legalMoveHinghLinghterCheckbox = new JCheckBoxMenuItem("Highlight legal moves");

        // make it selected, by default
        legalMoveHinghLinghterCheckbox.setSelected(true);

        // set the listener
        legalMoveHinghLinghterCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set the highlightLegalMoves
                highlightLegalMoves = legalMoveHinghLinghterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHinghLinghterCheckbox);
        return preferencesMenu;
    }

    /**
     * This enum represents the board direction
     * NORMAL stands for green down, orange up
     * FLIPPED stands for green up, orange down
     */
    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTile) {
                return boardTile;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTile) {
                List<TilePanel> reverseList = new ArrayList<>(boardTile);
                Collections.reverse(reverseList);
                return reverseList;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTile);
        abstract BoardDirection opposite();
    }
}
