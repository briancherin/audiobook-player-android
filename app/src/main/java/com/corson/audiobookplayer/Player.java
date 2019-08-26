package com.corson.audiobookplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.corson.audiobookplayer.api.AudioStore;
import com.corson.audiobookplayer.api.Factory;

import java.io.IOException;

public class Player extends AppCompatActivity {


    Button playButton;
    Button seekForward;
    Button seekBackward;
    TextView percentBufferedTextView;
    TextView progressTextView;
    SeekBar seekBar;

    AudioStore audioStore;

    Boolean playing = false;

    MediaPlayer mediaPlayer;
    final int SEEK_INCREMENT_TIME = 10000;  //10 second increment forward / backward

    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        audioStore = (new Factory()).createAudioStore();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                bookId = null;
            } else {
                bookId = extras.getString(MyConstants.BUNDLE_BOOK_ID_EXTRA);
            }
        } else {
            bookId = (String) savedInstanceState.getSerializable(MyConstants.BUNDLE_BOOK_ID_EXTRA);
        }

        String audioUrl = audioStore.getAudioStreamUrl(bookId);

       // String audioUrl = "https://test-audio-bucket-345345.s3.amazonaws.com/Spinning+Silver+(Unabridged).m4b";

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();

        playButton = findViewById(R.id.play_button);
        seekForward = findViewById(R.id.button_seek_forward);
        seekBackward = findViewById(R.id.button_seek_backward);
        percentBufferedTextView = findViewById(R.id.text_view_percent_buffered);
        progressTextView = findViewById(R.id.text_view_progress);
        seekBar = findViewById(R.id.seek_bar);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playing){
                    playButton.setText("Pause");
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        seekBar.setMax(mediaPlayer.getDuration()/1000);
                    }
                    playing = true;
                } else {
                    playButton.setText("Play");
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                    playing = false;
                }
            }
        });

        seekForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    incrementSeekPosition(mediaPlayer, SEEK_INCREMENT_TIME);
                }
            }
        });

        seekBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    incrementSeekPosition(mediaPlayer, -1 * SEEK_INCREMENT_TIME);
                }
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                percentBufferedTextView.setText("Buffered: " + i + "%");
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer.isPlaying() && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Handler progressHandler = new Handler();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    int currentPositionSeconds = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPositionSeconds);
                    progressTextView.setText(getTimestampFromMilli(mediaPlayer.getCurrentPosition()) + " / " + getTimestampFromMilli(mediaPlayer.getDuration()));
                }
                progressHandler.postDelayed(this, 1000);
            }
        });

    }

    void incrementSeekPosition(MediaPlayer mediaPlayer, int changeInMillis) {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int totalDuration = mediaPlayer.getDuration();

        int newPosition = currentPosition + changeInMillis;
        if (newPosition < 0) newPosition = 0;
        if (newPosition > totalDuration) newPosition = totalDuration;

        mediaPlayer.seekTo(newPosition);
    }

    String getTimestampFromMilli(long milli) {
        int timeInSeconds = (int) (milli / 1000);
        int seconds = timeInSeconds % 60;
        int timeInMinutes = timeInSeconds / 60;
        int minutes = timeInMinutes % 60;
        int hours = timeInMinutes / 60;

        String secondsString = (seconds < 10) ? ("0" + seconds) : seconds + "";
        String minutesString = (minutes < 10) ? ("0" + minutes) : minutes + "";
        String hoursString = (hours < 10) ? ("0" + hours) : hours + "";


        return hoursString + ":" + minutesString + ":" + secondsString;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }


}
