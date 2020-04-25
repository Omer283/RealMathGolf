
/*LEVEL CODE FORMAT:
MAIN CODE:
beginVal,goalVal|startNode,endNode|node,node,node|edge,edge,edge|3star,2star,out|param(0-10)
NODE:
val.type.positionClockwise
EDGE:
from.to
 */

package com.example.mathgolfnew;

import androidx.core.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * The type Random level generator.
 */
public class RandomLevelGenerator {
    enum Difficulty {EASY, MEDIUM, HARD}
    protected final static double SCALE_VALUE = 1;
    protected static Random rnd = new Random();
    protected static final int MAX = 8;


    protected static final char ADD = 'a';
    protected static final char SUBTRACT = 's';
    protected static final char MULTIPLY = 'm';
    protected static final char ZERO = 'z';
    protected static final char NEGATIVE = 'n';
    protected static final char CUT = 'c';

    /**
     * Creates a random level object
     * @param difficulty given difficulty
     * @return randomly generated object level
     */
    public static Level createRandomLevel(Difficulty difficulty) {
        Level l = new Level(); //create blank level
        int tmp;
        Pair<Integer, Integer> tmpPair, tmpPairRev;
        ArrayList<Integer> x = new ArrayList<Integer>();
        int nodesNum = randomiseNodesNum(difficulty);
        int movesNum = randomiseMovesNum(difficulty, nodesNum);
        int begin = randomiseBegin(difficulty); //beginning value
        OperationClass[] operationArray = randomiseOperationArray(nodesNum); //the actual nodes
        int[] movesToVictory = randomiseVictorySequence(movesNum,nodesNum); //the winning sequence
        int goal = calculateGoal(operationArray, movesToVictory, begin); //ending number
        while (begin == goal) {
            movesToVictory = randomiseVictorySequence(movesNum, nodesNum);
            goal = calculateGoal(operationArray, movesToVictory, begin);
        }
        l.setBeginGoal(new Pair(begin, goal)); //sets the beginGoalVal object
        Pair<Integer, Integer> p, pReverse;
        for (int k = 0; k < movesNum - 1; k++) {
            p = new Pair(movesToVictory[k], movesToVictory[k + 1]);
            pReverse = new Pair(p.second, p.first);
            if (l.getEdges() == null ||(!l.getEdges().contains(p) & !l.getEdges().contains(pReverse))) {
                l.addEdge(p);
            }
        } //adding edges of winning sequence
        l.initiateMat(nodesNum, l.getEdges());
        l.initiateList();
        HashSet<Integer> connectComponents = new DisjointSet(l.getEdges(), nodesNum).getFatherSet();
        boolean first = false;
        int prev = -1;
        for (int i : connectComponents) {
            if (!first) {
                first = true;
                prev = i;
                continue;
            }
            l.addEdge(new Pair<Integer, Integer>(i, prev));
            prev = i;
        }
        while (l.getEdges().size() < SCALE_VALUE * nodesNum) {
            l.addEdge(generateEdge(l.getEdges(),nodesNum));
        } //add edges until there are enough
        l.initiateMat(nodesNum, l.getEdges());
        l.initiateList();
        for (int k = 0; k < MAX; k++) {
            x.add(k);
        }
        java.util.Collections.shuffle(x);
        for (int k = 0; k < nodesNum; k++) {
            l.addVertex(new Node(operationArray[k].getValue(),operationArray[k].getOperation(), x.get(k)));
        } //adds nodes and sets locations
        l.setStartEnd(new Pair(l.getAdjList()[movesToVictory[0]].get(0), -1));
        int[] stars = {movesNum, (int) (movesNum * 1.5), movesNum * 2};
        l.setStars(stars);
        l.setParam(-1);
        if(l.getBeginGoal().second == 0 || possibleInOneMove(l)) return createRandomLevel(difficulty);
        //eliminating the case of goal being 0 or possible in one move
        return l;
    }

    /**
     * Checks if level is possible to complete with one move
     * @param l level
     * @return true iff you can complete within one move
     */
    protected static boolean possibleInOneMove(Level l) {
        int startIndex = l.getStartEnd().first, startVal = l.getBeginGoal().first,
                tmp = startVal, goal = l.getBeginGoal().second;
        ArrayList<Integer> connected = l.getAdjList()[startIndex];
        for (int i : connected) {
            switch(l.getVertices().get(i).getType()) {
                case ADD:
                    tmp += l.getVertices().get(i).getVal();
                    break;
                case SUBTRACT:
                    tmp -= l.getVertices().get(i).getVal();
                    break;
                case MULTIPLY:
                    tmp *= l.getVertices().get(i).getVal();
                    break;
                case NEGATIVE:
                    tmp = -tmp;
                    break;
                case ZERO:
                    tmp = 0;
                    break;
                case CUT:
                    tmp /= 10;
                    break;
            }
            if (tmp == goal) return true;
            tmp = startVal;
        }
        return false;
    }

    /**
     * checks connectivity component of given node (unused function)
     * @param adjList adjacency list
     * @param start starting node
     * @return connectivity component
     */
    protected static HashSet<Integer> connectivityArr(ArrayList<Integer>[] adjList, int start) {
        HashSet<Integer> visited = new HashSet<>();
        DFS(adjList, visited, start);
        return visited;
    }

    /**
     * DFSs the graph to check connectivity components
     * @param adjList adjacency list
     * @param visited visited arr
     * @param curr current node
     */
    protected static void DFS(ArrayList<Integer>[] adjList, HashSet<Integer> visited, int curr) {
        visited.add(curr);
        for (int k = 0; k < adjList[curr].size(); k++) {
            if (!visited.contains(k)) {
                DFS(adjList, visited, k);
            }
        }
    }

    /**
     * generated random edge
     * @param edges current edges
     * @param nodesNum number of nodes
     * @return randomly generated edge
     */
    protected static Pair<Integer, Integer> generateEdge(ArrayList<Pair<Integer, Integer>> edges, int nodesNum) {
        Pair<Integer, Integer> tmp, tmpRev;
        int from, to;
        while(true) {
            from = rnd.nextInt(nodesNum);
            to = rnd.nextInt(nodesNum);
            if (from != to) {
                tmp = new Pair(from, to);
                tmpRev = new Pair(to, from);
                if (!edges.contains(tmp) && !edges.contains(tmpRev)) {
                    return tmp;
                }
            }
        }
    }

    /**
     * Gets array of degrees of nodes (unused)
     * @param nodesNum number of nodes
     * @param edges edges
     * @return array of degrees
     */
    protected static int[] getDegrees(int nodesNum, ArrayList<Pair<Integer, Integer>> edges) {
        int[] degrees = new int[nodesNum];
        for (Pair<Integer, Integer> edge : edges) {
            degrees[edge.first]++;
            degrees[edge.second]++;
        }
        return degrees;
    }

    /**
     * Calculates the goal according to the winning sequence
     * @param operationArray operations
     * @param movesToVictory winning sequence
     * @param start starting value
     * @return goal
     */
    protected static int calculateGoal(OperationClass[] operationArray, int[] movesToVictory, int start) {
        int curr = start;
        OperationClass tmp;
        for (int i = 0; i < movesToVictory.length; i++) {
            tmp = operationArray[movesToVictory[i]];
            switch(tmp.getOperation()) {
                case ADD:
                    curr += tmp.getValue();
                    break;
                case SUBTRACT:
                    curr -= tmp.getValue();
                    break;
                case MULTIPLY:
                    curr *= tmp.getValue();
                    break;
                case NEGATIVE:
                    curr = -curr;
                    break;
                case ZERO:
                    curr = 0;
                    break;
                case CUT:
                    curr /= 10;
            }
        }
        return curr;
    }

    /**
     * Randomise begin value.
     *
     * @param difficulty the difficulty
     * @return the value
     */
    protected static int randomiseBegin(Difficulty difficulty) {
        return rnd.nextInt(15 * (difficulty.ordinal() + 1)) - 5;
    }

    /**
     * Randomise victory sequence.
     *
     * @param movesNum the moves num
     * @param nodesNum the nodes num
     * @return the victory sequence
     */
    protected static int[] randomiseVictorySequence(int movesNum, int nodesNum) {
        int[] moves = new int[movesNum];
        int prev = -1, res = -1;
        for (int i = 0; i < movesNum; i++) {
            while (res == prev) {
                res = rnd.nextInt(nodesNum);
            }
            moves[i] = res;
            prev = res;
        }
        return moves;
    }

    /**
     * Randomise operation array of level
     *
     * @param nodesNum the nodes num
     * @return the operations in the level
     */
    protected static OperationClass[] randomiseOperationArray(int nodesNum) {
        OperationClass[] opers = new OperationClass[nodesNum];
        boolean neg = false, zero = false, cut = false;
        int randomNum, specialCount = 0;
        float factor = 0.25f * nodesNum;
        for (int i = 0; i < nodesNum; i++) {
            opers[i] = new OperationClass();
            randomNum = rnd.nextInt(14);
            if (randomNum < 3) opers[i].setOperation(ADD);
            else if (randomNum < 6) opers[i].setOperation(SUBTRACT);
            else if (randomNum < 9) opers[i].setOperation(MULTIPLY);
            else if (randomNum < 11 && !neg && specialCount < 2) {
                opers[i].setOperation(NEGATIVE);
                neg = true;
                specialCount++;
            }
            else if (randomNum < 13 && !cut && specialCount < 2) {
                opers[i].setOperation(CUT);
                cut = true;
                specialCount++;
            }
            else if (randomNum == 13 && !zero && specialCount < 2) {
                opers[i].setOperation(ZERO);
                zero = true;
                specialCount++;
            }
            else {i--; continue;}
            switch(opers[i].getOperation()) {
                case ADD:
                case SUBTRACT:
                    opers[i].setValue(rnd.nextInt((int)(factor * 10f) + 1));
                    break;
                case MULTIPLY:
                    opers[i].setValue(rnd.nextInt((int)(factor * 4f) + 2));
                    break;
                case NEGATIVE:
                case ZERO:
                case CUT:
                    opers[i].setValue(-1);
                    break;
            }
        }
        return opers;
    }

    /**
     * Randomise nodes num.
     *
     * @param difficulty the difficulty
     * @return the number
     */
    public static int randomiseNodesNum(Difficulty difficulty){
        int add = rnd.nextInt(2);
        if (difficulty.ordinal() == 0) return 4;
        return 2 * difficulty.ordinal() + add + 3;
    }

    /**
     * Randomise moves number.
     *
     * @param difficulty the difficulty
     * @param nodesNum   the nodes number
     * @return the number of moves
     */
    protected static int randomiseMovesNum(Difficulty difficulty, int nodesNum) {
        int diff;
        if (difficulty == Difficulty.EASY) {
            diff = rnd.nextInt(3) - 1;
            return nodesNum + diff;
        } else if (difficulty == Difficulty.MEDIUM) {
            diff = rnd.nextInt(5) - 2;
            return nodesNum + diff;
        } else if (difficulty == Difficulty.HARD){
            diff = rnd.nextInt(6) - 2;
            return nodesNum + diff;
        }
        return -1;
    }

    /**
     * The type Disjoint set.
     */
    protected static class DisjointSet {
        HashSet<Integer> fatherSet = new HashSet<Integer>();
        int[] fatherEach;
        int[] depth;

        /**
         * Instantiates a new Disjoint set.
         *
         * @param e    the edges
         * @param size the size
         */
        public DisjointSet(ArrayList<Pair<Integer, Integer>> e, int size) {
            fatherEach = new int[size];
            depth = new int[size];
            for (int i = 0; i < size; i++) {
                fatherSet.add(i);
                fatherEach[i] = i;
                depth[i] = 1;
            }
            for (Pair<Integer, Integer> edge : e) {
                unite(edge.first, edge.second);
            }
        }

        /**
         * finds father of node.
         *
         * @param src the node
         * @return the father
         */
        protected int father(int src) {
            while(fatherEach[src] != src)
                src = fatherEach[src];
            return src;
        }

        /**
         * unites 2 sets.
         *
         * @param a the first set
         * @param b the second set
         * @return true iff father(a) != father(b)
         */
        protected boolean unite(int a, int b) {
            int fatherA = father(a), fatherB = father(b);
            if (fatherA == fatherB) return false;
            if (depth[fatherA] > depth[fatherB]) {
                fatherSet.remove(fatherB);
                fatherEach[fatherB] = fatherA;
                if (depth[fatherA] < depth[fatherB] + 1)
                    depth[fatherA] = depth[fatherB] + 1;
                return true;
            }
            else {
                fatherSet.remove(fatherA);
                fatherEach[fatherA] = fatherB;
                if (depth[fatherB] < depth[fatherA] + 1)
                    depth[fatherB] = depth[fatherA] + 1;
                return true;
            }
        }

        /**
         * Gets father set.
         *
         * @return the father set
         */
        public HashSet<Integer> getFatherSet() {
            return fatherSet;
        }
    }

    /**
     * The type Operation class.
     */
    static class OperationClass {
            protected char operation;
            protected int value;

            public OperationClass() {} //default

        /**
         * Instantiates a new Operation class.
         *
         * @param operation the operation
         * @param value     the value
         */
        public OperationClass(char operation, int value) {
                this.operation = operation;
                this.value = value;
            }

        /**
         * Sets operation.
         *
         * @param operation the operation
         */
        public void setOperation(char operation) {
                this.operation = operation;
            }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(int value) {
                this.value = value;
            }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
                return value;
            }

        /**
         * Gets operation.
         *
         * @return the operation
         */
        public char getOperation() {
                return operation;
            }
        }
}