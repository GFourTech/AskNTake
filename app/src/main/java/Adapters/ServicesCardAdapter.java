package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.ServiceDescriptionActivity_Services;
import com.askntake.admin.askntake.SocialMediaRegistrationsActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import Pojo.ItemServicePojo;

/**
 * Created by admin on 3/28/2017.
 */

public class ServicesCardAdapter extends RecyclerView.Adapter<ServicesCardAdapter.MyViewHolder> {
    private List<ItemServicePojo> servicesList;
    Context mContext;
    String distance;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView service_name, provider_name, location, views, favorities, days_old, miles, review_count;
        ImageView image_profile_card;
        Context context;
        List<ItemServicePojo> products_list;
        CardView card_view;
        RatingBar ratingbar;


        public MyViewHolder(View view, Context context, List<ItemServicePojo> products_list) {
            super(view);
            this.context = context;
            this.products_list = products_list;
            service_name = (TextView) view.findViewById(R.id.txt_service_name);
            provider_name = (TextView) view.findViewById(R.id.txt_provider_name);
            location = (TextView) view.findViewById(R.id.txt_location_name);
            views = (TextView) view.findViewById(R.id.txt_views_count);
            favorities = (TextView) view.findViewById(R.id.txt_favorites_count);
            days_old = (TextView) view.findViewById(R.id.txt_day_value);
            miles = (TextView) view.findViewById(R.id.txt_mile_value);
            review_count = (TextView) view.findViewById(R.id.ratings_count);
            ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
            image_profile_card = (ImageView) view.findViewById(R.id.image_profile_card);
            card_view = (CardView) view.findViewById(R.id.card_view);
            //image_profile_card.setOnClickListener(this);
            card_view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {


            String serviceTag = v.getTag().toString();

            String[] tagsArray = serviceTag.split(",");
            String service_id = "";
            try {
                service_id = tagsArray[0];
            } catch (Exception e) {
                service_id = "";
            }
            String userid = "";
            try {
                userid = tagsArray[1];
            } catch (Exception e) {
                userid = "";
            }
            String distance = "";
            try {
                distance = tagsArray[2];
            } catch (Exception e) {
                distance = "";
            }

            String distancetype = "";
            try {
                distancetype = tagsArray[3];
            } catch (Exception e) {
                distancetype = "";
            }
            Log.i("service_id", service_id);
            Log.i("userid", userid);
            Log.i("distance", distance);
            Log.i("distancetype", distancetype);


            SharedPreferences login_preferenes = context.getSharedPreferences(DataKeyValues.USER_DATA_PREF, context.MODE_PRIVATE);


            if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {
                Intent descriptionIntent = new Intent(context, ServiceDescriptionActivity_Services.class);
                descriptionIntent.putExtra("service_id", service_id);


                if (login_preferenes.getString(DataKeyValues.OWNER_ID, null) != null) {
                    if (userid.equalsIgnoreCase(login_preferenes.getString(DataKeyValues.OWNER_ID, null))) {
                        descriptionIntent.putExtra("myownproduct", true);
                        descriptionIntent.putExtra("user_id", userid);
                        descriptionIntent.putExtra("distance", distance);
                        descriptionIntent.putExtra("distance_type", distancetype);
                    } else {
                        descriptionIntent.putExtra("myownproduct", false);
                        descriptionIntent.putExtra("user_id", userid);
                        descriptionIntent.putExtra("distance", distance);
                        descriptionIntent.putExtra("distance_type", distancetype);
                    }
                } else {
                    descriptionIntent.putExtra("myownproduct", false);
                    descriptionIntent.putExtra("user_id", userid);
                    descriptionIntent.putExtra("distance", distance);
                    descriptionIntent.putExtra("distance_type", distancetype);

                }
                context.startActivity(descriptionIntent);
            } else {
               /* descriptionIntent.putExtra("myownproduct", false);
                descriptionIntent.putExtra("user_id", products_list.get(getAdapterPosition()).getService_owner_id());
                descriptionIntent.putExtra("distance", distance);*/
                Intent account_intent = new Intent(mContext, SocialMediaRegistrationsActivity.class);
                mContext.startActivity(account_intent);

            }

        }
    }

    public ServicesCardAdapter(Context mContext, List<ItemServicePojo> servicesList) {
        this.mContext = mContext;
        this.servicesList = servicesList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_services_card, parent, false);

        return new MyViewHolder(itemView, mContext, servicesList);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemServicePojo data = servicesList.get(position);
        holder.service_name.setText(data.getService_name());
        holder.provider_name.setText(data.getProvider_name());
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(300 * NavigationDrawerActivity.curScrenWidth / NavigationDrawerActivity.defaultScrenWidth, 290 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit);
        holder.image_profile_card.setLayoutParams(llp);
        holder.location.setText(data.getService_location());
        holder.views.setText(data.getService_views_count());
        holder.favorities.setText(data.getService_favorites());
       /* llp = new LinearLayout.LayoutParams(100 * NavigationDrawerActivity.curScrenWidth / NavigationDrawerActivity.defaultScrenWidth, 50 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit);
        holder.ratingbar.setLayoutParams(llp);*/
        holder.ratingbar.setRating(Float.parseFloat(data.getRating()));
        holder.review_count.setText(data.getReviewsCount());
        holder.days_old.setText(data.getDays_old());
        holder.miles.setText(data.getService_distance() + " " + data.getService_distance_type());
        Glide.with(mContext).load(AppConstants.IMG_BASE_URL + data.getService_image()).into(holder.image_profile_card);

        String service_id = data.getService_id();
        String userid = data.getService_owner_id();
        String distance = data.getService_distance();
        String distancetype = data.getService_distance_type();

        holder.card_view.setTag(service_id + "," + userid + "," + distance + "," + distancetype);

    }

    public void setFilter(List<ItemServicePojo> servicesList_Data) {
        servicesList = new ArrayList<>();
        servicesList.addAll(servicesList_Data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return servicesList.size();
    }

}
