package com.example.android.booktastic;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booktastic.data.BookContract.BookEntry;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    public static final String TAG = BookCursorAdapter.class.getName();

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.name);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);
        ImageButton sellButton = view.findViewById(R.id.sell);
        String quantityString = quantityTextView.getText().toString().trim();

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);

        // Read the pet attributes from the Cursor for the current pet
        String name = cursor.getString(nameColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        float price = cursor.getInt(priceColumnIndex) / 100;

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(name);
        quantityTextView.setText(context.getString(R.string.quantity_format, quantity));
        priceTextView.setText(context.getString(R.string.price_format, price));

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0)
                    saleBook(cursor, context);
                else
                    Toast.makeText(context, "Can't sell book without any!",
                            Toast.LENGTH_SHORT).show();
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(context, EditorActivity.class);
                int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
                int id = cursor.getInt(idColumnIndex);
                // Form the content URI that represents the specific book that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link BookEntry#CONTENT_URI}.
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentBookUri);
                // Launch the {@link EditorActivity} to display the data for the current pet.
                context.startActivity(intent);
            }
        });

    }

    /**
     * Reduce by one the amount of books.
     */
    private void saleBook(Cursor bookCursor, Context context) {
        int idColumnIndex = bookCursor.getColumnIndex(BookEntry._ID);
        int quantityColumnIndex = bookCursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
        int id = bookCursor.getInt(idColumnIndex);
        int quantity = bookCursor.getInt(quantityColumnIndex);
        if (quantity <= 0)
            return;
        // Create a ContentValues object where column names are the keys,
        // and book attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_QUANTITY, quantity - 1);
        Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
        int rowsAffected = context.getContentResolver().update(currentBookUri, values, null, null);
        // Show a toast message depending on whether or not the update was successful.
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(context, context.getString(R.string.editor_update_book_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(context, context.getString(R.string.editor_update_book_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
