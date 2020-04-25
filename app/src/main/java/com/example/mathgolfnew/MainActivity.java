//TODO FINAL LIST
//GOOD LOOKING MUTE ICONS
//MUSIC
//DIALOG SIZE
package com.example.mathgolfnew;
import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    enum Screen{MAIN_ACTIVITY, LEVEL_SELECT, GAME_BOARD, INSTRUCTIONS}

    protected Button btnPlay, btnInstructions, btnMuteMusic, btnMuteSounds;
    protected ImageView ivReset;
    protected static boolean mutedMusic = false;
    protected static boolean mutedSounds = false;
    protected static boolean inApp = true;
    protected static Screen currScreen = Screen.MAIN_ACTIVITY;
    protected boolean mIsBound = false;
    protected static MusicService mServ;
    protected DBManager dbManager;
    protected BroadcastBattery broadcastBattery;
    protected TextView tvBattery;
    protected ServiceConnection Scon =new ServiceConnection(){
        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    /**
     * Gets called when activity is created, stars playing music, initiates views, db, and battery status
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(this);
        initViews();
        doBindService();
        broadcastBattery = new BroadcastBattery();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        currScreen = Screen.MAIN_ACTIVITY;
        inApp = true;
    }

    /**
     * Initiates views and listeners.
     */
    protected void initViews() {
        btnPlay = findViewById(R.id.btnPlay);
        btnInstructions =  findViewById(R.id.btnInstructions);
        btnMuteMusic = findViewById(R.id.btnMuteMusic);
        btnMuteSounds = findViewById(R.id.btnMuteSounds);
        tvBattery = findViewById(R.id.tvBattery);
        ivReset = findViewById(R.id.ivReset);
        btnInstructions.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnMuteMusic.setOnClickListener(this);
        btnMuteSounds.setOnClickListener(this);
        registerForContextMenu(ivReset);
    }

    /**
     * Creates context menu
     * @param menu the menu
     * @param v view
     * @param menuInfo the menu info
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.reset_menu, menu);
    }

    /**
     * Listens to the item the player had selected from the context menu
     * @param item the item
     * @return whether the player clicked an item or not
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        switch(item.getItemId()) {
            case R.id.itemYes:
                dbManager.reset();
                return true;
            case R.id.itemNo:
                return true;
        }
        return false;
    }

    /**
     * Gets called when a view is clicked on
     * @param v the view
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnPlay:
                Intent intent = new Intent(MainActivity.this, com.example.mathgolfnew.SelectLevelActivity.class);
                startActivity(intent);
                break;
            case R.id.btnInstructions:
                intent = new Intent(MainActivity.this, com.example.mathgolfnew.InstructionsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMuteMusic:
                if (mutedMusic) {
                    mutedMusic = false;
                    mServ.resumeMusic();
                    btnMuteMusic.setText("Mute Music");
                }
                else {
                    mutedMusic = true;
                    mServ.pauseMusic();
                    btnMuteMusic.setText("Unmute Music");
                }
                break;
            case R.id.btnMuteSounds:
                if (mutedSounds) {
                    mutedSounds = false;
                    btnMuteSounds.setText("Mute Sounds");
                }
                else {
                    mutedSounds = true;
                    btnMuteSounds.setText("Unmute Music");
                }
        }
    }

    /**
     * Gets called when activity is resumed. Recalibrates battery broadcast and music
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        MainActivity.setCurrScreen(Screen.MAIN_ACTIVITY);
        MainActivity.setInApp(true);
        if (mServ != null && !mutedMusic) {
            mServ.resumeMusic();
        }
    }

    /**
     * Called when activity is paused, pauses battery broadcast
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastBattery);
    }

    /**
     * Called when activity is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (MainActivity.getCurrScreen() == Screen.MAIN_ACTIVITY)
            MainActivity.setInApp(false);
        if(MainActivity.getmServ() != null && !MainActivity.getMutedMusic() && !MainActivity.getInApp()) {
            MainActivity.getmServ().pauseMusic();
        }
    }

    /**
     * Called when activity is destroyed, responsible for stopping music
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);
    }

    /**
     * Do bind service.
     */
    public void doBindService() {
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /**
     * Do unbind service.
     */
    public void doUnbindService() {
        if(mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    /**
     * Sets whether user is in the app or not.
     *
     * @param inApp the status
     */
    public static void setInApp(boolean inApp) {
        MainActivity.inApp = inApp;
    }

    /**
     * Sets curr screen.
     *
     * @param currScreen the curr screen
     */
    public static void setCurrScreen(Screen currScreen) {
        MainActivity.currScreen = currScreen;
    }

    /**
     * Gets curr screen.
     *
     * @return the curr screen
     */
    public static Screen getCurrScreen() {
        return currScreen;
    }

    /**
     * Gets whether user is in app or not.
     *
     * @return the boolean
     */
    public static boolean getInApp() {return inApp;}

    /**
     * Gets music service.
     *
     * @return the service
     */
    public static MusicService getmServ() {
        return mServ;
    }

    /**
     * Gets whether music is muted or not.
     *
     * @return the boolean
     */
    public static boolean getMutedMusic() {
        return mutedMusic;
    }

    /**
     * Gets whether sounds is muted or not.
     *
     * @return the boolean
     */
    public static boolean getMutedSounds() {
        return mutedSounds;
    }

    /**
     * The type Broadcast battery.
     */
    protected class BroadcastBattery extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battery = intent.getIntExtra("level", 0);
            tvBattery.setText(battery + "%");
        }
    }
}
