package me.charles.sample.notify.ui.fragment.first;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.charles.eventbusactivityscope.EventBusActivityScope;
import me.charles.fragmentation.SupportFragment;
import me.charles.sample.R;
import me.charles.sample.notify.listener.OnItemClickListener;
import me.charles.sample.notify.adapter.HistoryMsgAdapter;
import me.charles.sample.notify.base.BaseMainFragment;
import me.charles.sample.notify.entity.HistoryMsg;
import me.charles.sample.notify.event.TabSelectedEvent;
import me.charles.sample.notify.ui.fragment.EditKeywordDialogFragment;
import me.charles.sample.notify.ui.fragment.MainFragment;
import me.charles.sample.notify.net.Interaction;
import me.charles.sample.notify.ui.fragment.second.SecondTabFragment;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class FirstTabFragment extends BaseMainFragment implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, EditKeywordDialogFragment.KeywordInputListener {
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;

    private boolean mInAtTop = true;
    private int mScrollTotal;

    private HistoryMsgAdapter mAdapter;

    private Interaction mInteraction;

    private static int KEYWORD = 6666;

    public static FirstTabFragment newInstance() {

        Bundle args = new Bundle();

        FirstTabFragment fragment = new FirstTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wechat_fragment_tab_first, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        EventBusActivityScope.getDefault(_mActivity).register(this);

        mToolbar.setTitle(R.string.message_history);
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
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

        mAdapter = new HistoryMsgAdapter(_mActivity);
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
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                // 因为启动的MsgFragment是MainFragment的兄弟Fragment,所以需要MainFragment.start()

                // 也可以像使用getParentFragment()的方式,拿到父Fragment来操作 或者使用 EventBusActivityScope
              ((MainFragment) getParentFragment()).startBrotherFragment(MsgFragment.newInstance(mAdapter.getMsg(position)));
            }
        });

        List<HistoryMsg> List = initDatas();
        mAdapter.setDatas(List);
        mInteraction = new Interaction();
        MainFragment mainFragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("MainFragment");

    }

    private List<HistoryMsg> initDatas() {
        List<HistoryMsg> msgList = new ArrayList<>();

        String[] details = new String[]{"1","2","3","4", "5","6"};
        String[] grade = new String[]{"debug","info","warning", "error", "critical"};

        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * 5);
            HistoryMsg HistoryMsg = new HistoryMsg();
            HistoryMsg.details = details[i];
            HistoryMsg.grade = grade[index];
            msgList.add(HistoryMsg);
        }
        return msgList;
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.mNotify();
                mRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) return;

        if (mInAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            mAdapter.mNotify();
            scrollToTop();
        }
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_anim:
                final PopupMenu popupMenu = new PopupMenu(_mActivity, mToolbar, GravityCompat.END);
                popupMenu.inflate(R.menu.home_pop);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.clearAll:
                                showEditDialog();
                                Toast.makeText(_mActivity, R.string.clearAll, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        popupMenu.dismiss();
                        return true;
                    }
                });
                popupMenu.show();
                break;
        }
        return true;
    }

    @Override
    public void onKeywordInputComplete(int keyword, SupportFragment fragment) {
        if (keyword == KEYWORD){
            mAdapter.clearAllDatas();
        }else {
        }
        Toast.makeText(getContext(),"get"+ keyword,Toast.LENGTH_SHORT).show();

    }


    public void showEditDialog()
    {
        FragmentManager fm = getFragmentManager();
        EditKeywordDialogFragment editNameDialogFragment = EditKeywordDialogFragment.newInstance(null);
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(FirstTabFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_keyword");
    }
}
