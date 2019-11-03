package top.wifistar.adapter.viewholder;

import android.media.MediaPlayer;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import top.wifistar.R;
import top.wifistar.bean.bmob.User;
import top.wifistar.view.CommentListView;
import top.wifistar.view.ExpandTextView;
import top.wifistar.view.PraiseListView;
import top.wifistar.view.SnsPopupWindow;
import top.wifistar.view.videolist.model.VideoLoadMvpView;
import top.wifistar.view.videolist.widget.TextureVideoView;
import top.wifistar.utils.GlideCircleTransform;


/**
 * Created by yiw on 2016/8/16.
 */
public abstract class MomentViewHolder extends RecyclerView.ViewHolder implements VideoLoadMvpView {

    public final static int TYPE_TEXT = 4;
    public final static int TYPE_URL = 1;
    public final static int TYPE_IMAGE = 2;
    public final static int TYPE_VIDEO = 3;

    public int viewType;

    public ImageView headIv;
    public TextView nameTv;
    public TextView urlTipTv;
    /** 动态的内容 */
    public ExpandTextView contentTv;
    public TextView timeTv;
    public TextView deleteBtn;
    public ImageView snsBtn;
    /** 点赞列表*/
    public PraiseListView praiseListView;

    public LinearLayout digCommentBody;
    public LinearLayout llRightContent;
    public View digLine;
    public User user;

    /** 评论列表 */
    public CommentListView commentList;
    // ===========================
    public SnsPopupWindow snsPopupWindow;

    public MomentViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);

        initSubView(viewType, viewStub);

        headIv = (ImageView) itemView.findViewById(R.id.headIv);
        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        digLine = itemView.findViewById(R.id.lin_dig);

        contentTv = (ExpandTextView) itemView.findViewById(R.id.contentTv);
        urlTipTv = (TextView) itemView.findViewById(R.id.urlTipTv);
        timeTv = (TextView) itemView.findViewById(R.id.timeTv);
        deleteBtn = (TextView) itemView.findViewById(R.id.deleteBtn);
        snsBtn = (ImageView) itemView.findViewById(R.id.snsBtn);
        praiseListView = (PraiseListView) itemView.findViewById(R.id.praiseListView);

        digCommentBody = (LinearLayout) itemView.findViewById(R.id.digCommentBody);
        llRightContent = (LinearLayout) itemView.findViewById(R.id.llRightContent);
        commentList = (CommentListView)itemView.findViewById(R.id.commentList);

        snsPopupWindow = new SnsPopupWindow(itemView.getContext());
        Glide.with(itemView.getContext()).load(R.drawable.default_avartar).transform(new GlideCircleTransform(itemView.getContext())).into(headIv);
        headIv.setVisibility(View.VISIBLE);
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

    @Override
    public TextureVideoView getVideoView() {
        return null;
    }

    @Override
    public void videoBeginning() {

    }

    @Override
    public void videoStopped() {

    }

    @Override
    public void videoPrepared(MediaPlayer player) {

    }

    @Override
    public void videoResourceReady(String videoPath) {

    }
}
