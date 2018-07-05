package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import top.wifistar.R;
import top.wifistar.activity.ChatActivity;
import top.wifistar.bean.bmob.User;
import top.wifistar.utils.GlideCircleTransform;
import top.wifistar.utils.Utils;

/**
 * 接收到的文本类型
 */
public class ReceiveTextHolder extends BaseViewHolder {

    protected ImageView iv_avatar;

    protected TextView tv_time;

    protected TextView tv_message;

    public ReceiveTextHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_message, onRecyclerViewListener);
        initView(itemView);
    }

    private void initView(View itemView) {
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_message = (TextView) itemView.findViewById(R.id.tv_message);
    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        String time = Utils.getFuzzyTime2(message.getCreateTime());
        tv_time.setText(time);
        Glide.with(context)
                .load(!Utils.isEmpty(conversationIcon) ? conversationIcon : R.drawable.default_avartar)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new GlideCircleTransform(context))
                .into(iv_avatar);
        String content = message.getContent();
        tv_message.setText(content);
        iv_avatar.setOnClickListener(v -> {
            if(((ChatActivity) context).isFromProfile()){
                ((ChatActivity) context).finish();
            }else{
                User user = ((ChatActivity) context).getCurrentUser();
                Utils.jumpToProfile(context, user, iv_avatar);
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tv_message.setOnLongClickListener(v -> {
            if (onRecyclerViewListener != null) {
                onRecyclerViewListener.onItemLongClick(getAdapterPosition());
            }
            return true;
        });
    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}