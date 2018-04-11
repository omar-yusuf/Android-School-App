package com.example.omar.C196;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class CourseScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    TextView tv_NoCourses;
    String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_screen);
        db =  new DatabaseHelper(this, null, null, 1);
        Cursor cursor = db.getTableData(DatabaseHelper.COURSE_TABLE);
        tv_NoCourses = findViewById(R.id.tv_NoCourses);
        if(cursor.getCount() == 0) {
            tv_NoCourses.setText("No courses entered");
        } else {
            while(cursor.moveToNext()){
                ll = findViewById(R.id.course_layout);
                TextView newCourse = new TextView(this);
                courseName = cursor.getString(cursor.getColumnIndex("name"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                String dates = startDate + " - " + endDate;
                String s = courseName + "\n" + dates;
                final SpannableString ss1=  new SpannableString(s);
                ss1.setSpan(new RelativeSizeSpan(2f), 0,s.indexOf("\n"), 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.indexOf("\n"), 0);// set color
                newCourse.setText(ss1);
                newCourse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CourseScreen.this, CourseDetailScreen.class);
                        courseName = ss1.toString().substring(0,ss1.toString().indexOf("\n"));
                        intent.putExtra("courseName", courseName);
                        startActivity(intent);
                    }
                });
                ll.addView(newCourse);
            }
        }

        String sql = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COL_COURSE_9 + " = \"yes\"";
        cursor = db.getWritableDatabase().rawQuery(sql,null);
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
    }

    public void btn_AddCourse(View view){
        Intent intent = new Intent(CourseScreen.this, NewCourseScreen.class);
        startActivity(intent);
    }
}
