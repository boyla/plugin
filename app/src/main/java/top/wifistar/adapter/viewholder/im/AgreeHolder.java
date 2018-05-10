package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import cn.bmob.newim.bean.BmobIMMessage;
import top.wifistar.R;

/**
 * 同意添加好友的agree类型
 */
public class AgreeHolder extends BaseViewHolder implements View.OnClickListener,View.OnLongClickListener {

  protected TextView tv_time;

  protected TextView tv_message;

  public AgreeHolder(Context context, ViewGroup root, OnRecyclerViewListener listener) {
    super(context, root, R.layout.item_chat_agree, listener);
    tv_time = (TextView) itemView.findViewById(R.id.tv_time);
    tv_message = (TextView) itemView.findViewById(R.id.tv_message);
  }

  @Override
  public void bindData(Object o) {
    final BmobIMMessage message = (BmobIMMessage)o;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    String time = dateFormat.format(message.getCreateTime());
    String content = message.getContent();
    tv_message.setText(content);
    tv_time.setText(time);
  }

  public void showTime(boolean isShow) {
    tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
  }
}
