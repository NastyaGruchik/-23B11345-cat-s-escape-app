package com.example.ex1_gruchikanastassia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer timer1;
    private final int COLONS = 3;
    private final int ROWS = 4;
    private FloatingActionButton lButton;
    private FloatingActionButton rButton;
    private ShapeableImageView[] hearts;
    private int lives=3;
    protected ShapeableImageView[][] dogs;
    private ShapeableImageView[] cats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        startView();
        rButton.setOnClickListener(v -> goRight());
        lButton.setOnClickListener(v -> goLeft());
    }

    @Override
    protected void onStart() {
        super.onStart();
        timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->falling());
            }
        }, 0,1000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer1 != null)
            timer1.cancel();
        timer1 = null;
    }

    private void falling() {
        for (int i=0;i<COLONS;i++){
            for(int j=ROWS-1;j>=0;j--){
                if (j==ROWS-1 && dogs[ROWS-1][i].getVisibility()==View.VISIBLE){
                    dogs[ROWS-1][i].setVisibility(View.INVISIBLE);
                    checkInfected(i);
                }
                if(dogs[j][i].getVisibility()==View.VISIBLE){
                    dogs[j+1][i].setVisibility(View.VISIBLE);
                    dogs[j][i].setVisibility(View.INVISIBLE);
                }
            }
        }
        int randomNum = (int) (Math.random() * (COLONS ));
        dogs[0][randomNum].setVisibility(View.VISIBLE);
    }
    private void checkInfected(int i) {
        if(cats[i].getVisibility()==View.VISIBLE){
            if(lives!=0){
                Toast toast= Toast.makeText(MainActivity.this,"oh no", Toast.LENGTH_LONG);
                toast.show();
                vibrate();
                lives--;
                hearts[lives].setVisibility(View.INVISIBLE);
            }
        }
    }
    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void goLeft() {
        for (int i=1;i<cats.length;i++){
            if (cats[i].getVisibility()== View.VISIBLE){
                cats[i].setVisibility(View.INVISIBLE);
                cats[i-1].setVisibility(View.VISIBLE);
            }
        }
    }
    private void goRight() {
        if (cats[1].getVisibility()==View.VISIBLE){
            cats[1].setVisibility(View.INVISIBLE);
            cats[2].setVisibility(View.VISIBLE);
        }
        else if (cats[0].getVisibility()==View.VISIBLE) {
            cats[0].setVisibility(View.INVISIBLE);
            cats[1].setVisibility(View.VISIBLE);
        }
    }

    private void startView() {
        for (int i =0; i<COLONS;i++){
            for(int j=0; j<ROWS;j++){
                dogs[j][i].setVisibility(View.INVISIBLE);
            }
        }
        cats[0].setVisibility(View.INVISIBLE);
        cats[1].setVisibility(View.VISIBLE);
        cats[2].setVisibility(View.INVISIBLE);
    }
    private void findViews() {

        lButton = findViewById(R.id.main_FAB_left);
        rButton = findViewById(R.id.main_FAB_right);
        hearts= new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart3),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart1)
        };
        dogs = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_dog00),findViewById(R.id.main_IMG_dog01),findViewById(R.id.main_IMG_dog02)},
                {findViewById(R.id.main_IMG_dog10),findViewById(R.id.main_IMG_dog11),findViewById(R.id.main_IMG_dog12)},
                {findViewById(R.id.main_IMG_dog20),findViewById(R.id.main_IMG_dog21),findViewById(R.id.main_IMG_dog22)},
                {findViewById(R.id.main_IMG_dog30),findViewById(R.id.main_IMG_dog31),findViewById(R.id.main_IMG_dog32)}
        };
        cats = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_cat1),
                findViewById(R.id.main_IMG_cat2),
                findViewById(R.id.main_IMG_cat3)
        };
    }
}
