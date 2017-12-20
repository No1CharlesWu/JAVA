package com.example.charles.notify_app;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    tv_getmsg.setText(getMsg);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void mysocket(int number, String msg){
        try {
            // 创建一个Socket对象，并指定服务端的IP及端口号
            //socket = new Socket("106.14.193.60", 9999);
            socket = new Socket("10.70.3.30", 9999);
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

}
