package hr.ferit.patrikvinicki.cardio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private ArrayList<Workout> workouts;
    private static ViewHolder.ClickListener clickListener;

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

    public static class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView tvWorkoutName;
        public TextView tvWorkoutTime;

        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.tvWorkoutName = (TextView) itemView.findViewById(R.id.tvWorkoutName);
            this.tvWorkoutTime = (TextView) itemView.findViewById(R.id.tvWorkoutTime);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(this.getLayoutPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(this.getLayoutPosition(), view);
            return false;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
            void onItemLongClick(int position, View v);
        }
    }

    public void setOnItemClickListener(ViewHolder.ClickListener clickListener){
        WorkoutAdapter.clickListener = clickListener;
    }

    public void remove(int position, String workoutName){
        Iterator<Workout> iterator = workouts.iterator();
        while(iterator.hasNext()){
            Workout workout = iterator.next();
            if(workout.getName().equals(workoutName)){
                iterator.remove();
            }
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, workouts.size());
    }

}
