package me.yokeyword.sample.demo_wechat.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.app.AlertDialog;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by charles on 2017/12/5 0005.
 */

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //调试信息输出
        Log.d("MYTAG", "onclock.............");
        String msg = intent.getStringExtra("msg");
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        //调用播放系统闹钟声音
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ticker Alert")
                .setMessage(msg)
                .setPositiveButton("关闭闹铃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        r.stop();
                    }
                });
        final AlertDialog ad = builder.create();
        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        ad.setCanceledOnTouchOutside(false);                                   //点击外面区域不会让dialog消失

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //doSomeThing
                        ad.show();
                    }
                });
            }
        }).start();

        new Handler().postDelayed(new Runnable() {

            public void run() {
                r.stop();
            }

        }, 10 * 1000);
    }
}
