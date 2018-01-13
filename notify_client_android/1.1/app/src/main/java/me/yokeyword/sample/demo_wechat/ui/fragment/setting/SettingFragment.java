package me.yokeyword.sample.demo_wechat.ui.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.base.BaseMainFragment;
import me.yokeyword.sample.demo_zhihu.ui.fragment.fourth.child.*;

/**
 * Created by charles on 2017/11/27 0027.
 */

public class SettingFragment extends BaseMainFragment {
    private Toolbar mToolbar;
    private View mView;

    public static SettingFragment newInstance() {

        Bundle args = new Bundle();

        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.setting_fragment, container, false);
        return mView;
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        loadRootFragment(R.id.fl_setting_container, MeFragment.newInstance());

        mToolbar = (Toolbar) mView.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.settings);
    }

}
