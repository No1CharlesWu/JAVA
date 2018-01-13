package me.yokeyword.sample.demo_wechat.net;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charles on 2017/12/14 0014.
 */

public class MySharePreference {
    private Context context;

    public MySharePreference(Context context) {
        this.context = context;
    }

    public void save(int ticker_delay, int alert_delay, int repeat_alert_delay) {
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ticker_delay", ticker_delay);
        editor.putInt("alert_delay", alert_delay);
        editor.putInt("repeat_alert_delay", repeat_alert_delay);
        editor.commit();
    }

    public Map<String, String> getPreferences(){
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        params.put("ticker_delay", String.valueOf(preferences.getInt("ticker_delay", 5)));
        params.put("alert_delay", String.valueOf(preferences.getInt("alert_delay", 3)));
        params.put("repeat_alert_delay", String.valueOf(preferences.getInt("repeat_alert_delay", 300)));
        return params;
    }
}
