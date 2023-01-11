package au.edu.jcu.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String SCOREBOARD_TABLE = "SCOREBOARD";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_PIN = "PIN";
    public static final String COLUMN_REFLEX_SCORE = "REFLEX_SCORE";
    public static final String COLUMN_MATH_SCORE = "MATH_SCORE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "educationalGame.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + SCOREBOARD_TABLE
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PIN + " TEXT, "
                + COLUMN_REFLEX_SCORE + " INTEGER DEFAULT 0, "
                + COLUMN_MATH_SCORE + " INTEGER DEFAULT 0);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean createNewUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues userValues = new ContentValues();
        userValues.put(COLUMN_USERNAME, userModel.getUserName());
        userValues.put(COLUMN_PIN, userModel.getPinNumber());
        long insert = db.insert(SCOREBOARD_TABLE, null, userValues);
        // insert = 1 when success, -1 when fail
        return insert > 0;
    }

    public List<UserModel> getAll() {
        List<UserModel> allUsers = new ArrayList<>();
        // Get all users from database
        String queryString = "SELECT * FROM " + SCOREBOARD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            // loop through all users
            do {
               String username = cursor.getString(1);
               int reflexScore = cursor.getInt(3);
               int mathScore = cursor.getInt(4);
               UserModel user = new UserModel(username, reflexScore, mathScore);
               allUsers.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return allUsers;
    }


    /**
     * Validate username and pinNumber for log in and registration.
     * @param username input username
     * @param pinNumber input pinNumber
     * @return true if username and pinNumber are valid, false if failed.
     */
    public boolean validateUser (String username, String pinNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        String queryString = "SELECT * FROM " + SCOREBOARD_TABLE
                + " WHERE " + COLUMN_USERNAME + " = ? ";
        String[] selectionArgs = new String[]{username};

        Cursor cursor = db.rawQuery(queryString, selectionArgs);

        if(cursor.moveToFirst()){
            String dbPinNumber = cursor.getString(2);
            if (dbPinNumber.equals(pinNumber)){
                cursor.close();
                db.close();
                return true;
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    public UserModel getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + SCOREBOARD_TABLE
                + " WHERE " + COLUMN_USERNAME + " = ? ";
        String[] selectionArgs = new String[]{username};
        Cursor cursor = db.rawQuery(queryString, selectionArgs);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            String pinNumber = cursor.getString(2);
            int reflexScore = cursor.getInt(3);
            int mathScore = cursor.getInt(4);
            cursor.close();
            db.close();
            return new UserModel(id, username, pinNumber, reflexScore, mathScore);
        }
        return null;
    }

    public boolean updateScoreboard (String username, int score, String gameMode){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        String field = gameMode.equals("math") ? COLUMN_MATH_SCORE : COLUMN_REFLEX_SCORE;
        cv.put(field, score);
        int update = db.update(SCOREBOARD_TABLE, cv, COLUMN_USERNAME + " = ? ", new String[]{username});
        db.close();
        return update > 0;
    }

    public boolean checkDuplicateUsername (String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + SCOREBOARD_TABLE
                + " WHERE " + COLUMN_USERNAME + " = ? ";
        String[] selectionArgs = new String[]{username};
        Cursor cursor = db.rawQuery(queryString, selectionArgs);
        if (cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }
}