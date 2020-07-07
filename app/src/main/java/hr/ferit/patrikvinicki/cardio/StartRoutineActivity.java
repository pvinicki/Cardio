package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        inProgress = false;
        wasPaused = false;
        prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        db = new DBhandler(this, "cardio.db", null, 3);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.finished);
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
                        startTimer(millisLeft);
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
            Toast.makeText(getApplicationContext(), "Congratulations! You completed the workout", Toast.LENGTH_LONG ).show();
            for(Workout workout : workouts){
                time += workout.getTime();
            }
            calories = (int)(((time/60)*(5*3.5*80))/200);
            db.updateStats(prefs.getString("username", "null"), calories, time);
            //show some dialog congratulations, pressing ok takes user to main activity

        }
    }

    void startTimer(int time) {
        cTimer = new CountDownTimer(time*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                String currentTime = String.valueOf(millisUntilFinished / 1000) + " s";
                millisLeft = (int) millisUntilFinished;
                tvTimer.setText(currentTime);
            }
            public void onFinish() {
                //play sound so user knows workout finished
                final MediaPlayer mp = MediaPlayer.create(StartRoutineActivity.this, R.raw.finished);
                mp.start();
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
