package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.askntake.admin.askntake.ServicesRequests_DescriptionActivity;
import com.askntake.admin.askntake.SocialMediaRegistrationsActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import Pojo.ItemServicePojo;
import Pojo.ServiceRequestsPojo;

/**
 * Created by admin on 3/28/2017.
 */

public class ServiceRequestsCardAdapter extends RecyclerView.Adapter<ServiceRequestsCardAdapter.MyViewHolder> {
    private List<ServiceRequestsPojo> serviceRequestsList;
    Context mContext;
    String distance;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView service_name, requester_name, location, views, days_old, miles;
        ImageView image_profile_card;
        Context context;
        List<ServiceRequestsPojo> serviceRequestsList;
        CardView card_view;


        public MyViewHolder(View view, Context context, List<ServiceRequestsPojo> serviceRequestsList) {
            super(view);
            this.context = context;
            this.serviceRequestsList = serviceRequestsList;
            service_name = (TextView) view.findViewById(R.id.txt_service_name);
            requester_name = (TextView) view.findViewById(R.id.txt_requester_name);
            location = (TextView) view.findViewById(R.id.txt_location_name);
            views = (TextView) view.findViewById(R.id.txt_views_count);
            days_old = (TextView) view.findViewById(R.id.txt_day_value);
            miles = (TextView) view.findViewById(R.id.txt_mile_value);
            image_profile_card = (ImageView) view.findViewById(R.id.image_profile_card);
            card_view = (CardView) view.findViewById(R.id.card_view);
           // image_profile_card.setOnClickListener(this);
            card_view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {


            String service_id=v.getTag().toString();

            Intent descriptionIntent = new Intent(context, ServicesRequests_DescriptionActivity.class);

            descriptionIntent.putExtra("service_id", service_id);

            SharedPreferences login_preferenes = context.getSharedPreferences(DataKeyValues.USER_DATA_PREF, context.MODE_PRIVATE);

            if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {

                if (login_preferenes.getString(DataKeyValues.OWNER_ID, null) != null) {

                    if (serviceRequestsList.get(getAdapterPosition()).getService_owner_id().equalsIgnoreCase(login_preferenes.getString(DataKeyValues.OWNER_ID, null))) {
                        descriptionIntent.putExtra("myownproduct", true);
                        descriptionIntent.putExtra("user_id", serviceRequestsList.get(getAdapterPosition()).getService_owner_id());
                        descriptionIntent.putExtra("distance", serviceRequestsList.get(getAdapterPosition()).getService_distance());
                    } else

                    {
                        descriptionIntent.putExtra("myownproduct", false);
                        descriptionIntent.putExtra("user_id", serviceRequestsList.get(getAdapterPosition()).getService_owner_id());
                        descriptionIntent.putExtra("distance", serviceRequestsList.get(getAdapterPosition()).getService_distance());
                    }
                } else {
                    descriptionIntent.putExtra("myownproduct", false);
                    descriptionIntent.putExtra("user_id", serviceRequestsList.get(getAdapterPosition()).getService_owner_id());
                    descriptionIntent.putExtra("distance", serviceRequestsList.get(getAdapterPosition()).getService_distance());

                }
                context.startActivity(descriptionIntent);
            } else {
                Intent account_intent = new Intent(mContext, SocialMediaRegistrationsActivity.class);
                mContext.startActivity(account_intent);

            }

        }
    }


    public ServiceRequestsCardAdapter(Context mContext, List<ServiceRequestsPojo> serviceRequestsList) {
        this.mContext = mContext;
        this.serviceRequestsList = serviceRequestsList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_service_requests_card, parent, false);

        return new MyViewHolder(itemView, mContext, serviceRequestsList);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServiceRequestsPojo data = serviceRequestsList.get(position);
        holder.service_name.setText(data.getCategory() + "(" + data.getSubcategory() + ")");
        holder.requester_name.setText(data.getRequester_name());
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(300 * NavigationDrawerActivity.curScrenWidth / NavigationDrawerActivity.defaultScrenWidth, 290 * NavigationDrawerActivity.curScrenHeit / NavigationDrawerActivity.defaultScrenHeit);
        holder.image_profile_card.setLayoutParams(llp);
        holder.location.setText(data.getService_location());
        holder.views.setText(data.getService_views_count());
        holder.days_old.setText(data.getDays_old());
        holder.miles.setText(data.getService_distance());
        String xyz = data.getService_image();
        if (!(data.getService_image().equalsIgnoreCase("noimage"))) {
            Glide.with(mContext).load(/*AppConstants.IMG_BASE_URL +*/data.getService_image()).into(holder.image_profile_card);
        } else {
            Glide.with(mContext).load(/*AppConstants.IMG_BASE_URL +*/ R.drawable.placeholder).into(holder.image_profile_card);
        }
        String service_req_id=data.getService_id();
        holder.card_view.setTag(service_req_id);


    }

    public void setFilter(List<ServiceRequestsPojo> servicesList_Data) {
        serviceRequestsList = new ArrayList<>();
        serviceRequestsList.addAll(servicesList_Data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return serviceRequestsList.size();
    }

}
