package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import java.text.SimpleDateFormat;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import top.wifistar.R;
import top.wifistar.utils.GlideCircleTransform;
import top.wifistar.utils.Utils;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder {

  protected ImageView iv_avatar;
  protected TextView tv_time;
  protected ImageView iv_picture;
  protected ProgressBar progress_load;

  public ReceiveImageHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_chat_received_image,onRecyclerViewListener);
    initView(itemView);
  }

  private void initView(View itemView) {
    iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
    tv_time = (TextView) itemView.findViewById(R.id.tv_time);
    iv_picture = (ImageView) itemView.findViewById(R.id.iv_picture);
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
    //可使用buildFromDB方法转化为指定类型的消息
    final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false,msg);
    //显示图片
    Glide.with(context)
            .load(message.getRemoteUrl())
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .bitmapTransform(new GlideCircleTransform(context))
            .error(R.drawable.loading_failed)
            .into(new GlideDrawableImageViewTarget(iv_picture) {
              @Override
              public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                progress_load.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onStart() {
                super.onStart();
                progress_load.setVisibility(View.VISIBLE);
              }

              @Override
              public void onStop() {
                super.onStop();
                progress_load.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                progress_load.setVisibility(View.INVISIBLE);
              }
            });


    iv_avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击" + info.getName() + "的头像");
      }
    });

    iv_picture.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toast("点击图片:"+message.getRemoteUrl()+"");
        if(onRecyclerViewListener!=null){
          onRecyclerViewListener.onItemClick(getAdapterPosition());
        }
      }
    });

    iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (onRecyclerViewListener != null) {
          onRecyclerViewListener.onItemLongClick(getAdapterPosition());
        }
        return true;
      }
    });

  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}