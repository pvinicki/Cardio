package hr.ferit.patrikvinicki.cardio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private ArrayList<Routine> routines;
    private static ViewHolder.ClickListener clickListener;

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

    public static class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView tvRoutineName;
        private TextView tvRoutineTime;
        private TextView tvRoutineWorkoutCount;


        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.tvRoutineName         = (TextView) itemView.findViewById(R.id.tvRoutineName);
            this.tvRoutineTime         = (TextView) itemView.findViewById(R.id.tvRoutineTime);
            this.tvRoutineWorkoutCount = (TextView) itemView.findViewById(R.id.tvRoutineWorkoutCount);
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
        RoutineAdapter.clickListener = clickListener;
    }

    public void remove(int position, String routineName){
        Iterator<Routine> iterator = routines.iterator();
        while(iterator.hasNext()){
            Routine routine = iterator.next();
            if(routine.getName().equals(routineName)){
                iterator.remove();
            }
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, routines.size());
    }
}
