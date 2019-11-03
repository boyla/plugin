package top.wifistar.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import top.wifistar.R;
import top.wifistar.activity.ChatActivity;
import top.wifistar.app.AppExecutor;
import top.wifistar.bean.bmob.User;
import top.wifistar.httpserver.NetUtils;
import top.wifistar.im.IMUtils;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.utils.ChatUtils;
import top.wifistar.utils.DecryptUtil;
import top.wifistar.utils.Utils;

public class IMUsersAdapter extends BaseRecycleViewAdapter {


    Context mContext;

    public IMUsersAdapter(Context context, List<IMUserRealm> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.message_item_view, parent, false
        ));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder baseHolder, int position) {
        IMUserRealm imUser = (IMUserRealm) datas.get(position);
        final ChildViewHolder textViewHolder = (ChildViewHolder) baseHolder;
        textViewHolder.shortUser = imUser.toBmobObject();
        textViewHolder.imUser = imUser;
//        BadgeView badgeView = (BadgeView) textViewHolder.rlHeader.getTag();
//        if (badgeView == null) {
//            badgeView = new BadgeView(mContext, textViewHolder.rlHeader);
//            textViewHolder.rlHeader.setTag(badgeView);
//            Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
//            badgeView.setTypeface(typeFace);
//            badgeView.setBadgeMargin(0, 0);
//            badgeView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
//            int width = Utils.dip2px(mContext, 14);
//            badgeView.setHeight(width);
//            badgeView.setGravity(Gravity.CENTER);
//            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        }
//        if (imUser.unReadNum > 0) {
//            badgeView.setText(imUser.unReadNum + "");
//            badgeView.show();
//        } else {
//            badgeView.hide();
//        }

        if (!imUser.sendSuccess) {
            textViewHolder.message_send_failed.setVisibility(View.VISIBLE);
        } else {
            textViewHolder.message_send_failed.setVisibility(View.GONE);
        }
        textViewHolder.mUserName.setText(imUser.name);
        if ("00000".equals(imUser.getRealmId())) {
            //Glide 加载公众号头像
            textViewHolder.mUserName.setText("Subscription");
        } else {
            Utils.setUserAvatar(imUser.toBmobObject(), textViewHolder.mHeadImg);
        }

        textViewHolder.mTime.setText(Utils.getFuzzyTime2(imUser.updateTime));
        if (!TextUtils.isEmpty(imUser.lastMsg)) {
            showMessageContent(imUser.lastMsg, textViewHolder.mMessageInfo, imUser.objectId);
        }
        if (imUser.unReadNum > 0) {
            textViewHolder.mNewPoint.setText("" + imUser.unReadNum);
            textViewHolder.mNewPoint.setVisibility(View.VISIBLE);
        } else {
            textViewHolder.mNewPoint.setVisibility(View.GONE);
        }
        boolean found = false;
        for (User user : NetUtils.usersInWiFi) {
            if (user.getObjectId().equals(imUser.objectId)) {
                found = true;
                break;
            }
        }
        textViewHolder.tvWiFi.setVisibility(found ? View.VISIBLE : View.GONE);
    }

    private void showMessageContent(String str, TextView textView, String userId) {
        textView.setText(ChatUtils.getEmotionContent(textView, DecryptUtil.decrypt(userId, Utils.getCurrentShortUserId(), str)));
    }

    static public class ChildViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearLayout;
        ImageView mHeadImg;
        TextView mNewPoint;
        TextView mUserName;
        TextView mMessageInfo;
        TextView mTime;
        ImageView message_send_failed;
        RelativeLayout rlHeader;
        TextView tvWiFi;
        public User shortUser;
        public IMUserRealm imUser;


        public ChildViewHolder(View itemView) {
            super(itemView);
            mHeadImg = (ImageView) itemView.findViewById(R.id.imageView_avatar);
            mNewPoint = (TextView) itemView.findViewById(R.id.message_item_has_new_message);
            mUserName = (TextView) itemView.findViewById(R.id.textview_username);
            tvWiFi = itemView.findViewById(R.id.tvWiFi);
            mMessageInfo = (TextView) itemView.findViewById(R.id.textview_content);
            mTime = (TextView) itemView.findViewById(R.id.textview_time);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_item);
            message_send_failed = (ImageView) itemView.findViewById(R.id.message_send_failed);
            //jump to conversation page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shortUser != null && !TextUtils.isEmpty(shortUser.getObjectId())) {
                        BmobIMConversation conversation = IMUtils.getConversationEntranceByShortUser(shortUser);
                        if (conversation != null) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("c", conversation);
                            bundle.putSerializable("ShortUser", shortUser);
                            Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                            intent.putExtras(bundle);
                            AppExecutor.getInstance().postMain(new Runnable() {
                                @Override
                                public void run() {
                                    BaseRealmDao.realm.beginTransaction();
                                    imUser.unReadNum = 0;
                                    BaseRealmDao.realm.commitTransaction();
                                }
                            },234);
                            mNewPoint.setVisibility(View.GONE);
                            itemView.getContext().startActivity(intent);
                            return;
                        }
                    }
                    Utils.makeSysToast("抱歉，打开会话失败");
                }
            });
            //to profile
            mHeadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shortUser != null) {
                        Utils.jumpToProfile(mHeadImg.getContext(), shortUser, mHeadImg);
                    }
                }
            });
            //delete or sth
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }
}
