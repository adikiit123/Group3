package com.example.vishank.xampp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AttendanceActivity extends AppCompatActivity {

    private SimpleLocation location;




     AlertDialog dialog = null;

    EditText passcode_edit;
    String myJSON;

    TextView error;
    TextView attempts;
    TextView location_text;

    JSONArray passcode_values = null;
    GPSTracker gps;

    LinearLayout layout_things;

    private static final String TAG_RESULTS = "result";


    private static final String TAG_PASS = "passcode";

    private static final String TAG_RESULTS1 = "result";


    private static final String TAG_PASS1 = "StudentID";

    TextView message;
    ImageView img;
    CardView cardView;

    Toolbar toolbar ;

    String studentId = "student";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        message = (TextView) findViewById(R.id.message);
        img = (ImageView) findViewById(R.id.img);
        cardView = (CardView) findViewById(R.id.carview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
/*        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");*/
        getSupportActionBar().setTitle("Mark Attendance");



        setFinishOnTouchOutside(false);

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }


        showDialog();
    }

    private void showDialog()
    {


        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.custom_attendance, null, false);

        passcode_edit = (EditText) view.findViewById(R.id.passcode_edit);
        location_text = (TextView) view.findViewById(R.id.location_text);
        error = (TextView) view.findViewById(R.id.error);
        attempts = (TextView) view.findViewById(R.id.attempts);
        layout_things = (LinearLayout) view.findViewById(R.id.layout_things);


        AlertDialog.Builder builder;  builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("proceed",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });

       dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            String passcode = "vishank";
            int attempt = 3;

            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog)
                    dialog.dismiss();
                else {
                    getPasscode();
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }

            public void getPasscode() {
                class GetDataJSON extends AsyncTask<String, Void, String> {
                    HttpPost httppost;

                    @Override
                    protected String doInBackground(String... params) {


                        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());


                        httppost = new HttpPost("http://10.0.128.165/Api/passcode.php?table=passcode&database=Share");
                        Log.d("subha", "Toast work");


                        //("http://localhost/casco/conect.php"

                        // Depends on your web service
                        httppost.setHeader("Content-type", "application/json");

                        InputStream inputStream = null;
                        String result = null;
                        try {
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();

                            inputStream = entity.getContent();
                            // json is UTF-8 by default
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();

                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            result = sb.toString();
                        } catch (Exception e) {

                            Toast.makeText(AttendanceActivity.this, "Not working", Toast.LENGTH_SHORT).show();

                            // Oops
                        } finally {
                            try {
                                if (inputStream != null) inputStream.close();
                            } catch (Exception squish) {
                                squish.printStackTrace();
                            }
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        myJSON = result;

                        //  Toast.makeText(AttendanceActivity.this, "" + myJSON, Toast.LENGTH_SHORT).show();

                        getValue();

                    }
                }
                GetDataJSON g = new GetDataJSON();
                g.execute();
            }

            protected void getValue() {
                try {


                    JSONObject jsonObj = new JSONObject(myJSON);
                    Log.d("subha", "working man2");
                    passcode_values = jsonObj.getJSONArray(TAG_RESULTS);

                    for (int i = 0; i < passcode_values.length(); i++) {
                        JSONObject c = passcode_values.getJSONObject(i);
                        // String id = c.getString(TAG_ID);
                        passcode = c.getString(TAG_PASS);


                        //Toast.makeText(AttendanceActivity.this, "last" + c.getString(TAG_PASS), Toast.LENGTH_SHORT).show();

                        //Toast.makeText(AttendanceActivity.this, "last 2" + passcode, Toast.LENGTH_SHORT).show();

                        getCoordinates();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            private void getCoordinates() {
                gps = new GPSTracker(AttendanceActivity.this);

                if (gps.canGetLocation()) {
                  /*  double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();*/

                   /* Toast.makeText(
                            getApplicationContext(),
                            "Your Location is -\nLat: " + latitude + "\nLong: "
                                    + longitude, Toast.LENGTH_LONG).show();*/
                } else {
                    gps.showSettingsAlert();
                }

                double latitude = location.getLatitude();
                latitude = Math.round(latitude * 100.0) / 100.0;
                double longitude = location.getLongitude();
                longitude = Math.round(longitude * 100.0) / 100.0;


                if (attempt > 1) {

                    if (!passcode_edit.getText().toString().equals("")) {

                        if (passcode.equals(passcode_edit.getText().toString())) {
                            Toast.makeText(AttendanceActivity.this, "Match", Toast.LENGTH_SHORT).show();

                            mark_attendance();

                            dialog.dismiss();
                            cardView.setVisibility(View.VISIBLE);
                            img.setImageResource(R.mipmap.success);
                        } else {
                            layout_things.setVisibility(View.VISIBLE);
                            slide_down(AttendanceActivity.this, layout_things);
                            --attempt;
                            attempts.setText("Attempts left - " + attempt);
                            location_text.setText("Your Location - 20.19 , 36.09 ");
                        }
                    } else
                        Toast.makeText(AttendanceActivity.this, "Please enter passCode!", Toast.LENGTH_SHORT).show();
                } else {

                    proxy_entry();
                }


            }


            public void slide_down(Context ctx, View v) {

                Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
                if (a != null) {
                    a.reset();
                    if (v != null) {
                        v.clearAnimation();
                        v.startAnimation(a);
                        a.setFillAfter(false);
                    }
                }
            }
        });
    }

    private void mark_attendance()
    {
        class GetDataJSON1 extends AsyncTask<String, Void, String> {
            HttpPost httppost;

            @Override
            protected String doInBackground(String... params) {


                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());


                httppost = new HttpPost("http://10.0.128.165/Api/studentid.php?database=Share&roll=1305166");
                Log.d("subha", "Toast work");


                //("http://localhost/casco/conect.php"

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {

                    Toast.makeText(AttendanceActivity.this, "Not working", Toast.LENGTH_SHORT).show();

                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                        squish.printStackTrace();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;

                //  Toast.makeText(AttendanceActivity.this, "" + myJSON, Toast.LENGTH_SHORT).show();

                getValue1();

            }
        }
        GetDataJSON1 g = new GetDataJSON1();
        g.execute();
    }

    protected void getValue1() {
        try {


            JSONObject jsonObj = new JSONObject(myJSON);
            Log.d("subha", "working man2");
            passcode_values = jsonObj.getJSONArray(TAG_RESULTS1);

            for (int i = 0; i < passcode_values.length(); i++) {
                JSONObject c = passcode_values.getJSONObject(i);
                // String id = c.getString(TAG_ID);
                studentId = c.getString(TAG_PASS1);

                Toast.makeText(AttendanceActivity.this, "" + studentId, Toast.LENGTH_SHORT).show();


                //Toast.makeText(AttendanceActivity.this, "last" + c.getString(TAG_PASS), Toast.LENGTH_SHORT).show();

                //Toast.makeText(AttendanceActivity.this, "last 2" + passcode, Toast.LENGTH_SHORT).show();



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void proxy_entry()
    {

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String roll = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        register(roll);


    }


    private void register(String roll) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProxyReportClass ruc = new ProxyReportClass();
            final String REGISTER_URL = "http://10.0.128.165/Api/proxy_report.php";



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {



                HashMap<String, String> data = new HashMap<String,String>();
                data.put("roll",params[0]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(roll);

        dialog.dismiss();

        message.setText("A proxy report has been generated corresponding to your roll number. Contact your teacher.");
        cardView.setVisibility(View.VISIBLE);
        img.setImageResource(R.mipmap.error);




    }


    @Override
    protected void onResume() {
        super.onResume();

        // make the device update its location
        location.beginUpdates();

        // ...
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        location.endUpdates();

        // ...

        super.onPause();
    }

   /* private void showDialog() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.custom_attendance, null, false);

        passcode_edit = (EditText) view.findViewById(R.id.passcode_edit);
        location_text = (TextView) view.findViewById(R.id.location_text);
        error = (TextView) view.findViewById(R.id.error);
        attempts = (TextView) view.findViewById(R.id.attempts);


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(view);



        //dialog.setTitle("Shake Silento!!");
        //dialog.setIcon(R.mipmap.ic_launcher);

        dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

            String passcode = "vishank";

            @Override
            public void onClick(DialogInterface dialog, int which) {


                getPasscode();
                Toast.makeText(AttendanceActivity.this, "" + passcode_edit.getText().toString() + "  " + passcode, Toast.LENGTH_SHORT).show();
                //initializeShake();


            }

            public void getPasscode() {
                class GetDataJSON extends AsyncTask<String, Void, String> {
                    HttpPost httppost;

                    @Override
                    protected String doInBackground(String... params) {


                        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());


                        httppost = new HttpPost("http://192.168.43.141/Api/passcode.php?table=passcode&database=Share");
                        Log.d("subha", "Toast work");


                        //("http://localhost/casco/conect.php"

                        // Depends on your web service
                        httppost.setHeader("Content-type", "application/json");

                        InputStream inputStream = null;
                        String result = null;
                        try {
                            HttpResponse response = httpclient.execute(httppost);
                            HttpEntity entity = response.getEntity();

                            inputStream = entity.getContent();
                            // json is UTF-8 by default
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();

                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            result = sb.toString();
                        } catch (Exception e) {

                            Toast.makeText(AttendanceActivity.this, "Not working", Toast.LENGTH_SHORT).show();

                            // Oops
                        } finally {
                            try {
                                if (inputStream != null) inputStream.close();
                            } catch (Exception squish) {
                                squish.printStackTrace();
                            }
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        myJSON = result;

                        Toast.makeText(AttendanceActivity.this, "" + myJSON, Toast.LENGTH_SHORT).show();

                        getValue();

                    }
                }
                GetDataJSON g = new GetDataJSON();
                g.execute();
            }

            protected void getValue() {
                try {


                    JSONObject jsonObj = new JSONObject(myJSON);
                    Log.d("subha", "working man2");
                    passcode_values = jsonObj.getJSONArray(TAG_RESULTS);

                    for (int i = 0; i < passcode_values.length(); i++) {
                        JSONObject c = passcode_values.getJSONObject(i);
                        // String id = c.getString(TAG_ID);
                        passcode = c.getString(TAG_PASS);

                        Toast.makeText(AttendanceActivity.this, "last" + c.getString(TAG_PASS), Toast.LENGTH_SHORT).show();

                        Toast.makeText(AttendanceActivity.this, "last 2" + passcode, Toast.LENGTH_SHORT).show();

                        getCoordinates();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            private void getCoordinates() {
                gps = new GPSTracker(AttendanceActivity.this);

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(
                            getApplicationContext(),
                            "Your Location is -\nLat: " + latitude + "\nLong: "
                                    + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }

                double latitude = location.getLatitude();
                latitude = Math.round(latitude * 100.0) / 100.0;
                double longitude = location.getLongitude();
                longitude = Math.round(longitude * 100.0) / 100.0;

                Toast.makeText(AttendanceActivity.this, "" + latitude + " " + longitude, Toast.LENGTH_SHORT).show();

            }


        });
        dialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(AlarmList.this, "Cancel selected", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();

        dialog.setOnItemSelectedListener(null);





    }
*/

}
