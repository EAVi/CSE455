package com.example.captain.schedit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nestor Saavedra on 11/2/2017.
 * using sqlite to store our task information
 */

public class DbHelper extends SQLiteOpenHelper {

//table and column definitions
    private static final String DB_NAME= "SCHEDITDB";
    private static final  int DB_VER = 1;
    public static final String DB_TABLE = "Tasks";
    public static final String DB_COLUMN = "TaskName";
    public static final String DB_COLUMN1 = "FavTask";
    public static final String DB_COLUMN2 = "ImportantTask";
    public static final String DB_COLUMN3 = "EndingSoonTask";

    public DbHelper(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }
//Create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL );", DB_TABLE,DB_COLUMN);
        db.execSQL(query);
    }
//if the table exists delete it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s ", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

//add an item to the database
    public void insertNewTask(String task){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);

        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

    }
//delete from data base
    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN + " =?", new String[]{task});
        db.close();

    }
//get list from the database
    public ArrayList<String> getTaskList(){

        ArrayList< String > taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_COLUMN},null,null,null,null,null);

        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));

        }
        cursor.close();
        db.close();
        return taskList;


    }

}

