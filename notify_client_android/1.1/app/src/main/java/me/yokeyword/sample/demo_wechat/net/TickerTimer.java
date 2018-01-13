package me.yokeyword.sample.demo_wechat.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.yokeyword.sample.demo_wechat.adapter.AlertAdapter;
import me.yokeyword.sample.demo_wechat.adapter.TickerAdapter;
import me.yokeyword.sample.demo_wechat.entity.Ticker;

import static android.app.PendingIntent.getActivity;

/**
 * Created by charles on 18/11/2017.
 */

public class TickerTimer {
    private Timer mTimer;
    private Interaction mInteraction;
    public int delay = 1000;
    public int period = 2000;
    private TickerAdapter mAdapter;
    private Context mContext;

    public TickerTimer(Context context, int delay, int period, TickerAdapter mAdapter){
        this.mContext = context;
        this.delay = delay;
        this.period = period;
        this.mAdapter = mAdapter;
        mInteraction = new Interaction();
        mTimer = new Timer();
        setTimerTask();
    }

    protected void cancel() {
        // cancel timer
        mTimer.cancel();
    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Ticker> mL = mInteraction.getData();
                if (!mL.isEmpty()){
                    //界面数据更新
                    mAdapter.setDatas(mL);
                    //设置差价提醒
                    //TODO:
                    AlertManager AM = new AlertManager(mContext, mL, (new AlertAdapter()).getAlertList());
                }
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, this.delay, this.period/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    /**
     * do some action
     */
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    // do some action
                    System.out.println("Timer 1");
                    mAdapter.mNotify();
                    break;
                default:
                    break;
            }
        }
    };

}
