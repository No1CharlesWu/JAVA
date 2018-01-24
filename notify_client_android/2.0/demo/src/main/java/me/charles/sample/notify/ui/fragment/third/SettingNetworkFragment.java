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

public class SettingNetworkFragment extends BaseBackFragment {
    private EditText ip;
    private EditText port;
    private Button mButton;
    private Toolbar mToolbar;

    private MySharePreference service;


    public static SettingNetworkFragment newInstance() {
        return new SettingNetworkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_network_fragment, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(R.string.personality_setting);

        ip = (EditText) view.findViewById(R.id.et_ip);
        port = (EditText) view.findViewById(R.id.et_port);
        mButton = (Button) view.findViewById(R.id.btn_save);

        service = new MySharePreference(getContext());
        Map<String, String> params = service.getPreferences();
        ip.setText(params.get("ip"));
        port.setText(params.get("port"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
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

        service.saveNetwork(mip, Integer.valueOf(mport));
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
