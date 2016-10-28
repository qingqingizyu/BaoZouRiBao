package com.team.android_baozou.drawer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team.android_baozou.R;
import com.team.android_baozou.bean.ChannelBean;

import java.util.List;

/**
 * 类描述：
 * 类名称：
 * 作者：飞哥
 * 创建时间:2016/10/13 0013 8:54 .
 * 备注：
 */

public class ChannelAdapter extends BaseAdapter {

    private List<ChannelBean> mList;

    public void setData(List<ChannelBean> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mList != null ? mList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*绑定数据*/
        ChannelBean bean = mList.get(position);
        String name = bean.getName();
        String summary = bean.getSummary();
        String thumbnail = bean.getThumbnail();
        /*绑定控件*/
        holder.title.setText(name);
        holder.content.setText(summary);
        Glide.with(parent.getContext()).load(thumbnail).into(holder.logo);
        return convertView;
    }
    class ViewHolder{
        ImageView logo;
        TextView title,content;
        ViewHolder(View ConvertView){
            logo = (ImageView) ConvertView.findViewById(R.id.chan_list_image);
            title = (TextView) ConvertView.findViewById(R.id.chan_list_title);
            content = (TextView) ConvertView.findViewById(R.id.chan_list_content);
        }
    }
}
