package hr.ferit.patrikvinicki.cardio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION    = 2;
    private static final String DATABASE_NAME    = "cardioDB.db";
    public static final String TABLE_NAME        = "Users";
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
        String SQL_CREATE_TABLE  = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + COLUMN_USERNAME + " TEXT," + COLUMN_EMAIL + " TEXT,"+ COLUMN_PASSWORD + "TEXT )";

        db.execSQL(SQL_CREATE_TABLE);
    };

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {};

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

    public boolean addHandler(user user) {

        String query = "SELECT" + COLUMN_USERNAME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = " + user.getUsername();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor == null) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, user.getUsername());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PASSWORD, user.getPassword());

            db.insert(TABLE_NAME, null, values);
            db.close();

            return true;
        }

        return false;
    };

    public boolean authenticate(user user){
        String query = "SELECT " + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = " + user.getUsername();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor == null){
            return false;
        } else {
            while (cursor.moveToNext()) {
                if (user.getPassword().equals(cursor.getString(3))) {
                    return true;
                }
            }
        }

        return false;
    }
}


