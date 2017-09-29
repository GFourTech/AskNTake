package Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.ReportThisProductScreenActivity;

public class ReportGridDisplyAdapter extends BaseAdapter {
    private Context mContext;
    Integer[] mThumbIds;
    int mainPostion;


    public ReportGridDisplyAdapter(Context c, Integer[] mThumbIds) {
        mContext = c;
        this.mThumbIds = mThumbIds;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder hold = new holder();
        if (convertView == null) {
            //grid = new View(mContext);
            convertView = inflater.inflate(R.layout.activity_report_items_display, null);
            hold.imageView = (ImageView) convertView.findViewById(R.id.img_grid_data);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(250 * ReportThisProductScreenActivity.curScrenWidth / ReportThisProductScreenActivity.defaultScrenWidth, 320 * ReportThisProductScreenActivity.curScrenHeit / ReportThisProductScreenActivity.defaultScrenHeit);
            hold.imageView.setLayoutParams(llp);
            hold.imageView.setScaleType(ScaleType.FIT_XY);

            convertView.setTag(hold);
        } else {
            hold = (holder) convertView.getTag();
        }

        if (hold.imageView != null) {

            hold.imageView.setImageResource(mThumbIds[position]);
        }

        hold.imageView.setTag(position + "");


        return convertView;
    }

    static class holder {
        ImageView imageView;

    }


}