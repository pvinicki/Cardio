package hr.ferit.patrikvinicki.cardio;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

public class Workout implements Parcelable {
    private String name;
    private int time;

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

    public Workout(String name, int time){
        this.name = name;
        this.time = time;
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
    }

    private Workout(Parcel in){
        this.name = in.readString();
        this.time = in.readInt();
    }
}
