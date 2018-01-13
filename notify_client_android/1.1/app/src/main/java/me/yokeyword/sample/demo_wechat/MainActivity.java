package me.yokeyword.sample.demo_wechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.net.AlertService;
import me.yokeyword.sample.demo_wechat.ui.fragment.MainFragment;
import me.yokeyword.sample.demo_wechat.ui.fragment.second.WechatSecondTabFragment;
import me.yokeyword.sample.demo_wechat.ui.fragment.setting.AlertListFragment;

/**
 * 仿微信交互方式Demo
 * Created by YoKeyword on 16/6/30.
 */
public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_activity_main);

        MainFragment mainFragment  = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mainFragment, "MainFragment").commit();

        Intent intent = new Intent(this, AlertService.class);
        startService(intent);

        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    protected void onResume() {
//        getNotify(getIntent());
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        getNotify(intent);
        setIntent(intent);
    }

    private void getNotify(Intent intent){
        String value = intent.getStringExtra("toValue");

        Log.i("TAG", "onNewIntent: " + value);

        if(!TextUtils.isEmpty(value)) {
            switch (value) {
                case "href":
                    MainFragment mainFragment  = new MainFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mainFragment, "MainFragment").commitAllowingStateLoss();
                    if (findFragment(MainFragment.class) == null) {
                        loadRootFragment(R.id.fl_container, MainFragment.newInstance());
                    }
                    //这里不是用的commit提交，用的commitAllowingStateLoss方式。commit不允许后台执行，不然会报Deferring update until onResume 错误
                    break;
            }
        }
        super.onNewIntent(intent);
    }
}
