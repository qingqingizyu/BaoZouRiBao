package com.team.android_baozou.main.view;

/**
 * Created by SoSho on 2016/10/11.
 */

public interface OnRefreshListener {

    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}