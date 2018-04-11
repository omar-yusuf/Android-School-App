package com.example.omar.C196;

import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.ASSESSMENT_TABLE;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_2;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_5;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_6;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;

public class HomeScreen extends AppCompatActivity {

    DatabaseHelper db;
    public static ArrayList<String> upcomingAlerts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        db =  new DatabaseHelper(this, null, null, 1);
        db.getWritableDatabase();
//        db.insertMentor("Jon Mentorsson", "123 Wisdom Road", "(123) 456-7890", "jonny@jon.com");
//        db.clearAll();
        checkNotifications();
    }

    public void btn_Course(View view){
        Intent intent = new Intent(HomeScreen.this, CourseScreen.class);
        startActivity(intent);
    }

    public void btn_Term(View view){
        Intent intent = new Intent(HomeScreen.this, TermScreen.class);
        startActivity(intent);
    }

    public void btn_Mentor(View view){
        Intent intent = new Intent(HomeScreen.this, MentorScreen.class);
        startActivity(intent);
    }

    public void checkNotifications(){
        String sql = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COL_COURSE_9 + " = \"yes\"";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql,null);
        while(cursor.moveToNext()){
            Calendar startDate = new GregorianCalendar();
            Calendar endDate = new GregorianCalendar();
            startDate.setTime(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("startDate")))));
            endDate.setTime(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("endDate")))));
            if(Calendar.getInstance().getTime().after(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("startDate")))))
                    && Calendar.getInstance().getTime().before(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("endDate")))))){
                upcomingAlerts.add("COURSE DATE: " + cursor.getString(cursor.getColumnIndex(COL_COURSE_2)));
            }
        }

        sql = "SELECT * FROM " + ASSESSMENT_TABLE + " WHERE " + COL_ASSESSMENT_6  + " = \"yes\"";
        cursor = db.getWritableDatabase().rawQuery(sql,null);
        while(cursor.moveToNext()){
            Calendar goalDate = new GregorianCalendar();
            goalDate.setTime(new Date(Date.parse(cursor.getString(cursor.getColumnIndex("goaldate")))));
            if(Calendar.getInstance().getTime().before(new Date(Date.parse(cursor.getString(cursor.getColumnIndex(COL_ASSESSMENT_5)))))){
                upcomingAlerts.add("EXAM: " + cursor.getString(cursor.getColumnIndex(COL_ASSESSMENT_2)));
            }
        }

        for(int i = 0; i < upcomingAlerts.size(); i++) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.icon)
                            .setContentTitle("Upcoming Due Date")
                            .setContentText(upcomingAlerts.get(i));
            NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
            notificationManager.notify(i, mBuilder.build());
        }
    }


}
