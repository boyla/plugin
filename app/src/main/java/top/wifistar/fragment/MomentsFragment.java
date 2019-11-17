package top.wifistar.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import top.wifistar.R;
import top.wifistar.activity.HomeActivity;
import top.wifistar.activity.PublishMomentActivity;
import top.wifistar.activity.UserMomentsActivity;
import top.wifistar.activity.mvp.contract.MomentsContract;
import top.wifistar.activity.mvp.presenter.MomentsPresenter;
import top.wifistar.adapter.MomentAdapter;
import top.wifistar.app.App;
import top.wifistar.app.BottomInputActivity;
import top.wifistar.bean.bmob.Comment;
import top.wifistar.bean.bmob.User;
import top.wifistar.bean.bmob.Moment;
import top.wifistar.bean.bmob.CommentConfig;
import top.wifistar.view.CommentListView;
import top.wifistar.view.OnTouchXRecyclerView;
import top.wifistar.view.ProgressCombineView;
import top.wifistar.view.TitleBar;
import top.wifistar.dialog.UpLoadDialog;
import top.wifistar.event.PublishMomentEvent;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.CommentRealm;
import top.wifistar.utils.CommonUtils;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.RealmUtils;
import top.wifistar.utils.Utils;

import static top.wifistar.activity.mvp.presenter.MomentsPresenter.NO_MORE_DATA;

/**
 * Created by hasee on 2017/4/8.
 */

public class MomentsFragment extends BaseFragment implements MomentsContract.View {

    //TextView tvNoData;

    MomentAdapter momentAdapter;

    protected static final String TAG = "Moments:  ";

    private View edittextbody;
    private EditText editText;
    private ImageView sendIv;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private MomentsPresenter presenter;
    private CommentConfig commentConfig;
    private RelativeLayout bodyLayout;
    private LinearLayoutManager layoutManager;
    private TitleBar titleBar;
    private BottomInputActivity activity;
    public static MomentsFragment momentsFragment;

    public final static int TYPE_PULLDOWNREFRESH = 1;
    public final static int TYPE_PULLUPMORE = 2;
    private UpLoadDialog uploadDialog;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    public OnTouchXRecyclerView recyclerView;
    ProgressCombineView progressCombineView;
    public String selectMomentId;
    public User replyUser;
    User currentUser;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    protected void viewCreated(View view, Bundle savedInstanceState) {
        //tvNoData = bindViewById(R.id.tvNoData);
        recyclerView = bindViewById(R.id.xRecyclerView);
        progressCombineView = bindViewById(R.id.progressCombineView);
        edittextbody = getActivity().findViewById(R.id.editTextBodyLl);
        if (getActivity() instanceof BottomInputActivity) {
            activity = (BottomInputActivity) getActivity();
            editText = activity.getBottomEditText();
            sendIv = activity.getBottomImageView();
        }
        presenter = new MomentsPresenter(this);
        momentAdapter = new MomentAdapter(getActivity(), ((BottomInputActivity) getActivity()).getSharedViewListener(), currentUser, this, topic);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotatePulse);
        recyclerView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        recyclerView.onTouchListener = () -> activity.showBottomInput(View.GONE);
        momentAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(momentAdapter);
        //优化更新item时的闪烁
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //recyclerView.getItemAnimator().setChangeDuration(0);
        setXRecyclerView();
        recyclerView.refresh();
        momentsFragment = this;

        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    //发布评论
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Utils.makeSysToast("评论内容不能为空...");
                        return;
                    }
                    activity.showBottomInput(View.GONE);
                    if (TextUtils.isEmpty(selectMomentId)) {
                        return;
                    }
                    presenter.addComment(content, selectMomentId, replyUser);
                    replyUser = null;
                }
                updateEditTextBodyVisible(View.GONE, null);
            }
        });
    }

    private void setXRecyclerView() {
        progressCombineView.showContent();
        recyclerView.setPullRefreshEnabled(true);

        recyclerView.setFocusable(false);
        recyclerView.setFocusableInTouchMode(false);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (mContext instanceof HomeActivity) {
                    ((HomeActivity) mContext).reSetAvatarList();
                }
                presenter.loadData(TYPE_PULLDOWNREFRESH, currentUser == null ? null : currentUser.getObjectId(), topic);
            }

            @Override
            public void onLoadMore() {
                if (NO_MORE_DATA) {
                    recyclerView.loadMoreComplete();
                } else {
                    presenter.loadData(TYPE_PULLUPMORE, currentUser == null ? null : currentUser.getObjectId(), topic);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (getActivity() instanceof HomeActivity) {
            inflater.inflate(R.menu.add, menu);
        }
        if (getActivity() instanceof UserMomentsActivity && !TextUtils.isEmpty(topic)) {
            inflater.inflate(R.menu.edit_topic, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish) {
            Intent intent = new Intent(getActivity(), PublishMomentActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.edit) {
            //TODO edit topic, topic是本人创建
        }
        return super.onOptionsItemSelected(item);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void update2DeleteMoment(String momentId, int position) {
        List<Moment> moments = momentAdapter.getDatas();
        for (int i = 0; i < moments.size(); i++) {
            if (momentId.equals(moments.get(i).getObjectId())) {
                Moment moment = moments.remove(i);
                RealmUtils.deleteFromRealmByObj(moment.toRealmObject());
                momentAdapter.notifyItemRemoved(position);
                if (position != moments.size()) {
                    momentAdapter.notifyItemRangeChanged(position, moments.size() - position);
                }
                if (!TextUtils.isEmpty(moment.getPhotos())) {
                    String[] urls = moment.getPhotos().split(",");
                    for (int index = 0; index < urls.length; index++) {
                        urls[index] = urls[index].split("_wh_")[0];
                    }
                    BmobFile.deleteBatch(urls, new DeleteBatchListener() {

                        @Override
                        public void done(String[] failUrls, BmobException e) {
                            if (e == null) {
                                Log.i("删除moment关联图片：", "全部删除成功");
                            } else {
                                if (failUrls != null) {
                                    Log.i("删除moment关联图片失败个数：", failUrls.length + "," + e.toString());
                                } else {
                                    Log.i("全部文件删除失败：", e.getErrorCode() + "," + e.toString());
                                }
                            }
                        }
                    });
                }
                //momentAdapter.notifyDataSetChanged();
                if (momentAdapter.getDatas().size() == 0) {
                    progressCombineView.showEmpty(null, "", "没有动态数据，认识更多朋友吧");
                }
                return;
            }
        }

    }

    @Override
    public void update2AddFavorite(int dataPosition) {
        User addItem = new User(App.currentUserProfile.getNickName(), App.currentUserProfile.getAvatar());
        addItem.id = App.currentUserProfile.getObjectId();
        if (addItem != null) {
            Moment item = (Moment) momentAdapter.getDatas().get(dataPosition);
            item.likes.add(0, addItem);
            if (item.likes.size() == 1) {
                momentAdapter.notifyDataSetChanged();
            } else {
                momentAdapter.notifyItemChanged(dataPosition + 2, "praise");
            }
        }
    }

    @Override
    public void update2DeleteFavort(int dataPosition) {
        String myProfileId = App.currentUserProfile.getObjectId();
        Moment item = (Moment) momentAdapter.getDatas().get(dataPosition);
        for (int i = 0; i < item.likes.size(); i++) {
            if (myProfileId.equals(item.likes.get(i).id)) {
                item.likes.remove(i);
                if (item.likes.size() == 0) {
                    momentAdapter.notifyDataSetChanged();
                } else {
                    momentAdapter.notifyItemChanged(dataPosition + 2, "praise");
                }
                return;
            }
        }
    }

    @Override
    public void update2AddComment(Comment comment) {
        if (comment != null && !TextUtils.isEmpty(comment.getMomentId())) {
            for (Moment item : ((List<Moment>) momentAdapter.getDatas())) {
                if (comment.getMomentId().equals(item.getObjectId())) {
                    item.getComments().add(comment);
                    momentAdapter.notifyDataSetChanged();
                    break;
                }
            }
            //momentAdapter.notifyItemChanged(circlePosition+1);
        }
        //清空评论文本
        editText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        Moment item = (Moment) momentAdapter.getDatas().get(circlePosition);
        List<Comment> items = item.getComments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId.equals(items.get(i).getObjectId())) {
                Comment commentToRemove = items.remove(i);
                momentAdapter.notifyDataSetChanged();
                RealmUtils.deleteFromRealmByObjId(CommentRealm.class, commentId);
                //momentAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        activity.showBottomInput(visibility);
        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            editText.clearFocus();
            CommonUtils.hideSoftInput(editText.getContext(), editText);
        }
    }

    @Override
    public void update2loadData(int loadType, List<Moment> datas) {
        if (datas != null) {
            for (Moment m : datas) {
                Utils.updateUser(m.user);
            }
        }
        if (loadType == TYPE_PULLDOWNREFRESH) {
            momentAdapter.setDatas(datas);
            updateDataList(loadType);
        } else if (loadType == TYPE_PULLUPMORE) {
            if (datas == null || datas.size() == 0) {
                NO_MORE_DATA = true;
                Utils.showToast("所有动态已加载完毕");
                recyclerView.loadMoreComplete();
            } else {
                momentAdapter.getDatas().addAll(datas);
                updateDataList(loadType);
            }
        }
    }

    @Override
    public void setCurrentMomentId(String momentId, User replyUserId) {
        selectMomentId = momentId;
        this.replyUser = replyUserId;
    }

    private void updateDataList(int loadType) {
        if (momentAdapter.getDatas().size() == 0) {
            progressCombineView.showEmpty(null, "", "没有动态数据，认识更多朋友吧");
        } else {
            momentAdapter.notifyDataSetChanged();
            App.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loadType == TYPE_PULLDOWNREFRESH) {
                        recyclerView.refreshComplete();
                    } else if (loadType == TYPE_PULLUPMORE) {
                        recyclerView.loadMoreComplete();
                    }
                }
            }, 888);
        }
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + MomentAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        } else {
            return;
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - titleBar.getHeight();
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    private void setViewTreeObserver() {
        bodyLayout = (RelativeLayout) getActivity().findViewById(R.id.rlContent);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = Utils.getStatusBarHeight(getActivity());//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }
                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                if (layoutManager != null && commentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + MomentAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtils.registerEventBus(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.unregisterEventBus(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    Moment temp;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMoment(PublishMomentEvent event) {
        if (event.isTempEvent) {
            if (momentAdapter.getDatas().size() == 0) {
                progressCombineView.showContent();
                recyclerView.refreshComplete();
            }
            momentAdapter.getDatas().add(0, event.moment);
            momentAdapter.notifyItemInserted(1);
        } else {
            ((Moment) momentAdapter.getDatas().get(0)).setObjectId(event.moment.getObjectId());
            ((Moment) momentAdapter.getDatas().get(0)).setPhotos(event.moment.getPhotos());
            BaseRealmDao.insertOrUpdate(((Moment) momentAdapter.getDatas().get(0)).toRealmObject());
        }
    }

    public void setUser(User user) {
        currentUser = user;
    }

    String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
