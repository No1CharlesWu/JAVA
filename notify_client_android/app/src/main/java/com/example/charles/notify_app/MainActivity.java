package com.example.charles.notify_app;

import android.app.Service;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText et_number;
    private EditText et_msg;
    private Button button;
    private TextView tv_getmsg;

    private int number;
    private String msg;
    private String getMsg;

    private Socket socket;

    private Vibrator vibrator;
    private int short_vibrate_milliseconds = 1000;
    private int long_vibrate_milliseconds = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view();
    }

    private void view(){
        et_number = (EditText)findViewById(R.id.et_number);
        et_msg = (EditText)findViewById(R.id.et_msg);
        button = (Button)findViewById(R.id.button);
        tv_getmsg = (TextView)findViewById(R.id.tv_getmsg);
        vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = Integer.valueOf(et_number.getText().toString());
                msg = et_msg.getText().toString();

                Thread thread=new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mysocket(number, msg);
                        // TODO Auto-generated method stub
                        Message message=new Message();
                        message.what=1;
                        mHandler.sendMessage(message);
                    }
                });
                thread.start();
            }
        });
    }

    private void mysocket(int number, String msg){
        try {
            // 创建一个Socket对象，并指定服务端的IP及端口号
            socket = new Socket("106.14.193.60", 9999);
//            socket = new Socket("10.70.3.30", 9999);
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
            ByteArrayOutputStream out = null;
            if (inputStream != null) {
                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 4];
                int len = -1;
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
                    tv_getmsg.setText(getMsg);
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

            if (level.equals("debug")){
                debug_alert();
            }else if (level.equals("info")){
                info_alert();
            }else if (level.equals("warning")){
                warning_alert();
            }else if (level.equals("error")){
                error_alert();
            }else if (level.equals("critical")){
                critical_alert();
            }else {
                Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //静默，什么都不做
    private void debug_alert(){
        Toast.makeText(this, "debug", Toast.LENGTH_SHORT).show();
    }

    //短震动
    private void info_alert(){
        vibrator.vibrate(short_vibrate_milliseconds);
        Toast.makeText(this, "info", Toast.LENGTH_SHORT).show();
    }

    //短响铃
    private void warning_alert(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, 2000);
        Toast.makeText(this, "warning", Toast.LENGTH_SHORT).show();
    }

    //短震动 + 短响铃
    private void error_alert(){
        vibrator.vibrate(short_vibrate_milliseconds);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, 2000);
        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

    //长震动 + 长响铃
    private void critical_alert(){
        vibrator.vibrate(long_vibrate_milliseconds);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                r.stop();
            }
        }, 10 * 1000);
        Toast.makeText(this, "critical", Toast.LENGTH_SHORT).show();
    }


}
