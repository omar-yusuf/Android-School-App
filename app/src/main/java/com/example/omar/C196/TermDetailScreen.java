package com.example.omar.C196;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_7;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_1;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.DatabaseHelper.TERM_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class TermDetailScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    TextView tv_NoCourses;
    TextView tv_TermName, tv_Dates, tv_Edit, tv_Delete;
    public String putCourseName;
    String courseName, termName, termId, dates;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_detail_screen);
        db =  new DatabaseHelper(this, null, null, 1);
        Intent intent = getIntent();
        termId = intent.getStringExtra("termId");
        termName = intent.getStringExtra("termName");
        dates = intent.getStringExtra("dates");
        Cursor cursor = db.getWritableDatabase().rawQuery(
                "SELECT *" +
                " FROM " + COURSE_TABLE +
                " WHERE " + COL_COURSE_7 + "= " + termId, null);
        tv_NoCourses = findViewById(R.id.tv_NoCourses);
        tv_TermName = findViewById(R.id.tv_TermName);
        tv_Dates = findViewById(R.id.tv_Dates);
        tv_Dates.setText(dates);
        ll = findViewById(R.id.term_layout);

        tv_TermName.setText(termName);
        tv_Dates.setText(dates);

        if(cursor.getCount() == 0) {
            tv_NoCourses.setText("No courses in this term        ");
        }
          else {
            while(cursor.moveToNext()){
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
                        Intent intent = new Intent(TermDetailScreen.this, CourseDetailScreen.class);
                        putCourseName = ss1.toString().substring(0,ss1.toString().indexOf("\n"));
                        intent.putExtra("courseName", putCourseName);
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
    public void deleteTerm(View view){
        db =  new DatabaseHelper(this, null, null, 1);
        Cursor cursor = db.getWritableDatabase().rawQuery("" +
                "SELECT *" +
                " FROM " + COURSE_TABLE +
                " WHERE " + COL_COURSE_7 + " =" + termId , null);
        if(cursor.moveToFirst()){
            Toast toast = Toast.makeText(this, "Remove all courses before deleting term", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            db.getWritableDatabase().execSQL("DELETE FROM " + TERM_TABLE + " WHERE " + COL_TERM_1 + " =" + termId);
            Intent intent = new Intent(TermDetailScreen.this, TermScreen.class);
            startActivity(intent);
        }
    }

    public void editTerm(View view){
        Intent intent = new Intent(TermDetailScreen.this, TermEditScreen.class);
        intent.putExtra("termId", termId);
        intent.putExtra("termName", termName);
        startActivity(intent);
    }
}

