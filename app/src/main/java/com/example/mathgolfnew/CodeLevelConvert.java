/*LEVEL CODE FORMAT:
MAIN CODE:
beginVal,goalVal|startNode, endNode|node,node,node|edge,edge,edge|3star,2star,out|param
NODE:
val.type.order
EDGE:
from.to
 */



package com.example.mathgolfnew;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The type Code level convert - converts string code < - > level.
 */
public class CodeLevelConvert {
    /**
     * Level object to string code.
     *
     * @param lvl the level object
     * @return the string code
     */
    public static String levelToCode(Level lvl) {
        //black magic
        String code = "";
        code = code + Objects.requireNonNull(lvl.getBeginGoal().first).toString() + "," + Objects.requireNonNull(lvl.getBeginGoal().second).toString() + "|";
        code = code + Objects.requireNonNull(lvl.getStartEnd().first).toString() + "," + Objects.requireNonNull(lvl.getStartEnd().second).toString() + "|";
        for (int i = 0; i < lvl.getVertices().size(); i++) {
            code = code + lvl.getVertices().get(i).getVal() + "." + lvl.getVertices().get(i).getType()
                    + "." + lvl.getVertices().get(i).getOrderClockwise();
            if (i == lvl.getVertices().size() - 1) code = code + "|";
            else code = code + ",";
        }
        for (int i = 0; i < lvl.getEdges().size(); i++) {
            code = code + lvl.getEdges().get(i).first + "." + lvl.getEdges().get(i).second;
            if (i == lvl.getEdges().size() - 1) {code = code + "|";}
            else code = code + ",";
        }
        code = code + lvl.getStars()[0] + "," + lvl.getStars()[1] + "," +
                lvl.getStars()[2] + "|";
        code = code + lvl.getParam();
        return code;
    }

    /**
     * String code to level object.
     *
     * @param lvlCode the string code
     * @return the level obj
     */
    public static Level codeToLevel(String lvlCode) {
        //more black magic
        ArrayList<Node> ver = new ArrayList<Node>();
        ArrayList<Pair<Integer,Integer>> edg = new ArrayList<Pair<Integer,Integer>>();
        Pair<Integer,Integer> bg = new Pair(0,0);
        Pair<Integer,Integer> se = new Pair(0,0);
        int[] st = new int[3];
        Level l = new Level();
        String[] tokens = lvlCode.split(Pattern.quote("|"));
        String[] beginGoalStr = tokens[0].split(Pattern.quote(","));
        l.setBeginGoal(new Pair(Integer.parseInt(beginGoalStr[0]),Integer.parseInt(beginGoalStr[1])));
        String[] startAndEnd = tokens[1].split(Pattern.quote(","));
        l.setStartEnd(new Pair(Integer.parseInt(startAndEnd[0]), Integer.parseInt(startAndEnd[1])));
        String[] v = tokens[2].split(Pattern.quote(","));
        for (int i = 0; i < v.length ; i++) {
            Node n = new Node(v[i]);
            l.addVertex(n);
        }
        String[] e = tokens[3].split(Pattern.quote(","));
        for (int i = 0; i < e.length; i++) {
            String[] tmp = e[i].split(Pattern.quote("."));
            l.addEdge(new Pair(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1])));
        }
        String[] star = tokens[4].split(Pattern.quote(","));
        int[] tmp = {Integer.parseInt(star[0]), Integer.parseInt(star[1]), Integer.parseInt(star[2])};
        l.setStars(tmp);
        l.setParam(Integer.parseInt(tokens[5]));
        return l;
    }
}
