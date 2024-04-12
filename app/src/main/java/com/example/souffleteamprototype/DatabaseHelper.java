package com.example.souffleteamprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version updated for schema change
    public static final String DATABASE_NAME = "UserDatabase";
    public static final int DATABASE_VERSION = 6;

    // Table & Column Names
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id"; // User ID
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_POINTS = "points";

    // Table for Reviews
    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "review_id";
    public static final String COLUMN_USER_ID = "user_id";  // Foreign key from users table
    public static final String COLUMN_REVIEW_TEXT = "review_text";
    public static final String COLUMN_RATING = "rating";

    // SQL Query: Creates User Tables updated to include new columns
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CUSTOMER_ID + " TEXT UNIQUE,"
            + COLUMN_FIRST_NAME + " TEXT,"
            + COLUMN_LAST_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_PHONE + " TEXT,"
            + COLUMN_POINTS + " INTEGER DEFAULT 0"  // Default 0
            + ")";

    // SQL for creating Reviews Table
    private static final String CREATE_TABLE_REVIEWS = "CREATE TABLE " + TABLE_REVIEWS + "("
            + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_REVIEW_TEXT + " TEXT,"
            + COLUMN_RATING + " INTEGER,"
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // To create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_REVIEWS); // Create reviews table
        Log.d("DatabaseHelper", "Database and tables created with new schema.");
    }

    // To upgrade / clear the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For actual app releases, consider implementing a migration strategy instead
        Log.d("DatabaseHelper", "Upgrading database, which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        onCreate(db);
    }

    // Retrieves user info using Email
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    // Updates user info
    public int updateUserInfo(String email, String firstName, String lastName) {
        Log.d("DatabaseHelper", "Updating user info for email: " + email);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        if(rowsAffected > 0) {
            Log.d("DatabaseHelper", "Successfully updated user info for email: " + email);
        } else {
            Log.d("DatabaseHelper", "Failed to update user info for email: " + email + ". User may not exist.");
        }

        db.close();
        return rowsAffected;
    }

    // Method to add a review
    public boolean addReview(long userId, String reviewText, int rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_REVIEW_TEXT, reviewText);
        values.put(COLUMN_RATING, rating);

        long result = db.insert(TABLE_REVIEWS, null, values);
        db.close();
        return result != -1; // Return true if insertion is successful
    }

    // Method to retrieve reviews for a specific user
    public Cursor getReviewsByUserId(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REVIEWS, new String[] {COLUMN_REVIEW_TEXT, COLUMN_RATING}, COLUMN_USER_ID + "=?", new String[] {String.valueOf(userId)}, null, null, null);
    }
}
