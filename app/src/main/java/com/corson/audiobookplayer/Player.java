package com.corson.audiobookplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.corson.audiobookplayer.api.AudioStore;
import com.corson.audiobookplayer.api.AudiobookManager;
import com.corson.audiobookplayer.api.DeviceInformationManager;
import com.corson.audiobookplayer.api.Factory;
import com.corson.audiobookplayer.api.IDeviceInformationManager;

import java.io.IOException;

public class Player extends AppCompatActivity {

    ImageView playButton;
    ImageView seekForward;
    ImageView seekBackward;
    TextView percentBufferedTextView;
    TextView progressTextView;
    SeekBar seekBar;

    AudioStore audioStore;
    AudiobookManager audiobookManager;
    IDeviceInformationManager deviceInformationManager;
    Factory factory;

    Boolean playing = false;
    Boolean mediaPlayerInitialized = false;

    MediaPlayer mediaPlayer;
    final short SEEK_INCREMENT_TIME_MILLIS = 10000;  //10 second increment forward / backward

    String bookId;
    String bookTitle;
    String bookFileExtension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        factory = new Factory(this);
        audioStore = factory.createAudioStore();
        audiobookManager = factory.createAudiobookManager();
        deviceInformationManager = factory.createDeviceInformationManager();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                bookId = null;
                bookTitle = null;
                bookFileExtension = null;
            } else {
                bookId = extras.getString(MyConstants.BUNDLE_BOOK_ID_EXTRA);
                bookTitle = extras.getString(MyConstants.BUNDLE_BOOK_TITLE_EXTRA);
                bookFileExtension = extras.getString(MyConstants.BUNDLE_BOOK_FILE_EXTENSION_EXTRA);
            }
        } else {
            bookId = (String) savedInstanceState.getSerializable(MyConstants.BUNDLE_BOOK_ID_EXTRA);
            bookTitle = (String) savedInstanceState.getSerializable(MyConstants.BUNDLE_BOOK_TITLE_EXTRA);
            bookFileExtension = (String) savedInstanceState.getSerializable(MyConstants.BUNDLE_BOOK_FILE_EXTENSION_EXTRA);
        }

        if (bookTitle != null) {
            getSupportActionBar().setTitle(bookTitle);
        }

        String audioUrl = audioStore.getAudioStreamUrl(bookId, bookFileExtension);
        System.out.println("In Player: audioUrl: " + audioUrl);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                        .build());

        try {
            mediaPlayer.setDataSource(audioUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();

        restoreSavedTimestamp();


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
                    setPlayButton(false);
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        mediaPlayerInitialized = true;
                        seekBar.setMax(mediaPlayer.getDuration()/1000);
                    }
                    playing = true;
                } else {
                    setPlayButton(true);
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        updateCurrentPositionOnline();
                    }
                    playing = false;
                }
            }
        });

        seekForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayerInitialized) {
                    incrementSeekPosition(mediaPlayer, SEEK_INCREMENT_TIME_MILLIS);
                }
            }
        });

        seekBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayerInitialized) {
                    incrementSeekPosition(mediaPlayer, -1 * SEEK_INCREMENT_TIME_MILLIS);
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
                if (mediaPlayerInitialized && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                    progressTextView.setText(getTimestampFromMilli(mediaPlayer.getCurrentPosition()) + " / " + getTimestampFromMilli(mediaPlayer.getDuration()));
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

        //Actions to perform every 1 second
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayerInitialized && mediaPlayer.isPlaying()) {
                    //Update the position of the progress bar to match the media player position
                    int currentPositionSeconds = getCurrentTimestampSeconds();
                    setSeekBarSeconds(currentPositionSeconds);

                    updateTimestampString();    //Update the display of the current position/timestamp

                    //Save the current position to local storage.
                    audiobookManager.updateCurrentPositionOffline(bookId, currentPositionSeconds);
                }
                progressHandler.postDelayed(this, 1000);
            }
        });

        //Actions to perform every 5 seconds
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayerInitialized && mediaPlayer.isPlaying()) {
                    updateCurrentPositionOnline();
                }

                progressHandler.postDelayed(this, 5000);
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
        setSeekBarSeconds(newPosition / 1000);
        updateTimestampString();
    }

    private int getCurrentTimestampSeconds() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }

    private void updateTimestampString() {
        progressTextView.setText(getTimestampFromMilli(mediaPlayer.getCurrentPosition()) + " / " + getTimestampFromMilli(mediaPlayer.getDuration()));
    }

    /**
     * Set the progress of the seekbar based on the specified number of seconds.
     * The location of the seekbar is based on the seekbar's maximum value,
     * which is defined as the total number of seconds in the media player
     * @param seconds
     */
    private void setSeekBarSeconds(int seconds) {
        seekBar.setProgress(seconds);
    }

    /**
     * Sets the image of the play/pause button based on pressToplay
     * @param pressToPlay Boolean value that specifies whether the button should be
     *                    changed to a play button or a pause button. If pressToPlay is
     *                    true, the button will be made into a play button. Else, a pause button.
     */
    private void setPlayButton(boolean pressToPlay) {
        if (pressToPlay) {
            playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
            playButton.setImageResource(R.drawable.ic_pause_black_24dp);
        }
    }

    private void updateCurrentPositionOnline() {
        audiobookManager.updateCurrentPositionOnline(bookId, getCurrentTimestampSeconds());
    }


    /**
     * Set the seek position to the last timestamp listened to,
     * either from the offline-saved timestamp, or from the database timestamp,
     * depending on which is appropriate and more recent.
     * TODO: Prompt user to give them the option to use the online timestamp (if it is different from the offline timestamp)
     */

    private void restoreSavedTimestamp() {

        int timestamp;

        if(deviceInformationManager.deviceIsOffline() || audiobookManager.isDeviceLastUsed(bookId)) {
            timestamp = audiobookManager.getCurrentPositionOffline(bookId);
        } else {
            timestamp = audiobookManager.getCurrentPositionOnline(bookId);
        }

        setSeekBarSeconds(timestamp / 1000);
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
