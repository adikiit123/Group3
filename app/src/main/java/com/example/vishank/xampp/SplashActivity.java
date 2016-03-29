package com.example.vishank.xampp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(1200);
                    Intent i = new Intent(SplashActivity.this, First.class);
                    startActivity(i);
                    finish();
                }
                catch (InterruptedException e)
                {

                }
            }// End of run
        }//end of annonymus class
                ;//end of Thread Statement

        t.start();
    }
}
