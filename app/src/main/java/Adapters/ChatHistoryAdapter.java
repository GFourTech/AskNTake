package Adapters;

import java.util.ArrayList;




import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;
import com.bumptech.glide.Glide;

import Pojo.OwnerHistoryPojo;

public class ChatHistoryAdapter extends ArrayAdapter<OwnerHistoryPojo> {

	private final Activity context;
	private final ArrayList<OwnerHistoryPojo> list;
	String showType;

	public ChatHistoryAdapter(Activity context, ArrayList<OwnerHistoryPojo> list,String showType) {
		super(context, R.layout.list_layout, list);
		this.context = context;
		this.showType=showType;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView person_name;
		protected TextView product_name;
		protected TextView last_message,chat_count;
		protected LinearLayout chat_history_row_lin;
		protected ImageView chat_product_image;
		protected View seperator;
		
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		System.out.println("SIZE : " + list.size());
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.chat_history_row, null);
			viewHolder = new ViewHolder();
			viewHolder.chat_history_row_lin = (LinearLayout) convertView
					.findViewById(R.id.chat_history_row_lin);
			viewHolder.person_name = (TextView) convertView
					.findViewById(R.id.person_name);
			viewHolder.product_name = (TextView) convertView
					.findViewById(R.id.product_name);
			viewHolder.last_message = (TextView) convertView
					.findViewById(R.id.last_message);
			viewHolder.seperator = (View) convertView
					.findViewById(R.id.seperator);
			
			viewHolder.chat_count= (TextView) convertView
					.findViewById(R.id.navigation_symbol);
			
			viewHolder.chat_product_image = (ImageView) convertView
					.findViewById(R.id.chat_product_image);

			RelativeLayout.LayoutParams rlp=new RelativeLayout.LayoutParams(200* NavigationDrawerActivity.curScrenWidth/NavigationDrawerActivity.defaultScrenWidth,
					200*NavigationDrawerActivity.curScrenHeit/NavigationDrawerActivity.defaultScrenHeit);
            rlp.setMargins(0,0,10*NavigationDrawerActivity.curScrenWidth/NavigationDrawerActivity.defaultScrenWidth,0);
            viewHolder.chat_product_image.setLayoutParams(rlp);


			viewHolder.person_name.setTextColor(Color.BLACK);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list != null) {
			OwnerHistoryPojo h = list.get(position);
			viewHolder.person_name.setText(h.getUserName());
			viewHolder.product_name.setText(h.getProductName());
			viewHolder.product_name.setTextColor(Color.parseColor(h.getProductColor()));
			viewHolder.last_message.setText(h.getLastMessage());
			if(h.getUnreadmessages()!=0)
			{
				viewHolder.chat_count.setVisibility(View.VISIBLE);
				viewHolder.chat_count.setText(""+h.getUnreadmessages());
			}
			else
			{
				viewHolder.chat_count.setVisibility(View.GONE);
			}
			

			try
			{
				if(showType.equalsIgnoreCase("product"))
				{
					Glide.with(context).load(h.getProductImage())
					.into(viewHolder.chat_product_image);
				}
				else
					if(!h.getProfile_image().equalsIgnoreCase("noimage"))
					{
						if(showType.equalsIgnoreCase("profile"))
						{
							Glide.with(context).load(h.getProfile_image())
							.into(viewHolder.chat_product_image);
						}

					}else
					{
						viewHolder.chat_product_image.setImageBitmap(BitmapFactory.decodeResource(parent.getContext().getResources(),
								R.drawable.ic_invoice_selected));
					}

			}
			catch(Exception e)
			{
				viewHolder.chat_product_image.setImageBitmap(BitmapFactory.decodeResource(parent.getContext().getResources(),
						R.drawable.ic_invoice_selected));
			}



			if (h.getStatus().equalsIgnoreCase("RECEIVED")) {
				viewHolder.chat_history_row_lin.setGravity(Gravity.LEFT);
			}
		}

		return convertView;
	}
}
