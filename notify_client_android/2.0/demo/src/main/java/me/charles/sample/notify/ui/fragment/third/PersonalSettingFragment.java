package me.charles.sample.notify.ui.fragment.third;

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

import me.charles.sample.R;
import me.charles.sample.notify.base.BaseBackFragment;
import me.charles.sample.notify.net.MySharePreference;

public class PersonalSettingFragment extends BaseBackFragment {
    private EditText ip;
    private EditText port;
    private EditText info_s;
    private EditText info_r;
    private EditText warning_s;
    private EditText warning_r;
    private EditText error_s;
    private EditText error_r;
    private EditText critical_s;
    private EditText critical_r;
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

        ip = (EditText) view.findViewById(R.id.et_ip);
        port = (EditText) view.findViewById(R.id.et_port);
        info_s = (EditText) view.findViewById(R.id.et_info_shake);
        info_r = (EditText) view.findViewById(R.id.et_info_ring);
        warning_s = (EditText) view.findViewById(R.id.et_warning_shake);
        warning_r = (EditText) view.findViewById(R.id.et_warning_ring);
        error_s = (EditText) view.findViewById(R.id.et_error_shake);
        error_r = (EditText) view.findViewById(R.id.et_error_ring);
        critical_s = (EditText) view.findViewById(R.id.et_critical_shake);
        critical_r = (EditText) view.findViewById(R.id.et_critical_ring);
        mButton = (Button) view.findViewById(R.id.btn_save);

        service = new MySharePreference(getContext());
        Map<String, String> params = service.getPreferences();
        ip.setText(params.get("ip"));
        port.setText(params.get("port"));
        info_s.setText(params.get("info_s"));
        info_r.setText(params.get("info_r"));
        warning_s.setText(params.get("warning_s"));
        warning_r.setText(params.get("warning_r"));
        error_s.setText(params.get("error_s"));
        error_r.setText(params.get("error_r"));
        critical_s.setText(params.get("critical_s"));
        critical_r.setText(params.get("critical_r"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
//                    String mip = ip.getText().toString();
//                    String mport = port.getText().toString();
//                    String minfo_s = info_s.getText().toString();
//                    String minfo_r = info_r.getText().toString();
//                    String mwarning_s = warning_s.getText().toString();
//                    String mwarning_r = warning_r.getText().toString();
//                    String merror_s = error_s.getText().toString();
//                    String merror_r = error_r.getText().toString();
//                    String mcritical_s = critical_s.getText().toString();
//                    String mcritical_r = critical_r.getText().toString();
//                    String toast_text = "ticker_delay:" + ticker_delay + "\nalert_delay:" + alert_delay + "\nrepeat_alert_delay:" + repeat_alert_delay;
//                    Toast toast=Toast.makeText(getContext(), toast_text, Toast.LENGTH_SHORT);
//                    toast.show();

                    save(view);
                }catch (Exception e){
                    Toast toast=Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void save(View v){
        String mip = ip.getText().toString();
        String mport = port.getText().toString();
        String minfo_s = info_s.getText().toString();
        String minfo_r = info_r.getText().toString();
        String mwarning_s = warning_s.getText().toString();
        String mwarning_r = warning_r.getText().toString();
        String merror_s = error_s.getText().toString();
        String merror_r = error_r.getText().toString();
        String mcritical_s = critical_s.getText().toString();
        String mcritical_r = critical_r.getText().toString();
        service.save(mip, Integer.valueOf(mport),Integer.valueOf(minfo_s),Integer.valueOf(minfo_r),Integer.valueOf(mwarning_s),Integer.valueOf(mwarning_r),Integer.valueOf(merror_s),Integer.valueOf(merror_r),Integer.valueOf(mcritical_s),Integer.valueOf(mcritical_r));
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
