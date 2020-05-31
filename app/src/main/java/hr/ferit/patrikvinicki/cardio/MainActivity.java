package hr.ferit.patrikvinicki.cardio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private FloatingActionButton fabAddRoutine;
    private Intent intent;
    private ArrayList<Routine> routines;
    private ArrayList<Workout> workouts;
    private RecyclerView rvRoutines;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private RoutineAdapter mRoutineAdapter;
    static final int GET_ROUTINE= 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("session", MODE_PRIVATE);
        workouts = new ArrayList<>();
        routines = new ArrayList<>();
        //change if so user gets taken to login if not logged in
        if(prefs.getInt("loggedin", 0) == 1){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }

        initializeUI();
    }

    public void initializeUI(){
        Context context = getApplicationContext();
        this.rvRoutines = (RecyclerView) findViewById(R.id.rvRoutines);
        this.mLayoutManager = new LinearLayoutManager(context);
        this.mItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);

        //populate adapter from db (serialized routine objects)
        this.mRoutineAdapter = new RoutineAdapter(routines);

        //this.rvRoutines.addItemDecoration(this.mItemDecoration);
        this.rvRoutines.setLayoutManager(this.mLayoutManager);
        this.rvRoutines.setAdapter(this.mRoutineAdapter);

        this.fabAddRoutine = (FloatingActionButton) findViewById(R.id.fabAddRoutine);

        this.fabAddRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CreateRoutineActivity.class);
                startActivityForResult(intent, GET_ROUTINE);
            }
        });

        this.mRoutineAdapter.setOnItemClickListener(new RoutineAdapter.ViewHolder.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                String routineName = ((TextView) rvRoutines.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvRoutineName)).getText().toString();
                Routine startRoutine;
                for(Routine routine : routines){
                    if(routine.getName().equals(routineName)){
                        startRoutine = routine;
                        intent = new Intent(MainActivity.this, StartRoutineActivity.class);
                        intent.putExtra("routineName", startRoutine.getName());
                        intent.putParcelableArrayListExtra("workouts", startRoutine.getWorkouts());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            //save routine to db and display on recyclerview

            String routineName =  data.getStringExtra("routineName");
            workouts = data.getExtras().getParcelableArrayList("workouts");
            Toast.makeText(getApplicationContext(), "Name is " + routineName, Toast.LENGTH_LONG ).show();
            for (Workout workout : workouts){
                Toast.makeText(getApplicationContext(), "Name is " + routineName + " workout name is" + workout.getName(), Toast.LENGTH_LONG ).show();
            }
            routines.add(new Routine(routineName, workouts));
            this.mRoutineAdapter = new RoutineAdapter(routines);
            this.rvRoutines.setAdapter(this.mRoutineAdapter);
        }
    }
}
