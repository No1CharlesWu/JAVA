package me.yokeyword.sample.demo_wechat.net;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.yokeyword.sample.demo_wechat.entity.Alert;
import me.yokeyword.sample.demo_wechat.entity.Ticker;

/**
 * Created by charles on 03/12/2017.
 */

public class AlertManager {
    private MySharePreference service;
    private Map<String, String> params;
    public int alert_delay = 3;
    public int repeat_alert_delay = 300;
    private List<Ticker> mTList;
    private List<Alert> mAList;
    private Context mContext;

    private static List<Alert> rAList = new ArrayList<>();

    public AlertManager(){

    }

    public AlertManager(Context context, List<Ticker> T, List<Alert> A){
        this.mContext = context;
        this.mTList = T;
        this.mAList = A;

        findTriggeredAlert();
    }

    private void findTriggeredAlert(){
        service = new MySharePreference(mContext);
        params = service.getPreferences();
        alert_delay = Integer.valueOf(params.get("alert_delay"));
        repeat_alert_delay = Integer.valueOf(params.get("repeat_alert_delay"));
        long now_time = System.currentTimeMillis();
        for (Alert alert:mAList) {
            PrintAlert(alert);
            switch (alert.type){
                case 0:
                    try {
                        if ((alert.last_alerted_time == 0 || (now_time - alert.last_alerted_time)/1000 >= repeat_alert_delay) && alert.had_alert == 0 && findTicker(alert.Sma_web) != null) {
                            if ((now_time - findTicker(alert.Sma_web).ticker_time)/1000 <= alert_delay && findTicker(alert.Sma_web).ticker_last >= alert.Sma_high_price || findTicker(alert.Sma_web).ticker_last <= alert.Sma_low_price) {
//                                alert.had_alert = 1;
                                alert.last_alerted_time = now_time;
                                rAList.add(alert);
                                System.out.println("******" + alert.alert_name + alert.Sma_web + "******");
                            }
                        }
                    }catch (Exception e){
                        System.out.println(e + "******" + alert.alert_name + alert.Sma_web + "******");
                    }
                    break;
                case 1:
                    try{
                        if ((alert.last_alerted_time == 0 || (now_time - alert.last_alerted_time)/1000 >= repeat_alert_delay)
                                && alert.had_alert == 0
                                && findTicker(alert.Msa_web_high) != null
                                && findTicker(alert.Msa_web_low) != null
                                && (now_time - findTicker(alert.Msa_web_high).ticker_time)/1000 <= alert_delay
                                && (now_time - findTicker(alert.Msa_web_low).ticker_time)/1000 <= alert_delay
                                && ((findTicker(alert.Msa_web_high).ticker_last / findTicker(alert.Msa_web_low).ticker_last) - 1) * 100 >= alert.Msa_spread){
//                            alert.had_alert = 1;
                            alert.last_alerted_time = now_time;
                            rAList.add(alert);
                            System.out.println("******" + alert.alert_name + "******");
                        }
                    }catch (Exception e){
                        System.out.println(e + "******" + alert.alert_name + alert.Msa_web_high + alert.Msa_web_low +"******");
                    }
                    break;
            }
        }
        System.out.println(getTriggeredAlert().size());
    }

    private void PrintAlert(Alert alert){
        System.out.println("name: "+ alert.alert_name+" type: " + alert.type + " Sma_web: "+alert.Sma_web + " Sma_high: "+alert.Sma_high_price + " Sma_low: "+alert.Sma_low_price);
        System.out.println("name: "+ alert.alert_name+" type: " + alert.type + " Msa_web_high: "+alert.Msa_web_high + " Msa_web_low: "+alert.Msa_web_low + " Msa_spread: "+alert.Msa_spread);
    }


    private Ticker findTicker(String name){
        for (int i = 0; i < mTList.size(); i++) {
            if (mTList.get(i).ticker_name.equals(name)){
                return mTList.get(i);
            }
        }
        return null;
    }

    public List<Alert> getTriggeredAlert(){
        return rAList;
    }

    public void cleanAlert(){
        rAList.clear();
    }
}
