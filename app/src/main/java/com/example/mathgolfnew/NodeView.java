package com.example.mathgolfnew;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.Random;

public class NodeView extends Node {
    protected float x, y, r;
    protected int id;
    protected Context context;
    protected static Random rnd = new Random();
    protected Paint paint = new Paint();
    protected int param;

    protected final char ADD = 'a';
    protected final char SUBTRACT = 's';
    protected final char MULTIPLY = 'm';
    protected final char ZERO = 'z';
    protected final char NEGATIVE = 'n';
    protected final char CUT = 'c';

    /**
     * Instantiates a new Node view.
     *
     * @param n         the node object
     * @param context   the context
     * @param height    the board height
     * @param width     the board width
     * @param id        the id
     * @param parameter the level custom parameter
     */
    public NodeView(Node n, Context context, float height, float width, int id, int parameter) {
        super(n.getVal(), n.getType(), n.getOrderClockwise());
        this.context = context;
        this.id = id;
        int order = n.getOrderClockwise();
        float heightUnit = height / 9, widthUnit = width / 9;
        param = (parameter == -1 ? rnd.nextInt(11) : parameter);
        switch(order) { //dictates coordinates on board
            case 0:
                x = 3 * widthUnit + (float)param / 10 * widthUnit;
                y = 3 * heightUnit * (float)param / 10;
                break;
            case 1:
                x = 5 * widthUnit + (float)param / 10 * widthUnit;
                y = 3 * heightUnit * (float)param / 10;
                break;
            case 2:
                x = 7 * widthUnit + 2 * (float)param / 10 * widthUnit;
                y = 3 * heightUnit + (float)param / 10 * heightUnit;
                break;
            case 3:
                x = 7 * widthUnit + 2 * (float)param / 10 * widthUnit;
                y = 5 * heightUnit + (float)param / 10 * heightUnit;
                break;
            case 4:
                x = 5 * widthUnit + (float)param / 10 * widthUnit;
                y = 6 * heightUnit + (float)param / 10 * 3 * heightUnit;
                break;
            case 5:
                x = 3 * widthUnit + (float)param / 10 * widthUnit;
                y = 6 * heightUnit + (float)param / 10 * 3 * heightUnit;
                break;
            case 6:
                x = 2 * widthUnit * (float)param / 10;
                y = 5 * heightUnit + (float)param / 10 * heightUnit;
                break;
            case 7:
                x = 2 * widthUnit * (float)param / 10;
                y = 3 * heightUnit + (float)param / 10 * heightUnit;
                break;
        }
        r = heightUnit / 2 > widthUnit / 2 ? 2 * heightUnit / 3 : 2 * widthUnit / 3;
        if (x < r) x = r + 30; //if generated out of screen
        else if (x > width - r) x = width - r - 30;
        if (y < r) y = r + 30;
        else if (y > height - r) y = height - r - 30;
        switch(n.getType()) { //sets color
            case ADD:
                paint.setColor(Color.parseColor("#0F570A"));
                break;
            case SUBTRACT:
                paint.setColor(Color.parseColor("#9A0404"));
                break;
            case MULTIPLY:
                paint.setColor(Color.parseColor("#5163BD"));
                break;
            case ZERO:
                paint.setColor(Color.BLACK);
                break;
            case NEGATIVE:
                paint.setColor(Color.MAGENTA);
                break;
            case CUT:
                paint.setColor(Color.GRAY);
                break;
        }
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
     * Gets y.
     *
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Draws node on canvas.
     *
     * @param canvas the canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, r, paint);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(48);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAlpha(255);
        textPaint.setFakeBoldText(true);
        textPaint.setElegantTextHeight(true);
        textPaint.setHinting(Paint.HINTING_ON);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        int val = this.getVal();
        switch(this.getType()) {
            case ADD:
            case SUBTRACT:
            case MULTIPLY:
                canvas.drawText(Integer.toString(val), x, y - r / 3, textPaint);
        }
    }

    /**
     * Checks whether player touched the node.
     *
     * @param x0 the x coordinate of the touch
     * @param y0 the y coordinate of the touch
     * @return true iff player touched node
     */
    public boolean touched(float x0, float y0) {
        return Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0)) <= r;
    }
}
