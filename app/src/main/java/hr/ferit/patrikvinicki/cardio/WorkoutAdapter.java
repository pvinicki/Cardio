package hr.ferit.patrikvinicki.cardio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private ArrayList<Workout> workouts;

    public WorkoutAdapter(ArrayList<Workout> workouts){
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public WorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View workoutView = inflater.inflate(R.layout.workout, parent, false);
        ViewHolder workoutViewHolder = new ViewHolder(workoutView);
        return workoutViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ViewHolder holder, int position) {
        String time;
        Workout workout = this.workouts.get(position);
        holder.tvWorkoutName.setText(workout.getName());

        time = workout.getTime() + " s";
        holder.tvWorkoutTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return this.workouts.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView tvWorkoutName;
        public TextView tvWorkoutTime;

        public ViewHolder(View itemView){
            super(itemView);
            this.tvWorkoutName = (TextView) itemView.findViewById(R.id.tvWorkoutName);
            this.tvWorkoutTime = (TextView) itemView.findViewById(R.id.tvWorkoutTime);
        }
    }
}
