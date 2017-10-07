package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.ServiceDescriptionActivity_Services;
import com.askntake.admin.askntake.ServicesRequests_DescriptionActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import Pojo.ItemServicePojo;
import Pojo.ServiceRequestsPojo;

/**
 * Created by admin on 4/6/2017.
 */

public class MyServiceRequestsListAdapter extends RecyclerView.Adapter<MyServiceRequestsListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServiceRequestsPojo> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_name;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            item_name = (TextView) view.findViewById(R.id.tv_service_title);
            thumbnail = (ImageView) view.findViewById(R.id.iv_service);
            thumbnail.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            SharedPreferences fb_data_pref = mContext.getSharedPreferences(DataKeyValues.USER_DATA_PREF, mContext.MODE_PRIVATE);
            String UserId_Main = fb_data_pref.getString(DataKeyValues.USER_USERID, null);
            if (UserId_Main != null) {
               /* SharedPreferences serviceIdPref = mContext.getSharedPreferences("serviceIdPref", mContext.MODE_PRIVATE);
                SharedPreferences.Editor add_edit = serviceIdPref.edit();
                add_edit.putString("clicked_service_id", "" + albumList.get(Integer.parseInt(view.getTag().toString())).getService_id());
                add_edit.commit();*/
                Intent myIntent = new Intent(mContext, ServicesRequests_DescriptionActivity.class);
                myIntent.putExtra("myownproduct", true);
                myIntent.putExtra("service_id", "" + albumList.get(Integer.parseInt(view.getTag().toString())).getService_id());
                myIntent.putExtra("user_id", "" + UserId_Main);
                mContext.startActivity(myIntent);
            }
        }

    }

    public MyServiceRequestsListAdapter(Context mContext, List<ServiceRequestsPojo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyServiceRequestsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view_myservices, parent, false);

        return new MyServiceRequestsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyServiceRequestsListAdapter.MyViewHolder holder, int position) {
        ServiceRequestsPojo servreqItems = albumList.get(position);

        holder.item_name.setText(servreqItems.getService_name());
        Glide.with(mContext).load(/*AppConstants.IMG_BASE_URL +*/ servreqItems.getService_image()).into(holder.thumbnail);
        holder.thumbnail.setTag(position+"");

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


}