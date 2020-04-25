package com.example.mathgolfnew;

import android.content.Context;

public class Game {
    protected final char ADD = 'a';
    protected final char SUBTRACT = 's';
    protected final char MULTIPLY = 'm';
    protected final char ZERO = 'z';
    protected final char NEGATIVE = 'n';
    protected final char CUT = 'c';
    enum Status{FAILED, SUCCESSFUL, VICTORY, NO_MORE_MOVES}

    protected Context context;
    protected Level level;
    protected int playerNodeNum;
    protected int moveCount;
    protected int value;

    /**
     * Instantiates a new Game.
     *
     * @param c     the context
     * @param level the level object
     */
    public Game(Context c, Level level) {
        this.context = c;
        this.level = level;
        this.playerNodeNum = level.getStartEnd().first;
        this.moveCount = 0;
        this.value = level.getBeginGoal().first;
    }

    /**
     * Moves the player to the nodeId (if possible)
     *
     * @param nodeId the node id
     * @return the status (whether succeeded or failed)
     */
    public Status move(int nodeId) {
        if (isConnected(playerNodeNum,nodeId)) {
            moveCount++;
            playerNodeNum = nodeId;
            changeVal(level.getVertices().get(playerNodeNum));
            if (value == level.getBeginGoal().second) {
                if (level.getStartEnd().second == -1) {
                    return Status.VICTORY;
                }
                else if (level.getStartEnd().second != -1) {
                    if (playerNodeNum == level.getStartEnd().second) return Status.VICTORY;
                }
            }
            if (moveCount <= level.getStars()[2]) {
                return Status.SUCCESSFUL;
            }
            else {
                return Status.NO_MORE_MOVES;
            }
        }
        else {
            return Status.FAILED;
        }
    }

    /**
     * Change the current score of the player according to node.
     *
     * @param node the node that the player arrived at
     * @return true if went successfully
     */
    protected boolean changeVal(Node node) {
        char mode = node.getType();
        switch (mode) {
            case ADD:
                value += node.getVal();
                break;
            case SUBTRACT:
                value -= node.getVal();
                break;
            case MULTIPLY:
                value *= node.getVal();
                break;
            case ZERO:
                value = 0;
                break;
            case NEGATIVE:
                value = (-value);
                break;
            case CUT:
                value /= 10;
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Checks if 2 nodes are connected.
     *
     * @param playerNodeNum the player node num
     * @param destNode      the dest node
     * @return true iff they're connected
     */
    protected boolean isConnected(int playerNodeNum, int destNode) {
        if (destNode == -1) return false;
        Integer i1 = new Integer(playerNodeNum);
        Integer i2 = new Integer(destNode);
        if ((level.getAdjMat())[i1][i2] == 1) {
            return true;
        }
        return false;
    }

    /**
     * Gets move count.
     *
     * @return the move count
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Gets player node num.
     *
     * @return the player node num
     */
    public int getPlayerNodeNum() {
        return playerNodeNum;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public Level getLevel() {
        return level;
    }
}
