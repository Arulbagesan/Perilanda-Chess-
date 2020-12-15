package com.perilandachess;

import com.perilandachess.player.GreenPlayer;
import com.perilandachess.player.OrangePlayer;
import com.perilandachess.player.Player;

public enum Alliance {
    ORANGE{
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isOrange() {
            return true;
        }

        @Override
        public boolean isGreen() {
            return false;
        }

        @Override
        public Player choosePlayer(OrangePlayer orangePlayer, GreenPlayer greenPlayer) {
            return orangePlayer;
        }
    },
    GREEN {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isOrange() {
            return false;
        }

        @Override
        public boolean isGreen() {
            return true;
        }

        @Override
        public Player choosePlayer(OrangePlayer orangePlayer, GreenPlayer greenPlayer) {
            return greenPlayer;
        }
    };

    /**
     * This method gets direction of the pieces based on the color
     * @return -1 for green, 1 for orange
     */
    public abstract int getDirection();

    /**
     * This method checks if piece is orange
     * @return true if piece is orange, false otherwise
     */
    public abstract boolean isOrange();

    /**
     * This method checks if piece is green
     * @return true if piece is green, false otherwise
     */
    public abstract boolean isGreen();

    /**
     * This method choose one of the players based on color
     * @param orangePlayer orange player
     * @param greenPlayer green player
     * @return GreenPlayer if player is green, OrangePlayer otherwise
     */
    public abstract Player choosePlayer(OrangePlayer orangePlayer, GreenPlayer greenPlayer);
}
