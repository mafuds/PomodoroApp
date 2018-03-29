package com.example.celio.pomodoro;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Boolean timerIsActive = Boolean.TRUE;
    TextView timeLeft;
    Boolean restTime = false; // Says if it's pomodoro (25:00) or rest time (5:00)
    MediaPlayer mplayer;
    long remainingTime = 0;
    Button pauseButton;

    public void controlPause(View view) {
        timerIsActive = !timerIsActive;

        if (!timerIsActive) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
            setTimer((int) remainingTime);
        }
    }

    public void updateTimer(int secondsLeft) {
        int minutes = (int) secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String secondString = Integer.toString(seconds);

        // Correcting the "one zero" problem
        if (seconds <= 9)
            secondString = "0" + secondString;

        timeLeft.setText(Integer.toString(minutes) + ":" + secondString);
    }

    // Sets the timer according to Pomodoro or Relax based on milliseconds
    public void setTimer(int milliseconds) {
        new CountDownTimer(milliseconds*60 + 100,1000) { // The added 100 milliseconds is just to correct the first second that goes to fast when the timer starts. It's a delay.
            @Override
            public void onTick(long l) {
                if (!timerIsActive) {
                    cancel();
                } else {
                    // To keep the TextView updated
                    updateTimer((int) l/1000); // It takes milliseconds
                    // Keeps the remaining time in case of pause
                    remainingTime = l;
                }
            }

            @Override
            public void onFinish() {
                // Plays the buzzer
                mplayer.start();
                // Prepares for next mode
                restTime = !restTime;
            }
        }.start();
    }

    public void controlTimer(View view) {
        if (!restTime)
            setTimer(25000);
        else
            setTimer(5000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView tomato = (ImageView) findViewById(R.id.tomato);
        timeLeft = (TextView) findViewById(R.id.timeLeft);
        timeLeft.setText("25:00");
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setText("Pause");
        pauseButton.setEnabled(true);
        mplayer = MediaPlayer.create(getApplicationContext(),R.raw.airhorn);
    }
}
