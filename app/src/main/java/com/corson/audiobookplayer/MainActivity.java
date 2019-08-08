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

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button playButton;
    Button seekForward;
    Button seekBackward;
    TextView percentBufferedTextView;
    TextView progressTextView;
    SeekBar seekBar;

    Boolean playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String audioUrl = "https://test-audio-bucket-345345.s3.amazonaws.com/Spinning+Silver+(Unabridged).m4b";

        final MediaPlayer mediaPlayer = new MediaPlayer();
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
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int forwardPosition = currentPosition + 10000;
                    if (forwardPosition > mediaPlayer.getDuration()) forwardPosition = mediaPlayer.getDuration();
                    mediaPlayer.seekTo(forwardPosition);
                }
            }
        });

        seekBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int backwardPosition = currentPosition - 10000;
                    if (backwardPosition < 0) backwardPosition = 0;
                    mediaPlayer.seekTo(backwardPosition);
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
}
