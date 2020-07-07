package hr.ferit.patrikvinicki.cardio;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

public class Routine implements Serializable {
    private String name;
    private ArrayList<Workout> workouts;
    int time;

    public Routine(String name, ArrayList<Workout> workouts){
        this.name = name;
        this.workouts = workouts;
    }

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int calculateTime(){

        for(Workout workout : workouts){
            time += workout.getTime();
        }

        return time;
    }

    public int getWorkoutCount(){
        return workouts.size();
    }
}
