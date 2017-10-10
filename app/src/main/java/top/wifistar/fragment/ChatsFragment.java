package top.wifistar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.realm.RealmResults;
import top.wifistar.R;
import top.wifistar.adapter.BaseRealmAdapter;
import top.wifistar.adapter.IMUsersAdapter;
import top.wifistar.bean.IMUserRealm;
import top.wifistar.customview.ProgressCombineView;
import top.wifistar.customview.RecyclerViewDivider;
import top.wifistar.customview.TopReminder;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.utils.EventUtils;

/**
 * Created by hasee on 2017/4/8.
 */

public class ChatsFragment extends BaseFragment{


    ProgressCombineView progressCombineView;

    private XRecyclerView xRecyclerView;

    private Button mEmptyBrowseMatches;

    private IMUsersAdapter mAdapter;

    private RealmResults<IMUserRealm> datas;

    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtils.registerEventBus(this);
        // Create the presenter
//        new IMUsersPresenter(
//                new MessageDao(),
//                this);

    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    protected void viewCreated(View view, Bundle savedInstanceState) {
        progressCombineView = bindViewById(R.id.progressCombineView);
        xRecyclerView = bindViewById(R.id.xRecyclerView);
        mEmptyBrowseMatches = bindViewById(R.id.empty_message_browsematches);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.addItemDecoration(new RecyclerViewDivider(
                mContext, LinearLayoutManager.HORIZONTAL, R.color.list_divider_default_color));
//        xRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);

//        topReminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                topReminder.dismiss();
// //               mPresenter.reLogin();
//
//            }
//        });
        /** init recyclerView */
        //datas = mPresenter.loadIMUsers();
        mAdapter = new IMUsersAdapter(getActivity(), datas);
        mAdapter.setOnItemClickListener(new BaseRealmAdapter.OnItemClickListener<IMUserRealm>() {
            @Override
            public void onItemClick(View view, IMUserRealm item, int position) {
                clickItem(item, position);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseRealmAdapter.OnItemLongClickListener<IMUserRealm>() {
            @Override
            public void onItemLongClick(View view, final IMUserRealm item, int position) {
                longClickItem(item, position);
            }
        });
        xRecyclerView.setAdapter(mAdapter);
       
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
    public void onMsgReceived(Object event) {

        mAdapter.notifyDataSetChanged();
        if (xRecyclerView != null) {
            xRecyclerView.scrollToPosition(View.SCROLL_INDICATOR_TOP);
        }
    }

}
