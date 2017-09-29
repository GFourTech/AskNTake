package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.askntake.admin.askntake.ItemFullImagesActivity;
import com.askntake.admin.askntake.R;
import com.bumptech.glide.Glide;

import java.util.List;

import AppUtils.AppConstants;

/**
 * Created by admin on 4/6/2017.
 */

public class ViewPagerImagesAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mResources;

    public ViewPagerImagesAdapter(Context mContext, List<String> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.sigle_image_viewpager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);

        String image_url = AppConstants.IMG_BASE_URL + mResources.get(position);
        Glide.with(mContext).load(/*AppConstants.IMG_BASE_URL+*/mResources.get(position)).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(mContext, ItemFullImagesActivity.class);
//                myintent.putExtra("itemImagesArray", itemImages);
//                myintent.putExtra("clickedItemPosition", Integer.parseInt(v.getTag().toString()));
//                myintent.putExtra("sizeOfItemArray", itemImages.length);
//                myintent.putExtra("mImagesDot", mImagesDot);
                mContext.startActivity(myintent);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}