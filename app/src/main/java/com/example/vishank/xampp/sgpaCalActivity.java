package com.example.vishank.xampp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class sgpaCalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> list;
    String[] tabName;
    String text_theory_1 = "";
    String text_theory_2 = "";
    String text_theory_3 = "";
    String text_theory_4 = "";
    String text_theory_5 = "";
    String text_theory_6 = "";
    String text_lab_1 = "";
    String text_lab_2 = "";
    String text_lab_3 = "";
    String text_lab_4 = "";
    String text_lab_5 = "";
    String text_lab_6 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgpa_cal);
        instantiate();

        ViewAdapter adapter = new ViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setTabsFromPagerAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    void set_text_theory_1(String text_theory_1) {
        this.text_theory_1 = text_theory_1;
        //Receive new text here
    }


    void set_text_theory_2(String text_theory_2) {
        this.text_theory_2 = text_theory_2;
        //Receive new text here
    }

    void set_text_theory_3(String text_theory_3) {
        this.text_theory_3 = text_theory_3;
        //Receive new text here
    }

    void set_text_theory_4(String text_theory_4) {
        this.text_theory_4 = text_theory_4;
        //Receive new text here
    }

    void set_text_theory_5(String text_theory_5) {
        this.text_theory_5 = text_theory_5;
        //Receive new text here
    }

    void set_text_theory_6(String text_theory_6) {
        this.text_theory_6 = text_theory_6;
        //Receive new text here
    }

    void set_text_lab_1(String text_lab_1) {
        this.text_lab_1 = text_lab_1;
        //Receive new text here
    }

    void set_text_lab_2(String text_lab_2) {
        this.text_lab_2 = text_lab_2;
        //Receive new text here
    }

    void set_text_lab_3(String text_lab_3) {
        this.text_lab_3 = text_lab_3;
        //Receive new text here
    }

    void set_text_lab_4(String text_lab_4) {
        this.text_lab_4 = text_lab_4;
        //Receive new text here
    }

    void set_text_lab_5(String text_lab_5) {
        this.text_lab_5 = text_lab_5;
        //Receive new text here
    }

    void set_text_lab_6(String text_lab_6) {
        this.text_lab_6 = text_lab_6;
        //Receive new text here
    }




    private void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_sgpa);
        setSupportActionBar(toolbar);
/*        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");*/
        getSupportActionBar().setTitle("Sgpa Calculator");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = sgpaCalActivity.this;
        viewPager = (ViewPager) findViewById(R.id.view_pager_sgpa);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_sgpa);

        list = new ArrayList<>();
        list.add(new sgpa_theory_fragment());
        list.add(new sgpa_lab_fragment());

        tabName = getResources().getStringArray(R.array.sgpa_cal_tabs);

    }




    class ViewAdapter extends FragmentStatePagerAdapter {
        public ViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sgpa_activity, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sgpa_done:

          /*     sgpa_lab_fragment sgpa_lab_fragment = new sgpa_lab_fragment();
                String val = sgpa_lab_fragment.getValues();
                Toast.makeText(sgpaCalActivity.this, ""+val, Toast.LENGTH_SHORT).show();*/


               // Toast.makeText(sgpaCalActivity.this, "" + text_theory_1 + "" + text_theory_2, Toast.LENGTH_SHORT).show();

                check_for_all_enteries();


                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private void check_for_all_enteries()
    {
        if(text_theory_1.equals("") || text_theory_2.equals("") || text_theory_3.equals("") || text_theory_4.equals("") || text_theory_5.equals("") || text_lab_1.equals("") || text_lab_2.equals("") || text_lab_3.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder = builder.setTitle("oops!!");
            builder.setMessage("You must enter expected grades for all theories as well as lab subjects to proceed.");

            builder.setNeutralButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            // TODO Auto-generated method stub
                            Log.d("VISHANKKKKKKKKK", "onClickkkkkkk");
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            calculate_sgpa();
        }

    }

    private void calculate_sgpa()
    {
        int theory_1 = get_int_from_grades(text_theory_1);
        int theory_2 = get_int_from_grades(text_theory_2);
        int theory_3 = get_int_from_grades(text_theory_3);
        int theory_4 = get_int_from_grades(text_theory_4);
        int theory_5 = get_int_from_grades(text_theory_5);
        int lab_1 = get_int_from_grades(text_lab_1);
        int lab_2 = get_int_from_grades(text_lab_2);
        int lab_3 = get_int_from_grades(text_lab_3);

        int total = 4*(theory_1 + theory_2 + theory_3) + 3*(theory_4 + theory_5) + 2*(lab_1 + lab_2 +lab_3);

        double sgpa = total/24.0;
         sgpa = (double) Math.round(sgpa * 100) / 100;
        show_sgpa(sgpa);

        //Toast.makeText(sgpaCalActivity.this, total +"ss"+ sgpa, Toast.LENGTH_SHORT).show();
    }

    private void show_sgpa(double sgpa)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder = builder.setTitle("Attention!!");
        builder.setMessage("The truth is out.\nYour SGPA for this semester is estimated to be " + sgpa);

        builder.setNeutralButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub
                        Log.d("VISHANKKKKKKKKK", "onClickkkkkkk");
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public int get_int_from_grades(String s)
    {
        int int_to_return = 0;
        switch (s)
        {
            case "O": int_to_return = 10;
                break;

            case "E": int_to_return = 9;
                break;

            case "A": int_to_return = 8;
                break;

            case "B": int_to_return = 7;
                break;

            case "C": int_to_return = 6;
                break;

            case "D": int_to_return = 5;
                break;

            case "F": int_to_return = 2;
                break;

        }
        return int_to_return;

    }

}
