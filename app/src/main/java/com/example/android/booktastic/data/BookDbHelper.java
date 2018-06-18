package com.example.android.booktastic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.booktastic.data.BookContract.BookEntry;

/**
 * Database Helper for the Books.
 * <p>
 * This handles the database creation, version managment and interactions.
 */
class BookDbHelper extends SQLiteOpenHelper {

    /**
     * Constant for logging.
     */
    @SuppressWarnings("unused")
    public static final String TAG = BookDbHelper.class.getName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "booktastic.db";

    /**
     * Database version. If you change the database schema, this must be increased.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link BookDbHelper}.
     *
     * @param context of the app
     */
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + BookEntry.COLUMN_PRICE + " INTEGER NOT NULL CHECK (" + BookEntry.COLUMN_PRICE +
                " >= 0), "
                + BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0 CHECK (" + BookEntry
                .COLUMN_QUANTITY + " >= 0), "
                + BookEntry.COLUMN_SUPPLIER_NAME + " TEXT, "
                + BookEntry.COLUMN_SUPPLIER_PHONE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}