package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.askntake.admin.askntake.R;
import com.askntake.admin.askntake.ServiceFilteringActivity;
import com.askntake.admin.askntake.TotalListener;

import java.util.ArrayList;
import java.util.Map;

import Fragments.Category_ServicesFragment;
import Fragments.Category_ServicesRequestFragment;

import static android.R.attr.key;
import static android.R.attr.value;

/**
 * Created by admin on 3/30/2017.
 */

public class ServiceCategoriesFilterAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<String>> mGroupList = new ArrayList<>();

    Context mContext;
    ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
    ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<>();
    TotalListener mListener;
    //    String[] testgroupData;
//    String[] testChildData;
    Map<String, Map<String, String>> data;
    ArrayList<String> categoriesArray;
    public static boolean isAllChecked = false;
    String request;
    Map<String, ArrayList<Integer>> positions;

    public void setmListener(TotalListener mListener) {
        this.mListener = mListener;
    }

    public void ServiceCategoriesFilterAdapter(ArrayList<ArrayList<String>> mGroupList) {
        this.mGroupList = mGroupList;

    }

    class ViewHolder {
        public CheckBox groupName;
        public TextView dummyTextView; // View to expand or shrink the list
        public CheckBox childCheckBox;
        public CheckBox all;
    }

    public ServiceCategoriesFilterAdapter(Context context/*, String[] testgroupData, String[] testChildData*/, Map<String, Map<String, String>> data, String request, Map<String, ArrayList<Integer>> positions) {
        mContext = context;
        this.data = data;
        categoriesArray = new ArrayList<>();
        this.request = request;

        this.positions = positions;

        int index = 0;
        int counter = 0;
        for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {

            if (counter > 0) {
                String category = entry.getKey();
                categoriesArray.add(category);
                ArrayList<String> subcategoriesData = new ArrayList<>();

                int flag = 0;
                for (Map.Entry<String, String> subData : entry.getValue().entrySet()) {

                    if (flag != 0) {
                        subcategoriesData.add(subData.getKey());
                    }
                    flag = flag + 1;
                }
                mGroupList.add(index, subcategoriesData);
                index = index + 1;

            }

            counter = counter + 1;
        }

        initCheckStates(true);

        setData(positions);
    }

    void refreshStatus(boolean isAllChecked) {
        initCheckStates(isAllChecked);
    }

    /**
     * Called to initialize the default check states of items
     *
     * @param defaultState : false
     */
    public void initCheckStates(boolean defaultState) {
        for (int i = 0; i < mGroupList.size(); i++) {
            selectedParentCheckBoxesState.add(i, defaultState);
            ArrayList<Boolean> childStates = new ArrayList<>();
            for (int j = 0; j < mGroupList.get(i).size(); j++) {
                childStates.add(defaultState);
            }

            selectedChildCheckBoxStates.add(i, childStates);
        }
    }

    /* public void setData(ArrayList<String> indexes)  {



         for (int i = 0; i < indexes.size(); i++) {



            String[] positionsArray= indexes.get(i).split(",");
             int groupPosition=Integer.parseInt(positionsArray[0]);
             int childPosition=Integer.parseInt(positionsArray[1]);
             if(selectedParentCheckBoxesState.get(groupPosition)){
                 for (int j = 0; j < indexes.size(); j++) {

                 }
             }
             selectedParentCheckBoxesState.set(groupPosition, true);
             ArrayList<Boolean> childStates = new ArrayList<>();
             for (int j = 0; j < mGroupList.get(i).size(); j++) {
                 childStates.add(childPosition,true);
             }
             selectedChildCheckBoxStates.set(groupPosition, childStates);
         }
     }*/
    public void setData(Map<String, ArrayList<Integer>> positions) {
        for (Map.Entry<String, ArrayList<Integer>> entry : positions.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            int groupPosition = Integer.parseInt(entry.getKey());
            selectedParentCheckBoxesState.set(groupPosition, true);
            ArrayList<Boolean> childStates = new ArrayList<>();
            for (int j = 0; j < mGroupList.get(groupPosition).size(); j++) {
                if (entry.getValue().contains(j)) {
                    childStates.add(j, true);
                } else {
                    childStates.add(j, false);
                }

            }
            selectedChildCheckBoxStates.set(groupPosition, childStates);
        }


    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_service_filter_group_layout, null);
            holder = new ViewHolder();
            holder.groupName = (CheckBox) convertView.findViewById(R.id.group_chk_box);
            holder.dummyTextView = (TextView) convertView.findViewById(R.id.dummy_txt_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        holder.groupName.setText(categoriesArray.get(groupPosition));
        if (selectedParentCheckBoxesState.size() <= groupPosition) {
            selectedParentCheckBoxesState.add(groupPosition, false);
        } else {
            holder.groupName.setChecked(selectedParentCheckBoxesState.get(groupPosition));
        }


        holder.groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Callback to expansion of group item
                if (!isExpanded)
                    mListener.expandGroupEvent(groupPosition, isExpanded);

                boolean state = selectedParentCheckBoxesState.get(groupPosition);
                Log.d("TAG", "STATE = " + state);
                selectedParentCheckBoxesState.remove(groupPosition);
                selectedParentCheckBoxesState.add(groupPosition, state ? false : true);

                for (int i = 0; i < mGroupList.get(groupPosition).size(); i++) {

                    selectedChildCheckBoxStates.get(groupPosition).remove(i);
                    selectedChildCheckBoxStates.get(groupPosition).add(i, state ? false : true);
                }
                notifyDataSetChanged();


                if (state) {
                    if (request.equalsIgnoreCase("serv_req_Category")) {
                        Category_ServicesRequestFragment.uncheckAll();
                    } else if (request.equalsIgnoreCase("serv_Category"))
                    {
                        Category_ServicesFragment.uncheckAll();
                    }
                }


               /* if (state) {
                    Category_ServicesRequestFragment.uncheckAll();
                }*/

                if (request.equalsIgnoreCase("Filtering")) {
                    ServiceFilteringActivity.uncheckAll();
                } else if (request.equalsIgnoreCase("serv_Category")) {
                    Category_ServicesFragment.uncheckAll();
                }


                boolean flag = true;
                boolean checkbox_state;
                for (int i = 0; i < mGroupList.size(); i++) {
                    checkbox_state = selectedParentCheckBoxesState.get(i);
                    if (!checkbox_state) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    if (request.equalsIgnoreCase("serv_req_Category")) {
                        Category_ServicesRequestFragment.checkAll();
                    }
                    else if (request.equalsIgnoreCase("serv_Category")) {
                        Category_ServicesFragment.checkAll();
                    }

                }

                // showTotal(groupPosition);
            }
        });


        //callback to expand or shrink list view from dummy text click
        holder.dummyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Callback to expansion of group item
                mListener.expandGroupEvent(groupPosition, isExpanded);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.single_service_filter_child_layout, null);
            holder = new ViewHolder();
            holder.childCheckBox = (CheckBox) convertView.findViewById(R.id.child_check_box);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.childCheckBox.setText(mGroupList.get(groupPosition).get(childPosition));
        if (selectedChildCheckBoxStates.size() <= groupPosition) {
            ArrayList<Boolean> childState = new ArrayList<>();
            for (int i = 0; i < mGroupList.get(groupPosition).size(); i++) {
                if (childState.size() > childPosition)
                    childState.add(childPosition, false);
                else
                    childState.add(false);
            }
            if (selectedChildCheckBoxStates.size() > groupPosition) {
                selectedChildCheckBoxStates.add(groupPosition, childState);
            } else
                selectedChildCheckBoxStates.add(childState);
        } else {
            if (selectedChildCheckBoxStates.get(groupPosition).size() > 0) {
                holder.childCheckBox.setChecked(selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
            }

        }
        holder.childCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);
                ArrayList<String> selectedSubcategories = getCheckedSubcategories();
                Log.i("selectedSubcategories ", "" + selectedSubcategories.size());
            }
        });

        return convertView;
    }

    /**
     * Called to reflect the sum of checked prices
     *
     * @param groupPosition : group position of list
     */
    private void showTotal(int groupPosition) {
        //Below code is to get the sum of checked prices
        int sum = 0;
        for (int j = 0; j < selectedChildCheckBoxStates.size(); j++) {
            Log.d("TAG", "J = " + j);
            for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition).size(); i++) {
                Log.d("TAG", "I = " + i);

                if (selectedChildCheckBoxStates.get(j).get(i)) {
                    sum += Integer.parseInt(mGroupList.get(j).get(i));
                }
            }
        }
        mListener.onTotalChanged(sum);
    }

    public ArrayList<String> getCheckedSubcategories() {
        //Below code is to get the sum of checked prices
        ArrayList<String> selecteSubcategoriesList = new ArrayList<>();
        // int sum = 0;
        for (int j = 0; j < mGroupList.size(); j++) {
            Log.d("TAG", "J = " + j);
            for (int i = 0; i < selectedChildCheckBoxStates.get(j).size(); i++) {
                Log.d("TAG", "I = " + i);
                if (selectedChildCheckBoxStates.get(j).get(i)) {
                    // sum += Integer.parseInt(mGroupList.get(j).get(i));
                    selecteSubcategoriesList.add(mGroupList.get(j).get(i));
                }
            }

        }
        //  mListener.onTotalChanged(selecteSubcategoriesList);
        return selecteSubcategoriesList;
    }

    public ArrayList<String> getCheckedSubcategoriesPositios() {
        //Below code is to get the sum of checked prices
        ArrayList<String> selecteSubcategoriesList = new ArrayList<>();
        // int sum = 0;
        for (int j = 0; j < mGroupList.size(); j++) {
            Log.d("TAG", "J = " + j);

            if (selectedParentCheckBoxesState.get(j)) {

                if (selectedChildCheckBoxStates.get(j).size() == 0) {
                    selecteSubcategoriesList.add(j + "," + 0);
                }
            }
            for (int i = 0; i < selectedChildCheckBoxStates.get(j).size(); i++) {
                Log.d("TAG", "I = " + i);
                if (selectedChildCheckBoxStates.get(j).get(i)) {
                    // sum += Integer.parseInt(mGroupList.get(j).get(i));
                    selecteSubcategoriesList.add(j + "," + i);
                }
            }
        }
        //  mListener.onTotalChanged(selecteSubcategoriesList);
        return selecteSubcategoriesList;
    }

    public ArrayList<String> getCheckedCategories() {
        //Below code is to get the sum of checked prices
        ArrayList<String> selecteCategoriesList = new ArrayList<>();
        // int sum = 0;
        for (int j = 0; j < categoriesArray.size(); j++) {
            Log.d("TAG", "J = " + j);

            if (selectedParentCheckBoxesState.get(j)) {

                selecteCategoriesList.add(categoriesArray.get(j));
            }

        }
        //  mListener.onTotalChanged(selecteSubcategoriesList);
        return selecteCategoriesList;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
