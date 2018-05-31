package com.example.android.booktastic.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.booktastic.data.BookContract.BookEntry;

/**
 * Database Helper for the Books.
 * <p>
 * This handles the database creation, version managment and interactions.
 */
public class BookDbHelper extends SQLiteOpenHelper {

    /**
     * Constant for logging.
     */
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

    /**
     * Basic DataBase query.
     * <p>
     * The standard call (no arguments) won't retrieve the supplier data.
     *
     * @return Cursor containing any possible data from the book database.
     */
    public Cursor queryData() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY};

        // Perform a query on the pets table
        return db.query(
                BookEntry.TABLE_NAME,   // The table to query
                columns,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

    }

    /**
     * Helper method to insert a single data row into the database.
     * <p>
     * All book data are expected as input.
     */
    public long insertBook(String name, int price, int quantity, String supplier, String
            supplier_phone) {
        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, name);
        values.put(BookEntry.COLUMN_PRICE, price);
        values.put(BookEntry.COLUMN_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, supplier);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, supplier_phone);

        // Insert a new row in the database, returning the ID of that new row.
        return db.insert(BookEntry.TABLE_NAME, null, values);
    }

    /**
     * Helper method to insert a single data row into the database.
     * <p>
     * Supplier data are set to null.
     */
    public long insertBook(String name, int price, int quantity) {
        return insertBook(name, price, quantity, null, null);
    }

    /**
     * Basic DataBase delete.
     * <p>
     * Wipes the database clean. Handle with care.
     */
    public int wipeData() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = getWritableDatabase();

        // Perform a query on the pets table
        return db.delete(BookEntry.TABLE_NAME, null, null);
    }
}