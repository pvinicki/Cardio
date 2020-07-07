package hr.ferit.patrikvinicki.cardio;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Time;

public class Workout implements Parcelable, Serializable {
    private String name;
    private int time;
    private int secs;
    private int mins;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSecs() {
        return secs;
    }

    public void setSecs(int secs) {
        this.secs = secs;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public Workout(String name, int time, int secs, int mins){
        this.name = name;
        this.time = time;
        this.secs = secs;
        this.mins = mins;
    }

    public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public static Creator<Workout> getCREATOR() {
        return CREATOR;
    }

    //implement this
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(time);
        parcel.writeInt(secs);
        parcel.writeInt(mins);
    }

    private Workout(Parcel in){
        this.name = in.readString();
        this.time = in.readInt();
        this.secs = in.readInt();
        this.mins = in.readInt();
    }
}
