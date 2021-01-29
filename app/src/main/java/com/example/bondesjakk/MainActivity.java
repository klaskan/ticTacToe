package com.example.bondesjakk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean gameActive = true;
    boolean playButtonPressed = false;

    //Timer variables
    Timer timerX;
    Timer timerO;
    TimerTask timerTask;
    double timeX = 0.0;
    double timeO = 0.0;
    TextView timeScore;
    boolean timerStarted = false;
    TextView textUpdateScore;
    TextView status;
    //Store the sum of times while playing the game.
    List<Double> sumTimerx = new ArrayList<Double>();
    List<Double> sumTimerO = new ArrayList<Double>();


    public void startTimerX() {
        timerX.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeX++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        double sumX = 0;
                        for(Double i : sumTimerx)
                            sumX += i;
                        textUpdateScore.setText(getTimerText(timeX, sumX));
                    }
                });
            }
        }, 0, 1000);
    }

    public void startTimerO() {
        timerO.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeO++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //add the last from the timer collection
                        double sumO = 0;
                        for(Double i : sumTimerO)
                            sumO += i;
                        textUpdateScore.setText(getTimerText(timeO, sumO));
                    }
                });
            }
        }, 0, 1000);
    }



    private String getTimerText(double timeHere, double progress){
        int rounded = (int) Math.round(timeHere + progress);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }


    private String calculateTimeScore(List<Double> timeX, List<Double> timeO){

        //Finds SUM in list of time values.
        double sumX = 0;
        for(Double i : timeX)
            sumX += i;

        double sumO = 0;
        for(Double j : timeO)
            sumO += j;


        int roundedTimeX = (int) Math.round(sumX);
        int roundedTimeO = (int) Math.round(sumO);

        int secondsX = ((roundedTimeX % 86400) % 3600) % 60;
        int minutesX = ((roundedTimeX % 86400) % 3600) / 60;
        int hoursX = ((roundedTimeX % 86400) / 3600);

        int secondsO = ((roundedTimeO % 86400) % 3600) % 60;
        int minutesO = ((roundedTimeO % 86400) % 3600) / 60;
        int hoursO = ((roundedTimeO % 86400) / 3600);

        int outputSeconds = Math.abs(secondsX + secondsO);
        int outputMinutes = Math.abs(minutesX + minutesO);
        int outputHours = Math.abs(hoursX + hoursO);


        return formatTime(outputSeconds, outputMinutes, outputHours);
    }


    private String formatTime(int sec, int min, int hour){
        return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
    }


    // Player representation
    // 0 == X
    // 1 == O
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // Different states on the board
    // 0 == X
    // 1 == O
    // 2 == Null/empty
    // put all win positions in a 2D array
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                            {0, 4, 8}, {2, 4, 6}};
    int counter = 0;

    public void playButtonTapped(View view){
         if (playButtonPressed){
            return;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(this.getResources().getColor(R.color.game_green));
        TextView playerX = (TextView) findViewById(R.id.playerx);
        playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));
        playButtonPressed = true;
        startTimerX();

    }

    // this function will be called every time a
    // players tap in an empty box of the grid
    public void playerTap(View view) {
        //Press the playbutton to able to play

        if (!playButtonPressed){
            return;
        }


        //Image button
        ImageView img = (ImageView) view;
        //tapped Image button id (on gameboard)
        int tappedImage = Integer.parseInt(img.getTag().toString());

        // game reset function will be called
        // if someone wins or the boxes are full
        if (!gameActive) {
            return;
        }

        // if the tapped image is empty
        if (gameState[tappedImage] == 2) {
            // increase the counter
            // after every tap
            counter++;

            // check if its the last box
            if (counter == 9) {
                // reset the game
                gameActive = false;
            }

            // mark this position
            gameState[tappedImage] = activePlayer;

            // this will give a motion
            // effect to the image
            img.setTranslationY(-1000f);

            // change the active player
            // from 0 to 1 or 1 to 0
            if (activePlayer == 0) {
                // set the image of x
                img.setImageResource(R.drawable.x_24);
                activePlayer = 1;
                TextView playerX = (TextView) findViewById(R.id.playerx);
                TextView playerO = (TextView) findViewById(R.id.playero);
                // change the status
                playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border));
                playerO.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));

                //Fixing time
                sumTimerx.add(timeX);
                timerX.cancel();
                timerX = new Timer();
                timeX = 0.0;
                startTimerO();


            } else {
                // set the image of o
                img.setImageResource(R.drawable.circle_24);
                activePlayer = 0;
                TextView playerX = (TextView) findViewById(R.id.playerx);
                TextView playerO = (TextView) findViewById(R.id.playero);
                // change the status
                playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));
                playerO.setBackground(this.getResources().getDrawable(R.drawable.text_border));

                //Fixing time
                sumTimerO.add(timeO);
                timerO.cancel();
                timerO = new Timer();
                timeO = 0.0;
                startTimerX(); //tar starttid x førse steg + start O. så dersom X brukt 5 sec, og O brukte 5 sec starter denne fra 10

            }
            img.animate().translationYBy(1000f).setDuration(0);
        }



        int flag = 0;
        // Check if any player has won
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {
                flag = 1;

                // Somebody has won! - Find out who!
                String winnerStr;

                // game reset function be called
                gameActive = false;
                if (gameState[winPosition[0]] == 0) {
                    winnerStr = getString(R.string.spill_en_vinner);
                    status.setText(winnerStr);
                    timeScore.setText(calculateTimeScore(sumTimerx, sumTimerO));
                    //Reset time
                    timeSetup();
                } else {
                    //
                    winnerStr = getString(R.string.spiller_to_vinner);
                    status.setText(winnerStr);
                    timeScore.setText(calculateTimeScore(sumTimerx, sumTimerO));
                    //reset time
                    timeSetup();
                }

            }

            }
                // set the status if the match draw
                if (counter == 9 && flag == 0) {
                    TextView status = findViewById(R.id.status);
                    status.setText(getString(R.string.uavgjort));
                    timeScore.setText(calculateTimeScore(sumTimerx, sumTimerO));
                    //reset time
                    timeSetup();;
        }

    }

    // reset the game
    public void gameReset(View view) {
        playButtonPressed = false;
        //reset colors
        TextView status = findViewById(R.id.status);
        TextView playerX = (TextView) findViewById(R.id.playerx);
        TextView playerO = (TextView) findViewById(R.id.playero);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        //White background linear layout
        linearLayout.setBackgroundColor(this.getResources().getColor(R.color.white));
        //White background player O text
        playerO.setBackground(this.getResources().getDrawable(R.drawable.text_border));
        //White background player X text
        playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border));
        //No status
        status.setText("");


        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
        counter = 0;

        // remove all the images from the boxes inside the grid
        ((ImageView) findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);

        resetTimer();
        playButtonPressed = false;

        //Setup time for new round
        timeSetup();
    }

    private void timeSetup(){
        sumTimerx.clear();
        sumTimerO.clear();
        timerX.cancel();
        timerO.cancel();
        timerX = new Timer();
        timerO = new Timer();
        timeO = 0.0;
        timeX = 0.0;
        sumTimerx.add(0.0);
        sumTimerO.add(0.0);
        textUpdateScore.setText("00:00:00");
    }

    private void resetTimer(){
        sumTimerx.clear();
        sumTimerO.clear();
        timerO.cancel();
        timerX.cancel();
        timeO = 0.0;
        timeX = 0.0;
        timerO = new Timer();
        timerX = new Timer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        status = findViewById(R.id.status);
        textUpdateScore = (TextView)findViewById(R.id.timerText);
        timerX = new Timer();
        timerO = new Timer();
        timeScore = (TextView)findViewById(R.id.timeScore);

        //need a start time
        sumTimerx.add(0.0);
        sumTimerO.add(0.0);
        textUpdateScore.setText("00:00:00");
    }





}