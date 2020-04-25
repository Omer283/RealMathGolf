package com.example.mathgolfnew;

import java.util.regex.Pattern;

public class Node {
    protected int val;
    protected char type;
    protected int orderClockwise;

    /**
     * Instantiates a new Node.
     *
     * @param val   the val
     * @param type  the type
     * @param order the order in clockwise fashion
     */
    public Node(int val, char type, int order) {
        this.val = val;
        this.type = type;
        this.orderClockwise = order;
    }

    /**
     * Instantiates a new Node according to string code.
     *
     * @param str the string
     */
    public Node(String str) {
        String[] tokens = str.split(Pattern.quote("."));
        this.val = Integer.parseInt(tokens[0]);
        this.type = tokens[1].charAt(0);
        this.orderClockwise = Integer.parseInt(tokens[2]);
    }

    /**
     * Gets order clockwise.
     *
     * @return the order clockwise
     */
    public int getOrderClockwise() {
        return orderClockwise;
    }

    /**
     * Gets val.
     *
     * @return the val
     */
    public int getVal() {
        return val;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public char getType() {
        return type;
    }
}
