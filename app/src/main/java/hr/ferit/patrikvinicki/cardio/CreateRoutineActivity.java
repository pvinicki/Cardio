package hr.ferit.patrikvinicki.cardio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Iterator;

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
    private Workout editWorkout;
    private AlertDialog.Builder builder;
    private int request;
    private Intent callIntent;
    static final int GET_WORKOUT = 3;
    static final int EDIT_WORKOUT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workouts = new ArrayList<>();
        //routine needs name
        //Routine routine = new Routine();
        setContentView(R.layout.activity_create_routine);
        builder = new AlertDialog.Builder(this);
        callIntent = getIntent();
        request = callIntent.getIntExtra("requestCode", 0);
        initializeUI();
    }

    public void initializeUI(){
        this.edRoutineName = (EditText) findViewById(R.id.edRoutineName);
        this.btnSaveRoutine = (Button) findViewById(R.id.btnSaveRoutine);

        Context context = getApplicationContext();
        this.rvWorkouts = (RecyclerView) findViewById(R.id.rvWorkouts);
        this.mLayoutManager = new LinearLayoutManager(context);
        this.mItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);

        //populate adapter from db (serialized routine objects)
        if(request == 2){
            String routineName = callIntent.getStringExtra("routineName");
            workouts = callIntent.getParcelableArrayListExtra("workouts");
            for (Workout workout : workouts){
                Toast.makeText(getApplicationContext(), "Name is " + routineName + " workout name is" + workout.getName() + " workout secs is " + workout.getSecs() + " workout time is " + workout.getTime(), Toast.LENGTH_LONG ).show();
            }
            this.edRoutineName.setText(routineName);
            this.mWorkoutAdapter = new WorkoutAdapter(workouts);
        } else {
            this.mWorkoutAdapter = new WorkoutAdapter(workouts);
        }

        this.mWorkoutAdapter.setOnItemClickListener(new WorkoutAdapter.ViewHolder.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int secs = 0;
                int mins = 0;
                //get clicked workout
                String workoutName = ((TextView) rvWorkouts.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvWorkoutName)).getText().toString();
                for(Workout workout : workouts){
                    if(workout.getName().equals(workoutName)){
                        secs = workout.getSecs();
                        mins = workout.getMins();
                        editWorkout = workout;
                    }
                }
                intent = new Intent(CreateRoutineActivity.this, CreateWorkoutActivity.class);
                intent.putExtra("workoutName", workoutName);
                intent.putExtra("secs", secs);
                intent.putExtra("mins", mins);

                startActivityForResult(intent, EDIT_WORKOUT);
            }

            @Override
            public void onItemLongClick(final int position, View v) {
                final String workoutName = ((TextView) rvWorkouts.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvWorkoutName)).getText().toString();
                builder.setTitle("Delete selected workout?");
                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Iterator<Workout> iterator = workouts.iterator();
                        while(iterator.hasNext()){
                            Workout workout = iterator.next();
                            if(workout.getName().equals(workoutName)){
                                iterator.remove();
                            }
                        }

                        mWorkoutAdapter.remove(position, workoutName);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

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
                switch(request){
                    case(3):
                        if(edRoutineName.getText().toString().isEmpty()){
                            edRoutineName.setError("Routine name empty");
                        } else {
                            intent = new Intent();
                            intent.putExtra("routineName",edRoutineName.getText().toString());
                            intent.putParcelableArrayListExtra("workouts", workouts);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                    case(2):
                        if(edRoutineName.getText().toString().isEmpty()){
                            edRoutineName.setError("Routine name empty");
                        } else {
                            intent = new Intent();
                            intent.putExtra("routineName",edRoutineName.getText().toString());
                            intent.putParcelableArrayListExtra("workouts", workouts);
                            setResult(2, intent);
                            finish();
                        }
                }
            }
        });
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //refactor later
            if (resultCode == Activity.RESULT_OK) {
                String name;
                int time, secs, mins;

                name = data.getStringExtra("workoutName");
                time = data.getIntExtra("time", 1);
                secs = data.getIntExtra("secs", 0);
                mins = data.getIntExtra("mins", 0);
                Workout workout = new Workout(name, time, secs, mins);
                Toast.makeText(getApplicationContext()," workout secs is " + workout.getSecs(), Toast.LENGTH_LONG ).show();
                workouts.add(workout);
                mWorkoutAdapter = new WorkoutAdapter(workouts);
                rvWorkouts.setAdapter(mWorkoutAdapter);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                //implement something if canceled
            } else if (resultCode == 2){
                String name;
                int time, secs, mins;

                name = data.getStringExtra("workoutName");
                time = data.getIntExtra("time", 1);
                secs = data.getIntExtra("secs", 0);
                mins = data.getIntExtra("mins", 0);
                //remove previous workout
                Iterator<Workout> iterator = workouts.iterator();
                while(iterator.hasNext()){
                    Workout workout = iterator.next();
                    if(workout.getName().equals(editWorkout.getName())){
                        workout.setName(name);
                        workout.setTime(time);
                        workout.setSecs(secs);
                        workout.setMins(mins);
                    }
                }

                //add edited workout
                mWorkoutAdapter = new WorkoutAdapter(workouts);
                rvWorkouts.setAdapter(mWorkoutAdapter);

            }
        }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }
}


