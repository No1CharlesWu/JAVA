package me.charles.sample.notify.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class HistoryMsg implements Parcelable {
    public String details;
    public String grade;
    public long time;

    public HistoryMsg(){

    }

    public HistoryMsg(String details, String grade, long time) {
        this.details = details;
        this.grade = grade;
        this.time = time;
    }


    protected HistoryMsg(Parcel in) {
        details = in.readString();
        grade = in.readString();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(details);
        dest.writeString(grade);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoryMsg> CREATOR = new Creator<HistoryMsg>() {
        @Override
        public HistoryMsg createFromParcel(Parcel in) {
            return new HistoryMsg(in);
        }

        @Override
        public HistoryMsg[] newArray(int size) {
            return new HistoryMsg[size];
        }
    };
}
