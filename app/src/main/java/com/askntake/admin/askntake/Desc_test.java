package com.askntake.admin.askntake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by on 21/04/2017.
 */

public class Desc_test extends AppCompatActivity {

    TextView desc_textview,desc_text,icon_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_arrow_layout);

        desc_text=(TextView)findViewById(R.id.desc_text);
        icon_view=(TextView)findViewById(R.id.icon_view);
        desc_textview=(TextView)findViewById(R.id.desc_textview);

        icon_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(desc_text.getVisibility()==View.GONE)
                {

                    icon_view.setBackgroundResource(R.drawable.upwardsarrow);

                    desc_text.setVisibility(View.VISIBLE);

//                    desc_text.setAnimation(R.anim.slide_down);

                }
                else
                {
                    icon_view.setBackgroundResource(R.drawable.downarrow);
                    desc_text.setVisibility(View.GONE);
                }


            }
        });

    }
}
