package com.example.mathgolfnew;
/*LEVEL CODE FORMAT:
MAIN CODE:
beginVal,goalVal|startNode, endNode|node,node,node|edge,edge,edge|3star,2star,out|param
NODE:
val.type.order
EDGE:
from.to
 */

/**
 * The class that generates built in levels
 */
public class BuiltInLevels {
    static int levelAmount = 20;

    /**
     * Gets level amount.
     *
     * @return the level amount
     */
    public static int getLevelAmount() {
        return levelAmount;
    }

    /**
     * Gets specified level string code.
     *
     * @param i the level id
     * @return the level string code
     */
    public static String getLevel(int i) {
        switch(i) {
            case 1:
                return "6,3|0,-1|2.m.6,5.a.0,8.s.3|0.1,1.2|2,2,2|3";
            case 2:
                return "9,1|2,-1|1.a.4,4.s.3,5.s.7,2.m.1|1.2,2.0,2.3|3,4,6|7";
            case 3:
                return "3,7|0,-1|5.a.7,3.m.2,7.s.4|0.1,1.2,0.2|3,3,3|5";
            case 4:
                return "2,-4|0,-1|2.a.6,3.m.5,1.s.7,-1.n.1|3.1,1.0,3.2,0.2,0.3|3,4,6|4";
            case 5:
                return "-4,-1|1,-1|-1.n.0,5.m.5,6.a.3,7.a.1,6.a.2|4.2,2.0,0.3,2.1,1.3,1.4|4,6,8|9";
            case 6:
                return "-4,-2|1,-1|4.a.5,3.m.4,5.s.7,-1.c.1,10.s.2|3.1,1.0,0.4,0.2,1.4|5,7,10|1";
            case 7:
                return "21,4|1,-1|7.a.5,-1.c.7,4.m.0,4.s.3,4.a.1,8.a.4|0.4,4.5,5.3,3.0,0.1,4.2|6,9,12|6";
            case 8:
                return "19,-7|0,-1|-1.c.5,-1.n.1,7.s.2,2.s.0,3.s.4|2.4,4.0,0.2,2.1,1.3,2.3|4,6,8|2";
            case 9:
                return "39,20|1,-1|1.s.1,-1.c.4,5.a.0,2.a.7,8.s.5,4.m.6|4.1,1.6,6.3,3.0,0.5,5.3,5.4,5.6|5,7,10|-1";
            case 10:
                return "39,20|1,-1|1.s.1,-1.c.4,5.s.3,5.a.0,2.a.7,8.s.5,4.m.6|4.1,1.6,6.3,3.0,0.2,0.5,5.3,5.4,5.6|5,7,10|5";
            case 11:
                return "6,3|0,-1|2.m.6,5.a.0,8.s.3|0.1,1.2|2,2,2|3";
            case 12:
                return "9,1|2,-1|1.a.4,4.s.3,5.s.7,2.m.1|1.2,2.0,2.3|3,4,6|7";
            case 13:
                return "3,7|0,-1|5.a.7,3.m.2,7.s.4|0.1,1.2,0.2|3,3,3|5";
            case 14:
                return "2,-4|0,-1|2.a.6,3.m.5,1.s.7,-1.n.1|3.1,1.0,3.2,0.2,0.3|3,4,6|4";
            case 15:
                return "-4,-1|1,-1|-1.n.0,5.m.5,6.a.3,7.a.1,6.a.2|4.2,2.0,0.3,2.1,1.3,1.4|4,6,8|9";
            case 16:
                return "-4,-2|1,-1|4.a.5,3.m.4,5.s.7,-1.c.1,10.s.2|3.1,1.0,0.4,0.2,1.4|5,7,10|1";
            case 17:
                return "21,4|1,-1|7.a.5,-1.c.7,4.m.0,4.s.3,4.a.1,8.a.4|0.4,4.5,5.3,3.0,0.1,4.2|6,9,12|6";
            case 18:
                return "19,-7|0,-1|-1.c.5,-1.n.1,7.s.2,2.s.0,3.s.4|2.4,4.0,0.2,2.1,1.3,2.3|4,6,8|2";
            case 19:
                return "39,20|1,-1|1.s.1,-1.c.4,5.a.0,2.a.7,8.s.5,4.m.6|4.1,1.6,6.3,3.0,0.5,5.3,5.4,5.6|5,7,10|-1";
            case 20:
                return "39,20|1,-1|1.s.1,-1.c.4,5.s.3,5.a.0,2.a.7,8.s.5,4.m.6|4.1,1.6,6.3,3.0,0.2,0.5,5.3,5.4,5.6|5,7,10|5";
        }
        return null;
    }
}
