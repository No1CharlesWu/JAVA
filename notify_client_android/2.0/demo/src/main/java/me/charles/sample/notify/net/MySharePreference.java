package me.charles.sample.notify.net;

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

    public void saveNetwork(String ip, int port){
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", ip);
        editor.putInt("port", port);
        editor.commit();
    }

    public void saveFileLocation(String location){
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("location", location);
        editor.commit();
    }

    public void saveAlert(int info_s,int info_r,int warning_s,int warning_r,int error_s,int error_r, int critical_s, int critical_r) {
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("info_s", info_s);
        editor.putInt("info_r", info_r);
        editor.putInt("warning_s", warning_s);
        editor.putInt("warning_r", warning_r);
        editor.putInt("error_s", error_s);
        editor.putInt("error_r", error_r);
        editor.putInt("critical_s", critical_s);
        editor.putInt("critical_r", critical_r);
        editor.commit();
    }

    public void save(String ip, int port,int info_s,int info_r,int warning_s,int warning_r,int error_s,int error_r, int critical_s, int critical_r) {
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip", ip);
        editor.putInt("port", port);
        editor.putInt("info_s", info_s);
        editor.putInt("info_r", info_r);
        editor.putInt("warning_s", warning_s);
        editor.putInt("warning_r", warning_r);
        editor.putInt("error_s", error_s);
        editor.putInt("error_r", error_r);
        editor.putInt("critical_s", critical_s);
        editor.putInt("critical_r", critical_r);
        editor.commit();
    }

    public Map<String, String> getPreferences(){
        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences preferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        params.put("ip",preferences.getString("ip","106.14.193.60"));
        params.put("port", String.valueOf(preferences.getInt("port", 9999)));
        params.put("location",preferences.getString("location",""));
        params.put("info_s", String.valueOf(preferences.getInt("info_s", 1)));
        params.put("info_r", String.valueOf(preferences.getInt("info_r", 0)));
        params.put("warning_s", String.valueOf(preferences.getInt("warning_s", 0)));
        params.put("warning_r", String.valueOf(preferences.getInt("warning_r", 2)));
        params.put("error_s", String.valueOf(preferences.getInt("error_s", 1)));
        params.put("error_r", String.valueOf(preferences.getInt("error_r", 2)));
        params.put("critical_s", String.valueOf(preferences.getInt("critical_s", 5)));
        params.put("critical_r", String.valueOf(preferences.getInt("critical_r", 10)));
        return params;
    }
}
