package hr.ferit.patrikvinicki.cardio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateRoutineActivity extends AppCompatActivity {
    private Routine routine;
    private ArrayList<Workout> workouts;
    private FloatingActionButton fabAddWorkout;
    private Intent intent;
    private EditText edRoutineName;
    private Button btnSaveRoutine;
    private RecyclerView rvWorkouts;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private WorkoutAdapter mWorkoutAdapter;
    private Bundle bundle;
    static final int GET_WORKOUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workouts = new ArrayList<>();
        //routine needs name
        //Routine routine = new Routine();
        setContentView(R.layout.activity_create_routine);
        initializeUI();
    }

    public void initializeUI(){
        this.edRoutineName = (EditText) findViewById(R.id.edRoutineName);
        this.btnSaveRoutine = (Button) findViewById(R.id.btnSaveRoutine);

        Context context = getApplicationContext();
        workouts.add(new Workout("Test", 20));
        this.rvWorkouts = (RecyclerView) findViewById(R.id.rvWorkouts);
        this.mLayoutManager = new LinearLayoutManager(context);
        this.mItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);

        //populate adapter from db (serialized routine objects)
        this.mWorkoutAdapter = new WorkoutAdapter(workouts);

        //this.rvWorkouts.addItemDecoration(this.mItemDecoration);
        this.rvWorkouts.setLayoutManager(this.mLayoutManager);
        this.rvWorkouts.setAdapter(this.mWorkoutAdapter);

        this.fabAddWorkout = (FloatingActionButton) findViewById(R.id.fabAddWorkout);

        this.fabAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(CreateRoutineActivity.this, CreateWorkoutActivity.class);
                startActivityForResult(intent, GET_WORKOUT);
            }
        });


        this.btnSaveRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edRoutineName.getText().toString().isEmpty()){
                    edRoutineName.setError("Routine name empty");
                } else {
                    intent = new Intent();

                    intent.putExtra("routineName",edRoutineName.getText().toString());
                    intent.putParcelableArrayListExtra("workouts", workouts);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK) {
                String name;
                int time;

                name = data.getStringExtra("workoutName");
                time = data.getIntExtra("time", 1);

                workouts.add(new Workout(name, time));
                mWorkoutAdapter = new WorkoutAdapter(workouts);
                rvWorkouts.setAdapter(mWorkoutAdapter);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                //implement something if canceled
            }
        }
    }


