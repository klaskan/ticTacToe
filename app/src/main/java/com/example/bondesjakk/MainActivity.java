package com.example.bondesjakk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    boolean gameActive = true;
    boolean playButtonPressed = false;
    //Timer



    // Player representation
    // 0 - X
    // 1 - O
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    // put all win positions in a 2D array
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                            {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;

    public void playButtonTapped(View view){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(this.getResources().getColor(R.color.game_green));
        TextView status = findViewById(R.id.status);
        status.setText("X's Turn - Tap to play");
        TextView playerX = (TextView) findViewById(R.id.playerx);
        playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));

        playButtonPressed = true;

    }




    // this function will be called every time a
    // players tap in an empty box of the grid
    public void playerTap(View view) {
        //Press the playbutton to able to play
        if (!playButtonPressed){
            return;
        }

        ImageView img = (ImageView) view;
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
                TextView status = findViewById(R.id.status);
                TextView playerX = (TextView) findViewById(R.id.playerx);
                TextView playerO = (TextView) findViewById(R.id.playero);
                // change the status
                status.setText("O's Turn - Tap to play");
                //playerX.setBackgroundColor(this.getResources().getColor(R.color.white));
                playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border));
                playerO.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));
            } else {
                // set the image of o
                img.setImageResource(R.drawable.circle_24);
                activePlayer = 0;
                TextView status = findViewById(R.id.status);
                TextView playerX = (TextView) findViewById(R.id.playerx);
                TextView playerO = (TextView) findViewById(R.id.playero);
                // change the status
                status.setText("X's Turn - Tap to play");
                //playerO.setBackgroundColor(this.getResources().getColor(R.color.white));
                playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));
                playerO.setBackground(this.getResources().getDrawable(R.drawable.text_border));

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
                    winnerStr = "X has won";
                    TextView status = findViewById(R.id.status);
                    status.setText(winnerStr);
                } else {
                    winnerStr = "O has won";
                    TextView status = findViewById(R.id.status);
                    status.setText(winnerStr);
                }
            }
            // set the status if the match draw
            if (counter == 9 && flag == 0) {
                TextView status = findViewById(R.id.status);
                status.setText("Match Draw");
                return;
            }
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



        //playerX.setBackground(this.getResources().getDrawable(R.drawable.text_border_green));
        //status.setText("X's Turn - Tap to play");
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

    }
}