package com.example.mathgolfnew;

import androidx.core.util.Pair;
import java.util.ArrayList;

public class Level {
    protected ArrayList<Node> vertices;
    protected ArrayList<Pair<Integer, Integer>> edges;
    protected Pair<Integer, Integer> startEndNode;
    protected int[][] adjMat;
    protected Pair<Integer, Integer> beginGoalVal;
    protected int[] stars;
    protected ArrayList<Integer>[] adjList;
    protected int param;

    /**
     * Instantiates a new Level.
     */
    public Level() {
    }

    /**
     * Instantiates a new Level.
     *
     * @param vertices     the vertices
     * @param edges        the edges
     * @param beginGoalVal the pair representing starting score and ending score
     * @param startEndNode the start node id, end node id
     * @param stars        the stars for the level
     * @param param        the custom parameter for the level
     */
    public Level(ArrayList<Node> vertices, ArrayList<Pair<Integer, Integer>> edges, Pair<Integer, Integer> beginGoalVal, Pair<Integer, Integer> startEndNode, int[] stars, int param) {
        this.vertices = vertices;
        this.edges = edges;
        this.startEndNode = startEndNode;
        this.beginGoalVal = beginGoalVal;
        this.stars = stars;
        this.param = param;
        initiateMat();
        initiateList();
    }

    /**
     * Instantiates a new Level.
     *
     * @param lvlCode the level string code
     */
    public Level(String lvlCode) {
        Level tmp = CodeLevelConvert.codeToLevel(lvlCode);
        this.vertices = tmp.getVertices();
        this.edges = tmp.getEdges();
        this.startEndNode = tmp.getStartEnd();
        this.beginGoalVal = tmp.getBeginGoal();
        this.stars = tmp.getStars();
        this.param = tmp.getParam();
        initiateMat();
        initiateList();
    }

    /**
     * Initiates adjacency matrix.
     */
    public void initiateMat() {
        this.adjMat = new int[vertices.size()][vertices.size()];
        for (Pair<Integer, Integer> k : edges) {
            this.adjMat[k.first.intValue()][k.second.intValue()] = this.adjMat[k.second.intValue()][k.first.intValue()] = 1;
        }
    }

    /**
     * Initiate adjacency matrix given size and edges.
     *
     * @param size  the size
     * @param edges the edges
     */
    public void initiateMat(int size, ArrayList<Pair<Integer, Integer>> edges) {
        this.adjMat = new int[size][size];
        for (Pair<Integer, Integer> k : edges) {
            this.adjMat[k.first][k.second] = this.adjMat[k.second][k.first] = 1;
        }
    }

    /**
     * Initiate adjacency list, using the adjacency matrix.
     */
    public void initiateList() {
        if (adjMat != null) {
            int len = adjMat[0].length;
            adjList = new ArrayList[len];
            for (int i = 0; i < len; i++) {
                adjList[i] = new ArrayList<>();
                for (int j = 0; j < len; j++) {
                    if (adjMat[i][j] == 1) {
                        adjList[i].add(j);
                    }
                }
            }
        }
    }

    /**
     * Get adjList.
     *
     * @return the adj list
     */
    public ArrayList<Integer>[] getAdjList() {
        return adjList;
    }


    /**
     * Gets vertices.
     *
     * @return the vertices
     */
    public ArrayList<Node> getVertices() {
        return vertices;
    }

    /**
     * Gets edges.
     *
     * @return the edges
     */
    public ArrayList<Pair<Integer, Integer>> getEdges() {
        return edges;
    }

    /**
     * Gets start node end node.
     *
     * @return the start end
     */
    public Pair<Integer, Integer> getStartEnd() {
        return startEndNode;
    }

    /**
     * Add edge.
     *
     * @param edge the edge
     */
    public void addEdge(Pair<Integer,Integer> edge) {
        if (this.edges == null) this.edges = new ArrayList<Pair<Integer, Integer>>();
        this.edges.add(edge);
    }

    /**
     * Add vertex.
     *
     * @param n the vertex
     */
    public void addVertex(Node n) {
        if (this.vertices == null) this.vertices = new ArrayList<Node>();
        this.vertices.add(n);
    }


    /**
     * Sets start node end node.
     *
     * @param startEndNode the start end node
     */
    public void setStartEnd(Pair<Integer, Integer> startEndNode) {
        this.startEndNode = startEndNode;
    }


    /**
     * Get adjMat
     *
     * @return the adjMat
     */
    public int[][] getAdjMat() {
        return adjMat;
    }

    /**
     * Sets begin score goal score.
     *
     * @param beginGoalVal the begin goal pair score
     */
    public void setBeginGoal(Pair<Integer, Integer> beginGoalVal) {
        this.beginGoalVal = beginGoalVal;
    }

    /**
     * Gets begin score goal score.
     *
     * @return the begin goal
     */
    public Pair<Integer, Integer> getBeginGoal() {
        return beginGoalVal;
    }

    /**
     * Get stars requirements.
     *
     * @return the stars req
     */
    public int[] getStars() {
        return stars;
    }

    /**
     * Sets stars requirements.
     *
     * @param stars the stars req
     */
    public void setStars(int[] stars) {
        this.stars = stars;
    }

    /**
     * Gets param.
     *
     * @return the param
     */
    public int getParam() {
        return param;
    }

    /**
     * Sets param.
     *
     * @param param the param
     */
    public void setParam(int param) {
        this.param = param;
    }
}