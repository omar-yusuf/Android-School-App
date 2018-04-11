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

import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_1;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_2;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_3;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_4;
import static com.example.omar.C196.DatabaseHelper.COL_MENTOR_5;
import static com.example.omar.C196.DatabaseHelper.MENTOR_TABLE;

public class MentorScreen extends AppCompatActivity {

    DatabaseHelper db;
    LinearLayout ll;
    TextView tv_NoMentors;
    Button btn_AddMentor;
    String mentorId, mentorName, mentorAddress, mentorPhone, mentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_screen);

        tv_NoMentors = findViewById(R.id.tv_NoMentors);
        btn_AddMentor = findViewById(R.id.btn_AddMentor);

        db =  new DatabaseHelper(this, null, null, 1);
        Cursor cursor = db.getTableData(MENTOR_TABLE);
        if(cursor.getCount() == 0) {
            tv_NoMentors.setText("No mentors entered");
        }
            while(cursor.moveToNext()){
                mentorId = cursor.getString(cursor.getColumnIndex(COL_MENTOR_1));
                mentorName = cursor.getString(cursor.getColumnIndex(COL_MENTOR_2));
                mentorAddress = cursor.getString(cursor.getColumnIndex(COL_MENTOR_3));
                mentorPhone = cursor.getString(cursor.getColumnIndex(COL_MENTOR_4));
                mentorEmail = cursor.getString(cursor.getColumnIndex(COL_MENTOR_5));

                ll = findViewById(R.id.mentor_layout);
                TextView newMentor = new TextView(this);
                String s = mentorName;
                final SpannableString ss1=  new SpannableString(s);
                ss1.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0); // set size
                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);// set color
                newMentor.setText(ss1);
                newMentor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MentorScreen.this, MentorDetailScreen.class);
                        intent.putExtra("mentorId", mentorId);
                        intent.putExtra("mentorName", mentorName);
                        intent.putExtra("mentorAddress", mentorAddress);
                        intent.putExtra("mentorPhone", mentorPhone);
                        intent.putExtra("mentorEmail", mentorEmail);
                        startActivity(intent);
                    }
                });
                ll.addView(newMentor);
            }
        }

        public void AddMentor(View view){
            Intent intent = new Intent(MentorScreen.this, NewMentorScreen.class);
            startActivity(intent);
        }
    }
