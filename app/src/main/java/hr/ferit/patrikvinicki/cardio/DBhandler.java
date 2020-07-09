package hr.ferit.patrikvinicki.cardio;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION    = 3;
    private static final String DATABASE_NAME    = "cardio.db";
    public static final String TABLE_NAME        = "USERS";
    public static final String COLUMN_ID         = "userID";
    public static final String COLUMN_USERNAME   = "Username";
    public static final String COLUMN_EMAIL      = "Email";
    public static final String COLUMN_PASSWORD   = "Password";
    private ArrayList<user> users = new ArrayList<user>();

    public DBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory , version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        System.out.println("ON CREATE DB EXECUTING!!");
        String SQL_CREATE_TABLE  = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + COLUMN_USERNAME + " TEXT," + COLUMN_EMAIL + " TEXT,"+ COLUMN_PASSWORD + " TEXT )";
        db.execSQL(SQL_CREATE_TABLE);

        String SQL_CREATE_OBJECT_TABLE = "CREATE TABLE ROUTINES (routineID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , user TEXT, routineName TEXT, routine BLOB)";
        db.execSQL(SQL_CREATE_OBJECT_TABLE);

        String SQL_CREATE_STATS_TABLE = "CREATE TABLE STATS (statID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, user TEXT, numWorkouts INTEGER, calories INTEGER, timeWorkouts INTEGER)";
        db.execSQL(SQL_CREATE_STATS_TABLE);
    };

    public void updateStats(String user, int calories, int time){
        String query = "SELECT numWorkouts, calories, timeWorkouts FROM STATS WHERE user = " + "'" + user + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                ContentValues values = new ContentValues();
                values.put("numWorkouts", cursor.getInt(0) + 1);
                values.put("calories", cursor.getInt(1) + calories);
                values.put("timeWorkouts", cursor.getInt(2) + time);
                db.update("STATS", values, "user = " + "'" + user + "'", null);
                db.close();
            }
        } else {
            ContentValues values = new ContentValues();
            values.put("user", user);
            values.put("numWorkouts", 1);
            values.put("calories", calories);
            values.put("timeWorkouts", time);
            db.insert("STATS", null, values);
            db.close();
        }
    }

    public int[] getUserStats(String user){
        int[] values = new int[3];
        String query = "SELECT numWorkouts, calories, timeWorkouts FROM STATS WHERE user = " + "'" + user + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()){
            values[0] = cursor.getInt(0);
            values[1] = cursor.getInt(1);
            values[2] = cursor.getInt(2);
        }
        return values;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) { return;};

    public ArrayList<user> loadHandler() {
        String query = "SELECT * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            user user = new user();
            user.setUsername(cursor.getString(1));
            user.setEmail(cursor.getString(2));

            users.add(user);
        }

        return users;
    };

    public boolean addHandler(user usr) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = " + "'" + usr.getUsername() + "'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, usr.getUsername());
            values.put(COLUMN_EMAIL, usr.getEmail());
            values.put(COLUMN_PASSWORD, usr.getPassword());

            db.insert(TABLE_NAME, null, values);
            db.close();

            return true;
        }

        return false;
    };

    public boolean authenticate(user usr){
        String query = "SELECT " + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = " + "'" + usr.getUsername() + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor == null){
            return false;
        } else {
            while (cursor.moveToNext()) {
                if (usr.getPassword().equals(cursor.getString(1))) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean serializeObjectToDB(Routine routine, String user){
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] buffer = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(routine);
            oos.flush();
            oos.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("routineName", routine.getName());
        values.put("routine", buffer);

        db.insert("ROUTINES", null, values);
        db.close();

        return true;
    }

    public boolean deleteObjectFromDB(Routine routine, String user){
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] buffer = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(routine);
            oos.flush();
            oos.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String SQL_DELETE_QUERY = "DELETE FROM ROUTINES WHERE user = " + "'" + user + "'" + " AND routineName = " + "'" + routine.getName() + "'";
        db.execSQL(SQL_DELETE_QUERY);
        db.close();

        return true;
    }

    public boolean updateObjectInDB(Routine oldRoutine, Routine newRoutine, String user){
        int id;
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] newRout = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(newRoutine);
            oos.flush();
            oos.close();
            bos.close();
            newRout = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put("user", user);
        values.put("routineName", newRoutine.getName());
        values.put("routine", newRout);

        String SQL_SELECT_QUERY = "SELECT routineID FROM ROUTINES WHERE user = " + "'" + user + "'" + " AND routineName = " + "'" + oldRoutine.getName() + "'";
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                id = cursor.getInt(0);
                db.update("ROUTINES", values, "routineID = " + id, null);
            }
        }

        return true;
    }

    public ArrayList<Routine> deserializeObjectFromDB(String user) throws IOException, ClassNotFoundException {
        byte[] buffer;
        ArrayList<Routine> routines = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT routine FROM ROUTINES WHERE user = " + "'" + user + "'";
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            buffer = cursor.getBlob(0);
            ObjectInputStream objectIn = null;
            if(buffer != null){
                objectIn = new ObjectInputStream(new ByteArrayInputStream(buffer));
            }

            Object object = objectIn.readObject();
            Routine routine = (Routine) object;
            routines.add(routine);
        }

        return routines;
    }
}


