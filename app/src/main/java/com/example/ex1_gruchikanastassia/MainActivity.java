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
    protected ShapeableImageView[][] viruses;
    private ShapeableImageView[] people;

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
                if (j==ROWS-1 && viruses[ROWS-1][i].getVisibility()==View.VISIBLE){
                    viruses[ROWS-1][i].setVisibility(View.INVISIBLE);
                    checkInfected(i);
                }
                if(viruses[j][i].getVisibility()==View.VISIBLE){
                    viruses[j+1][i].setVisibility(View.VISIBLE);
                    viruses[j][i].setVisibility(View.INVISIBLE);
                }
            }
        }
        int randomNum = (int) (Math.random() * (COLONS ));
        viruses[0][randomNum].setVisibility(View.VISIBLE);
    }
    private void checkInfected(int i) {
        if(people[i].getVisibility()==View.VISIBLE){
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
        for (int i=1;i<people.length;i++){
            if (people[i].getVisibility()== View.VISIBLE){
                people[i].setVisibility(View.INVISIBLE);
                people[i-1].setVisibility(View.VISIBLE);
            }
        }
    }
    private void goRight() {
        if (people[1].getVisibility()==View.VISIBLE){
            people[1].setVisibility(View.INVISIBLE);
            people[2].setVisibility(View.VISIBLE);
        }
        else if (people[0].getVisibility()==View.VISIBLE) {
            people[0].setVisibility(View.INVISIBLE);
            people[1].setVisibility(View.VISIBLE);
        }
    }

    private void startView() {
        for (int i =0; i<COLONS;i++){
            for(int j=0; j<ROWS;j++){
                viruses[j][i].setVisibility(View.INVISIBLE);
            }
        }
        people[0].setVisibility(View.INVISIBLE);
        people[1].setVisibility(View.VISIBLE);
        people[2].setVisibility(View.INVISIBLE);
    }
    private void findViews() {

        lButton = findViewById(R.id.main_FAB_left);
        rButton = findViewById(R.id.main_FAB_right);
        hearts= new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart3),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart1)
        };
        viruses = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_virus00),findViewById(R.id.main_IMG_virus01),findViewById(R.id.main_IMG_virus02)},
                {findViewById(R.id.main_IMG_virus10),findViewById(R.id.main_IMG_virus11),findViewById(R.id.main_IMG_virus12)},
                {findViewById(R.id.main_IMG_virus20),findViewById(R.id.main_IMG_virus21),findViewById(R.id.main_IMG_virus22)},
                {findViewById(R.id.main_IMG_virus30),findViewById(R.id.main_IMG_virus31),findViewById(R.id.main_IMG_virus32)}
        };
        people = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_person1),
                findViewById(R.id.main_IMG_person2),
                findViewById(R.id.main_IMG_person3)
        };
    }
}
