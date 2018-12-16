package top.wifistar.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import top.wifistar.R;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;


/**
 * Created by hasee on 2017/3/24.
 */

public class BottomMenuView extends FrameLayout{

    public static final int Item_None = 0;

    public static final int Item_Chats = 1;

    public static final int Item_Moments = 2;

    public static final int Item_Connections = 3;

    public static final int Item_Discover = 4;

    public static final int Item_Me = 5;

    public static final int Item_Join = 6;

    private Context mContext;

    TextView tvChats;

    TextView tvMoments;

    TextView tvConnections;

    TextView tvDiscover;

    TextView tvMe;

    ThemeImageView ivChats;

    ThemeImageView ivMoments;

    ThemeImageView ivConnections;

    ThemeImageView ivDiscover;

    ThemeImageView ivMe;

    private TextView choosedItem;

    public int currentItem = BottomMenuView.Item_Chats;

    private int redPointPos = BadgeView.POSITION_CENTER_RIGHT;

    public BottomMenuView(Context context) {
        super(context);
        this.mContext = context;
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutInflater.from(context).inflate(R.layout.bottom_menu, this);
        initUI();
    }

    public BottomMenuView(Context context, AttributeSet attrs) {
        super(context, attrs );
        this.mContext = context;
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutInflater.from(context).inflate(R.layout.bottom_menu, this);
        initUI();
        setClickListener();
    }

    public BottomMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle );
        this.mContext = context;
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutInflater.from(context).inflate(R.layout.bottom_menu, this);
        initUI();
    }

//    public BottomMenuView(Context context, int redPointPos) {
//        this(context);
//
//        this.redPointPos = redPointPos;
//    }


    private void showRedot() {
//        connectionNum = 0;
//        matchNum = App.getInstance().cache_noticeBean.new_meet_count
//                + App.getInstance().cache_noticeBean.meet_like_me_count;
//        if (App.getInstance().cache_noticeBean.interested_count != null) {
//            connectionNum += App.getInstance().cache_noticeBean.interested_count.getNew_count();
//        }
//        if (App.getInstance().cache_noticeBean.viewed_count != null) {
//            connectionNum += App.getInstance().cache_noticeBean.viewed_count.getNew_count();
//        }
//        if (redPointPos == BadgeView.POSITION_TOP_RIGHT) {
//            if (hasBlog) {
//                showRedPoint(ivBlog, App.getInstance().cache_noticeBean.new_blog_comment_count
//                        + App.getInstance().cache_noticeBean.moment_new_notification_count);
//            } else {
//                showRedPoint(ivActivity, App.getInstance().cache_noticeBean.moment_new_notification_count);
//            }
//        } else {
//            showRedPoint(tvActivity, App.getInstance().cache_noticeBean.moment_new_notification_count);
//            showRedPoint(tvBlog, App.getInstance().cache_noticeBean.new_blog_comment_count);
//        }
//        if (redPointPos == BadgeView.POSITION_TOP_RIGHT) {
//            showRedPoint(ivConnection, connectionNum);
//            showRedPoint(ivMessage, App.getInstance().cache_noticeBean.message_count);
//            showRedPoint(ivMatch, matchNum);
//        } else {
//            showRedPoint(tvConnection, connectionNum);
//            showRedPoint(tvMessage, App.getInstance().cache_noticeBean.message_count);
//            showRedPoint(tvMatch, matchNum);
//        }
    }

    private void showRedPoint(View targetView, int count) {
        boolean isShow;
        if (count > 0) {
            isShow = true;
        } else {
            isShow = false;
        }
        BadgeView badgeView = (BadgeView) targetView.getTag();
        if (badgeView == null) {
            badgeView = createBadgeView(getContext(), targetView);
        }
        if (isShow) {
            int oldnum = 0;
            if (!TextUtils.isEmpty(badgeView.getText())) {
                if (badgeView.getText().toString().contains("+")) {
                    oldnum = 99;
                } else {
                    oldnum = Integer.parseInt(badgeView.getText().toString());
                }
            }
            if (count < 10 && count > 0) {
                int width = Utils.dip2px(getContext(), 14);
                badgeView.setWidth(width);
                badgeView.setHeight(width);
            }
            if (oldnum < 10 && count >= 10) {
                badgeView.hide();
                badgeView = createBadgeView(getContext(), badgeView.getTarget());
            }
            badgeView.show();
        } else {
            badgeView.hide();
        }
        if (count > 99) {
            badgeView.setText("99+");
        } else {
            badgeView.setText(count + "");
        }
    }

    private BadgeView createBadgeView(Context context, View targetView) {
        BadgeView badgeView = new BadgeView(context, targetView);
        targetView.setTag(badgeView);
        badgeView.setBadgePosition(redPointPos);

        int width = Utils.dip2px(context, 16);
        badgeView.setHeight(width);

        if (redPointPos == BadgeView.POSITION_CENTER_RIGHT) {
            badgeView.setBadgeMargin(20, 0);
        } else if (redPointPos == BadgeView.POSITION_TOP_RIGHT) {
            badgeView.setBadgeMargin(0, Utils.dip2px(context, 3));
            badgeView.setTextSize(8);
            badgeView.setGravity(Gravity.CENTER);
            badgeView.setText("");
            badgeView.setHasCircle(true);
        }
        return badgeView;
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void showOrHideRedPoint(NoticeEvent event) {
//        showRedot();
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMomentNotificationsUpdatedEvent(MomentNotificationsUpdatedEvent event) {
//        showRedot();
//    }

//    @Subscribe
//    public void onAvatarUpdated(AvatarUpdatedEvent event) {
//        if (null != ivHeaderImg) {
//            PhotoLoader.setMyAvatar(ivHeaderImg);
//        }
//    }

    private void initUI() {
        tvChats = (TextView) findViewById(R.id.tvChats);
        tvMoments = (TextView) findViewById(R.id.tvMoments);
        tvConnections = (TextView) findViewById(R.id.tvConnections);
        tvDiscover = (TextView) findViewById(R.id.tvDiscover);
        tvMe = (TextView) findViewById(R.id.tvMe);
        ivChats = (ThemeImageView) findViewById(R.id.ivChats);
        ivMoments = (ThemeImageView) findViewById(R.id.ivMoments);
        ivConnections = (ThemeImageView) findViewById(R.id.ivConnections);
        ivDiscover = (ThemeImageView) findViewById(R.id.ivDiscover);
        ivMe = (ThemeImageView) findViewById(R.id.ivMe);
        setItemFocus(currentItem);
    }


    public void setItemFocus(int item) {
        int color = Utils.getLevelColor(mContext);
        if (null != choosedItem) {
            choosedItem.setTextColor(mContext.getResources().getColor(R.color.bottom_tab_text_color_normal));
            if (ivChats != null) {
                ivChats.setSelected(false);
            }
            if (ivMoments != null) {
                ivMoments.setSelected(false);
            }
            if (ivConnections != null) {
                ivConnections.setSelected(false);
            }
            if (ivDiscover != null) {
                ivDiscover.setSelected(false);
            }
            if (ivMe != null) {
                ivMe.setSelected(false);
            }
        }

        switch (item) {
            case Item_Chats:
                tvChats.setTextColor(color);
                ivChats.setSelected(true);
                choosedItem = tvChats;
                break;

            case Item_Moments:
                tvMoments.setTextColor(color);
                ivMoments.setSelected(true);
                choosedItem = tvMoments;
                break;

            case Item_Connections:
                tvConnections.setTextColor(color);
                ivConnections.setSelected(true);
                choosedItem = tvConnections;
                break;

            case Item_Discover:
                tvDiscover.setTextColor(color);
                ivDiscover.setSelected(true);
                choosedItem = tvDiscover;
                break;

            case Item_Me:
                tvMe.setTextColor(color);
                ivMe.setSelected(true);
                choosedItem = tvMe;
                break;
        }
    }

    private void setClickListener() {
        OnClickListener listener = v -> {
            if (Utils.isFastClick()) {
                return;
            }
            int id = v.getId();
            if (null != choosedItem) {
                if (id == choosedItem.getId()) {
                    EventUtils.post(new BottomMenuItemClickEvent(Item_None));
                    return;
                }
            }

            if (id == R.id.tvChats || id == R.id.rlChats) {
                currentItem = Item_Chats;
            } else if (id == R.id.tvMoments || id == R.id.rlMoments) {
                currentItem = Item_Moments;
            } else if (id == R.id.tvConnections || id == R.id.rlConnections) {
                currentItem = Item_Connections;
            } else if (id == R.id.tvDiscover || id == R.id.rlDiscover) {
                currentItem = Item_Discover;
            } else if (id == R.id.tvMe || id == R.id.rlMe) {
                currentItem = Item_Me;
            }

            setItemFocus(currentItem);
            EventUtils.post(new BottomMenuItemClickEvent(currentItem));
            showRedot();
        };

        findViewById(R.id.rlChats).setOnClickListener(listener);
        findViewById(R.id.rlMoments).setOnClickListener(listener);
        findViewById(R.id.rlConnections).setOnClickListener(listener);
        findViewById(R.id.rlDiscover).setOnClickListener(listener);
        findViewById(R.id.rlMe).setOnClickListener(listener);
    }


}
