package top.wifistar.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import top.wifistar.bean.bmob.Comment;
import top.wifistar.R;
import top.wifistar.bean.bmob.User;
import top.wifistar.chain.user.NetUserRequest;
import top.wifistar.utils.UrlUtils;
import top.wifistar.utils.Utils;

/**
 * Created by yiwei on 16/7/9.
 */
public class CommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<Comment> mDatas;
    private LayoutInflater layoutInflater;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<Comment> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<Comment> getDatas() {
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.praise_item_default));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.praise_item_selector_default));

        } finally {
            typedArray.recycle();
        }
    }

    volatile int[] requestCount;

    public void notifyDataSetChanged() {

        removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        requestCount = new int[mDatas.size()];
        for (int i = 0; i < requestCount.length; i++) {
            requestCount[i] = 2;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            final int index = i;
            View view = getView(index);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comment, null, false);

        TextView commentTv = convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);
        final Comment comment = mDatas.get(position);
        String userId = comment.getUser().getObjectId().trim();

        NetUserRequest.NetRequestCallBack queryUsesrCallBack = new NetUserRequest.NetRequestCallBack() {
            @Override
            public void onSuccess(User user) {
                String name = comment.getUser().getName();
                String toReplyName = "";
                if (comment.getToReplyUser() != null) {
                    toReplyName = comment.getToReplyUser().getName();
                }

                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(setClickableSpan(name, comment.getUser().getObjectId()));

                if (!TextUtils.isEmpty(toReplyName)) {
                    builder.append(" 回复 ");
                    builder.append(setClickableSpan(toReplyName, comment.getToReplyUser().getObjectId()));
                }
                builder.append(": ");
                //转换表情字符
                String contentBodyStr = comment.getContent();
                builder.append(UrlUtils.formatUrlString(contentBodyStr));
                commentTv.setText(builder);
                commentTv.setMovementMethod(circleMovementMethod);
                commentTv.setOnClickListener(v -> {
                    if (circleMovementMethod.isPassToTv()) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                });
                commentTv.setOnLongClickListener(v -> {
                    if (circleMovementMethod.isPassToTv()) {
                        if (onItemLongClickListener != null) {
                            onItemLongClickListener.onItemLongClick(position);
                        }
                        return true;
                    }
                    return false;
                });
            }

            @Override
            public void onFailure(String e) {
                Utils.showToast("获取用户失败:" + e);
            }
        };

        // need 2 request done
        if (TextUtils.isEmpty(comment.user.getName())) {
            Utils.queryShortUser(userId, new NetUserRequest.NetRequestCallBack() {
                @Override
                public void onSuccess(User user) {
                    comment.setUser(user);
                    countDownAndCheck(queryUsesrCallBack, position);
                }

                @Override
                public void onFailure(String msg) {
                    countDownAndCheck(queryUsesrCallBack, position);
                }
            });
        } else {
            countDownAndCheck(queryUsesrCallBack, position);
        }

        if (comment.getToReplyUser() == null) {
            countDownAndCheck(queryUsesrCallBack, position);
        } else {
            if (TextUtils.isEmpty(comment.user.getName())) {
                Utils.queryShortUser(comment.getToReplyUser().getObjectId(), new NetUserRequest.NetRequestCallBack() {
                    @Override
                    public void onSuccess(User user) {
                        comment.setToReplyUser(user);
                        countDownAndCheck(queryUsesrCallBack, position);
                    }

                    @Override
                    public void onFailure(String msg) {
                        countDownAndCheck(queryUsesrCallBack, position);
                    }
                });
            } else {
                countDownAndCheck(queryUsesrCallBack, position);
            }
        }

        return convertView;
    }

    private void countDownAndCheck(NetUserRequest.NetRequestCallBack callBack, int position) {
        requestCount[position]--;
        if (requestCount[position] == 0) {
            callBack.onSuccess(null);
        }
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        String res;
        if (TextUtils.isEmpty(textStr)) {
            res = "unknown";
        } else {
            res = textStr;
        }
        SpannableString subjectSpanText = new SpannableString(res);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        Utils.queryShortUser(id, new NetUserRequest.NetRequestCallBack() {
                                            @Override
                                            public void onSuccess(User user) {
                                                Utils.jumpToProfile(CommentListView.this.getContext(), user, null);
                                            }

                                            @Override
                                            public void onFailure(String msg) {
                                                Utils.makeSysToast(msg);
                                            }
                                        });
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
