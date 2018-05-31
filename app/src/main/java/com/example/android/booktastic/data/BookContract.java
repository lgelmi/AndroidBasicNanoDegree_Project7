package com.example.android.booktastic.data;

import android.provider.BaseColumns;

/**
 * Book contract class for database interactions.
 */
public class BookContract {

    /**
     * Constant for logging.
     */
    public static final String TAG = BookContract.class.getName();


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {
    }

    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single book.
     */
    public static final class BookEntry implements BaseColumns {

        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Book Name.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "name";

        /**
         * Book Price.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Book Quantity.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Book supplier name.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "s_name";

        /**
         * Book supplier phone number.
         * <p>
         * A Text permits international numbers and extensions.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE = "s_phone";
    }

}

