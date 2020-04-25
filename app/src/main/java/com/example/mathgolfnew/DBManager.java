package com.example.mathgolfnew;

/* FILE SAVING METHOD
STARS,MOVES|STARS,MOVES
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * The type Db manager, manages database.
 */
public class DBManager {

    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected FileOutputStream out;
    protected FileInputStream in;
    protected Context context;

    enum UpdateStatus{
        NEW_LEVEL,
        IMPROVE,
        NO_IMPROVE,
        ERROR}

    /**
     * Instantiates a new Db manager.
     *
     * @param context the context
     */
    public DBManager(Context context) { //todo files
        this.context = context;
        sp = context.getSharedPreferences("spFile", 0);
        editor = sp.edit();
    }

    /**
     * Updates best level.
     *
     * @param best the new best level
     */
    public void updateBestLevel(int best) {
        editor.putInt("best", best);
        editor.commit();
    }

    /**
     * Gets best level.
     *
     * @return the best level
     */
    public int getBestLevel() {
        return sp.getInt("best", 0);
    }

    /**
     * Update level status (stars, moves).
     *
     * @param level the level
     * @param stars the stars data of the level
     * @param moves the moves it took for player
     * @return whether the player succeeded in breaking his record
     */
    public UpdateStatus updateLevel(int level, int[] stars, int moves) {
        int star, len;
        String lvls = read(), replace;
        if (moves <= stars[0]) star = 3;
        else if (moves <= stars[1]) star = 2;
        else star = 1;
        replace = star + "," + moves + "|";
        if (level == getBestLevel() + 1) {
            lvls += replace;
            updateBestLevel(level);
            updateLength(getLength() + replace.length());
            write(lvls);
            return UpdateStatus.NEW_LEVEL; //means new level
        }
        else if (level <= getBestLevel()){
            Pair<Integer, Integer> prev = getLevel(level);
            int prevLen = (int)Math.log10(prev.first) + (int)Math.log10(prev.second) + 4,
                    lenChange = replace.length() - prevLen;
            if (prev.second > moves) {
                String[] tokens = lvls.split(Pattern.quote("|"));
                String res = "";
                for (int i = 0; i < getBestLevel(); i++) {
                    if (i != level - 1) res += (tokens[i] + "|");
                    else res += replace;
                }
                updateLength(getLength() + lenChange);
                write(res);
                return UpdateStatus.IMPROVE; //means updated
            }
            else return UpdateStatus.NO_IMPROVE; //didnt work
        }
        else return UpdateStatus.ERROR; //wtf
    }

    /**
     * Update length of the data string.
     *
     * @param i the length
     */
    public void updateLength(int i) {
        editor.putInt("len", i);
        editor.commit();
    }

    /**
     * Gets level player data.
     *
     * @param level the level id
     * @return the stars and the moves of the player, or null if nonexistent
     */
    public Pair<Integer, Integer> getLevel(int level) {
        if (level > getBestLevel()) return null;
        String str = read(), lvl;
        String[] tokens = str.split(Pattern.quote("|"));
        lvl = tokens[level - 1];
        tokens = lvl.split(Pattern.quote(","));
        return new Pair<>(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }

    /**
     * Write string into file.
     *
     * @param str the string
     */
    public void write(String str) {
        try
        {
            out = context.openFileOutput("details1",Context.MODE_PRIVATE);
            if(str!=null)
            {
                try {
                    out.write(str.getBytes(), 0, str.length());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Read string from file.
     *
     * @return the string
     */
    public String read() {
        if (getLength() == 0) return "";
        byte[]buffer=new byte[4096];
        try {
            in=context.openFileInput("details1");
            in.read(buffer,0,getLength());
            in.close();
            return new String(buffer,0,getLength());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets length of file string.
     *
     * @return the length
     */
    public int getLength() {
        return sp.getInt("len",0);
    }

    /**
     * resets database.
     */
    public void reset() {
        updateBestLevel(0);
        updateLength(0);
        write("");
    }
}
