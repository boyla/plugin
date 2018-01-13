package top.wifistar.activity;


import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import top.wifistar.app.App;
import top.wifistar.customview.BottomMenuView;
import top.wifistar.customview.LoadingView;
import top.wifistar.R;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.customview.TopReminder;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;

import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_BLOG_DISCOVER;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_CHATS;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_CONNECTIONS;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_MOMENTS;

public class HomeActivity extends ToolbarActivity {

    LoadingView loadingView;
    TopReminder topReminder;
    BottomMenuView bottomMenuView;
    View editTextBodyLl;
    int lastBottomItem = BottomMenuView.Item_None;
    protected String currentPageStr = null;
    boolean isFirstIn = true;

    @Override
    protected void initUI() {
        super.setContentView(R.layout.activity_main);
        topReminder = (TopReminder) findViewById(R.id.topReminder);
        bottomMenuView = (BottomMenuView) findViewById(R.id.bottomMenuView);
        editTextBodyLl =  findViewById(R.id.editTextBodyLl);
        setToolbarTitle();
        EventUtils.registerEventBus(this);
        refreshPage();
    }

    @Override
    protected void initTopBar() {

    }


    protected void setToolbarTitle() {
        mToolbar.setNavigationIcon(null);
        mToolbar.setTitle("");
        tool_bar_frame.setVisibility(View.VISIBLE);
        updateTitle();
        App.getHandler().postDelayed(() -> Utils.setUserSelfAvatar(mCustomLogo),1234);
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        EventUtils.unregisterEventBus(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePageByBottomItem(BottomMenuItemClickEvent event) {
        refreshPage();
    }

    public void changePage() {
        if (lastBottomItem == bottomMenuView.currentItem) {
            return;
        }

        switch (bottomMenuView.currentItem) {
            case BottomMenuView.Item_Chats:
                gotoPage(R.id.flContent, FRAGMENT_PAGE_CHATS, currentPageStr);
                currentPageStr = FRAGMENT_PAGE_CHATS;
                break;
            case BottomMenuView.Item_Moments:
                gotoPage(R.id.flContent, FRAGMENT_PAGE_MOMENTS, currentPageStr);
                currentPageStr = FRAGMENT_PAGE_MOMENTS;
                break;
            case BottomMenuView.Item_Connections:
                gotoPage(R.id.flContent, FRAGMENT_PAGE_CONNECTIONS, currentPageStr);
                currentPageStr = FRAGMENT_PAGE_CONNECTIONS;
                break;
            case BottomMenuView.Item_Discover:
                gotoPage(R.id.flContent, FRAGMENT_PAGE_BLOG_DISCOVER, currentPageStr);
                currentPageStr = FRAGMENT_PAGE_BLOG_DISCOVER;
                break;

        }
    }

    public void updateTitle() {
        String title;
        switch (bottomMenuView.currentItem) {
            case BottomMenuView.Item_Chats:
                title = getResources().getString(R.string.chats);
                break;
            case BottomMenuView.Item_Moments:
                title = getResources().getString(R.string.moments);
                break;
            case BottomMenuView.Item_Connections:
                title = getResources().getString(R.string.connections);
                break;
            case BottomMenuView.Item_Discover:
                title = getResources().getString(R.string.discover);
                break;
            default:
                title = getResources().getString(R.string.chats);

        }
        setCenterTitle(title);
        lastBottomItem = bottomMenuView.currentItem;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void refreshPage() {
        if (isFirstIn) {
            gotoPage(R.id.flContent, FRAGMENT_PAGE_CHATS, currentPageStr);
            currentPageStr = FRAGMENT_PAGE_CHATS;
            isFirstIn = false;
        } else {
            changePage();
            updateTitle();
        }
    }

    public void showBottomInput(int visibility) {
        editTextBodyLl.setVisibility(visibility);
    }

    public EditText getBottomEditText() {
        return (EditText) editTextBodyLl.findViewById(R.id.circleEt);
    }

    public ImageView getBottomImageView() {
        return (ImageView) editTextBodyLl.findViewById(R.id.sendIv);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
//        setEnterSharedElementCallback();
    }
}
