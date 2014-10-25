package com.gaya.whoami.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.gaya.whoami.questions.Answer;


public class AnswersDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "answers.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AnswersContract.AnswerEntry.TABLE_NAME + " (" +
                    AnswersContract.AnswerEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    //AnswersContract.AnswerEntry.COLUMN_NAME_PLAYER_ID + " TEXT ," +
                    AnswersContract.AnswerEntry.COLUMN_NAME_QUESTION_ID + " TEXT UNIQUE ON CONFLICT REPLACE," +
                    AnswersContract.AnswerEntry.COLUMN_NAME_ANSWER_ID + " TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AnswersContract.AnswerEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_A =
            " DELETE FROM " + AnswersContract.AnswerEntry.TABLE_NAME;

    private SQLiteDatabase mMainDB;


    public AnswersDbHelper(Context context) {
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

    public void save(String question ,String answer)
    {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AnswersContract.AnswerEntry.COLUMN_NAME_QUESTION_ID, question);
        values.put(AnswersContract.AnswerEntry.COLUMN_NAME_ANSWER_ID, answer);


// Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                AnswersContract.AnswerEntry.TABLE_NAME,
                "",
                values);


        Log.d("", "DB RESULT save: " + question  + " , " +  answer);


    }
    static final String[] projection = {
            AnswersContract.AnswerEntry.COLUMN_NAME_QUESTION_ID,
            AnswersContract.AnswerEntry.COLUMN_NAME_ANSWER_ID
    };

    public String query(String  question)
    {
        Cursor c = getReadableDatabase().query(AnswersContract.AnswerEntry.TABLE_NAME,projection,AnswersContract.AnswerEntry.COLUMN_NAME_QUESTION_ID +"=?",
                new String[]{question},null,null,null);
        try {
            if (c.moveToFirst())
                return c.getString(c.getColumnIndex(AnswersContract.AnswerEntry.COLUMN_NAME_ANSWER_ID));
        }finally {
            c.close();
        }
        return null;
    }
/*
    public void show (SQLiteDatabase db){


// Define a projection that specifies which columns from the database
// you will actually use after this query.


// How you want the results sorted in the resulting Cursor
        String sortOrder =
                AnswersContract.AnswerEntry.COLUMN_NAME_QUESTION_ID + " DESC";

        Cursor c = db.query(
                AnswersContract.AnswerEntry.TABLE_NAME,  // The table to query
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
                 c.getColumnIndexOrThrow(AnswersContract.AnswerEntry.COLUMN_NAME_QUESTION_ID)
         );

         int answer_id = c.getInt(
                 c.getColumnIndexOrThrow(AnswersContract.AnswerEntry.COLUMN_NAME_ANSWER_ID)
         );

         Log.d("", count + ": DB RESULT QUESTION: " + question_id);
         Log.d("", count + ": DB RESULT ANSWER: " + answer_id);

        count++;
     }

        while(c.moveToNext());










    }*/

    void deleteDataBase(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES_A);
    }

}