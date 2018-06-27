package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMLocationMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import top.wifistar.R;
import top.wifistar.utils.GlideCircleTransform;
import top.wifistar.utils.Utils;

/**
 * 发送的语音类型
 */
public class SendLocationHolder extends BaseViewHolder {

  protected ImageView iv_avatar;

  protected ImageView iv_fail_resend;

  protected TextView tv_time;

  protected LinearLayout layout_location;

  protected TextView tv_location;

  protected TextView tv_send_status;

  protected ProgressBar progress_load;
  BmobIMConversation c;
  public SendLocationHolder(Context context, ViewGroup root,BmobIMConversation c,OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_chat_sent_location,onRecyclerViewListener);
    initView(itemView);
    this.c = c;
  }

  private void initView(View itemView) {
    iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
    iv_fail_resend = (ImageView) itemView.findViewById(R.id.iv_fail_resend);
    tv_time = (TextView) itemView.findViewById(R.id.tv_time);
    layout_location = (LinearLayout) itemView.findViewById(R.id.layout_location);
    tv_location = (TextView) itemView.findViewById(R.id.tv_location);
    tv_send_status = (TextView) itemView.findViewById(R.id.tv_send_status);
    progress_load = (ProgressBar) itemView.findViewById(R.id.progress_load);
  }

  @Override
  public void bindData(Object o) {
    BmobIMMessage msg = (BmobIMMessage)o;
    //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
    final BmobIMUserInfo info = msg.getBmobIMUserInfo();
    String avatarUrl = info != null ? info.getAvatar() : null;
    Glide.with(context)
            .load(!Utils.isEmpty(avatarUrl) ? avatarUrl : R.drawable.default_avartar)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .bitmapTransform(new GlideCircleTransform(context))
            .into(iv_avatar);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(msg.getCreateTime());
    tv_time.setText(time);

    final BmobIMLocationMessage message = BmobIMLocationMessage.buildFromDB(msg);
    tv_location.setText(message.getAddress());
    int status =message.getSendStatus();
    if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
      iv_fail_resend.setVisibility(View.VISIBLE);
      progress_load.setVisibility(View.GONE);
    } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
      iv_fail_resend.setVisibility(View.GONE);
      progress_load.setVisibility(View.VISIBLE);
    } else {
      iv_fail_resend.setVisibility(View.GONE);
      progress_load.setVisibility(View.GONE);
    }
    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击" + info.getName() + "的头像");
      }
    });

    tv_location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("经度：" + message.getLongitude() + ",维度：" + message.getLatitude());
        if(onRecyclerViewListener!=null){
          onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
      }
    });
    tv_location.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
          onRecyclerViewListener.onItemLongClick(getAdapterPosition());
        }
        return true;
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
