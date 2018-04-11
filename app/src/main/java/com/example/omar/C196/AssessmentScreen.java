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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.ASSESSMENT_TABLE;
import static com.example.omar.C196.DatabaseHelper.COL_ASSESSMENT_3;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class AssessmentScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    TextView tv_CourseTitle;
    String courseName;
    Button btn_NewAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessnment_screen);

        tv_CourseTitle = findViewById(R.id.tv_CourseTitle);
        btn_NewAssessment = findViewById(R.id.btn_NewAssessment);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        tv_CourseTitle.setText(courseName + " Assessments");

        db = new DatabaseHelper(this, null, null, 1);
        String sql = "SELECT * " +
                " FROM " + ASSESSMENT_TABLE +
                " WHERE " + COL_ASSESSMENT_3 + " = \"" + courseName + "\"";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            tv_CourseTitle.setText("No assessments for this course");
        } else {
            while (cursor.moveToNext()) {
                ll = findViewById(R.id.assessment_layout);
                TextView newAssessment = new TextView(this);
                String s = cursor.getString(cursor.getColumnIndex("name")) + "  :  " + cursor.getString(cursor.getColumnIndex("type"));
                final SpannableString ss1=  new SpannableString(s);
                ss1.setSpan(new RelativeSizeSpan(1.5f), 0, s.length(), 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);// set color
                newAssessment.setText(ss1);
                newAssessment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AssessmentScreen.this, AssessmentDetailScreen.class);
                        courseName = ss1.toString().substring(0,ss1.toString().indexOf(":"));
                        intent.putExtra("courseName", courseName);
                        startActivity(intent);
                    }
                });
                ll.addView(newAssessment);
            }
        }

        sql = "SELECT * FROM " + COURSE_TABLE + " WHERE " + COL_COURSE_9 + " = \"yes\"";
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

    public void  btn_NewAssessment(View view){
        Intent intent = new Intent(AssessmentScreen.this, NewAssessmentScreen.class);
        intent.putExtra("courseName", courseName);
        startActivity(intent);
    }
}
