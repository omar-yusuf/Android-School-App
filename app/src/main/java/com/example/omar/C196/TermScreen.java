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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.omar.C196.DatabaseHelper.COL_COURSE_2;
import static com.example.omar.C196.DatabaseHelper.COL_COURSE_9;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_1;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_2;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_3;
import static com.example.omar.C196.DatabaseHelper.COL_TERM_4;
import static com.example.omar.C196.DatabaseHelper.COURSE_TABLE;
import static com.example.omar.C196.HomeScreen.upcomingAlerts;

public class TermScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    TextView tv_NoTerms;
    Button btn_AddTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_screen);
        db =  new DatabaseHelper(this, null, null, 1);
        Cursor cursor = db.getTableData(DatabaseHelper.TERM_TABLE);
        tv_NoTerms = findViewById(R.id.tv_NoTerms);
        btn_AddTerm = findViewById(R.id.btn_AddTerm);
        if(cursor.getCount() == 0) {
            tv_NoTerms.setText("No terms entered");
        }
            while(cursor.moveToNext()){
                ll = findViewById(R.id.term_layout);
                final String termId = cursor.getString(cursor.getColumnIndex(COL_TERM_1));
                String startDate = cursor.getString(cursor.getColumnIndex(COL_TERM_2));
                String endDate = cursor.getString(cursor.getColumnIndex(COL_TERM_3));
                final String termName = cursor.getString(cursor.getColumnIndex(COL_TERM_4));
                TextView newTerm = new TextView(this);
                final String dates = startDate + " - " + endDate;
                String s = termName + "\n" + dates;
                final SpannableString ss1=  new SpannableString(s);
                ss1.setSpan(new RelativeSizeSpan(2f), 0,s.indexOf("\n"), 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.indexOf("\n"), 0);// set color
                newTerm.setText(ss1);
                newTerm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(TermScreen.this, TermDetailScreen.class);
                        intent.putExtra("termId", termId);
                        intent.putExtra("dates", dates);
                        intent.putExtra("termName", termName);
                        startActivity(intent);
                    }
                });
                ll.addView(newTerm);
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

        public void btn_AddTerm(View view){
            Intent intent = new Intent(TermScreen.this, NewTermScreen.class);
            startActivity(intent);
        }
    }
