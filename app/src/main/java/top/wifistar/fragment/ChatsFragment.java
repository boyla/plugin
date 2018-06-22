package top.wifistar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import top.wifistar.R;
import top.wifistar.activity.HomeActivity;
import top.wifistar.adapter.IMUsersAdapter;
import top.wifistar.bean.bmob.User;
import top.wifistar.customview.OnTouchXRecyclerView;
import top.wifistar.customview.ProgressCombineView;
import top.wifistar.customview.RecyclerViewDivider;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.event.EurekaEvent;
import top.wifistar.httpserver.NetUtils;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.utils.EventUtils;

import static top.wifistar.httpserver.NetUtils.usersInWiFi;

/**
 * Created by hasee on 2017/4/8.
 */

public class ChatsFragment extends BaseFragment {


    ProgressCombineView progressCombineView;

    private OnTouchXRecyclerView xRecyclerView;

    private Button mEmptyBrowseMatches;

    private IMUsersAdapter mAdapter;

    private List<IMUserRealm> datas = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtils.registerEventBus(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    protected void viewCreated(View view, Bundle savedInstanceState) {
        progressCombineView = bindViewById(R.id.progressCombineView);
        xRecyclerView = bindViewById(R.id.xRecyclerView);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotatePulse);
        xRecyclerView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        xRecyclerView.onTouchListener = () -> ((HomeActivity) getActivity()).showBottomInput(View.GONE);

        mEmptyBrowseMatches = bindViewById(R.id.empty_message_browsematches);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new RecyclerViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, R.color.list_divider_default_color));
//        xRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        mAdapter = new IMUsersAdapter(getActivity(), datas);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                loadConversationsData();
            }

            @Override
            public void onLoadMore() {

            }
        });
        xRecyclerView.refresh();
        mEmptyBrowseMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventUtils.post(new BottomMenuItemClickEvent(6));
            }
        });

    }


    public void updateResultPage() {
        if (mAdapter.getItemCount() == 0) {
            progressCombineView.showCustom();
            xRecyclerView.setVisibility(View.GONE);
        } else {
            progressCombineView.showContent();
            xRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    //    @Override
    public void longClickItem(@NonNull final IMUserRealm item, final int position) {
//        new CustomAlertDialog(getActivity()).builder()
//                .setMsg(R.string.Message_Confirm_Deletion)
//                .setPositiveButton(R.string.Delete, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mPresenter.deleteIMUsers(item.getUserid(), item.getUsername());
//                        mAdapter.notifyDataSetChanged();
//                        updateResultPage();
//                        App.getInstance().cache_noticeBean.setMessage_count(0);
//                        NoticeEvent messageCount = new NoticeEvent();
//                        messageCount.messagePoint = 0;
//                        EventBus.getDefault().post(messageCount);
//                    }
//                })
//                .setNegativeButton(R.string.Cancel, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                }).show();
    }


    public void clickItem(@NonNull IMUserRealm item, int position) {
//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(IntentExtraDataKeyConfig.EXTRA_MESSAGE_USER_BEAN, Parcels.wrap(IMUserRealm.class, item));
//        bundle.putBoolean(ChatActivity.WILLSHOWIME, false);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    @Override
    public void onDetach() {
        EventUtils.unregisterEventBus(this);
        //mPresenter.closeRealm();
        super.onDetach();
    }

    @Subscribe
    public void refreshReddot(Object event) {
//        mPresenter.loadIMUsers(RefreshType.REFRESH);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkNetState(Object event) {
//        if (event.networkAvailable) {
//            topReminder.dismiss();
//            mPresenter.reLogin();
//        } else {
//            topReminder.show(getString(R.string.network_unavailable), false, true);
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLoginIM(Object imLoginStateEvent) {
//        /** receive the block message **/
//        if (topReminder == null) return;
//        if (imLoginStateEvent.isSuccess) {
//            topReminder.dismiss();
//        } else {
//            topReminder.show(getString(R.string.network_unavailable), false, true);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgReceived(IMUserRealm event) {
        refreshConversations();
//        if (xRecyclerView != null) {
//            xRecyclerView.scrollToPosition(View.SCROLL_INDICATOR_TOP);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewGuy(EurekaEvent event) {
        User user = event.user;
        if (user == null) {
            return;
        }
        addUser(user);
        mAdapter.notifyDataSetChanged();
//        if (xRecyclerView != null) {
//            xRecyclerView.scrollToPosition(View.SCROLL_INDICATOR_TOP);
//        }
    }

    private void addUser(User user) {
        boolean found = false;
        for (IMUserRealm item : datas) {
            if (item.objectId.equals(user.getObjectId())) {
                found = true;
                break;
            }
        }
        if (!found) {
            IMUserRealm realm = user.toIMRealm();
            realm.updateTime = System.currentTimeMillis();
            realm.lastMsg = "TA在你身边";
            datas.add(0,realm);
        }
    }

    private void loadConversationsData() {
        datas.clear();
        RealmResults<IMUserRealm> dbData = (RealmResults<IMUserRealm>) BaseRealmDao.findAll(IMUserRealm.class, "updateTime");
        if (!dbData.isEmpty()) {
            if (dbData.isLoaded()) {
                List<IMUserRealm> tempUnReadData = new ArrayList<>();
                for (IMUserRealm item : dbData) {
                    if (item.isInConversation) {
                        if (item.unReadNum > 0) {
                            tempUnReadData.add(item);
                        } else {
                            datas.add(item);
                        }
                    }
                }
                if (tempUnReadData.size() > 0) {
                    datas.addAll(0, tempUnReadData);
                }
            }
        }

        for(User item: NetUtils.usersInWiFi){
            addUser(item);
        }
        if(datas.size()>0){
            progressCombineView.showContent();
            mAdapter.notifyDataSetChanged();
            xRecyclerView.refreshComplete();
        }else{
            xRecyclerView.refreshComplete();
            progressCombineView.showCustom();
        }
    }

    private void refreshConversations() {
        datas.clear();
        loadConversationsData();
        mAdapter.notifyDataSetChanged();
    }
}
