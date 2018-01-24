package me.charles.sample.notify.ui.fragment.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.charles.eventbusactivityscope.EventBusActivityScope;
import me.charles.fragmentation.SupportFragment;
import me.charles.sample.R;
import me.charles.sample.notify.ui.fragment.EditKeywordDialogFragment;
import me.charles.sample.notify.ui.fragment.MainFragment;

/**
 * Created by charles on 2017/11/27 0027.
 */

public class MeFragment extends SupportFragment implements EditKeywordDialogFragment.KeywordInputListener {
    private TextView getTvBtnSettings;
    private TextView getTvBtnDocumentLocation;
    private TextView getTvBtnSetAlert;

    private static int KEYWORD = 6666;
    private boolean isOK = false;
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
        getTvBtnSettings = (TextView) view.findViewById(R.id.tv_btn_settings);
        getTvBtnDocumentLocation = (TextView) view.findViewById(R.id.tv_btn_document_location);
        getTvBtnSetAlert = (TextView) view.findViewById(R.id.tv_btn_set_alert);


        getTvBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(v, SettingNetworkFragment.newInstance());
//                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(PersonalSettingFragment.newInstance());
            }
        });
        getTvBtnDocumentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(v, SettingFileLocationFragment.newInstance());
//                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(PersonalSettingFragment.newInstance());
            }
        });
        getTvBtnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(SettingAlertFragment.newInstance());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    public void showEditDialog(View view, SupportFragment fragment)
    {
        FragmentManager fm = getFragmentManager();
        EditKeywordDialogFragment editNameDialogFragment = EditKeywordDialogFragment.newInstance(fragment);
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(MeFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_keyword");
    }

    @Override
    public void onKeywordInputComplete(int keyword, SupportFragment fragment)
    {
        if (keyword == KEYWORD){
            isOK = true;
            ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(fragment);
        }else {
            isOK = false;
        }
        Toast.makeText(getContext(),"hi"+ keyword,Toast.LENGTH_SHORT).show();
    }
}
