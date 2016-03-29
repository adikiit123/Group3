package com.example.vishank.xampp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    String myJSON ;


    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
   /* private static final String TAG_NAME = "name";
    private static final String TAG_ADD ="address";*/
   private static final String TAG_SUBJECT_NAME="subject_name";
    private static final String TAG_TIME = "subject_time";
    private static final String TAG_CLASSROOM = "subject_class";
    private static final String TAG_TEACHER ="subject_teacher";


    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);
        intantiate();
        personList = new ArrayList<HashMap<String,String>>();
        getData();
    }

    private void intantiate()
    {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        getSupportActionBar().setTitle(username);

    }


    protected void showList(){
        try {


            JSONObject jsonObj = new JSONObject(myJSON);
            Log.d("subha", "working man2");
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
               // String id = c.getString(TAG_ID);
                String subject_name = c.getString(TAG_SUBJECT_NAME);
                String subject_time = c.getString(TAG_TIME);
                String subject_class = c.getString(TAG_CLASSROOM);
                String subject_teacher = c.getString(TAG_TEACHER);

                HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_SUBJECT_NAME,subject_name);
                persons.put(TAG_TIME,subject_time);
                persons.put(TAG_CLASSROOM,subject_class);
                persons.put(TAG_TEACHER,subject_teacher);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList, R.layout.custom_list_item,
                    new String[]{TAG_SUBJECT_NAME,TAG_TIME,TAG_CLASSROOM,TAG_TEACHER},
                    new int[]{R.id.subject_name, R.id.subject_time, R.id.subject_class, R.id.subject_teacher}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.43.141/Api/fetch_tubu.php?table=Monday");

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
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {

                    Toast.makeText(MainActivity.this, "Not working", Toast.LENGTH_SHORT).show();

                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();
                        }catch(Exception squish){
                        squish.printStackTrace();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;

                showList();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.apply();

                        //Starting login activity
                        Intent intent = new Intent(MainActivity.this, First.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuLogout)
        {
            logout();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}