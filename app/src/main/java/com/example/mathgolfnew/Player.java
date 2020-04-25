package com.example.mathgolfnew;

public class Player {
    protected float x, y;
    protected int id;

    /**
     * Instantiates a new Player.
     *
     * @param a the x
     * @param b the y
     * @param c the starting node id
     */
    public Player(float a, float b, int c) {
        x = a;
        y = b;
        id = c;
    }


    /**
     * Instantiates a new Player.
     */
    public Player() {}

    /**
     * Gets current node id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * Sets current node id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(float y) {
        this.y = y;
    }
}
