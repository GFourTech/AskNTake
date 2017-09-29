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
import android.widget.TextView;

import com.askntake.admin.askntake.ItemDescriptionActivity_BuyAndSell;
import com.askntake.admin.askntake.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Pojo.BuyAndSellItems;
import AppUtils.AppConstants;
import AppUtils.DataKeyValues;

/**
 * Created by admin on 4/3/2017.
 */

public class BuyAndSellItemsAdapter extends RecyclerView.Adapter<BuyAndSellItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<BuyAndSellItems> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_name, item_price;
        public ImageView thumbnail, overflow;
        Context context;
        List<BuyAndSellItems> products_list;
        CardView card_view;

        public MyViewHolder(View view, Context context, List<BuyAndSellItems> products_list) {
            super(view);
            this.context = context;
            this.products_list = products_list;
            item_name = (TextView) view.findViewById(R.id.title);
            item_price = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            card_view = (CardView) view.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
            //thumbnail.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Intent descriptionIntent = new Intent(context, ItemDescriptionActivity_BuyAndSell.class);
            descriptionIntent.putExtra("product_id", products_list.get(getAdapterPosition()).getItem_id());


            SharedPreferences login_preferenes = context.getSharedPreferences(DataKeyValues.USER_DATA_PREF, context.MODE_PRIVATE);

            if (login_preferenes.getBoolean(DataKeyValues.USER_LOGIN_STATUS, false)) {

                if (login_preferenes.getString(DataKeyValues.USER_USERID, null) != null) {
                    if (products_list.get(getAdapterPosition()).getItem_owner_id().equalsIgnoreCase(login_preferenes.getString(DataKeyValues.USER_USERID, null)))

                    {
                        descriptionIntent.putExtra("myownproduct", true);
                        descriptionIntent.putExtra("user_id", products_list.get(getAdapterPosition()).getItem_owner_id());
                    } else

                    {
                        descriptionIntent.putExtra("myownproduct", false);
                        descriptionIntent.putExtra("user_id", products_list.get(getAdapterPosition()).getItem_owner_id());
                    }
                } else {
                    descriptionIntent.putExtra("myownproduct", false);
                    descriptionIntent.putExtra("user_id", products_list.get(getAdapterPosition()).getItem_owner_id());

                }
            } else {
                descriptionIntent.putExtra("myownproduct", false);
                descriptionIntent.putExtra("user_id", products_list.get(getAdapterPosition()).getItem_owner_id());

            }
            context.startActivity(descriptionIntent);


        }
    }


    public BuyAndSellItemsAdapter(Context mContext, List<BuyAndSellItems> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view_item_buy_and_sell, parent, false);

        return new MyViewHolder(itemView, mContext, albumList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BuyAndSellItems buyAndSellItems = albumList.get(position);
        holder.item_name.setText(buyAndSellItems.getItem_name());
        holder.item_price.setText(buyAndSellItems.getItem_price_type() + " " + buyAndSellItems.getItem_price());

        Glide.with(mContext).load(AppConstants.IMG_BASE_URL + buyAndSellItems.getItem_image_url()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void setFilter(List<BuyAndSellItems> countryModels) {
        albumList = new ArrayList<>();
        albumList.addAll(countryModels);
        notifyDataSetChanged();
    }


}