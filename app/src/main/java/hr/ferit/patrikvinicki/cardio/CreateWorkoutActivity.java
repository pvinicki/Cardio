package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// implement if user quits activity return no data RESULT_CANCELED

public class CreateWorkoutActivity extends AppCompatActivity {
    private EditText edWorkoutName;
    private TextView tvSecs;
    private TextView tvMins;
    private SeekBar  sbSecs;
    private SeekBar  sbMins;
    private FloatingActionButton fabSaveWorkout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        initializeUI();
    }

    public void initializeUI(){
        this.edWorkoutName  = (EditText) findViewById(R.id.edWorkoutName);
        this.sbSecs         = (SeekBar)  findViewById(R.id.seekBarSecs);
        this.sbMins         = (SeekBar)  findViewById(R.id.seekBarMins);
        this.edWorkoutName  = (EditText) findViewById(R.id.edWorkoutName);
        this.tvSecs         = (TextView) findViewById(R.id.tvSeconds);
        this.tvMins         = (TextView) findViewById(R.id.tvMinutes);
        this.fabSaveWorkout = (FloatingActionButton) findViewById(R.id.fabSaveWorkout);

        this.sbSecs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                String secs = progress + " s";
                tvSecs.setText(secs);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.sbMins.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                String mins = progress + " m";
                tvMins.setText(mins);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.fabSaveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edWorkoutName.getText().toString().isEmpty()){
                    edWorkoutName.setError("Workout name empty");

                } else if (sbSecs.getProgress() != 0 || sbMins.getProgress() != 0){
                    String workoutName = edWorkoutName.getText().toString();
                    int time = (sbSecs.getProgress() + ((sbMins.getProgress() * 60) - 60));
                    intent = new Intent();

                    intent.putExtra("workoutName", workoutName);
                    intent.putExtra("time", time);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
