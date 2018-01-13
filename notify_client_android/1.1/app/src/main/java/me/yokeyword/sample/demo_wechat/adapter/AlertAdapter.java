package me.yokeyword.sample.demo_wechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.entity.Alert;
import me.yokeyword.sample.demo_wechat.entity.Ticker;
import me.yokeyword.sample.demo_wechat.listener.OnItemClickListener;
import me.yokeyword.sample.demo_wechat.ui.fragment.MainFragment;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.VH> {
    private LayoutInflater mInflater;
    private static List<Alert> mItems = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public AlertAdapter(){
    }

    public AlertAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void addAlertList(Alert item){
        mItems.add(item);
    }

    public List<Alert> getAlertList(){
        return mItems;
    }

    public void setDatas(List<Alert> beans) {
        mItems.clear();
        mItems.addAll(beans);
//        notifyDataSetChanged();
    }

    public void openItem(int pos){
        Alert item = mItems.get(pos);
        item.had_alert = 0;
        notifyDataSetChanged();
    }
    public void closeItem(int pos){
        Alert item = mItems.get(pos);
        item.had_alert = 1;
        notifyDataSetChanged();
    }
    public void removeItem(int pos){
        mItems.remove(pos);
        notifyItemRemoved(pos);
    }
    public void mNotify(){
        notifyDataSetChanged();
    }

    public void refreshMsg(Alert bean) {
        int index = mItems.indexOf(bean);
        if (index < 0) return;

        notifyItemChanged(index);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_alert, parent, false);
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
        Alert item = mItems.get(position);

        if (item.had_alert == 0){
            holder.had_alert.setText("已开启提醒");
        }
        else if (item.had_alert == 1){
            holder.had_alert.setText("已关闭提醒");
        }
        switch (item.type){
            case 0:
                holder.alert_name.setText(item.alert_name);
                holder.alert_msg.setText(item.alert_msg);
                break;
            case 1:
                holder.alert_name.setText(item.alert_name);
                holder.alert_msg.setText(item.alert_msg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public Alert getMsg(int position) {
        return mItems.get(position);
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView alert_name, alert_msg, had_alert;

        public VH(View itemView) {
            super(itemView);
            alert_name = (TextView) itemView.findViewById(R.id.alert_name);
            alert_msg = (TextView) itemView.findViewById(R.id.alert_msg);
            had_alert = (TextView) itemView.findViewById(R.id.had_alert);
        }
    }
}
