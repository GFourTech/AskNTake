package com.askntake.admin.askntake;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.io.InputStream;

/**
 * Created by admin on 4/1/2017.
 */

public class LoginDetailsActivity extends AppCompatActivity {

    TextView name_tv,surname_tv,name_id,surname_id;
    //ImageView profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_details);

        name_tv=(TextView)findViewById(R.id.name_tv);
        surname_tv=(TextView)findViewById(R.id.surname_tv);

        name_id=(TextView)findViewById(R.id.name_id);
        surname_id=(TextView)findViewById(R.id.surname_id);
        // profile_pic=(ImageView) findViewById(R.id.imageview);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        name_id.setText(name);
        surname_id.setText(surname);



        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(LoginDetailsActivity.this, SocialMediaRegistrationsActivity.class);
                startActivity(login);
                finish();
            }
        });
        new DownloadImage((ImageView)findViewById(R.id.imageview)).execute(imageUrl);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        LoginManager.getInstance().logOut();
        Intent login = new Intent(LoginDetailsActivity.this, SocialMediaRegistrationsActivity.class);
        startActivity(login);
        finish();
        /*finish();*//*

        Intent intent=new Intent(SecondActivity.this,MainActivity.class);
        startActivity(intent);*/
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
