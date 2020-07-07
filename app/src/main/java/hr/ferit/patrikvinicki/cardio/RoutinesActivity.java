package hr.ferit.patrikvinicki.cardio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class RoutinesActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private FloatingActionButton fabAddRoutine;
    private Intent intent;
    private Context context;
    private ArrayList<Routine> routines;
    private ArrayList<Workout> workouts;
    private DBhandler db;
    private RecyclerView rvRoutines;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration mItemDecoration;
    private RoutineAdapter mRoutineAdapter;
    private AlertDialog.Builder builder;
    private Routine editRoutine;
    static final int GET_ROUTINE= 3;
    static final int EDIT_ROUTINE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        db = new DBhandler(this, "cardio.db", null, 3);
        workouts = new ArrayList<>();
        builder = new AlertDialog.Builder(this);
        try {
            routines = db.deserializeObjectFromDB(prefs.getString("username", "null"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        initializeUI();
    }

    public void initializeUI(){
        context = getApplicationContext();
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
                intent = new Intent(RoutinesActivity.this, CreateRoutineActivity.class);
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
                        intent = new Intent(RoutinesActivity.this, StartRoutineActivity.class);
                        intent.putExtra("routineName", startRoutine.getName());
                        intent.putParcelableArrayListExtra("workouts", startRoutine.getWorkouts());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onItemLongClick(final int position, View v) {
                final String routineName = ((TextView) rvRoutines.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tvRoutineName)).getText().toString();
                builder.setTitle("Edit or delete selected routine?");
                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Iterator<Routine> iterator = routines.iterator();
                        while(iterator.hasNext()){
                            Routine routine = iterator.next();
                            if(routine.getName().equals(routineName)){
                                db.deleteObjectFromDB(routine, prefs.getString("username", "null"));
                                iterator.remove();
                            }
                        }

                        mRoutineAdapter.remove(position, routineName);
                    }
                });
                builder.setNegativeButton("edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(RoutinesActivity.this, CreateRoutineActivity.class);
                        for(Routine routine : routines){
                            if(routine.getName().equals(routineName)){
                                editRoutine = routine;
                                intent.putExtra("routineName", routine.getName());
                                intent.putParcelableArrayListExtra("workouts", routine.getWorkouts());

                                startActivityForResult(intent, EDIT_ROUTINE);
                            }
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            //save routine to db and display on recyclerview

            String routineName =  data.getStringExtra("routineName");
            workouts = data.getParcelableArrayListExtra("workouts");
            for (Workout workout : workouts){
                Toast.makeText(getApplicationContext(), "Name is " + routineName + " workout name is" + workout.getName() + " workout secs is " + workout.getSecs(), Toast.LENGTH_LONG ).show();
            }
            Routine routine = new Routine(routineName, workouts);
            db.serializeObjectToDB(routine, prefs.getString("username", "null"));

            routines.add(routine);
            this.mRoutineAdapter = new RoutineAdapter(routines);
            this.rvRoutines.setAdapter(this.mRoutineAdapter);
        } else if (resultCode == 2){
            //save routine to db and display on recyclerview

            String routineName =  data.getStringExtra("routineName");
            workouts = data.getParcelableArrayListExtra("workouts");

            Iterator<Routine> iterator = routines.iterator();
            while(iterator.hasNext()){
                Routine routine = iterator.next();
                if(routine.getName().equals(editRoutine.getName())){
                    routine.setName(routineName);
                    routine.setWorkouts(workouts);

                    db.updateObjectInDB(editRoutine, routine, prefs.getString("username", "null"));
                }
            }

            this.mRoutineAdapter = new RoutineAdapter(routines);
            this.rvRoutines.setAdapter(this.mRoutineAdapter);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }
}

