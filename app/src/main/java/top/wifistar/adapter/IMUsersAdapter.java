package top.wifistar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.realm.RealmResults;
import top.wifistar.R;
import top.wifistar.bean.IMUserRealm;
import top.wifistar.customview.BadgeView;
import top.wifistar.utils.TimeUtils;
import top.wifistar.utils.Utils;

public class IMUsersAdapter extends BaseRealmAdapter<IMUserRealm> {


    private Context mContext;

    public IMUsersAdapter(Context context, RealmResults<IMUserRealm> datas) {
        super(context, datas);
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.message_item_view, parent, false
        ));
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder baseHolder, final IMUserRealm imUser) {
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
        if (imUser.getNewMsgCount() > 0) {
            badgeView.setText(imUser.getNewMsgCount() + "");
            badgeView.show();
        } else {
            badgeView.hide();
        }

        if (("1").equals(imUser.getSendState())) {
            textViewHolder.message_send_failed.setVisibility(View.VISIBLE);
        } else {
            textViewHolder.message_send_failed.setVisibility(View.GONE);
        }
        textViewHolder.mUserName.setText(imUser.getUserName());
        textViewHolder.mHeadImg.setTag(imUser.getUserId());
        if ("00000".equals(imUser.getUserId())) {
            //Glide 加载公众号头像
            textViewHolder.mUserName.setText("Subscription");
        } else {
            Utils.setUserAvatar(context, imUser, textViewHolder.mHeadImg);
            textViewHolder.mUserName.setText(imUser.getUserName());
        }

        textViewHolder.mTime.setText(TimeUtils.transformTimeInside(imUser.getTime()));
//        showMessageContent("image".equals(imUser.getMessage_type()) ? "[Photo]" : imUser.getBody(), textViewHolder.mMessageInfo);
//        String draft = ScreenUtils.MessageDraftUtils.fetch(mContext, imUser.getUserid());
//        if (!TextUtils.isEmpty(draft)) {
//            Spannable sText = new SpannableString(mContext.getResources().getString(R.string.Draft) + " " + draft);
//            sText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red)), 0, 7, 0);
//            textViewHolder.mMessageInfo.setTextHtml(sText, false);
//        }
    }

    private void showMessageContent(String str, TextView textView) {

            textView.setText(str);

    }

    static public class ChildViewHolder extends BaseViewHolder {

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

        }
    }
}
