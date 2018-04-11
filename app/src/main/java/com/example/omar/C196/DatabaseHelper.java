package com.example.omar.C196;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "C196.db";
    public static final String COURSE_TABLE = "CourseTable";
    public static final String MENTOR_TABLE = "MentorTable";
    public static final String TERM_TABLE = "TermTable";
    public static final String ASSESSMENT_TABLE = "AssessmentTable";
    public static final String COL_COURSE_1 = "id";
    public static final String COL_COURSE_2 = "name";
    public static final String COL_COURSE_3 = "startDate";
    public static final String COL_COURSE_4 = "endDate";
    public static final String COL_COURSE_5 = "mentor";
    public static final String COL_COURSE_6 = "status";
    public static final String COL_COURSE_7 = "TermId";
    public static final String COL_COURSE_8 = "notes";
    public static final String COL_COURSE_9 = "notify";
    public static final String COL_MENTOR_1 = "id";
    public static final String COL_MENTOR_2 = "name";
    public static final String COL_MENTOR_3 = "address";
    public static final String COL_MENTOR_4 = "phone";
    public static final String COL_MENTOR_5 = "email";
    public static final String COL_TERM_1 = "id";
    public static final String COL_TERM_2 = "startDate";
    public static final String COL_TERM_3 = "endDate";
    public static final String COL_TERM_4 = "name";
    public static final String COL_ASSESSMENT_1 = "id";
    public static final String COL_ASSESSMENT_2 = "name";
    public static final String COL_ASSESSMENT_3 = "course";
    public static final String COL_ASSESSMENT_4 = "type";
    public static final String COL_ASSESSMENT_5 = "goaldate";
    public static final String COL_ASSESSMENT_6 = "notify";
    public static final String COL_ASSESSMENT_7 = "duedate";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + " (" +
                COL_COURSE_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COURSE_2 + " VARCHAR, " +
                COL_COURSE_3 + " DATE, " +
                COL_COURSE_4 + " DATE, " +
                COL_COURSE_5 + " VARCHAR," +
                COL_COURSE_6 + " VARCHAR," +
                COL_COURSE_7 + " VARCHAR," +
                COL_COURSE_8 + " VARCHAR," +
                COL_COURSE_9 + " VARCHAR" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MENTOR_TABLE + " (" +
                COL_MENTOR_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MENTOR_2 + " VARCHAR, " +
                COL_MENTOR_3 + " VARCHAR, " +
                COL_MENTOR_4 + " VARCHAR, " +
                COL_MENTOR_5 + " VARCHAR" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TERM_TABLE + " (" +
                COL_TERM_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TERM_2 + " DATE, " +
                COL_TERM_3 + " DATE," +
                COL_TERM_4 + " VARCHAR" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ASSESSMENT_TABLE + " (" +
                COL_ASSESSMENT_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ASSESSMENT_2 + " VARCHAR, " +
                COL_ASSESSMENT_3 + " VARCHAR," +
                COL_ASSESSMENT_4 + " VARCHAR," +
                COL_ASSESSMENT_5 + " VARCHAR," +
                COL_ASSESSMENT_6 + " VARCHAR," +
                COL_ASSESSMENT_7 + " VARCHAR" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MENTOR_TABLE);
        onCreate(db);
    }

    public boolean insertCourse(String name, String startDate, String endDate, String mentor, String status, String term, String notes, String notify){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(COL_COURSE_2, name);
        values.put(COL_COURSE_3, startDate);
        values.put(COL_COURSE_4, endDate);
        values.put(COL_COURSE_5, mentor);
        values.put(COL_COURSE_6, status);
        values.put(COL_COURSE_7, term);
        values.put(COL_COURSE_8, notes);
        values.put(COL_COURSE_9, notify);
        long result = db.insert(COURSE_TABLE, null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean insertMentor(String name,  String address, String phone, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(COL_MENTOR_2, name);
        values.put(COL_MENTOR_3, address);
        values.put(COL_MENTOR_4, phone);
        values.put(COL_MENTOR_5, email);
        long result = db.insert(MENTOR_TABLE, null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean insertTerm(String startDate, String endDate, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(COL_TERM_2, startDate);
        values.put(COL_TERM_3, endDate);
        values.put(COL_TERM_4, name);
        long result = db.insert(TERM_TABLE, null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean insertAssessment(String name, String course, String type, String goalDate, String notify, String dueDate){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(COL_ASSESSMENT_2, name);
        values.put(COL_ASSESSMENT_3, course);
        values.put(COL_ASSESSMENT_4, type);;
        values.put(COL_ASSESSMENT_5, goalDate);
        values.put(COL_ASSESSMENT_6, notify);
        values.put(COL_ASSESSMENT_7, dueDate);
        long result = db.insert(ASSESSMENT_TABLE, null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public void clear(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table);
    }

    public void drop(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + table);
    }

    public void clearAll(){
        clear(COURSE_TABLE);
        clear(MENTOR_TABLE);
        clear(ASSESSMENT_TABLE);
        clear(TERM_TABLE);
    }

    public void dropAll(){
        drop(COURSE_TABLE);
        drop(MENTOR_TABLE);
        drop(ASSESSMENT_TABLE);
        drop(TERM_TABLE);
    }

    public Cursor getTableData(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * " +  " FROM " +  table, null);
        return cursor;
    }


}
