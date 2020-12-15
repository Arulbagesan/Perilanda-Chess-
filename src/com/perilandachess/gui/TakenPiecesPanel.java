package com.perilandachess.gui;

import com.perilandachess.board.Move;
import com.perilandachess.gui.Table.MoveLog;
import com.perilandachess.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TakenPiecesPanel extends JPanel {

    // north JPanel, for orange pieces
    private final JPanel northPanel;

    // southe JPanel, for greenPieces
    private final JPanel southPanel;

    // panel border
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    // panel color
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");

    // panel dimension
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80, 80);

    /**
     * Constructor for TakenPiecesPanel
     */
    public TakenPiecesPanel(){
        super(new BorderLayout());
        // set background of panel
        setBackground(PANEL_COLOR);
        //set border of panel
        setBorder(PANEL_BORDER);

        // create two JPanels with gridlayout 7 x 2 for 14 pieces max
        northPanel = new JPanel(new GridLayout(7, 2));
        southPanel = new JPanel(new GridLayout(7, 2));

        // set the background of created panels
        northPanel.setBackground(PANEL_COLOR);
        southPanel.setBackground(PANEL_COLOR);

        // add created panels to taken pieces panel
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        // set size of taken pieces panel
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    /**
     * This method will place new piece on the taken pieces panel
     * @param moveLog taken moves
     */
    public void redo(final MoveLog moveLog){
        // clear both panels
        southPanel.removeAll();
        northPanel.removeAll();

        // create two arrayLists for green and orange taken pieces
        final List<Piece> greenTakenPieces = new ArrayList<>();
        final List<Piece> orangeTakenPieces = new ArrayList<>();

        // for each move from move log
        for(final Move move : moveLog.getMoves()){
            // check if it was attack move
            if(move.isAttack()){
                // take the taken pieces
                final Piece takenPiece = move.getAttackedPiece();
                // if piece was green add it to green pieces list
                if(takenPiece.getPieceAlliance().isGreen()){
                    orangeTakenPieces.add(takenPiece);
                }
                // taken piece was orange add it to orange pieces list
                else if(takenPiece.getPieceAlliance().isOrange()){
                    greenTakenPieces.add(takenPiece);
                }
                // unknown piece color
                else{
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        //sort green taken pieces
        Collections.sort(greenTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return o1.getPieceValue() - o2.getPieceValue();
            }
        });


        // sort orange taken pieces
        Collections.sort(orangeTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return o1.getPieceValue() - o2.getPieceValue();
            }
        });

        // for each green taken piece
        for(final Piece takenPiece : greenTakenPieces){
            try{
                // create buffered image for the piece (path is: "art" + "\"(for windows) + "G"(piece is green) + "A"(for advancer, taken piece name in general) + ".png")
                // so for example path would be "art\GA.png"
                final BufferedImage image = ImageIO.read(new File("art" + File.separator + takenPiece.getPieceAlliance().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                // create ImageIcon object with image
                final ImageIcon imageIcon = new ImageIcon(image);

                // create new JLabel with given image
                final JLabel imageLabel = new JLabel(imageIcon);

                // add it to south panel
                southPanel.add(imageLabel);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        // same job just orange pieces
        for(final Piece takenPiece : orangeTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("art" + File.separator + takenPiece.getPieceAlliance().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon imageIcon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(imageIcon);
                northPanel.add(imageLabel);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }

    /**
     * This method clears both subpanels
     */
    public void reset(){
        northPanel.removeAll();
        southPanel.removeAll();
        validate();
        repaint();
    }
}
