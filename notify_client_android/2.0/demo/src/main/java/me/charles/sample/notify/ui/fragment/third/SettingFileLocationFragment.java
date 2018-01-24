package me.charles.sample.notify.ui.fragment.third;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Map;

import me.charles.sample.R;
import me.charles.sample.notify.base.BaseBackFragment;
import me.charles.sample.notify.net.MySharePreference;

import static android.util.Log.i;

public class SettingFileLocationFragment extends BaseBackFragment {
    private EditText location;
    private Button mSelectButton;
    private Button mButton;
    private Toolbar mToolbar;

    private Intent intent;

    private MySharePreference service;

    private static int FILE_SELECT_CODE = 1000;


    public static SettingFileLocationFragment newInstance() {
        return new SettingFileLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_file_location_fragment, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(R.string.personality_setting);

        location = (EditText) view.findViewById(R.id.et_location);
        mSelectButton = (Button) view.findViewById(R.id.btn_select_file);
        mButton = (Button) view.findViewById(R.id.btn_save);

        service = new MySharePreference(getContext());
        Map<String, String> params = service.getPreferences();
        location.setText(params.get("location"));

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

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

    /** 调用文件选择软件来选择文件 **/
    private void showFileChooser() {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK&&requestCode==FILE_SELECT_CODE){
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String string =uri.toString();
            File file;
            String a[]=new String[2];
            //判断文件是否在sd卡中
            if (string.indexOf(String.valueOf(Environment.getExternalStorageDirectory()))!=-1){
                //对Uri进行切割
                a = string.split(String.valueOf(Environment.getExternalStorageDirectory()));
                //获取到file
                file = new File(Environment.getExternalStorageDirectory(),a[1]);

                location.setText(string);
                Toast.makeText(getContext(),a[1],Toast.LENGTH_SHORT).show();

            }else if(string.indexOf(String.valueOf(Environment.getDataDirectory()))!=-1){ //判断文件是否在手机内存中
                //对Uri进行切割
                a =string.split(String.valueOf(Environment.getDataDirectory()));
                //获取到file
                file = new File(Environment.getDataDirectory(),a[1]);

                location.setText(string);

                Toast.makeText(getContext(),a[1],Toast.LENGTH_SHORT).show();

            }else{
                //出现其他没有考虑到的情况
                Toast.makeText(getContext(),"文件路径解析失败！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public void save(View v){
        String mlocation = location.getText().toString();
        service.saveFileLocation(mlocation);
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
