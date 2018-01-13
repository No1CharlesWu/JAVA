package me.yokeyword.sample.demo_wechat.ui.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.adapter.AlertAdapter;
import me.yokeyword.sample.demo_wechat.base.BaseBackFragment;
import me.yokeyword.sample.demo_wechat.entity.Alert;

public class SingleMarketAlertFragment extends BaseBackFragment {
    private Spinner mSpinner;
    private EditText mHigh;
    private EditText mLow;
    private Button mButton;
    private Toolbar mToolbar;

    public static SingleMarketAlertFragment newInstance() {
        return new SingleMarketAlertFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_market_alert_fragment, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private  void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(R.string.single_market_alert);

        mSpinner = (Spinner)view.findViewById(R.id.spinner_web_list);
        mHigh = (EditText)view.findViewById(R.id.et_high_price);
        mLow = (EditText)view.findViewById(R.id.et_low_price);
        mButton = (Button)view.findViewById(R.id.btn_save);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String select_web = mSpinner.getSelectedItem().toString();
                    String high_price = mHigh.getText().toString();
                    String low_price = mLow.getText().toString();
                    String toast_text = "Spinner:" + select_web + "\nHigh:" + high_price + "\nLow:" + low_price;
                    Toast toast=Toast.makeText(getContext(), toast_text, Toast.LENGTH_SHORT);
                    toast.show();

                    Alert alert = new Alert();
                    alert.type = 0;
                    alert.had_alert = 0;
                    alert.alert_name = "单个市场预警";
                    alert.Sma_web = mSpinner.getSelectedItem().toString().trim();

                    if (! mHigh.getText().toString().equals(""))
                        alert.Sma_high_price = Double.valueOf(mHigh.getText().toString().trim());
                    else
                        alert.Sma_high_price = 999999;

                    if (! mLow.getText().toString().equals(""))
                        alert.Sma_low_price = Double.valueOf(mLow.getText().toString().trim());
                    else
                        alert.Sma_low_price = -100000;

                    if (alert.Sma_high_price == 999999){
                        alert.alert_msg = "当" + alert.Sma_web + "低于" + alert.Sma_low_price;
                    }
                    else if(alert.Sma_low_price == -100000){
                        alert.alert_msg = "当" + alert.Sma_web + "高于" + alert.Sma_high_price;
                    }
                    else {
                        alert.alert_msg = "当" + alert.Sma_web + "高于" + alert.Sma_high_price + "低于" + alert.Sma_low_price;
                    }
                    AlertAdapter tmp = new AlertAdapter();
                    tmp.addAlertList(alert);
                }catch (Exception e){
                    Toast toast=Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                startWithPop(AlertListFragment.newInstance());
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 懒加载
        // 同级Fragment场景、ViewPager场景均适用
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        // 当对用户可见时 回调
        // 不管是 父Fragment还是子Fragment 都有效！
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        // 当对用户不可见时 回调
        // 不管是 父Fragment还是子Fragment 都有效！
    }
}
