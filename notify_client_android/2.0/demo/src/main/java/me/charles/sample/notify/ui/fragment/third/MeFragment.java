package me.charles.sample.notify.ui.fragment.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.charles.eventbusactivityscope.EventBusActivityScope;
import me.charles.fragmentation.SupportFragment;
import me.charles.sample.R;
import me.charles.sample.notify.ui.fragment.MainFragment;

/**
 * Created by charles on 2017/11/27 0027.
 */

public class MeFragment extends SupportFragment{
    private TextView mTvBtnSettings;

    public static MeFragment newInstance() {

        Bundle args = new Bundle();

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fl_me_container, container, false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        mTvBtnSettings = (TextView) view.findViewById(R.id.tv_btn_settings);


        mTvBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(PersonalSettingFragment.newInstance());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }
}
