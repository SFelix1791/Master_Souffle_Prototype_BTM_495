// DatabaseHelper.java
package com.example.souffleteamprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    public static final String DATABASE_NAME = "UserDatabase";
    public static final int DATABASE_VERSION = 11;

    // Table & Column Names
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_POINTS = "points";

    // Table for General Reviews
    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "review_id";
    public static final String COLUMN_USER_ID = "user_id";  // Foreign key from users table
    public static final String COLUMN_REVIEW_TEXT = "review_text";
    public static final String COLUMN_RATING = "rating";

    // New Table for Order Feedback
    public static final String TABLE_ORDER_FEEDBACK = "order_feedback";
    public static final String COLUMN_FEEDBACK_ID = "feedback_id";
    public static final String COLUMN_FEEDBACK_USER_ID = "user_id";
    public static final String COLUMN_FEEDBACK_TEXT = "feedback_text";
    public static final String COLUMN_FEEDBACK_RATING = "rating";

    // SQL for creating tables
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CUSTOMER_ID + " TEXT UNIQUE,"
            + COLUMN_FIRST_NAME + " TEXT,"
            + COLUMN_LAST_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_PHONE + " TEXT,"
            + COLUMN_POINTS + " INTEGER DEFAULT 0"
            + ")";

    private static final String CREATE_TABLE_REVIEWS = "CREATE TABLE " + TABLE_REVIEWS + "("
            + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_REVIEW_TEXT + " TEXT,"
            + COLUMN_RATING + " INTEGER,"
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    private static final String CREATE_TABLE_ORDER_FEEDBACK = "CREATE TABLE " + TABLE_ORDER_FEEDBACK + "("
            + COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FEEDBACK_USER_ID + " INTEGER,"
            + COLUMN_FEEDBACK_TEXT + " TEXT,"
            + COLUMN_FEEDBACK_RATING + " INTEGER,"
            + "FOREIGN KEY (" + COLUMN_FEEDBACK_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_REVIEWS);
        db.execSQL(CREATE_TABLE_ORDER_FEEDBACK);  // Create the order feedback table
        Log.d("DatabaseHelper", "Database and tables created with new schema.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_FEEDBACK);
        onCreate(db);
    }

    public boolean addOrderFeedback(long userId, String feedbackText, int rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FEEDBACK_USER_ID, userId);
        values.put(COLUMN_FEEDBACK_TEXT, feedbackText);
        values.put(COLUMN_FEEDBACK_RATING, rating);

        long result = db.insert(TABLE_ORDER_FEEDBACK, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getFeedbackByUserId(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ORDER_FEEDBACK, new String[]{COLUMN_FEEDBACK_TEXT, COLUMN_FEEDBACK_RATING}, COLUMN_FEEDBACK_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public Cursor getAllOrderFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ORDER_FEEDBACK, new String[]{COLUMN_FEEDBACK_TEXT, COLUMN_FEEDBACK_RATING}, null, null, null, null, null);
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
