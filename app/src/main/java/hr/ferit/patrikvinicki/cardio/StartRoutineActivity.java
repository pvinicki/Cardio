package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_routine);
        initializeUI();
        inProgress = false;
        wasPaused = false;
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.finished);
        intent = getIntent();
    }

    public void initializeUI(){
        this.tvRoutineName = (TextView) findViewById(R.id.tvRoutineName);
        this.tvWorkoutName = (TextView) findViewById(R.id.tvWorkoutName);
        this.tvTimer       = (TextView) findViewById(R.id.tvTimer);

        workouts = intent.getParcelableArrayListExtra("workouts");
        this.tvRoutineName.setText(intent.getStringExtra("routineName"));

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
                    cTimer.cancel();
                }

            }
        });
    }

    void beginWorkout(){
        for(Workout workout : workouts){
            String time = workout.getTime() + " s";
            this.tvWorkoutName.setText(workout.getName());
            this.tvTimer.setText(time);
            startTimer(workout.getTime());
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
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}
