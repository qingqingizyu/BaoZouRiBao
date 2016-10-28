package com.team.android_baozou.drawer.view;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team.android_baozou.R;
import com.team.android_baozou.main.adapter.RankAdapter;
import com.team.android_baozou.main.view.MainActivity;
import com.team.android_baozou.main.view.RankmianFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * <p/>
 * 排行榜的Fragment
 */
public class RankFragment extends Fragment {


    @BindView(R.id.rank_toolbat)
    Toolbar rankToolbat;
    @BindView(R.id.rank_tab)
    TabLayout rankTab;
    @BindView(R.id.rank_viewpager)
    ViewPager rankViewpager;
    private final String ACTION_NAME = "OPEN";
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSetting();
        setPagerWithTab();

    }

    private void initSetting() {
        //获取tab的名称
        String[] array = getResources().getStringArray(R.array.ranktab_list);
        List<Fragment> fragments = new ArrayList<>();

        //创建Fragment，并且设置tab的名称
        for (int i = 0; i < array.length; i++) {
            //设置标题栏名称
            TabLayout.Tab tab = rankTab.newTab();
            tab.setText(array[i]);
            rankTab.addTab(tab);
            //创建fragment
            rankTab.setTabTextColors(ColorStateList.valueOf(Color.RED));
            RankmianFragment fragment = new RankmianFragment();
            fragments.add(fragment);

        }

        //给viewpager关联适配器
        rankViewpager.setAdapter(new RankAdapter(getActivity().getSupportFragmentManager(), fragments, array));
        rankToolbat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).Draweropen();
            }
        });
        rankToolbat.inflateMenu(R.menu.rank_menu);
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_rank_actionbar_arrow);
        rankToolbat.setOverflowIcon(drawable);
        //toolbar子菜单项目的点击事件
       rankToolbat.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.menu_item_today:
                       path = "http://dailyapi.ibaozou.com/api/v31/rank/read/day";
                       break;
                   case R.id.menu_item_week:
                       path = "http://dailyapi.ibaozou.com/api/v31/rank/read/week";
                       break;
                   case R.id.menu_item_month:
                         path = "http://dailyapi.ibaozou.com/api/v31/rank/read/month";
                       break;
               }
               return true;
           }
       });

    }
    public String RankPath(){
        String ranpath=path;
//        Log.d("RankFragment", ranpath);
        return ranpath;
    }

    private void setPagerWithTab() {

        //切换viewpager，实现标题栏切换
        rankViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(rankTab)

        );

        //点击标题栏，实现viewpager切换
        rankTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()

                                         {
                                             @Override
                                             public void onTabSelected(TabLayout.Tab tab) {
                                                 rankViewpager.setCurrentItem(tab.getPosition(), false);
                                             }

                                             @Override
                                             public void onTabUnselected(TabLayout.Tab tab) {

                                             }

                                             @Override
                                             public void onTabReselected(TabLayout.Tab tab) {

                                             }
                                         }
        );
    }

}
