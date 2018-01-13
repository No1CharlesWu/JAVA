package me.yokeyword.sample.demo_wechat.ui.fragment.setting;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_wechat.adapter.AlertAdapter;
import me.yokeyword.sample.demo_wechat.base.BaseBackFragment;
import me.yokeyword.sample.demo_wechat.entity.Alert;
import me.yokeyword.sample.demo_wechat.listener.OnItemClickListener;
import me.yokeyword.sample.demo_wechat.ui.fragment.CycleFragment;
import me.yokeyword.sample.demo_wechat.ui.fragment.MainFragment;
import me.yokeyword.sample.demo_wechat.ui.fragment.second.NewFeatureFragment;
import me.yokeyword.sample.demo_wechat.ui.fragment.second.ViewFragment;

public class AlertListFragment extends BaseBackFragment implements SwipeRefreshLayout.OnRefreshListener{
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;

    private  boolean mInAtTop = true;
    private  int mScrollTotal;
    private  AlertAdapter mAdapter;


    public static AlertListFragment newInstance() {
        Bundle args = new Bundle();
        AlertListFragment fragment = new AlertListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_list_fragment, container, false);
        initView(view);
        return attachToSwipeBack(view);
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        initToolbarNav(mToolbar);
        mToolbar.setTitle(R.string.alert_list);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 懒加载
        // 同级Fragment场景、ViewPager场景均适用
        mRefreshLayout.setOnRefreshListener(this);

        mRecy.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecy.setHasFixedSize(true);
        final int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        mRecy.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, space);
            }
        });

        //AlertAdapter的绑定和添加点击事件
        mAdapter = new AlertAdapter(_mActivity);
        mRecy.setAdapter(mAdapter);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder holder) {
                showPopMenu(view,position);
            }
        });
    }

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(this.getContext(),view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.openItem:
                        mAdapter.openItem(pos);
                        return true;
                    case R.id.closeItem:
                        mAdapter.closeItem(pos);
                        return true;
                    case R.id.removeItem:
                        mAdapter.removeItem(pos);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    //调试使用 自定义list的数据
    private List<Alert> initDatas() {
        List<Alert> msgList = new ArrayList<>();

        String[] type = new String[]{"单个市场预警", "市场差价预警"};
        String[] name = new String[]{"Bitfinex", "OKCoin", "OKEX", "OKEX本周", "OKEX下周", "OKEX季度"};
        double[] num = new double[]{1,2,3,4,5,6};


        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * 5);
            Alert alert = new Alert();
            alert.type = i%2;
            alert.alert_name = type[i%2];
            alert.Sma_web = name[index];
            alert.Sma_high_price = num[index];
            alert.Sma_low_price = num[index];

            alert.Msa_web_high = name[index];
            alert.Msa_web_low = name[(int)(Math.random() * 5)];
            alert.Msa_spread = num[index];
            msgList.add(alert);
        }
        return msgList;
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        // 当对用户可见时 回调
        // 不管是 父Fragment还是子Fragment 都有效！
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        // 当对用户不可见时 回调
        // 不管是 父Fragment还是子Fragment 都有效！
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
