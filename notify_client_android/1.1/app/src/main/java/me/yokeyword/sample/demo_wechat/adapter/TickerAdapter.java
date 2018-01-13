package me.yokeyword.sample.demo_wechat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.entity.Ticker;
import me.yokeyword.sample.demo_wechat.listener.OnItemClickListener;
import me.yokeyword.sample.demo_wechat.net.MySharePreference;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class TickerAdapter extends RecyclerView.Adapter<TickerAdapter.VH> {
    private MySharePreference service;
    private Map<String, String> params;
    public int delay = 5;
    private LayoutInflater mInflater;
    private static List<Ticker> mItems = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public TickerAdapter(Context context) {
        service = new MySharePreference(context);
        params = service.getPreferences();
        delay = Integer.valueOf(params.get("ticker_delay"));
        mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<Ticker> beans) {
        List<Ticker> tmp;
        tmp = diffList(beans);
        mItems.clear();
        mItems.addAll(tmp);
//        notifyDataSetChanged();
    }

    public void mNotify(){
        notifyDataSetChanged();
    }

    public void refreshMsg(Ticker bean) {
        int index = mItems.indexOf(bean);
        if (index < 0) return;

        notifyItemChanged(index);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_ticker, parent, false);
        final VH holder = new VH(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(holder.getAdapterPosition(), v, holder);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Ticker item = mItems.get(position);

        setDatas(holder, item);
    }

    private void setDatas(VH holder, Ticker item){
        DecimalFormat df = new DecimalFormat("#.00");

        holder.tickername.setText(item.ticker_name);
        holder.tickerbuy.setText("买一价" + df.format(item.ticker_buy));
        holder.tickersell.setText("卖一价" + df.format(item.ticker_sell));

        holder.tickervolume.setText("量 " + df.format(item.ticker_volume/10000) + "万");
        holder.tickerlast.setText("最新 ¥ " + df.format(item.ticker_last));

        item.time_offset = (System.currentTimeMillis() - item.ticker_time) / 1000;
        holder.tickertime.setText("更新于约" + item.time_offset +"秒之前：");

        params = service.getPreferences();
        delay = Integer.valueOf(params.get("ticker_delay"));
        if (item.time_offset <= delay){
            holder.tickername.setTextColor(Color.GREEN);
        }
        else{
            holder.tickername.setTextColor(Color.RED);
        }

//        holder.tickerhigh.setText("高 " + String.valueOf(item.ticker_high));
//        holder.tickerlow.setText("低 " + String.valueOf(item.ticker_low));
    }

    private List<Ticker> diffList(List<Ticker> list){
        List<Ticker> tmp = new ArrayList<>();
        for (Ticker ntmp:list){
            Ticker mtmp = findTicker(ntmp.ticker_name);
            if (isSameTicker(mtmp, ntmp)){
                tmp.add(mtmp);
            }
            else{
               tmp.add(ntmp);
            }
        }
        return tmp;
    }

    private Ticker findTicker(String name){
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).ticker_name.equals(name)){
                return mItems.get(i);
            }
        }
        return null;
    }

    private boolean isSameTicker(Ticker m, Ticker n){
        if (m == null){
            return false;
        }
        System.out.println(m.ticker_name);

        if (m.ticker_name.equals(n.ticker_name) &&
                m.ticker_volume == n.ticker_volume &&
                m.ticker_last ==n.ticker_last &&
                m.ticker_buy == n.ticker_buy &&
                m.ticker_sell == n.ticker_sell)
            return true;
        else
            return false;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public Ticker getMsg(int position) {
        return mItems.get(position);
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView tickername, tickervolume, tickerlast, tickerhigh, tickerlow,tickerbuy,tickersell,tickertime;

        public VH(View itemView) {
            super(itemView);
            tickername = (TextView) itemView.findViewById(R.id.ticker_name);
            tickervolume = (TextView) itemView.findViewById(R.id.ticker_volume);
            tickerlast = (TextView) itemView.findViewById(R.id.ticker_last);
            tickerbuy = (TextView) itemView.findViewById(R.id.ticker_buy);
            tickersell = (TextView) itemView.findViewById(R.id.ticker_sell);
            tickertime = (TextView) itemView.findViewById(R.id.ticker_time);
//            tickerhigh = (TextView) itemView.findViewById(R.id.ticker_high);
//            tickerlow = (TextView) itemView.findViewById(R.id.ticker_low);
        }
    }
}
