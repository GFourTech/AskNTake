package Adapters;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


import com.askntake.admin.askntake.NavigationDrawerActivity;
import com.askntake.admin.askntake.R;

import Pojo.FilterPojo;

public class CatogiryGridDisplyAdapter extends BaseAdapter{
	private Context mContext;
	Integer[] mThumbIds;
	String[] CatogiryNames;
	int  mainPostion;
	FilterPojo filterPojo;


	public CatogiryGridDisplyAdapter(Context c, Integer[] mThumbIds, String[] CatogiryNames, FilterPojo filterPojo) {
		mContext = c;
		this.mThumbIds=mThumbIds;
		this.CatogiryNames=CatogiryNames;
		this.filterPojo=filterPojo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		holder hold=new holder();


		if (convertView == null) {
			//grid = new View(mContext);
			convertView = inflater.inflate(R.layout.activity_search_categories, null);
			hold.imageView = (ImageView)convertView.findViewById(R.id.img_grid_data);
			hold.imageView.setScaleType(ScaleType.FIT_XY);
			
			convertView.setTag(hold);
		} else {
			hold = (holder) convertView.getTag();
		}

		if(hold.imageView !=null)
		{
			
			hold.imageView.setImageResource(mThumbIds[position]);
		}
		 
		 hold.imageView.setTag(position+"");
		
		hold.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {


                if(filterPojo!=null)
                {


                    ArrayList<String> categoriesList=new ArrayList<>();
                    categoriesList.add(CatogiryNames[Integer.parseInt(v.getTag().toString())].replace(" ","").trim());
                    filterPojo.setCategories(categoriesList);
                }


				Intent filterItent = new Intent(mContext, NavigationDrawerActivity.class);
				filterItent.putExtra("filterdata", filterPojo);
                filterItent.putExtra("selected_single_category", CatogiryNames[Integer.parseInt(v.getTag().toString())].replace(" ","").trim());
				filterItent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(filterItent);
				((Activity)mContext).finish();
				


				
				
			}
		});


		return convertView;
	}
	static class holder{
		ImageView imageView;

	}
	
	
}