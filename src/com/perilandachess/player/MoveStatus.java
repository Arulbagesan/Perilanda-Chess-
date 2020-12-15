package com.perilandachess.player;

public enum MoveStatus {
    DONE{
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone() {
            return false;
        }
    };

    /**
     * This method is used to check if move is successfully done
     * @return true if state is DONE, false if state is ILLEGAL_MOVE
     */
    public abstract boolean isDone();
}
