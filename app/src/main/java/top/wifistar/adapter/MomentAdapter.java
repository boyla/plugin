package top.wifistar.adapter;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.realm.RealmResults;
import top.wifistar.R;
import top.wifistar.activity.HomeActivity;
import top.wifistar.activity.ImagePagerActivity;
import top.wifistar.activity.mvp.presenter.MomentsPresenter;
import top.wifistar.adapter.viewholder.MomentViewHolder;
import top.wifistar.adapter.viewholder.ImageViewHolder;
import top.wifistar.adapter.viewholder.TextViewHolder;
import top.wifistar.adapter.viewholder.URLViewHolder;
import top.wifistar.adapter.viewholder.VideoViewHolder;
import top.wifistar.app.App;
import top.wifistar.app.BottomInputActivity;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.bean.bmob.ActionItem;
import top.wifistar.bean.bmob.Moment;
import top.wifistar.bean.bmob.CommentConfig;
import top.wifistar.bean.bmob.Comment;
//import top.wifistar.bean.demo.Favor;
import top.wifistar.bean.bmob.User;
import top.wifistar.chain.user.NetUserRequest;
import top.wifistar.customview.CircleVideoView;
import top.wifistar.customview.CommentListView;
import top.wifistar.customview.PraiseListView;
import top.wifistar.customview.SnsPopupWindow;
import top.wifistar.dialog.CommentDialog;
import top.wifistar.fragment.MomentsFragment;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.MomentRealm;
import top.wifistar.realm.UserRealm;
import top.wifistar.utils.UrlUtils;
import top.wifistar.utils.Utils;


public class MomentAdapter extends BaseRecycleViewAdapter {

    public final static int TYPE_HEAD = 0;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 1;

    int curPlayIndex = -1;

    private MomentsPresenter presenter;
    private Context context;
    private HomeActivity.OnSharedViewListener sharedViewListener;
    public User user;
    public MomentsFragment momentsFragment;

    public void setCirclePresenter(MomentsPresenter presenter) {
        this.presenter = presenter;
    }

    public MomentAdapter(Context context) {
        this.context = context;
    }


    public MomentAdapter(Context context, HomeActivity.OnSharedViewListener sharedViewListener,User user,MomentsFragment momentsFragment) {
        this.context = context;
        this.sharedViewListener = sharedViewListener;
        this.user = user;
        this.momentsFragment = momentsFragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        int itemType = 0;
        Moment item = (Moment) datas.get(position - 1);
        if (Moment.TYPE_URL.equals(item.getType())) {
            itemType = MomentViewHolder.TYPE_URL;
        } else if (Moment.TYPE_IMG.equals(item.getType())) {
            itemType = MomentViewHolder.TYPE_IMAGE;
        } else if (Moment.TYPE_VIDEO.equals(item.getType())) {
            itemType = MomentViewHolder.TYPE_VIDEO;
        } else if (Moment.TYPE_TEXT.equals(item.getType())) {
            itemType = MomentViewHolder.TYPE_TEXT;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_circle, parent, false);
            viewHolder = new HeaderViewHolder(headView);
            TextView tvName = headView.findViewById(R.id.tvName);
            ImageView ivBg = headView.findViewById(R.id.ivBg);
            User temp;
            if(user==null){
                temp = Utils.getCurrentShortUser();
            }else{
                temp = user;
            }
            tvName.setText(temp.getName());
            if (TextUtils.isEmpty(temp.headBgUrl)) {
                ivBg.setImageResource(R.drawable.splash);
            } else {
                ivBg.setImageResource(R.color.darkgray);
                Glide.with(context).load(temp.headBgUrl.split("_wh_")[0]).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivBg);
            }
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moment, parent, false);
            if (viewType == MomentViewHolder.TYPE_URL) {
                viewHolder = new URLViewHolder(view);
            } else if (viewType == MomentViewHolder.TYPE_IMAGE) {
                viewHolder = new ImageViewHolder(view);
            } else if (viewType == MomentViewHolder.TYPE_VIDEO) {
                viewHolder = new VideoViewHolder(view);
            } else if (viewType == MomentViewHolder.TYPE_TEXT) {
                viewHolder = new TextViewHolder(view);
            }
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        } else {
            final int dataPosition = position - HEADVIEW_SIZE;
            final MomentViewHolder holder = (MomentViewHolder) viewHolder;
            final Moment moment = (Moment) datas.get(dataPosition);
            //todo 查询帖子关联的User,以及赞列表和评论列表
            queryUser(moment, holder);
            holder.digCommentBody.setVisibility(View.GONE);
            queryLikes(moment, holder, dataPosition);

            final String content = moment.getContent();
            String createTime = moment.getCreatedAt();
            holder.timeTv.setText(Utils.getFuzzyTime(createTime));

            if (!TextUtils.isEmpty(content)) {
                holder.contentTv.setExpand(moment.isExpand());
                holder.contentTv.setExpandStatusListener(isExpand -> moment.setExpand(isExpand));
                holder.contentTv.setText(UrlUtils.formatUrlString(content));
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
            //comment or praise dialog
            final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(dataPosition, moment, BUser.getCurrentUser().getObjectId()));
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType) {
                case MomentViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                    if (holder instanceof URLViewHolder) {
                        String linkImg = moment.getLinkImg();
                        String linkTitle = moment.getLinkTitle();
                        Glide.with(context).load(linkImg).into(((URLViewHolder) holder).urlImageIv);
                        ((URLViewHolder) holder).urlContentTv.setText(linkTitle);
                        ((URLViewHolder) holder).urlBody.setVisibility(View.VISIBLE);
                        ((URLViewHolder) holder).urlTipTv.setVisibility(View.VISIBLE);
                    }
                    break;
                case MomentViewHolder.TYPE_IMAGE:// 处理图片
                    if (holder instanceof ImageViewHolder) {
                    String photosStr = "";
                    if(!TextUtils.isEmpty(moment.getPhotos())){
                        photosStr = moment.getPhotos();
                    }
                        final List<String> photos = Arrays.asList(photosStr.split(","));
                        if (photos != null && photos.size() > 0) {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).multiImageView.setAdapterPosition(position);
                            ((ImageViewHolder) holder).multiImageView.setList(moment.getPhotosBean());
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener((view, picPosition) -> {
                                //imagesize是作为loading时的图片size
                                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                                ImagePagerActivity.startImagePagerActivity(((ImageViewHolder) holder).multiImageView, context, photos, viewHolder.getAdapterPosition(), picPosition, imageSize);
                                if(sharedViewListener!=null){
                                    sharedViewListener.onSharedViewListener(((ImageViewHolder) holder).multiImageView.getSharedViews(), picPosition);
                                }
                            });
                        } else {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }
                    }

                    break;
                case MomentViewHolder.TYPE_VIDEO:
                    if (holder instanceof VideoViewHolder) {
                        ((VideoViewHolder) holder).videoView.setVideoUrl(moment.getVideoUrl());
                        ((VideoViewHolder) holder).videoView.setVideoImgUrl(moment.getVideoImgUrl());//视频封面图片
                        ((VideoViewHolder) holder).videoView.setPostion(position);
                        ((VideoViewHolder) holder).videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener() {
                            @Override
                            public void onPlayClick(int pos) {
                                curPlayIndex = pos;
                            }
                        });
                    }

                    break;
                case MomentViewHolder.TYPE_TEXT:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (payloads.isEmpty() || holder instanceof HeaderViewHolder) {
            onBindViewHolder(holder, position);
        } else {
            final int dataPosition = position - HEADVIEW_SIZE;
            String playLoad = (String) payloads.get(0);
            MomentViewHolder momentViewHolder = (MomentViewHolder) holder;
            Moment item = (Moment) datas.get(dataPosition);
            switch (playLoad) {
                case "comment":
                    momentViewHolder.commentList.setDatas(item.getComments());
                    break;
                case "praise":
                    refreshLike(item, momentViewHolder);
                    refreshLikeListAndCommentList(item, (MomentViewHolder) holder, dataPosition, false);
                    break;
            }
        }
    }

    private void queryLikes(Moment moment, MomentViewHolder holder, int dataPosition) {
        if (moment.likes != null) {
            refreshLike(moment, holder);
            queryComments(moment, holder, dataPosition);
            return;
        }
        // 查询喜欢这个帖子的所有用户，因此查询的是用户表
        if (Utils.isNetworkConnected()) {
            BmobQuery<User> query = new BmobQuery<User>();
            Moment post = new Moment();
            post.setObjectId(moment.getObjectId());
            //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
            query.addWhereRelatedTo("favors", new BmobPointer(post));
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> userList, BmobException e) {
                    if (e == null) {
                        for (User item : userList) {
                            item.favorMoments += "," + moment.getObjectId();
                        }
                        moment.likes = userList;
                        refreshLike(moment, holder);
                    } else {
                        Log.i("bmob", "失败：" + e.getMessage());
                    }
                    App.getHandler().post(() -> queryComments(moment, holder, dataPosition));
                }
            });
        } else {
            // query from db
            RealmResults<UserRealm> dbData = BaseRealmDao.realm.where(UserRealm.class).contains("favorMoments", moment.getObjectId()).findAll();
            List<User> data = new ArrayList<>();
            if (dbData.isLoaded()) {
                // 完成查询
                if (!dbData.isEmpty()) {
                    for (UserRealm item : dbData) {
                        data.add(item.toBmobObject());
                    }
                }
            }
            moment.likes = data;
            refreshLike(moment, holder);
            App.getHandler().post(() -> queryComments(moment, holder, dataPosition));
        }
    }

    private void refreshLike(Moment moment, MomentViewHolder holder) {
        boolean hasLike = moment.hasLikes();
        if (hasLike) {//处理点赞列表
            holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    //todo open profile with profile id(user.id)

                }
            });
            userFirst(moment.likes);
            holder.praiseListView.setUsers(moment.likes);
            holder.praiseListView.setVisibility(View.VISIBLE);
        } else {
            holder.praiseListView.setVisibility(View.GONE);
        }
        //判断是否已点赞,加载完likes后
        SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        if (moment.isCurUserLike()) {
            snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
        } else {
            snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
        }
    }

    private void userFirst(List<User> likes) {
        String curId = App.currentUserProfile.getObjectId();
        for (int i = 0; i < likes.size(); i++) {
            if (curId.equals(likes.get(i).id)) {
                likes.add(0, likes.get(i));
                likes.remove(i + 1);
                break;
            }
        }
    }

    private void queryComments(Moment moment, MomentViewHolder holder, int dataPosition) {
        if (moment.getComments() != null) {
            refreshLikeListAndCommentList(moment, holder, dataPosition);
            return;
        }
        if(TextUtils.isEmpty(moment.getObjectId())){
            return;
        }
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("momentId", moment.getObjectId());
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
//执行查询方法
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                if (e == null) {
                    moment.setComments(object);
                } else {
                    Log.i("bmob", "获取评论失败：" + e.getMessage() + "," + e.getErrorCode());
                    moment.setComments(new ArrayList<>());
                }
                MomentRealm momentRealm = moment.toRealmObject();
                BaseRealmDao.insertOrUpdate(momentRealm);
                refreshLikeListAndCommentList(moment, holder, dataPosition);
            }
        });
    }

    private void refreshLikeListAndCommentList(Moment moment, MomentViewHolder holder, int dataPosition) {
        refreshLikeListAndCommentList(moment, holder, dataPosition, true);
    }

    private void refreshLikeListAndCommentList(Moment moment, MomentViewHolder holder, int dataPosition, boolean refreshComment) {
        List<Comment> commentsDatas = moment.getComments();
        boolean hasComment = moment.hasComment();
        boolean hasLike = moment.hasLikes();
        TransitionManager.beginDelayedTransition(holder.llRightContent);
        TransitionManager.beginDelayedTransition(holder.digCommentBody);
        holder.digLine.setVisibility(hasLike && hasComment ? View.VISIBLE : View.GONE);
        if (hasLike || hasComment) {
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }

        if (hasComment) {//处理评论列表
            if (refreshComment) {
                holder.commentList.setDatas(commentsDatas);
            }
            holder.commentList.setVisibility(View.VISIBLE);
            holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                @Override
                public void onItemClick(int commentPosition) {
                    Comment comment = commentsDatas.get(commentPosition);
                    if (Utils.getCurrentShortUser().getObjectId().equals(comment.getUser().getObjectId().trim())) {//复制或者删除自己的评论
                        CommentDialog dialog = new CommentDialog(context, presenter, comment, dataPosition);
                        dialog.show();
                    } else {//回复别人的评论
                        if (presenter != null) {
                            CommentConfig config = new CommentConfig();
                            config.circlePosition = dataPosition;
                            config.commentPosition = commentPosition;
                            config.commentType = CommentConfig.Type.REPLY;
                            config.replyUser = comment.getUser();

                            momentsFragment.selectMomentId = moment.getObjectId();
                            momentsFragment.replyUser = comment.getUser();

                            presenter.showEditTextBody(config);
                        }
                    }
                }
            });
            holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(int commentPosition) {
                    //长按进行复制或者删除
                    Comment comment = commentsDatas.get(commentPosition);
                    CommentDialog dialog = new CommentDialog(context, presenter, comment, dataPosition);
                    dialog.show();
                }
            });
        } else {
            holder.commentList.setVisibility(View.GONE);
        }
    }

    private void queryUser(Moment moment, MomentViewHolder holder) {
        if (moment.getUser() != null && moment.getUser().getName() != null) {
            setUserToHolder(moment, holder);
            return;
        }
        holder.headIv.setImageDrawable(null);
        Utils.queryShortUser(moment.getUser().getObjectId(), new NetUserRequest.NetRequestCallBack() {

            @Override
            public void onSuccess(User user) {
                moment.setUser(user);
                setUserToHolder(moment, holder);
            }

            @Override
            public void onFailure(String e) {
                Utils.showToast("获取用户失败：" + e);
            }
        });
    }

    private void setUserToHolder(Moment moment, MomentViewHolder holder) {
        User user = moment.getUser();
        holder.user = user;
        //如果头像是用户自己，保存至列表
        if (Utils.getCurrentShortUser().getObjectId().equals(user.getObjectId())) {
            ((BottomInputActivity) context).selfAvatarsInMomentList.add(holder.headIv);
        }
        holder.nameTv.setText(user.getName());
        Utils.setUserAvatar(user, holder.headIv);

        //加载完User后
        if (BUser.getCurrentUser().getProfileId().equals(user.id)) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setOnClickListener(v -> {
                Utils.showSimpleDialog(context, "确定删除该动态？", (dialog, which) -> {
                    //删除
                    if (presenter != null) {
                        presenter.deleteMoment(moment.getObjectId(),holder.getAdapterPosition());
                    }
                });
            });
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;//有head需要加1
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String mFavorId;
        //动态在列表中的位置
        private int dataPosition;
        private long mLasttime = 0;
        private Moment mMoment;

        public PopupItemClickListener(int dataPosition, Moment moment, String favorId) {
            this.mFavorId = favorId;
            this.dataPosition = dataPosition;
            this.mMoment = moment;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenter != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenter.addFavort(mMoment.getObjectId(), dataPosition);
                        } else {//取消点赞
                            presenter.deleteFavort(mMoment.getObjectId(), dataPosition);
                        }
                    }
                    break;
                case 1://发布评论
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = dataPosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        presenter.showEditTextBody(config);
                        presenter.setCurrentMomentId(mMoment.getObjectId(), null);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
