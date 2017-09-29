package com.askntake.admin.askntake;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapDisplyingActivity extends AppCompatActivity implements LocationListener {
    ImageView back_button;
    double latitudeOfMap, longitudeOfMap;
    private GoogleMap googleMap;
    double radiusInMeters;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.map_displaying_screen);
        Toolbar toolbar_map = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latitudeOfMap = getIntent().getDoubleExtra("latitudeOfMap", 0);
        longitudeOfMap = getIntent().getDoubleExtra("longitudeOfMap", 0);


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.google_map)).getMap();
            }


            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitudeOfMap, longitudeOfMap), 16.3f));

            radiusInMeters = 0.2 * 1000;
            int strokeColor = Color.parseColor("#99CCCCCC");
            int shadeColor = Color.parseColor("#99CCCCCC");
            CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitudeOfMap, longitudeOfMap)).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);


            final Circle circle = googleMap.addCircle(new CircleOptions().center(new LatLng(latitudeOfMap, longitudeOfMap))
                    .strokeColor(Color.parseColor("#59008abd")).strokeWidth(2).fillColor(Color.parseColor("#59008abd")).radius(radiusInMeters));

            ValueAnimator vAnimator = new ValueAnimator();
            vAnimator.setIntValues(0, 100);
            vAnimator.setDuration(1000);
            vAnimator.setEvaluator(new IntEvaluator());
            vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    circle.setRadius(animatedFraction * radiusInMeters);
                }
            });
            vAnimator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

}