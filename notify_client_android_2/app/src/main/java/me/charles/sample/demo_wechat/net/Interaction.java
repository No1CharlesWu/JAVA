package me.charles.sample.demo_wechat.net;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.charles.sample.demo_wechat.entity.Ticker;

/**
 * Created by charles on 17/11/2017.
 */

public class Interaction {
    public String url = "106.14.193.60";
    public String urlPath = "http://" + url + "/test.json";

    public Interaction(){}

    public String getJsonData() {
        //1:确定地址
        String path = urlPath;
        try {
            URL url = new URL(path);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("GET");
            //设置请求超时时间
            conn.setConnectTimeout(5000);
            //设置读取超时时间
            conn.setReadTimeout(5000);
            //判断是否获取成功
            if (conn.getResponseCode() == 200) {
                InputStream is;
                //获得输入流
                is = conn.getInputStream();

                return parseResponseInfo(is);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @Nullable
    private String parseResponseInfo(InputStream is) throws JSONException {
        String jsonStr = "";
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024000];
            int len = 0;
            while ((len = is.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            is.close();
            jsonStr = new String(outStream.toByteArray());
            System.out.println(jsonStr);
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Ticker> parseJson(String jsonStr) throws JSONException{
        List<Ticker> TList = new ArrayList<>();
        if (jsonStr.isEmpty()) {
            return TList;
        }
        JSONArray jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Ticker tmp = new Ticker();
            tmp.ticker_name = parseName(jsonObject.getString("type"));
            tmp.ticker_last = jsonObject.getDouble("last");
            tmp.ticker_high = jsonObject.getDouble("high");
            tmp.ticker_low = jsonObject.getDouble("low");
            tmp.ticker_buy = jsonObject.getDouble("buy");
            tmp.ticker_sell = jsonObject.getDouble("sell");
            tmp.ticker_volume = jsonObject.getDouble("vol");
            tmp.ticker_time = jsonObject.getLong("timestamp");
            TList.add(tmp);
        }
        return TList;
    }

    private String parseName(String type){
        String[] name = new String[]{"Bitfinex", "OKCoin", "OKEX", "OKEX本周", "OKEX下周", "OKEX季度","BitMex永续","BitMexZ17"};
        if (type.equals("bitfinexcom_rest_btc_ticker")){
            return name[0];
        } else if (type.equals("okcoincom_rest_btc_ticker")){
            return name[1];
        }else if (type.equals("okexcom_rest_btc_ticker")){
            return name[2];
        }else if (type.equals("okexcom_rest_btc_future_ticker_this_week")){
            return name[3];
        }else if (type.equals("okexcom_rest_btc_future_ticker_next_week")){
            return name[4];
        }else if (type.equals("okexcom_rest_btc_future_ticker_quarter")){
            return name[5];
        }else if (type.equals("bitmexcom_rest_btc_xbtusd")){
            return name[6];
        }else if (type.equals("bitmexcom_rest_btc_xbtz17")){
            return name[7];
        }
        else {
            return "未知";
        }
    }


    public List<Ticker> getData(){
        List<Ticker> tmp = new ArrayList<>();
        try {
            String jsonStr = getJsonData();
            tmp = parseJson(jsonStr);
        } catch (Exception e){
            e.printStackTrace();
        }
        return tmp;
    }
}
