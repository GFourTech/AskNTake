package com.askntake.admin.askntake;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by admin on 4/13/2017.
 */

public class ReportingServiceRequestTextActivity extends AppCompatActivity {
    TextInputLayout txt_input_layout_otherconcern;
    EditText edt_otherConcern;
    Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_other_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_input_layout_otherconcern=(TextInputLayout)findViewById(R.id.txt_input_layout_otherconcern);
        edt_otherConcern=(EditText)findViewById(R.id.edt_otherConcern);
        btn_send=(Button)findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_otherConcern.getText().toString().length()>0)
                {
                    Intent myIntent=new Intent(ReportingServiceRequestTextActivity.this,ReportThisServiceRequestsScreenActivity.class);
                    myIntent.putExtra("other_text", edt_otherConcern.getText().toString());
                    startActivity(myIntent);
                    finish();

                }
                else
                {
                    txt_input_layout_otherconcern.setError("We need you to fill in all of the fields!");
                }

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
        finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
