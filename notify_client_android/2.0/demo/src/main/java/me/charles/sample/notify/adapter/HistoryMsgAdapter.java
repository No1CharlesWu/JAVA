package me.charles.sample.notify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.charles.sample.R;
import me.charles.sample.notify.entity.HistoryMsg;
import me.charles.sample.notify.listener.OnItemClickListener;
import me.charles.sample.notify.net.MySharePreference;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class HistoryMsgAdapter extends RecyclerView.Adapter<HistoryMsgAdapter.VH> {
    private MySharePreference service;
    private Map<String, String> params;
    private LayoutInflater mInflater;
    private static List<HistoryMsg> mItems = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public HistoryMsgAdapter(){
    }

    public HistoryMsgAdapter(Context context) {
        service = new MySharePreference(context);
        params = service.getPreferences();
        mInflater = LayoutInflater.from(context);
    }

    public void addData(String details, String grade, long time){
        HistoryMsg tmp = new HistoryMsg(details, grade, time);
        System.out.println(details + grade + time);
        this.addData(tmp);
    }

    public void addData(HistoryMsg item){
        mItems.add(0, item);
    }

    public void clearAllDatas(){
        mItems.clear();
        notifyDataSetChanged();
    }

    public void setDatas(List<HistoryMsg> beans) {
        mItems.clear();
        mItems.addAll(beans);
//        notifyDataSetChanged();
    }

    public void mNotify(){
        notifyDataSetChanged();
    }

    public void refreshMsg(HistoryMsg bean) {
        int index = mItems.indexOf(bean);
        if (index < 0) return;

        notifyItemChanged(index);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_historymsg, parent, false);
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
        HistoryMsg item = mItems.get(position);

        setDatas(holder, item);
    }

    private void setDatas(VH holder, HistoryMsg item){
        holder.details.setText(item.details);
        holder.grade.setText(item.grade);
        holder.time.setText("" + item.time);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public HistoryMsg getMsg(int position) {
        return mItems.get(position);
    }

    class VH extends RecyclerView.ViewHolder {
        private TextView details, grade, time;

        public VH(View itemView) {
            super(itemView);
            details = (TextView) itemView.findViewById(R.id.details);
            grade = (TextView) itemView.findViewById(R.id.grade);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
