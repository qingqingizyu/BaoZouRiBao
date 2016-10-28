package com.team.android_baozou.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.team.android_baozou.R;
import com.team.android_baozou.bean.HomeListBean;

import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 类描述：
 * 类名称：HomeAdapter
 * 作者：飞哥
 * 创建时间:2016/10/12 0012 11:43 .
 * 备注：
 */

public class HomeAdapter extends BaseAdapter {
    private List<HomeListBean> mList;

    public  void setData(List<HomeListBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mList != null ? mList : null;
    }

    @Override
    public long getItemId(int i) {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_list, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //绑定数据
        HomeListBean bean = mList.get(position);
        String title = bean.getTitle();
        String thumbnail = bean.getThumbnail();
        String author_name = bean.getAuthor_name();
        String author_avatar = bean.getAuthor_avatar();
        String source_name = bean.getSource_name();
        //设置数据控件
        holder.tvTitle.setText(title);
        Glide.with(viewGroup.getContext()).load(thumbnail).into(holder.ivShow);
        holder.author.setText(author_name);
        holder.source.setText(source_name);
        Glide.with(viewGroup.getContext()).load(author_avatar).transform(new CircleTransform(context)).into(holder.authorImg);
        return convertView;
    }

    class ViewHolder {
        ImageView ivShow;
        ImageView authorImg;
        TextView tvTitle, author, source;

        public ViewHolder(View view) {
            ivShow = (ImageView) view.findViewById(R.id.home_ivShow);
            authorImg = (ImageView) view.findViewById(R.id.home_ivAuthorImage);
            author = (TextView) view.findViewById(R.id.home_tvAuthor);
            tvTitle = (TextView) view.findViewById(R.id.home_tvTitle);
            source = (TextView) view.findViewById(R.id.home_tvSource);
        }
    }

    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }
}
