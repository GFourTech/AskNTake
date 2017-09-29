package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.askntake.admin.askntake.R;
import com.bumptech.glide.Glide;

import java.util.List;

import Pojo.ProductItemsPojo;
import AppUtils.AppConstants;

/**
 * Created by admin on 4/6/2017.
 */

public class SellingListAdapter extends RecyclerView.Adapter<SellingListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductItemsPojo> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView item_name, item_price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            item_name = (TextView) view.findViewById(R.id.title);
            item_price = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public SellingListAdapter(Context mContext, List<ProductItemsPojo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public SellingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_view_item_buy_and_sell, parent, false);

        return new SellingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SellingListAdapter.MyViewHolder holder, int position) {
        ProductItemsPojo buyAndSellItems = albumList.get(position);
        holder.item_name.setText(buyAndSellItems.getItem_name());
       // holder.item_price.setText(buyAndSellItems.getPrice());
        holder. item_price.setVisibility(View.GONE);


        Glide.with(mContext).load(AppConstants.IMG_BASE_URL+buyAndSellItems.getMainImage_url()).into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }


}