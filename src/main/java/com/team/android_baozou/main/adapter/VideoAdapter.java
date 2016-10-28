package com.team.android_baozou.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.team.android_baozou.R;
import com.team.android_baozou.base.BaseActivity;
import com.team.android_baozou.base.VIdeoBean;
import com.team.android_baozou.main.view.MainActivity;
import com.team.android_baozou.main.view.VideoFragment;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SoSho on 2016/10/11.
 */

public class VideoAdapter extends BaseAdapter {
    private List<VIdeoBean> mlist = new ArrayList<>();
    private Context context;
    private Activity mActivity;

    public void setData(List<VIdeoBean> list ,Activity activity) {
        this.mActivity=activity;
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
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String url = mlist.get(position).getFile_url();
        holder.Video.setVideoURI(Uri.parse(url));
        holder.videoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Back.setVisibility(View.GONE);
                holder.Video.setVisibility(View.VISIBLE);
                holder.Video.start();
            }
        });
        if (holder.Video.isPlaying()) {
            holder.videoPlay.setVisibility(View.GONE);
        } else {
            holder.videoPlay.setVisibility(View.VISIBLE);
        }
        int play_time = mlist.get(position).getPlay_time();
        Glide.with(parent.getContext()).load(mlist.get(position).getImage()).into(holder.Back);
        holder.Time.setText(String.valueOf(play_time));
        holder.videoTitle.setText(mlist.get(position).getTitle());
        holder.videoCount.setText("播放:"+String.valueOf(mlist.get(position).getPlay_count()));
        holder.Zan.setText(String.valueOf(mlist.get(position).getComment_count()));
        holder.Said.setText(String.valueOf(mlist.get(position).getVote_count()));

        holder.Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHARE_MEDIA[] list = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS, SHARE_MEDIA.RENREN, SHARE_MEDIA.YNOTE
                };
                new ShareAction(mActivity)
                        .setDisplayList(list)//设置分享的平台列表
                        .withText("分享的内容")//分享的内容
                        .withTitle("分享的标题")//分享的标题
                        .withMedia(new UMImage(mActivity, R.mipmap.ic_launcher))//多媒体:图片,音频,视频,表情等
                        //.withTargetUrl(url)//分享一个链接
                        //.addButton()
                        .setCallback(umShareListener).open();
                //分享的监听
                       ;
                        //打开分享面板
            }
        });
        return convertView;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(mActivity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mActivity, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    static class ViewHolder {
        @BindView(R.id.video_play)
        ImageView videoPlay;
        @BindView(R.id._video)
        VideoView Video;
        @BindView(R.id._time)
        TextView Time;
        @BindView(R.id.video_title)
        TextView videoTitle;
        @BindView(R.id.video_count)
        TextView videoCount;
        @BindView(R.id._zan)
        TextView Zan;
        @BindView(R.id._said)
        TextView Said;
        @BindView(R.id._back)
        ImageView Back;
        @BindView(R.id._content)
        ImageView Content;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
