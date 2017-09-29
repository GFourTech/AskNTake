package com.askntake.admin.askntake;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

/**
 * Created by on 02/05/2017.
 */

public class FrameLay_Ex extends FragmentActivity {


    FrameLayout frameLayout;
    //    Context context;
    Spinner spinner;
    View firstView, secondView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framelayout_example);

        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        spinner = (Spinner) findViewById(R.id.spinner_example);

        String[] spinnerArray = getResources().getStringArray(R.array.ex_spin_values);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();

        firstView = findViewById(R.id.layout_one_block);
        secondView = findViewById(R.id.layout_two_block);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner.getSelectedItem().toString().trim().equals("One")) {
                    if (!spinner.getSelectedItem().toString().isEmpty()) {


                        firstView.setVisibility(View.VISIBLE);
                        secondView.setVisibility(View.GONE);

                        /*LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.one, null);
                        frameLayout.addView(myView);*/
                    }
                }

                if (spinner.getSelectedItem().toString().trim().equals("Two")) {
                    if (!spinner.getSelectedItem().toString().isEmpty()) {


                        firstView.setVisibility(View.GONE);
                        secondView.setVisibility(View.VISIBLE);

                       /* LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.two, null);
                        frameLayout.addView(myView);*/
                    }
                }
                if (spinner.getSelectedItem().toString().trim().equals("Three")) {
                    if (!spinner.getSelectedItem().toString().isEmpty()) {

                        firstView.setVisibility(View.GONE);
                        secondView.setVisibility(View.GONE);

                        /*LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout myView = (LinearLayout) inflater.inflate(R.layout.three, null);
                        frameLayout.addView(myView);*/
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
