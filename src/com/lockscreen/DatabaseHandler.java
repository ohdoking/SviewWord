package com.lockscreen;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "wordsManager";
 
    // Contacts table name
    private static final String TABLE_WORDS = "wordPaper";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_WORD = "word";
    private static final String KEY_MEAN = "mean";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WORD + " TEXT,"
                + KEY_MEAN + " TEXT" + ")";
        db.execSQL(CREATE_WORDS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
 
        // Create tables again
        onCreate(db);
    }
    void addContact(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word.get_word()); // Contact Name
        values.put(KEY_MEAN, word.get_mean()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_WORDS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    Word getWord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ID,
                KEY_WORD, KEY_MEAN }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Word word = new Word(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return word;
    }
     
    // Getting All Contacts
    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<Word>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.set_id(Integer.parseInt(cursor.getString(0)));
                word.set_word(cursor.getString(1));
                word.set_mean(cursor.getString(2));
                // Adding contact to list
                wordList.add(word);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return wordList;
    }
 
    // Updating single contact
    public int updateWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word.get_word());
        values.put(KEY_MEAN, word.get_mean());
 
        // updating row
        return db.update(TABLE_WORDS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(word.get_id()) });
    }
 
    // Deleting single contact
    /*public void deleteWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORDS, KEY_ID + " = ?",
                new String[] { String.valueOf(word.get_id()) });
        db.close();
    }*/
    public void deleteWord(String i) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_WORDS, KEY_WORD + " = \""+i+"\"",null);
    	 Log.v("in delete","id: " + i);
    	db.close();
    }
 
 
    // Getting contacts Count
    public int getWordsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
}