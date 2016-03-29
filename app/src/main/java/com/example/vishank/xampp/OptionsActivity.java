package com.example.vishank.xampp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button view_timetable;
    Button cal_sgpa;
    Button mark_attnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        instantiate();

        view_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this, FragmentActivity.class));
            }
        });


        cal_sgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsActivity.this, sgpaCalActivity.class));
            }
        });

        mark_attnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(OptionsActivity.this , AttendanceActivity.class));
            }
        });


    }


    private void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_options);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view_timetable = (Button) findViewById(R.id.view_timetable);
        cal_sgpa = (Button) findViewById(R.id.cal_sgpa);
        mark_attnd = (Button) findViewById(R.id.mark_attnd);

    }
}
