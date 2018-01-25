package me.charles.sample.notify.ui.fragment.second;

import android.app.Service;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import me.charles.fragmentation.SupportFragment;
import me.charles.sample.R;
import me.charles.sample.notify.adapter.HistoryMsgAdapter;
import me.charles.sample.notify.base.BaseMainFragment;
import me.charles.sample.notify.net.MySharePreference;
import me.charles.sample.notify.ui.fragment.EditKeywordDialogFragment;

public class SecondTabFragment extends BaseMainFragment implements EditKeywordDialogFragment.KeywordInputListener{
    private Toolbar mToolbar;
    private EditText mEditText;
    private Button mLoadFile;
    private Button mButton;
    private Vibrator vibrator;

    private Socket socket;
    private String msg;
    private String getMsg;

    private static int KEYWORD = 6666;

    private MySharePreference service;
    private Map<String,String> params;

    public static SecondTabFragment newInstance() {
        Bundle args = new Bundle();
        SecondTabFragment fragment = new SecondTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wechat_fragment_tab_second, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.send_message);
        mLoadFile = (Button) view.findViewById(R.id.btn_load_file);
        mButton = (Button) view.findViewById(R.id.btn_send);
        mEditText = (EditText) view.findViewById(R.id.et_send_message);
        vibrator = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);

        service = new MySharePreference(getContext());
        params = service.getPreferences();

        mLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params = service.getPreferences();
                mEditText.setText(readFileSdcardFile(params.get("location")));
                Toast.makeText(getContext(), readFileSdcardFile(params.get("location")) ,Toast.LENGTH_SHORT).show();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    public String readFileSdcardFile(String fileName){
        FileInputStream in;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    private void mysocket(int number, String msg){
        try {
            // 创建一个Socket对象，并指定服务端的IP及端口号
            socket = new Socket(params.get("ip"), Integer.valueOf(params.get("port")));
            // 获取Socket的OutputStream对象用于发送数据。
            OutputStream outputStream = socket.getOutputStream();

            //JSON
            JSONObject jsonObject = new JSONObject();
            //写入对应属性
            jsonObject.put("msg", msg);
            jsonObject.put("keyword", number);
            System.out.println(jsonObject);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(jsonObject.toString());
            writer.flush();

            // 创建一个InputStream用户读取要发送的文件。
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream out;
            if (inputStream != null) {
                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 4];
                int len;
                if ((len = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                byte[] data = out.toByteArray();
                getMsg = new String(data, "utf-8");
                System.out.println(getMsg);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    setAlert(getMsg);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void setAlert(String getMsg){
        try {
            JSONObject obj = new JSONObject(getMsg);
            String level = obj.getString("level");
            String msg = obj.getString("msg");

            HistoryMsgAdapter historyMsgAdapter = new HistoryMsgAdapter();
            historyMsgAdapter.addData(msg, level, System.currentTimeMillis());
            historyMsgAdapter.mNotify();

            switch (level) {
                case "debug":
                    debug_alert();
                    break;
                case "info":
                    info_alert();
                    break;
                case "warning":
                    warning_alert();
                    break;
                case "error":
                    error_alert();
                    break;
                case "critical":
                    critical_alert();
                    break;
                default:
                    Toast.makeText(getContext(), "nothing", Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //静默，什么都不做
    private void debug_alert(){
        Toast.makeText(getContext(), "debug", Toast.LENGTH_SHORT).show();
    }

    //短震动
    private void info_alert(){
        vibrator.vibrate(Integer.valueOf(params.get("info_s"))* 1000);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, Integer.valueOf(params.get("info_r")) * 1000);
        Toast.makeText(getContext(), "info", Toast.LENGTH_SHORT).show();
    }

    //短响铃
    private void warning_alert(){
        vibrator.vibrate(Integer.valueOf(params.get("warning_s"))* 1000);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, Integer.valueOf(params.get("warning_r")) * 1000);
        Toast.makeText(getContext(), "warning", Toast.LENGTH_SHORT).show();
    }

    //短震动 + 短响铃
    private void error_alert(){
        vibrator.vibrate(Integer.valueOf(params.get("error_s"))* 1000);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, Integer.valueOf(params.get("error_r")) * 1000);
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
    }

    //长震动 + 长响铃
    private void critical_alert(){
        vibrator.vibrate(Integer.valueOf(params.get("critical_s")) * 1000);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, Integer.valueOf(params.get("critical_r")) * 1000);
        Toast.makeText(getContext(), "critical", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void showEditDialog()
    {
        FragmentManager fm = getFragmentManager();
        EditKeywordDialogFragment editNameDialogFragment = EditKeywordDialogFragment.newInstance(null);
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(SecondTabFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_keyword");
    }

    @Override
    public void onKeywordInputComplete(int keyword, SupportFragment fragment)
    {
        if (keyword == KEYWORD){
            try {
                msg = mEditText.getText().toString();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mysocket(KEYWORD, msg);

                        Message message=new Message();
                        message.what=1;
                        mHandler.sendMessage(message);
                    }
                });
                thread.start();
            }catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }else {
        }
        Toast.makeText(getContext(),"send"+ keyword,Toast.LENGTH_SHORT).show();
    }
}
