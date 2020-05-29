package hr.ferit.patrikvinicki.cardio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private ArrayList<Routine> routines;

    public RoutineAdapter(ArrayList<Routine> routines){
        this.routines = routines;
    }

    @NonNull
    @Override
    public RoutineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View routineView = inflater.inflate(R.layout.routine, parent, false);
        ViewHolder routineViewHolder = new ViewHolder(routineView);
        return routineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineAdapter.ViewHolder holder, int position) {
        Routine routine = this.routines.get(position);
        holder.tvRoutineName.setText(routine.getName());

        String routineTime = routine.calculateTime() + " s";
        holder.tvRoutineTime.setText(routineTime);

        String workoutCount = routine.getWorkoutCount() + " workouts";
        holder.tvRoutineWorkoutCount.setText(workoutCount);
    }

    @Override
    public int getItemCount() {
        return this.routines.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView tvRoutineName;
        private TextView tvRoutineTime;
        private TextView tvRoutineWorkoutCount;


        public ViewHolder(View itemView){
            super(itemView);
            this.tvRoutineName         = (TextView) itemView.findViewById(R.id.tvRoutineName);
            this.tvRoutineTime         = (TextView) itemView.findViewById(R.id.tvRoutineTime);
            this.tvRoutineWorkoutCount = (TextView) itemView.findViewById(R.id.tvRoutineWorkoutCount);
        }
    }
}
