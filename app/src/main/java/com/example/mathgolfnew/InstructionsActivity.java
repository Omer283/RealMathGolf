package com.example.mathgolfnew;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class InstructionsActivity extends Activity implements View.OnClickListener {

    ImageView btnBack;

    /**
     * called when activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_menu);
        btnBack = findViewById(R.id.ivBackFromInstructions);
        btnBack.setOnClickListener(this);
    }

    /**
     * called when a view is clicked
     * @param v the view
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ivBackFromInstructions:
                finish();
                break;
        }
    }

    /**
     * Called when activity is resumed, updates current screen
     */
    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.setCurrScreen(MainActivity.Screen.INSTRUCTIONS);
        if (!MainActivity.getInApp()) {
            MainActivity.setInApp(true);
            if (!MainActivity.getMutedMusic())
                MainActivity.getmServ().resumeMusic();
        }
    }

    /**
     * Called when activity is stopped, sets according screen and shuts down music if needed
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (MainActivity.getCurrScreen() == MainActivity.Screen.INSTRUCTIONS)
            MainActivity.setInApp(false);
        if(MainActivity.getmServ() != null && !MainActivity.getMutedMusic() && !MainActivity.getInApp()) {
            MainActivity.getmServ().pauseMusic();
        }
    }
}
