package com.hijewel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hijewel.R;
import com.hijewel.models.CatsModel;
import com.hijewel.models.MenuCatsModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ${Dhruv} on 27-02-2023.
 */

public class CatsAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<CatsModel>> child;
    private List<MenuCatsModel> header;

    public CatsAdapter(HashMap<String, List<CatsModel>> child, List<MenuCatsModel> header) {
        this.child = child;
        this.header = header;
    }

    @Override
    public int getGroupCount() {
        return header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(header.get(groupPosition).getTitle()).size();
    }

    @Override
    public MenuCatsModel getGroup(int groupPosition) {
        return header.get(groupPosition);
    }

    @Override
    public CatsModel getChild(int groupPosition, int childPosition) {
        return child.get(header.get(groupPosition).getTitle()).get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        MenuCatsModel cm = getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.subcat_item_head, parent, false);
        }
        TextView localTextView = convertView.findViewById(R.id.title);
        final ImageView plus_minus = convertView.findViewById(R.id.plus_minus);
        final ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageResource(cm.getIcon());
        localTextView.setText(cm.getTitle());
        if (getChildrenCount(groupPosition) > 0) {
            plus_minus.setVisibility(View.VISIBLE);
            if (isExpanded) {
                plus_minus.setImageResource(R.drawable.ic_minus);
            } else {
                plus_minus.setImageResource(R.drawable.ic_plus);
            }
        } else {
            plus_minus.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CatsModel cm = getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcat_item_child, parent, false);
        }
        TextView child = convertView.findViewById(R.id.child);
        child.setText(cm.getCategoryDisplayName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

