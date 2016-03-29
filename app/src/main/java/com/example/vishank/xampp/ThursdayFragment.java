package com.example.vishank.xampp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ThursdayFragment extends Fragment {

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




    public ThursdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tuesday, container, false);

        list = (ListView) view.findViewById(R.id.listView);
        intantiate(view);
        personList = new ArrayList<HashMap<String,String>>();
        getData();

        return view;
    }

    private void intantiate(View view)
    {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

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
                    getActivity(), personList, R.layout.custom_list_item,
                    new String[]{TAG_SUBJECT_NAME,TAG_TIME,TAG_CLASSROOM,TAG_TEACHER},
                    new int[]{R.id.subject_name, R.id.subject_time, R.id.subject_class, R.id.subject_teacher}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            HttpPost httppost;

            @Override
            protected String doInBackground(String... params) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

                int user = Integer.parseInt(username);
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

                if(user > 1305001 && user <= 1305065)
                {
                    httppost = new HttpPost("http://10.0.128.165/Api/fetch_tubu.php?table=Thursday&database=CS1");
                    Log.d("subha" , "Toast work");
                }
                else if(user >=1305127 && user<= 1305186 )
                {
                    httppost = new HttpPost("http://192.168.43.141/Api/fetch_tubu.php?table=Thursday&database=First");
                    Log.d("subha" , "cs3");
                }

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

                    Toast.makeText(getActivity(), "Not working", Toast.LENGTH_SHORT).show();

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








}
