package hr.ferit.patrikvinicki.cardio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {
    private TextView tvWorkoutsDone;
    private TextView tvCaloriesBurned;
    private TextView tvTotalWorkoutTime;
    private DBhandler db;
    private int[] values;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        prefs = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        db = new DBhandler(this, "cardio.db", null, 3);
        values = new int[3];
        initializeUI();
    }

    private void initializeUI(){
        this.tvWorkoutsDone = findViewById(R.id.tvWorkoutsCompletedNum);
        this.tvCaloriesBurned = findViewById(R.id.tvCaloriesBurnedNum);
        this.tvTotalWorkoutTime = findViewById(R.id.tvTotalCardioTimeNum);

        values = db.getUserStats(prefs.getString("username", "null"));

        tvWorkoutsDone.setText(String.valueOf(values[0]));
        tvCaloriesBurned.setText(String.valueOf(values[1]));
        tvTotalWorkoutTime.setText(String.valueOf(values[2]));
    }
}
