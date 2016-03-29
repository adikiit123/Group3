package com.example.vishank.xampp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class First extends AppCompatActivity {

    private boolean loggedIn = false;
    Button login_explore;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        instantiate();
    }

    private void instantiate()
    {


        toolbar = (Toolbar) findViewById(R.id.toolbar_first);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        login_explore = (Button) findViewById(R.id.login_explore);
        login_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(First.this, LoginActivity.class));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(First.this, OptionsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
