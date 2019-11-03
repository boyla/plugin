package top.wifistar.app;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.shizhefei.view.indicator.FixedIndicatorView;

import top.wifistar.R;
import top.wifistar.view.CircleImageView;


public abstract class ToolbarActivity extends BaseActivity {

    protected static final int TAB_NONE = -1;

    protected static final int TAB_LEFT = 0;

    protected static final int TAB_RIGHT = 1;

    protected LinearLayout mContentView;

    protected Toolbar mToolbar;

    protected CircleImageView mCustomLogo;

    protected TextView mCustomTitle;

    protected View tool_bar_frame;

    protected FixedIndicatorView tabIndicator;

    protected View custom_view;

    protected TextView mCustomSubTitle;

    protected TextView mTitleCenter;

    protected LinearLayout rgToolBarTab;

    protected TextView tabLeft;

    protected TextView tabRight;

    protected View.OnClickListener toolbarTabClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            tabLeft.setSelected(false);
            tabRight.setSelected(false);

            view.setSelected(true);

//            MainMenuItemEnum itemEnum = (MainMenuItemEnum) view.getTag(R.id.toolbarTabTag);
//            onToolBarTabChange(itemEnum);
        }
    };

    public interface KeyBoardListener {

        void onHideKeyboard();

        void onShowKeyboard(int keyboardHeight);
    }

    public KeyBoardListener mKeyBoardListener = null;


    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            // navigation bar height
            int navigationBarHeight = 0;
            int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            // status bar height
            int statusBarHeight = 0;
            resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            // display window size for the app layout
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

            // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
            int keyboardHeight = mContentView.getRootView().getHeight() - (statusBarHeight + navigationBarHeight + rect.height());

            if (keyboardHeight <= 0) {
                if (mKeyBoardListener != null)
                    mKeyBoardListener.onHideKeyboard();
            } else {
                if (mKeyBoardListener != null)
                    mKeyBoardListener.onShowKeyboard(keyboardHeight);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        int mStatusBarColor = getResources().getColor(R.color.primary);
        StatusBarUtil.setColorNoTranslucent(this, mStatusBarColor);
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
    }

    public TextView getCustomTitle() {
        return mCustomTitle;
    }

    public View getToolBarFrame() {
        return tool_bar_frame;
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }

    public FixedIndicatorView getTabIndicator() {
        return tabIndicator;
    }

    protected abstract void initTopBar();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.closeKeyBoard();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityCompat.finishAfterTransition(this);
            } else {
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View setToolbarContent(View contentView) {
        mContentView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_tool_bar, null, false);
        mTitleCenter = (TextView) mContentView.findViewById(R.id.tvTitleCenter);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(lp);
        mContentView.addView(contentView);
        loadToolBar(mContentView);
        return mContentView;
    }

    @Override
    public void setContentView(int layoutResID) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        }
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        super.setContentView(setToolbarContent(contentView));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(setToolbarContent(view));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(setToolbarContent(view), params);
    }

    private void loadToolBar(LinearLayout contentView) {
        mToolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
        mToolbar.setContentInsetsRelative(0, 0);
        setSupportActionBar(mToolbar);
        mToolbar.setClickable(false);
        mCustomLogo = (CircleImageView) mToolbar.findViewById(R.id.mCustomLogo);
        mCustomTitle = (TextView) mToolbar.findViewById(R.id.mCustomTitle);
        tool_bar_frame = mToolbar.findViewById(R.id.tool_bar_frame);
        mCustomSubTitle = (TextView) mToolbar.findViewById(R.id.mCustomSubTitle);
        tabIndicator = (FixedIndicatorView) mToolbar.findViewById(R.id.tab_indicator);
        custom_view = mToolbar.findViewById(R.id.custom_view);

        rgToolBarTab = (LinearLayout) mToolbar.findViewById(R.id.rgToolBarTab);

        tabLeft = (TextView) mToolbar.findViewById(R.id.toolbarTabLeft);
        tabLeft.setOnClickListener(toolbarTabClickListener);

        tabRight = (TextView) mToolbar.findViewById(R.id.toolbarTabRight);
        tabRight.setOnClickListener(toolbarTabClickListener);
    }

    public void setCenterTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mToolbar.setTitle("");
            mTitleCenter.setText(title);
            mTitleCenter.setVisibility(View.VISIBLE);
        } else {
            mTitleCenter.setVisibility(View.GONE);
        }
    }

    public void setCenterTitle(int title) {
        mToolbar.setTitle("");
        mTitleCenter.setText(title);
        mTitleCenter.setVisibility(View.VISIBLE);
    }

//    protected void onToolBarTabChange(MainMenuItemEnum itemEnum) {
//
//    }
//
//    public void addToolBarTab(List<ToolBarTab> tabList) {
//        rgToolBarTab.setVisibility(View.VISIBLE);
//
//        clearSelectedTab();
//
//        int i = 0;
//        for (ToolBarTab tab : tabList) {
//            if (i == TAB_LEFT) {
//                tabLeft.setText(tab.name);
//                tabLeft.setTag(R.id.toolbarTabTag, tab.tag);
//            } else if (i == tabList.size() - 1) {
//                tabRight.setText(tab.name);
//                tabRight.setTag(R.id.toolbarTabTag, tab.tag);
//            }
//
//            i++;
//        }
//
//        makeTabWidth();
//
//        setSelectedTab(TAB_LEFT);
//    }

    protected void makeTabWidth() {
        Paint mPaint = tabLeft.getPaint();
        String tabLeftText = tabLeft.getText().toString();
        String tabRightText = tabRight.getText().toString();

        float tabLeftWidth = mPaint.measureText(tabLeftText);
        float tabRightWidth = mPaint.measureText(tabRightText);

        float tabWidth = tabRightWidth > tabLeftWidth ? tabRightWidth : tabLeftWidth;
        int paddingWidth = mResources.getDimensionPixelSize(R.dimen.toolbar_margin);
        tabLeft.setWidth((int) tabWidth + 2 * paddingWidth);
        tabRight.setWidth((int) tabWidth + 2 * paddingWidth);
    }

    public void removeTab() {
        rgToolBarTab.setVisibility(View.GONE);
    }

    public TextView getTab(int pos) {
        if (TAB_LEFT == pos) {
            return tabLeft;
        } else if (TAB_RIGHT == pos) {
            return tabRight;
        }

        return tabLeft;
    }

    public void setSelectedTab(int pos) {
        tabLeft.setSelected(pos == TAB_LEFT ? true : false);
        tabRight.setSelected(pos == TAB_RIGHT ? true : false);
    }

    public void clearSelectedTab() {
        setSelectedTab(TAB_NONE);
    }

    public View getLeftMenu() {
        return null;
    }

}