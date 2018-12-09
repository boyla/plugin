package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import top.wifistar.R;
import top.wifistar.bean.bmob.User;
import top.wifistar.utils.ChatUtils;
import top.wifistar.utils.GlideCircleTransform;
import top.wifistar.utils.Utils;

/**
 * 发送的文本类型
 */
public class SendTextHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    protected ImageView iv_avatar;

    protected ImageView iv_fail_resend;

    protected TextView tv_time;

    protected TextView tv_message;
    protected TextView tv_send_status;

    protected ProgressBar progress_load;

    BmobIMConversation c;

    public SendTextHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener listener) {
        super(context, root, R.layout.item_chat_sent_message, listener);
        initView(itemView);
        this.c = c;
    }

    private void initView(View itemView) {
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        iv_fail_resend = (ImageView) itemView.findViewById(R.id.iv_fail_resend);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_message = (TextView) itemView.findViewById(R.id.tv_message);
        tv_send_status = (TextView) itemView.findViewById(R.id.tv_send_status);
        progress_load = (ProgressBar) itemView.findViewById(R.id.progress_load);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
        Glide.with(context)
                .load((info != null && !Utils.isEmpty(info.getAvatar())) ? info.getAvatar() : R.drawable.default_avartar)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new GlideCircleTransform(context))
                .into(iv_avatar);
        String content = message.getContent();
        tv_message.setText(ChatUtils.getEmotionContent(tv_message,content));
        String time = Utils.getFuzzyTime2(message.getCreateTime());
        tv_time.setText(time);

        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
            iv_fail_resend.setVisibility(View.VISIBLE);
            progress_load.setVisibility(View.GONE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            iv_fail_resend.setVisibility(View.GONE);
            progress_load.setVisibility(View.VISIBLE);
        } else {
            iv_fail_resend.setVisibility(View.GONE);
            progress_load.setVisibility(View.GONE);
        }

        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + message.getContent());
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tv_message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Utils.getCurrentShortUser();
                Utils.jumpToProfile(context, user, iv_avatar);
            }
        });

        //重发
        iv_fail_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage msg) {
                        progress_load.setVisibility(View.VISIBLE);
                        iv_fail_resend.setVisibility(View.GONE);
                        tv_send_status.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            tv_send_status.setVisibility(View.VISIBLE);
                            tv_send_status.setText("已发送");
                            iv_fail_resend.setVisibility(View.GONE);
                            progress_load.setVisibility(View.GONE);
                        } else {
                            iv_fail_resend.setVisibility(View.VISIBLE);
                            progress_load.setVisibility(View.GONE);
                            tv_send_status.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
