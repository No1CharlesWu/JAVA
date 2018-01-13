package me.yokeyword.sample.demo_wechat.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class Alert implements Parcelable {
    public String alert_name;
    public String alert_msg;

    public static final int FIRST = 0; //单个市场预警
    public static final int SECOND = 1;//市场差价预警
    public int type = 0;
    public int had_alert = 0;//0:开启提醒，1：关闭提醒
    public long last_alerted_time = 0;

    //Sma == single_market_alert
    public String Sma_web = "";
    public double Sma_high_price = -1;
    public double Sma_low_price = -1;

    //Msa == market_spread_alert
    public String Msa_web_high = "";
    public String Msa_web_low = "";
    public double Msa_spread = -1;

    public Alert() {
    }

    protected Alert(Parcel in) {
        alert_name = in.readString();
        alert_msg = in.readString();

        type = in.readInt();
        had_alert = in.readInt();
        last_alerted_time = in.readLong();

        Sma_web = in.readString();
        Sma_high_price = in.readDouble();
        Sma_low_price = in.readDouble();

        Msa_web_high = in.readString();
        Msa_web_low = in.readString();
        Msa_spread = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alert_name);
        dest.writeString(alert_msg);

        dest.writeInt(type);
        dest.writeInt(had_alert);
        dest.writeLong(last_alerted_time);

        dest.writeString(Sma_web);
        dest.writeDouble(Sma_high_price);
        dest.writeDouble(Sma_low_price);

        dest.writeString(Msa_web_high);
        dest.writeString(Msa_web_low);
        dest.writeDouble(Msa_spread);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alert> CREATOR = new Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
}
