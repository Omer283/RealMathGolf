
/*MATH BITS:
 -Adjacency matrix / List
 -Measure connectivity of graph
 -Draw lines between nodes
 -connectivity components using disjoint set (union find) with path compression and union by size
 -Drawing Lines (Analytic Geometry)
 -Hash set
 */

package com.example.mathgolfnew;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;

import java.util.ArrayList;

/**
 * The type Game board activity.
 */
public class GameBoardActivity extends Activity implements View.OnClickListener {

    protected Game game;
    protected ImageView ivReset, ivReturn;
    protected String levelCode;
    protected Level level;
    protected Board board;
    protected TextView tvMoves, tvScore, tvGoal;
    protected LinearLayout lMain;
    protected static int bestLevel;
    protected static int id;
    protected DBManager dbManager;
    protected Dialog d;

    /**
     * function that is called when activity is created, initiates the views and game.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        MainActivity.setCurrScreen(MainActivity.Screen.GAME_BOARD);
        dbManager = new DBManager(this);
        initViews();
        initGame();
    }

    /**
     * Initiates the game, the board and the views.
     */
    protected void initGame() {
        bestLevel = dbManager.getBestLevel();
        Intent intent = getIntent();
        levelCode = intent.getStringExtra("levelCode");
        id = intent.getIntExtra("id", 1);
        level = new Level(levelCode);
        game = new Game(this, level);
        board = new Board(this, tvMoves, tvScore, null);
        tvGoal.setText(Integer.toString(game.getLevel().getBeginGoal().second));
        lMain.addView(board);
    }

    /**
     * Initiates the views and sets listeners.
     */
    protected void initViews() {
        tvMoves = findViewById(R.id.tvMovesNum);
        tvGoal = findViewById(R.id.tvGoalNum);
        tvScore = findViewById(R.id.tvScoreNum);
        lMain = findViewById(R.id.lMain);
        ivReset = findViewById(R.id.ivResetLevel);
        ivReturn = findViewById(R.id.ivBackToLevelMenu);
        ivReset.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
    }

    /**
     * Acts as a listener to clicks on various views
     * @param v - the view that was clicked
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ivResetLevel:
                lMain.removeAllViews();
                game = new Game(this, game.getLevel());
                board = new Board(this, tvMoves, tvScore, board.getNodeArr());
                lMain.addView(board);
                break;
            case R.id.returnToLevel:
                lMain.removeAllViews();
                game = new Game(this, game.getLevel());
                board = new Board(this, tvMoves, tvScore, board.getNodeArr());
                lMain.addView(board);
                d.dismiss();
                break;
            case R.id.ivBackToLevelMenu:
                finish();
                break;
            case R.id.backToLevelMenu:
                d.dismiss();
                finish();
                break;
            case R.id.toNextLevel:
                if (id == BuiltInLevels.getLevelAmount()) {
                    Toast.makeText(this, "No next level", Toast.LENGTH_LONG).show();
                } else {
                    lMain.removeAllViews();
                    bestLevel = dbManager.getBestLevel();
                    levelCode = BuiltInLevels.getLevel(id + 1);
                    level = new Level(levelCode);
                    game = new Game(this, level);
                    id++;
                    board = new Board(this, tvMoves, tvScore, null);
                    tvGoal.setText(Integer.toString(game.getLevel().getBeginGoal().second));
                    lMain.addView(board);
                }
                d.dismiss();
                break;
        }
    }

    /**
     * function that is called when activity is stopped, changes current screen and stops music
     * if the app was shut down and not only the activity.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (MainActivity.getCurrScreen() == MainActivity.Screen.GAME_BOARD)
            MainActivity.setInApp(false);
        if(MainActivity.getmServ() != null && !MainActivity.getMutedMusic() && !MainActivity.getInApp()) {
            MainActivity.getmServ().pauseMusic();
        }
    }

    /**
     * called when app is resumed, resumes the music if needed and updates current screen.
     */
    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.setCurrScreen(MainActivity.Screen.GAME_BOARD);
        if (!MainActivity.getInApp()) {
            MainActivity.setInApp(true);
            if (!MainActivity.getMutedMusic())
            MainActivity.getmServ().resumeMusic();
        }
    }

    /**
     * Function that is called in the case that the player has won.
     *
     * @param moves the moves that the player took to complete level
     */
    public void onWin(int moves) {
        int best = dbManager.getBestLevel(), stars;
        if(game.getLevel().getStars()[0] >= game.getMoveCount()) stars = 3; //sets appropriate stars
        else if (game.getLevel().getStars()[1] >= game.getMoveCount()) stars = 2;
        else stars = 1;
        if(id == -1) { //the level was random
            createDialog(DBManager.UpdateStatus.ERROR, stars, game.getMoveCount());
            return;
        }
        else if (best >= id - 1) { //not a random level
            DBManager.UpdateStatus t = dbManager.updateLevel(id, game.getLevel().getStars(), game.getMoveCount());
            //checks whether the player improved current score
            createDialog(t, stars , game.getMoveCount());
        }
        else { //should never reach here, means that the player has beat a non discovered level
            Log.d("Error", "Error - updated wrong level");
            return;
        }
    }

    /**
     * Create dialog displaying winning information.
     *
     * @param mode      whether improved or not
     * @param stars     the player's stars
     * @param moveCount the move count
     */
    public void createDialog(DBManager.UpdateStatus mode, int stars, int moveCount) {
        d = new Dialog(this);
        d.setContentView(R.layout.win_dialog);
        d.setCancelable(true);
        ImageView ivReturnFromDia, ivRestart, ivNext;
        TextView tvStatus;
        ImageView ivStarsVictory;
        ivReturnFromDia = d.findViewById(R.id.backToLevelMenu);
        ivRestart = d.findViewById(R.id.returnToLevel);
        ivNext = d.findViewById(R.id.toNextLevel);
        tvStatus = d.findViewById(R.id.tvStatus);
        ivStarsVictory = d.findViewById(R.id.ivStarsVictory);
        ivRestart.setOnClickListener(this);
        ivReturnFromDia.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        if(id == -1) ivNext.setVisibility(View.INVISIBLE);
        switch(mode) {
            case NEW_LEVEL:
                tvStatus.setText("New level completed!");
                break;
            case IMPROVE:
                tvStatus.setText("Improved level!");
                break;
            case NO_IMPROVE:
                tvStatus.setText("Didn't improve");
                break;
            default:
                tvStatus.setText("");
        }
        SelectLevelActivity.setStarImage(stars, ivStarsVictory);
        switch(stars) {

        }
        d.show();
        Window w = d.getWindow();
        w.setLayout((int)(0.75f * (Resources.getSystem().getDisplayMetrics().widthPixels)) , (int)(0.52f * (Resources.getSystem().getDisplayMetrics().heightPixels)));
    }

    /**
     * The type Board.
     */
    public class Board extends View {
        Context context;
        Paint paint;
        TextView tvMoves, tvScore;
        ArrayList<NodeView> nodeArr;
        Player player;
        float radius;
        boolean firstDraw;
        boolean moving = false;

        /**
         * Instantiates a new Board.
         *
         * @param context the context
         * @param tvMoves the view that displays moves
         * @param tvScore the view that displays score
         * @param arr     the nodes
         */
        public Board(Context context, TextView tvMoves, TextView tvScore, ArrayList<NodeView> arr) {
            super(context);
            this.context = context;
            this.tvMoves = tvMoves;
            this.tvScore = tvScore;
            this.nodeArr = arr;
            this.paint = new Paint();
            paint.setStrokeWidth(5);
            this.player = new Player();
            firstDraw = true;
            updateScores();
        }

        /**
         * draws board on canvas
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (nodeArr == null) nodeArr = new ArrayList<>();
            if (firstDraw) {
                firstDraw = false;
                float heightUnit = canvas.getHeight() / 9, widthUnit = canvas.getWidth() / 9;
                radius = heightUnit / 2 > widthUnit / 2 ? 2 * heightUnit / 3 : 2 * widthUnit / 3;
                int count = 0;
                if (nodeArr.isEmpty()) {
                    for (Node n : game.getLevel().getVertices()) { //updates node arr
                        nodeArr.add(new NodeView(n, context, canvas.getHeight(), canvas.getWidth(), count, game.getLevel().getParam()));
                        count++;
                    }
                }
                player.setX(nodeArr.get(game.getLevel().getStartEnd().first).getX());
                player.setY(nodeArr.get(game.getLevel().getStartEnd().first).getY());
                player.setId(game.getLevel().getStartEnd().first);
            }
            drawVertices(canvas);
            drawEdges(canvas);
            drawPlayer(canvas);
        }

        /**
         * Draw player.
         *
         * @param canvas the canvas
         */
        protected void drawPlayer(Canvas canvas) {
            Paint c = new Paint();
            c.setColor(Color.WHITE);
            canvas.drawCircle(player.getX(), player.getY(), 15, c);
        }

        /**
         * Draw edges.
         *
         * @param canvas the canvas
         */
        protected void drawEdges(Canvas canvas) {
            ArrayList<Pair<Integer, Integer>> edgesArr = game.getLevel().getEdges();
            paint.setColor(Color.parseColor("#aabbcc"));
            paint.setStrokeWidth(13f);
            NodeView n1, n2;
            for (Pair<Integer, Integer> edge : edgesArr) {
                n1 = findNodeViewById(edge.first);
                n2 = findNodeViewById(edge.second);
                float firstX = n1.getX(), firstY = n1.getY(), lastX = n2.getX(), lastY = n2.getY();
                drawEdge(firstX, firstY, lastX, lastY, radius, canvas);
            }
        }

        /**
         * Draws edge.
         *
         * @param firstX the x1 coordinate
         * @param firstY the y1 coordinate
         * @param lastX  the x2 coordinate
         * @param lastY  the y2 coordinate
         * @param radius the radius
         * @param canvas the canvas
         */
        protected void drawEdge(float firstX, float firstY, float lastX, float lastY, float radius, Canvas canvas) {
            float x0, y0, x1, y1, d = dist(firstX, firstY, lastX, lastY);
            x0 = ((d - radius) / d) * firstX + radius * lastX / d;
            y0 = ((d - radius) / d) * firstY + radius * lastY / d;
            x1 = ((d - radius) / d) * lastX + radius * firstX / d;
            y1 = ((d - radius) / d) * lastY + radius * firstY / d;
            canvas.drawLine(x0,y0,x1,y1,paint);
        }

        /**
         * Draw vertices.
         *
         * @param canvas the canvas
         */
        protected void drawVertices(Canvas canvas) {
            for (NodeView n : nodeArr) {
                n.draw(canvas);
            }
        }

        /**
         * Find node view by id node view.
         *
         * @param id the node id
         * @return the node view
         */
        protected NodeView findNodeViewById(int id) {
            for (NodeView n : nodeArr) {
                if (n.getId() == id) return n;
            }
            return null;
        }

        /**
         * Listens to board touch events
         * @param event
         * @return whether the detection was successful
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (!moving) {
                NodeView n = null;
                for (int k = 0; k < nodeArr.size(); k++) {
                    if (nodeArr.get(k).touched(event.getX(), event.getY())) {
                        n = nodeArr.get(k);
                        break;
                    }
                }
                if (n != null) {
                    MediaPlayer mp;
                    NodeView prev = nodeArr.get(game.getPlayerNodeNum());
                    Game.Status res = game.move(n.getId());
                    switch (res) {
                        case SUCCESSFUL:
                            updateScores();
                            player.setX(nodeArr.get(game.getPlayerNodeNum()).getX());
                            player.setY(nodeArr.get(game.getPlayerNodeNum()).getY());
                            player.setId(game.getPlayerNodeNum());
                            if(!MainActivity.getMutedSounds()) {
                                mp = MediaPlayer.create(context, R.raw.right);
                                mp.setLooping(false);
                                mp.start();
                            }
                            invalidate();
                            break;
                        case VICTORY:
                            updateScores();
                            player.setX(nodeArr.get(game.getPlayerNodeNum()).getX());
                            player.setY(nodeArr.get(game.getPlayerNodeNum()).getY());
                            player.setId(game.getPlayerNodeNum());
                            invalidate();
                            if(!MainActivity.getMutedSounds()) {
                                mp = MediaPlayer.create(context, R.raw.win1);
                                mp.setLooping(false);
                                mp.start();
                            }
                            onWin(game.getMoveCount());
                            break;
                        case FAILED:
                            mp = MediaPlayer.create(context, R.raw.wrong);
                            mp.setLooping(false);
                            mp.start();
                            break;
                        case NO_MORE_MOVES:
                            //TODO IMPROVE
                            break;

                    }
                }
            }
            return super.onTouchEvent(event);
        }

        /**
         * Update scores on board.
         */
        protected void updateScores() {
            tvScore.setText(Integer.toString(game.getValue()));
            tvMoves.setText(Integer.toString(game.getLevel().getStars()[2] - game.getMoveCount()));
        }

        /**
         * Gets node arr.
         *
         * @return the node arr
         */
        public ArrayList<NodeView> getNodeArr() {
            return nodeArr;
        }

        /**
         * Calculates distance between 2 points
         *
         * @param x0 the x 0
         * @param y0 the y 0
         * @param x1 the x 1
         * @param y1 the y 1
         * @return the float
         */
        protected float dist(float x0, float y0, float x1, float y1) {
            return (float) Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
        }
    }
}
