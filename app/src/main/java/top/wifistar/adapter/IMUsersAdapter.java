package top.wifistar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import top.wifistar.R;
import top.wifistar.customview.BadgeView;
import top.wifistar.realm.UserRealm;
import top.wifistar.utils.TimeUtils;
import top.wifistar.utils.Utils;

public class IMUsersAdapter extends BaseRecycleViewAdapter {


    private Context mContext;

    public IMUsersAdapter(Context context, List<UserRealm> datas) {
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
        UserRealm imUser = (UserRealm) datas.get(position);
        final ChildViewHolder textViewHolder = (ChildViewHolder) baseHolder;
        BadgeView badgeView = (BadgeView) textViewHolder.rlHeader.getTag();
        if (badgeView == null) {
            badgeView = new BadgeView(mContext, textViewHolder.rlHeader);
            textViewHolder.rlHeader.setTag(badgeView);
            Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
            badgeView.setTypeface(typeFace);
            badgeView.setBadgeMargin(0, 0);
            badgeView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
            int width = Utils.dip2px(mContext, 14);
            badgeView.setHeight(width);
            badgeView.setGravity(Gravity.CENTER);
            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        }
        if (imUser.unReadNum > 0) {
            badgeView.setText(imUser.unReadNum + "");
            badgeView.show();
        } else {
            badgeView.hide();
        }

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
            Utils.setUserAvatar(mContext, imUser, textViewHolder.mHeadImg);
        }

        textViewHolder.mTime.setText(TimeUtils.transformTimeInside(new Date(imUser.updateTime)));
        if(!TextUtils.isEmpty(imUser.lastMsg)){
            showMessageContent(imUser.lastMsg,textViewHolder.mMessageInfo);
        }
    }

    private void showMessageContent(String str, TextView textView) {
        textView.setText(str);
    }

    static public class ChildViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearLayout;
        ImageView mHeadImg;
        TextView mNewPoint;
        View mViewGender;
        TextView mUserName;
        TextView mMessageInfo;
        TextView mTime;
        ImageView message_send_failed;
        RelativeLayout rlHeader;


        public ChildViewHolder(View itemView) {
            super(itemView);
            mHeadImg = (ImageView) itemView.findViewById(R.id.imageView_avatar);
            mNewPoint = (TextView) itemView.findViewById(R.id.message_item_has_new_message);
            mUserName = (TextView) itemView.findViewById(R.id.textview_username);
            mViewGender = itemView.findViewById(R.id.imageview_gender);
            mMessageInfo = (TextView) itemView.findViewById(R.id.textview_content);
            mTime = (TextView) itemView.findViewById(R.id.textview_time);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_item);
            message_send_failed = (ImageView) itemView.findViewById(R.id.message_send_failed);
            rlHeader = (RelativeLayout) itemView.findViewById(R.id.mrlHeader);
            //jump to conversation page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
