package com.team.android_baozou.drawer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.team.android_baozou.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/12.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listitem;

    public ListViewAdapter(Context context, List<Map<String, Object>> listitem) {
        this.context = context;
        this.listitem = listitem;
    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_item, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.task_icon);
        TextView textView = (TextView) convertView.findViewById(R.id.task_name);
        TextView textView1 = (TextView) convertView.findViewById(R.id.task_count);
        Map<String, Object> map = listitem.get(position);
        imageView.setImageResource((Integer) map.get("image"));
        textView.setText(map.get("title") + "");
        textView1.setText(map.get("count") + "");
        return convertView;
    }
}
