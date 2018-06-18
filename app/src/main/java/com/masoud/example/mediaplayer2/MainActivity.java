package com.masoud.example.mediaplayer2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageButton imgBtn;
    TextView titleTextView;
    MediaPlayer mp;
    SeekBar seekbar;
    SeekBar seekbar2;
    Handler handler;
    Runnable runnable;
    ImageView songImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgBtn = (ImageButton) findViewById(R.id.playBtn);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        songImageView = (ImageView) findViewById(R.id.songImage);
        titleTextView = (TextView) findViewById(R.id.title);

        titleTextView.setText("bana goresin");
        songImageView.setImageResource(R.drawable.photo1);

        mp = MediaPlayer.create(MainActivity.this, R.raw.banagoresin);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeekbar();
            }
        };

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = (String) imgBtn.getTag();
                if (s.equals("pause")) {
                    play();
                } else {
                    pause();
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long now = (long) ((float) seekbar.getProgress() / 100 * mp.getDuration());
                mp.seekTo((int) now);
                handler.postDelayed(runnable, 1000);
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pause();
            }
        });
    }


    public void updateSeekbar() {
        //find current progress position of mediaplayer
        float progress = ((float) mp.getCurrentPosition() / mp.getDuration());
        //set this progress to seekbar
        seekbar.setProgress((int) (progress * 100));
        //run handler again after 1 second
        handler.postDelayed(runnable, 1000);
    }


    public void pause() {
        mp.pause();
        imgBtn.setTag("pause"); // next state should be 'pause'
        imgBtn.setBackgroundResource(R.drawable.play_selector);
        // stop seekbar
        handler.removeCallbacks(runnable);
    }

    public void play() {
        mp.start();
        imgBtn.setTag("play"); // next state should be 'pause'
        imgBtn.setBackgroundResource(R.drawable.pause_selector);
        //start seekbar
        updateSeekbar();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();
        imgBtn.setBackgroundResource(R.drawable.play_selector);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String s = (String) imgBtn.getTag();
        // check if its not first onResume which is invoked
        // note that onResum invoked always after onCreate()-> onStart()->
        // and also after we come back to our application (Activity)
        // if s equals to 'pause' it means we used setTag('pause') before
        // it means that we now should start  the player again.
        if (!s.equals("pause")) {
            mp.start();
            imgBtn.setBackgroundResource(R.drawable.pause_selector);
        }
    }
}
