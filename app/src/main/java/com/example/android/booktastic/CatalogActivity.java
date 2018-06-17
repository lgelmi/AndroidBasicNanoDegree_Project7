package com.example.android.booktastic;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booktastic.data.BookContract.BookEntry;
import com.example.android.booktastic.data.BookDbHelper;

import java.util.Random;

/**
 * Shows debug data regarding the Book db.
 */
public class CatalogActivity extends AppCompatActivity {

    /**
     * Constant for logging.
     */
    public static final String TAG = CatalogActivity.class.getName();

    /**
     * Database helper instance to provide access to the database
     */
    private BookDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new BookDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateConsole();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the book database.
     */
    private void updateConsole() {
        TextView displayView = findViewById(R.id.console);
        try (Cursor cursor = mDbHelper.queryData()) {
            // Figure out the index of each column
            displayView.setText("The book table contains " + cursor.getCount() + " books.\n\n");

            int idColumn = cursor.getColumnIndex(BookEntry._ID);
            int nameColumn = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumn = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumn = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int id = cursor.getInt(idColumn);
                String name = cursor.getString(nameColumn);
                Float price = (float) cursor.getInt(priceColumn) / 100;
                int quantity = cursor.getInt(quantityColumn);
                // Display the values from each column of the current row in the cursor in the
                // TextView
                displayView.append(("\n" + id + " - " + name + " - " + price + " - " + quantity));
            }
        }
    }

    /**
     * Helper method to insert hardcoded book data into the database. For debugging purposes only.
     */
    private void insertBook() {
        long newRowId = mDbHelper.insertBook("1984", 1984, 1984, "Big Brother", "Unprobable phone");

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method to insert hardcoded book data into the database. For debugging purposes only.
     */
    private void insertRandomBook() {
        Random random = new Random();
        String[] books = {"S", "A song of Ice and Fire", "The Hobbit", "Percy Jackson", "One, " +
                "Noone, One Hundred Thousands", "Dummy Book Name"};

        long newRowId = mDbHelper.insertBook(books[random.nextInt(books.length)], random.nextInt
                (5000), random.nextInt(50));

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/main_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_insert:
                insertBook();
                updateConsole();
                return true;
            case R.id.action_insert_random:
                insertRandomBook();
                updateConsole();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all:
                int deleted = mDbHelper.wipeData();
                Toast.makeText(this, "Books removed: " + deleted, Toast.LENGTH_SHORT).show();
                updateConsole();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
