package com.gaya.whoami.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "answers.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_QUESTION_ID + " TEXT PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ANSWER_ID + " INTEGER)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_A =
            " DELETE FROM " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private SQLiteDatabase mMainDB;


    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(SQL_CREATE_ENTRIES);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("", "DB UPGRADE ");

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void save(SQLiteDatabase db,String q,int p)
    {


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_QUESTION_ID, q);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ANSWER_ID, p);


// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                "",
                values);


        Log.d("", "DB RESULT save: " + q  + " , " +  p);


    }

    public void show (SQLiteDatabase db){


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_QUESTION_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ANSWER_ID
              };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_QUESTION_ID + " DESC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        int count = 1;

        c.moveToFirst();

     do{
         String question_id = c.getString(
                 c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_QUESTION_ID)
         );

         int answer_id = c.getInt(
                 c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ANSWER_ID)
         );

         Log.d("", count + ": DB RESULT QUESTION: " + question_id);
         Log.d("", count + ": DB RESULT ANSWER: " + answer_id);

        count++;
     }

        while(c.moveToNext());










    }

    public void deleteDataBase(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES_A);
    }

}