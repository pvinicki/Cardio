package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class StartRoutineActivity extends AppCompatActivity {
    private Intent intent;
    private TextView tvRoutineName;
    private TextView tvWorkoutName;
    private TextView tvTimer;
    private ArrayList<Workout> workouts;
    private CountDownTimer cTimer;
    private int millisLeft;
    private FloatingActionButton fabStartPause;
    private boolean inProgress;
    private boolean wasPaused;
    private DBhandler db;
    private int calories;
    private int time;
    SharedPreferences prefs;
    private int counter;
    private MediaPlayer mp;
    private MediaPlayer countdown;
    private MediaPlayer routineFinished;
    private android.app.AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        inProgress = false;
        wasPaused = false;
        prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        builder = new AlertDialog.Builder(this);
        db = new DBhandler(this, "cardio.db", null, 3);
        mp = MediaPlayer.create(this, R.raw.finished);
        countdown = MediaPlayer.create(this, R.raw.countdown);
        routineFinished = MediaPlayer.create(this, R.raw.routinefinished);
        intent = getIntent();
        initializeUI();
    }

    public void initializeUI(){
        this.tvRoutineName = (TextView) findViewById(R.id.tvRoutineName);
        this.tvWorkoutName = (TextView) findViewById(R.id.tvWorkoutName);
        this.tvTimer       = (TextView) findViewById(R.id.tvTimer);
        this.fabStartPause = (FloatingActionButton) findViewById(R.id.fabStartPauseWorkout);

        workouts = intent.getParcelableArrayListExtra("workouts");
        this.tvRoutineName.setText(intent.getStringExtra("routineName"));
        Workout workout = workouts.get(0);
        this.tvWorkoutName.setText(workout.getName());
        String time = workout.getTime() + " s";
        this.tvTimer.setText(time);


        fabStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inProgress){
                    fabStartPause.setImageDrawable(ContextCompat.getDrawable(StartRoutineActivity.this, R.drawable.ic_pause_black_24dp));
                    inProgress = true;
                    if (wasPaused){
                        startTimer(millisLeft/1000);
                    } else{
                        beginWorkout();
                    }

                } else {
                    fabStartPause.setImageDrawable(ContextCompat.getDrawable(StartRoutineActivity.this, R.drawable.ic_play_arrow_black_48dp));
                    wasPaused = true;
                    inProgress = false;
                    cTimer.cancel();
                }

            }
        });
    }

    void beginWorkout(){
        if(counter != workouts.size()) {
            Workout workout = workouts.get(counter);
            String time = workout.getTime() + " s";
            this.tvWorkoutName.setText(workout.getName());
            this.tvTimer.setText(time);
            startTimer(workout.getTime());
        } else {
            for(Workout workout : workouts){
                time += workout.getTime();
            }
            calories = (int)(((time/60)*(5*3.5*80))/200);
            db.updateStats(prefs.getString("username", "null"), calories, time);
            routineFinished.start();

            builder.setTitle("Congratulations!");
            builder.setMessage("You burned " + calories + " calories");
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent = new Intent(StartRoutineActivity.this, MainActivity.class);

                    startActivity(intent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    void startTimer(int time) {
        cTimer = new CountDownTimer(time*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                String currentTime = String.valueOf(millisUntilFinished / 1000) + " s";
                millisLeft = (int) millisUntilFinished;
                if(millisLeft <= 3000){
                    countdown.start();
                }
                tvTimer.setText(currentTime);
            }
            public void onFinish() {
                //play sound so user knows workout finished
                if(counter != workouts.size()){
                    mp.start();
                }
                counter += 1;
                beginWorkout();
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}
