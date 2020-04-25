
/*LEVEL CODE FORMAT:
MAIN CODE:
begin,goal|start,end|node,node,node|edge,edge,edge|3star,2star|param(0-10)
NODE:
val.type.positionClockwise
EDGE:
from.to
 */

package com.example.mathgolfnew;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectLevelActivity extends Activity implements View.OnClickListener {

    protected final int LEVELS_AMOUNT = BuiltInLevels.getLevelAmount();
    protected int bestLevel;
    protected int currLevel;
    protected ImageView ivStart, ivLeft, ivRight, ivBack, ivPlayRandom, ivStars;
    protected SharedPreferences sp;
    protected DBManager dbManager;
    protected TextView tvLevel;
    protected Dialog d;

    /**
     * Gets called when activity is created, initiates db and views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_menu);
        dbManager = new DBManager(this);
        initViews();
        if (currLevel == bestLevel) ivRight.setClickable(false);
        if (currLevel == 1) ivLeft.setClickable(false);
        MainActivity.setCurrScreen(MainActivity.Screen.LEVEL_SELECT);
    }

    /**
     * Initiates views.
     */
    protected void initViews() {
        ivLeft = findViewById(R.id.ivLeft);
        ivRight = findViewById(R.id.ivRight);
        ivBack = findViewById(R.id.ivBackToMainMenu);
        tvLevel = findViewById(R.id.tvLevel);
        ivStars = findViewById(R.id.ivStars);
        ivPlayRandom = findViewById(R.id.ivPlayRandom);
        bestLevel = dbManager.getBestLevel();
        ivStart = findViewById(R.id.btnStartLevel);
        currLevel = Math.min(LEVELS_AMOUNT, bestLevel + 1);
        Pair<Integer, Integer> tmp = dbManager.getLevel(currLevel);
        if (tmp == null) ivStars.setVisibility(View.INVISIBLE); //if there's an error or level incomplete
        else setStarImage(tmp.first, ivStars);
        ivPlayRandom.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        tvLevel.setText(Integer.toString(currLevel));
        tvLevel.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        if(currLevel >= bestLevel) ivRight.setVisibility(View.INVISIBLE); //if the player is in last level, cant go right
        else ivRight.setVisibility(View.VISIBLE);
        if(currLevel == 1) ivLeft.setVisibility(View.INVISIBLE); //if level is first, cant go left
        else ivLeft.setVisibility(View.VISIBLE);
        if(LEVELS_AMOUNT != bestLevel) ivPlayRandom.setVisibility(View.INVISIBLE); //if hasnt finished all levels, cant play random
        else ivPlayRandom.setVisibility(View.VISIBLE);
    }

    /**
     * Click listener
     * @param v the view that was clicked
     */
    @Override
    public void onClick(View v) {
        MediaPlayer mp;
        Intent intent;
        switch(v.getId()) {
            case R.id.btnStartLevel: //start level
                if(currLevel >= 1 && currLevel <= bestLevel + 1) {
                    intent = new Intent(this, GameBoardActivity.class);
                    intent.putExtra("levelCode", BuiltInLevels.getLevel(currLevel)); //match with currLevel
                    intent.putExtra("id", currLevel);
                    startActivity(intent);
                }
                break;
            case R.id.ivRight: //go to next level
                if(currLevel < Math.min(bestLevel + 1, LEVELS_AMOUNT)) {
                    currLevel++;
                    tvLevel.setText(Integer.toString(currLevel));
                    if (currLevel == bestLevel + 1) ivStars.setVisibility(View.INVISIBLE);
                    else setStarImage(dbManager.getLevel(currLevel).first, ivStars);
                    if (currLevel == bestLevel + 1 || (currLevel == LEVELS_AMOUNT)) {
                        ivRight.setVisibility(View.INVISIBLE);
                        ivRight.setClickable(false);
                    }
                    ivLeft.setClickable(true);
                    ivLeft.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ivLeft: //go to prev level
                if(currLevel > 1) {
                    currLevel--;
                    tvLevel.setText(Integer.toString(currLevel));
                    setStarImage(dbManager.getLevel(currLevel).first, ivStars);
                    if (currLevel == 1) {
                        ivLeft.setVisibility(View.INVISIBLE);
                        ivLeft.setClickable(false);
                    }
                    ivRight.setClickable(true);
                    ivRight.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ivBackToMainMenu: //go to main menu
                finish();
                break;
            case R.id.ivPlayRandom: //open random level dialog
                showDialogRandom();
                break;
            case R.id.btnEasy: //send player to easy random level
                intent = new Intent(this, GameBoardActivity.class);
                intent.putExtra("levelCode", CodeLevelConvert.levelToCode(RandomLevelGenerator.createRandomLevel(RandomLevelGenerator.Difficulty.EASY)));
                intent.putExtra("id", -1);
                startActivity(intent);
                d.dismiss();
                break;
            case R.id.btnMedium://send player to medium random level
                intent = new Intent(this, GameBoardActivity.class);
                intent.putExtra("levelCode", CodeLevelConvert.levelToCode(RandomLevelGenerator.createRandomLevel(RandomLevelGenerator.Difficulty.MEDIUM)));
                intent.putExtra("id", -1);
                startActivity(intent);
                d.dismiss();
                break;
            case R.id.btnHard: //send player to hard random level
                intent = new Intent(this, GameBoardActivity.class);
                intent.putExtra("levelCode", CodeLevelConvert.levelToCode(RandomLevelGenerator.createRandomLevel(RandomLevelGenerator.Difficulty.HARD)));
                intent.putExtra("id", -1);
                startActivity(intent);
                d.dismiss();
                break;
            case R.id.backFromRandomDialog:
                d.dismiss();
                break;
        }
    }

    /**
     * Sets star image.
     *
     * @param stars   the stars
     * @param ivStars the image view
     */
    public static void setStarImage(int stars, ImageView ivStars) {
        ivStars.setVisibility(View.VISIBLE);
        switch(stars) {
            case 3:
                ivStars.setImageResource(R.drawable.star3);
                break;
            case 2:
                ivStars.setImageResource(R.drawable.star2);
                break;
            case 1:
                ivStars.setImageResource(R.drawable.star1);
                break;
            default:
                ivStars.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * Show dialog to select random level difficulty.
     */
    protected void showDialogRandom() {
        d = new Dialog(this);
        d.setContentView(R.layout.random_level_dialog);
        d.setCancelable(true);
        Button btnEasy, btnMedium, btnHard, btnExitRandom;
        btnEasy = d.findViewById(R.id.btnEasy);
        btnMedium = d.findViewById(R.id.btnMedium);
        btnHard = d.findViewById(R.id.btnHard);
        btnExitRandom = d.findViewById(R.id.backFromRandomDialog);
        btnEasy.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        btnExitRandom.setOnClickListener(this);
        d.show();
        Window w = d.getWindow();
        w.setLayout((int)(0.75f * (Resources.getSystem().getDisplayMetrics().widthPixels)) , (int)(0.50f * (Resources.getSystem().getDisplayMetrics().heightPixels)));
    }

    /**
     * Called when app is resumed, responsible to resume music
     */
    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.setCurrScreen(MainActivity.Screen.LEVEL_SELECT);
        if (!MainActivity.getInApp()) {
            MainActivity.setInApp(true);
            if (!MainActivity.getMutedMusic())
            MainActivity.getmServ().resumeMusic();
        }
        initViews();
    }

    /**
     * Called when app is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (MainActivity.getCurrScreen() == MainActivity.Screen.LEVEL_SELECT)
            MainActivity.setInApp(false);
        if(MainActivity.getmServ() != null && !MainActivity.getMutedMusic() && !MainActivity.getInApp()) {
            MainActivity.getmServ().pauseMusic();
        }
    }
}
