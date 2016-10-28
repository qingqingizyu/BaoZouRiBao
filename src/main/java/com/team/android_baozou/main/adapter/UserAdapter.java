package com.team.android_baozou.main.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team.android_baozou.R;
import com.team.android_baozou.base.UserBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SoSho on 2016/10/11.
 */

public class UserAdapter extends BaseAdapter {
    private List<UserBean> mlist = new ArrayList<>();

    public void setData(List<UserBean> list) {
        this.mlist = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist != null ? mlist.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mlist != null ? mlist : null;
    }

    @Override
    public long getItemId(int position) {
        return mlist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String image = mlist.get(position).getAuthor_avatar();
        String title = mlist.get(position).getTitle();
        String name = mlist.get(position).getAuthor_name();
        Glide.with(parent.getContext()).load(image).into(holder.uitemImage);
        holder.userTitle.setText(title);
        holder.userUsername.setText(name);
        Log.d("UserAdapter", "mlist.size():" + mlist.size());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.uitem_image)
        ImageView uitemImage;
        @BindView(R.id.user_title)
        TextView userTitle;
        @BindView(R.id.user_username)
        TextView userUsername;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
