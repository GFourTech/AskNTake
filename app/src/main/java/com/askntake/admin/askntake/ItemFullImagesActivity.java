package com.askntake.admin.askntake;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import AppUtils.DepthPageTransformer;


public class ItemFullImagesActivity extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    String[] itemsImagesArray;
    int[] mImagesDot;
    ImageView imageViewDot;

    public int defaultScrenWidth = 800, defaultScrenHeit = 1280;
    public int curScrenHeit;
    public int curScrenWidth;


    AppUtils.TouchImageView imageView;


    private static final ScaleType[] scaleTypes = {ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE, ScaleType.FIT_XY, ScaleType.FIT_CENTER};
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        curScrenHeit = displaymetrics.heightPixels;
        curScrenWidth = displaymetrics.widthPixels;
        setContentView(R.layout.activity_item_full_images);
        mPager = (ViewPager) findViewById(R.id.pager);
        // Instantiate a ViewPager and a PagerAdapter.

        imageViewDot = (ImageView) findViewById(R.id.imageViewDot);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rlp.setMargins(0, 0, 0, 50 * curScrenHeit / defaultScrenHeit);
        imageViewDot.setLayoutParams(rlp);
        int sizeOfItemArray = getIntent().getIntExtra("sizeOfItemArray", 0);
        itemsImagesArray = new String[sizeOfItemArray];
        mImagesDot = new int[sizeOfItemArray];
        itemsImagesArray = getIntent().getStringArrayExtra("itemImagesArray");
        mImagesDot = getIntent().getIntArrayExtra("mImagesDot");
        int clickedItemPosition = getIntent().getIntExtra("clickedItemPosition", 0);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ImagePagerAdapter();
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(clickedItemPosition);

        mPager.setPageTransformer(true, new DepthPageTransformer());

        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
               // imageViewDot.setImageResource(mImagesDot[arg0]);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return itemsImagesArray.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ItemFullImagesActivity.this;

            imageView = new AppUtils.TouchImageView(context);


            imageView.setScaleType(ScaleType.FIT_CENTER);


            Picasso.with(getApplicationContext()).load(/*AppUtils.IMG_BASE_URL +*/ itemsImagesArray[position]).placeholder(R.drawable.progress_animation).into(imageView);

			
			/*	Picasso.with(getApplicationContext()).load(AppUtils.IMG_BASE_URL+itemImages[position]).placeholder(R.drawable.progress_animation).into(imageView,new ImageLoadedCallback(progressBar_place_holder) {
                @Override
                public void onSuccess() {
                    if (this.progressBar != null) {
                        this.progressBar.setVisibility(View.GONE);
                     }
                }
          });*/

            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    index = ++index % scaleTypes.length;
                    ScaleType currScaleType = scaleTypes[index];
                    imageView.setScaleType(currScaleType);

                }
            });

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);

        }

    }

}
