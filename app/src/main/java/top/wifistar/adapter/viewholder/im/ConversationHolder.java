package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import top.wifistar.R;
import top.wifistar.utils.Utils;

public class ConversationHolder extends BaseViewHolder {

  public ImageView iv_recent_avatar;
  public TextView tv_recent_name;
  public TextView tv_recent_msg;
  public TextView tv_recent_time;
  public TextView tv_recent_unread;

  public ConversationHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_conversation,onRecyclerViewListener);
    initView(itemView);
  }

    private void initView(View itemView) {
        iv_recent_avatar = (ImageView) itemView.findViewById(R.id.iv_recent_avatar);
        tv_recent_name = (TextView) itemView.findViewById(R.id.tv_recent_name);
        tv_recent_msg = (TextView) itemView.findViewById(R.id.tv_recent_msg);
        tv_recent_time = (TextView) itemView.findViewById(R.id.tv_recent_time);
        tv_recent_unread = (TextView) itemView.findViewById(R.id.tv_recent_unread);
    }

    @Override
  public void bindData(Object o) {
      BmobIMConversation conversation =(BmobIMConversation)o;
      List<BmobIMMessage> msgs =conversation.getMessages();
      if(msgs!=null && msgs.size()>0){
          BmobIMMessage lastMsg =msgs.get(0);
          String content =lastMsg.getContent();
          if(lastMsg.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
              tv_recent_msg.setText(content);
          }else if(lastMsg.getMsgType().equals(BmobIMMessageType.IMAGE.getType())){
              tv_recent_msg.setText("[图片]");
          }else if(lastMsg.getMsgType().equals(BmobIMMessageType.VOICE.getType())){
              tv_recent_msg.setText("[语音]");
          }else if(lastMsg.getMsgType().equals(BmobIMMessageType.LOCATION.getType())){
              tv_recent_msg.setText("[位置]"+content);
          }else{//开发者自定义的消息类型，需要自行处理
              tv_recent_msg.setText("[未知]");
          }
          tv_recent_time.setText(Utils.getChatTime(false, lastMsg.getCreateTime()));
      }
      //会话图标
      Utils.setAvatar(conversation.getConversationIcon(), iv_recent_avatar);
      //会话标题
      tv_recent_name.setText(conversation.getConversationTitle());
      //TODO 会话：4.3、查询指定会话下的未读消息数
      long unread = BmobIM.getInstance().getUnReadCount(conversation.getConversationId());
      if(unread>0){
          tv_recent_unread.setVisibility(View.VISIBLE);
          tv_recent_unread.setText(String.valueOf(unread));
      }else{
          tv_recent_unread.setVisibility(View.GONE);
      }
  }

}