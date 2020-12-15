package com.perilandachess.gui;

import com.perilandachess.Alliance;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a part of the window that will be in a color of next move maker
 */
public class NextMovePanel extends JPanel {
    // this label will set the color of the next move maker
    private JLabel nextMoveLabel;

    /**
     * Constructor for the next move maker
     */
    public NextMovePanel(){
        super(new BorderLayout());
        nextMoveLabel = new JLabel();
        this.add(nextMoveLabel);
        setPreferredSize(new Dimension(60, 5));
    }

    /**
     * This method will cahnge the color of the entire panel
     * @param alliance Color to be set on panel
     */
    public void redo(Alliance alliance){
        if(alliance == Alliance.ORANGE){
            setBackground(Color.ORANGE);
        }else{
            setBackground(Color.GREEN);
        }
    }
}
