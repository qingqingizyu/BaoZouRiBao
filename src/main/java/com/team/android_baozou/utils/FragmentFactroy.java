package com.team.android_baozou.utils;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.team.android_baozou.main.view.HomeFragment;
import com.team.android_baozou.main.view.UserFragment;
import com.team.android_baozou.main.view.VideoFragment;

/**
 * Created by Administrator on 2016/10/11.
 */
public class FragmentFactroy {
    private static final int FIST_FRAMENT = 0;
    private static final int SECOND_FRAMENT = 1;
    private static final int THRID_FRAMENT = 2;
    private static final int FOURTH_FRAMET = 3;
    private static SparseArray<Fragment> fragments = new SparseArray<>();

    public static Fragment createFragment(int index) {//创建一个Fragment的工厂

        Fragment fragment = fragments.get(index);
        if (fragment == null) {

            switch (index) {
                case FIST_FRAMENT:
                    fragment = new HomeFragment();
                    break;
                case SECOND_FRAMENT:
                    fragment = new UserFragment();
                    break;
                case THRID_FRAMENT:
                    fragment = new VideoFragment();
                    break;
                case FOURTH_FRAMET:
                    break;
            }
            fragments.put(index, fragment);
        }
        return fragment;
    }
}
