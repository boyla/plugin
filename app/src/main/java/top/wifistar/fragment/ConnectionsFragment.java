package top.wifistar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import top.wifistar.R;
import top.wifistar.adapter.FollowAdapter;
import top.wifistar.bean.bmob.Follow;
import top.wifistar.bean.bmob.User;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.FollowRealm;
import top.wifistar.utils.Utils;
import top.wifistar.view.OnTouchXRecyclerView;
import top.wifistar.view.ProgressCombineView;
import top.wifistar.view.RecyclerViewDivider;

/**
 * Created by hasee on 2017/4/8.
 */

public class ConnectionsFragment extends BaseFragment {

    ProgressCombineView progressCombineView;

    private OnTouchXRecyclerView xRecyclerView;

    private Button mEmptyBrowseMatches;

    private FollowAdapter mAdapter;

    private List<Follow> follows = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventUtils.registerEventBus(this);
//    }

    @Override
    protected void viewCreated(View view, Bundle savedInstanceState) {
        progressCombineView = bindViewById(R.id.progressCombineView);
        xRecyclerView = bindViewById(R.id.xRecyclerView);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotatePulse);
        xRecyclerView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mEmptyBrowseMatches = bindViewById(R.id.empty_message_browsematches);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new RecyclerViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, R.color.list_divider_default_color));
//        xRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        mAdapter = new FollowAdapter(getActivity(), follows);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadFollowsData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        xRecyclerView.refresh();
    }

    String userId;

    private void loadFollowsData() {
        User currentUser = Utils.getCurrentShortUser();
        if (currentUser != null && !TextUtils.isEmpty(currentUser.getObjectId())) {
            userId = currentUser.getObjectId();
            //load by local
            RealmResults<FollowRealm> dbData = BaseRealmDao.realm.where(FollowRealm.class).contains("followers", userId).findAll();
            if (dbData.isLoaded()) {
                // 完成查询
                if (!dbData.isEmpty()) {
                    follows.clear();
                    for (FollowRealm it : dbData) {
                        if (it.followState > 0) {
                            follows.add(it.toBmobObject());
                        }
                    }
                    showData();
                }
            }
            //update from net
//            if (NetUtils.isNetworkAvailable(getContext())) {
//                BmobQuery query = new BmobQuery<Follow>().addWhereContains("followers", userId);
//                query.order("followState").findObjects(new FindListener<Follow>() {
//                    @Override
//                    public void done(List<Follow> res, BmobException e) {
//                        if (e == null) {
//                            follows.clear();
//                            for (Follow it : res) {
//                                if (it.followState > 0) {
//                                    FollowRealm fr = it.toRealmObject();
//                                    follows.add(it);
//                                    BaseRealmDao.insertOrUpdate(fr);
//                                } else {
//                                    FollowRealm fr = BaseRealmDao.realm.where(FollowRealm.class).equalTo("objectId", it.getObjectId()).findFirst();
//                                    if(fr!=null){
//                                        BaseRealmDao.realm.executeTransaction(realm -> fr.deleteFromRealm());
//                                    }
//                                }
//                            }
//                        }
//                        showData();
//                    }
//                });
//            }

        }
    }

    private void showData() {
        if (follows.size() > 0) {
            progressCombineView.showContent();
            mAdapter.notifyDataSetChanged();
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.refreshComplete();
            progressCombineView.showCustom();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //相当于Fragment的onResume，为true时，Fragment已经可见
            loadFollowsData();
        } else {
            //相当于Fragment的onPause，为false时，Fragment不可见
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFollowsData();
    }
}
