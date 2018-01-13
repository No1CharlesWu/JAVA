package me.yokeyword.sample.demo_wechat.ui.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.base.BaseBackFragment;
import me.yokeyword.sample.demo_wechat.net.MySharePreference;

public class PersonalSettingFragment extends BaseBackFragment {
    private EditText mticker_delay;//检查超过ticker_delay就显示红色，否则绿色
    private EditText malert_delay;//检查超过alert_delay就过时了，不报警
    private EditText mrepeat_alert_delay; //检查每隔repeat_alert_delay报警一次
    private Button mButton;
    private Toolbar mToolbar;

    private MySharePreference service;


    public static PersonalSettingFragment newInstance() {
        return new PersonalSettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_setting_fragment, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(R.string.personality_setting);

        mticker_delay = (EditText) view.findViewById(R.id.et_ticker_delay);
        malert_delay = (EditText) view.findViewById(R.id.et_alert_delay);
        mrepeat_alert_delay = (EditText) view.findViewById(R.id.et_repeat_alert_delay);
        mButton = (Button) view.findViewById(R.id.btn_save);

        service = new MySharePreference(getContext());
        Map<String, String> params = service.getPreferences();
        mticker_delay.setText(params.get("ticker_delay"));
        malert_delay.setText(params.get("alert_delay"));
        mrepeat_alert_delay.setText(params.get("repeat_alert_delay"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String ticker_delay = mticker_delay.getText().toString();
                    String alert_delay = malert_delay.getText().toString();
                    String repeat_alert_delay = mrepeat_alert_delay.getText().toString();
                    String toast_text = "ticker_delay:" + ticker_delay + "\nalert_delay:" + alert_delay + "\nrepeat_alert_delay:" + repeat_alert_delay;
                    Toast toast=Toast.makeText(getContext(), toast_text, Toast.LENGTH_SHORT);
                    toast.show();

                    save(view);
                }catch (Exception e){
                    Toast toast=Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void save(View v){
        String ticker_delay = mticker_delay.getText().toString();
        String alert_delay = malert_delay.getText().toString();
        String repeat_alert_delay = mrepeat_alert_delay.getText().toString();
        service.save(Integer.valueOf(ticker_delay),Integer.valueOf(alert_delay),Integer.valueOf(repeat_alert_delay));
        Toast.makeText(getContext(), "success save", Toast.LENGTH_SHORT).show();
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
