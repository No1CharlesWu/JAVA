package me.yokeyword.sample.demo_wechat.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class Ticker implements Parcelable {
    public String ticker_name;
    public double ticker_volume;
    public double ticker_last;
    public double ticker_high;
    public double ticker_low;

    public double ticker_buy;
    public double ticker_sell;
    public long ticker_time;

    public long time_offset;
    
    public Ticker() {
    }


    protected Ticker(Parcel in) {
        ticker_name = in.readString();
        ticker_volume = in.readDouble();
        ticker_last = in.readDouble();
        ticker_high = in.readDouble();
        ticker_low = in.readDouble();

        ticker_buy = in.readDouble();
        ticker_sell = in.readDouble();
        ticker_time = in.readLong();

        time_offset = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticker_name);
        dest.writeDouble(ticker_volume);
        dest.writeDouble(ticker_last);
        dest.writeDouble(ticker_high);
        dest.writeDouble(ticker_low);

        dest.writeDouble(ticker_buy);
        dest.writeDouble(ticker_sell);
        dest.writeLong(ticker_time);

        dest.writeLong(time_offset);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ticker> CREATOR = new Creator<Ticker>() {
        @Override
        public Ticker createFromParcel(Parcel in) {
            return new Ticker(in);
        }

        @Override
        public Ticker[] newArray(int size) {
            return new Ticker[size];
        }
    };
}
