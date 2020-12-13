package com.example.tamz2_pop_projekt;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Bundle bundle = getIntent().getExtras();
        int lvl = bundle.getInt("level");


        setContentView(R.layout.activity_game);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.song);

        mediaPlayer.start();

        this.setContentView(new GameSurface(this, lvl));
    }
}