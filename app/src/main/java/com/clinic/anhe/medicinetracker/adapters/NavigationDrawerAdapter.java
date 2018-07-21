package com.clinic.anhe.medicinetracker.adapters;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.BaseExpandableListAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.GroupMenuModel;

import java.util.HashMap;
import java.util.List;

public class NavigationDrawerAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<GroupMenuModel> mListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<GroupMenuModel, List<GroupMenuModel>> mListDataChild;
    ExpandableListView expandList;

    public NavigationDrawerAdapter(Context context, List<GroupMenuModel> listDataHeader,
                                   HashMap<GroupMenuModel, List<GroupMenuModel>> listChildData,
                                   ExpandableListView expandList) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = expandList;

    }

    @Override
    public int getGroupCount() {
        return mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //right now only Patients Group item (groupPosition 0) has submenu,
        //so only check if group Position 0, others return 0;
        if(groupPosition == 0) {
            return mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupMenuModel groupMenu = (GroupMenuModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater groupMenuInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = groupMenuInflater.inflate(R.layout.nav_groupmenu, null);
        }
        TextView groupMenuTitle= (TextView) convertView
                .findViewById(R.id.group_menu_title);
        ImageView groupMenuIconImage = (ImageView) convertView.findViewById(R.id.group_menu_iconimage);
        groupMenuTitle.setText(groupMenu.getIconName());
        groupMenuIconImage.setImageResource(groupMenu.getIconImg());

        //hide indicator for group menu item without child
//        ImageView indicator = convertView.findViewById(R.id.list_item_arrow);
//
//        if( indicator != null ) {
//            if( getChildrenCount( groupPosition ) == 0 ) {
//                Log.d("the group positon:" + groupPosition , "is with no child, set invisible");
//                //set visibility to Gone, not invisble!
//                //  https://stackoverflow.com/questions/11303314/expandablelistview-is-showing-indicator-for-groups-with-no-child?rq=1
//                indicator.setVisibility( View.GONE );
//            } else {
//                indicator.setVisibility( View.VISIBLE );
//            }
//        }

        Log.d("when group is clicked", "getGroupView is called "+ groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final GroupMenuModel subMenu = (GroupMenuModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater subMenuInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = subMenuInflater.inflate(R.layout.nav_submenu, null);
        }

        TextView subMenuTitle = (TextView) convertView
                .findViewById(R.id.sub_menu_title);
        subMenuTitle.setText(subMenu.getIconName());
        subMenuTitle.setTextColor(ContextCompat.getColor(mContext, R.color.menuTextIconColor));
        ImageView subMenuIconImage = (ImageView) convertView.findViewById(R.id.sub_menu_iconimage);
        subMenuIconImage.setImageResource(subMenu.getIconImg());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
