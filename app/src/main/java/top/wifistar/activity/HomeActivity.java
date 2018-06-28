package top.wifistar.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.wifistar.app.App;
import top.wifistar.app.BottomInputActivity;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.customview.BottomMenuView;
import top.wifistar.R;
import top.wifistar.customview.TopReminder;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.event.RefreshAvatarsEvent;
import top.wifistar.httpserver.NetUtils;
import top.wifistar.httpserver.ServerManager;
import top.wifistar.utils.ACache;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.UpdateUtils;
import top.wifistar.utils.Utils;

import static top.wifistar.app.App.SELF_WLAN_SERVER_AVALIABLE;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_BLOG_DISCOVER;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_CHATS;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_CONNECTIONS;
import static top.wifistar.fragment.FragmentPageConfig.FRAGMENT_PAGE_MOMENTS;
import static top.wifistar.utils.ACache.PROFILE_CACHE;

public class HomeActivity extends BottomInputActivity {

    TopReminder topReminder;
    BottomMenuView bottomMenuView;
    int lastBottomItem = BottomMenuView.Item_None;
    protected String currentPageStr = null;
    boolean isFirstIn = true;
    public static HomeActivity INSTANCE;
    ServerManager mServerManager;

    @Override
    protected void initUI() {
        super.setContentView(R.layout.activity_main);
        INSTANCE = this;
        refreshTopAvatar();
        if (BUser.getCurrentUser() != null)
            App.currentUserProfile = (UserProfile) ACache.get(this).getAsObject(PROFILE_CACHE + BUser.getCurrentUser().getObjectId());
        topReminder = findViewById(R.id.topReminder);
        bottomMenuView = findViewById(R.id.bottomMenuView);
        editTextBodyLl = findViewById(R.id.editTextBodyLl);
        setToolbarTitle();
        EventUtils.registerEventBus(this);
        refreshPage();
        selfAvatarsInMomentList.add(mCustomLogo);
        //LAN server
        mServerManager = new ServerManager(this);
        mServerManager.register();
        mServerManager.startService();
        NetUtils.userJson = App.gson.toJson(Utils.getCurrentShortUser());
        new Thread(() -> NetUtils.scan()).start();
        try {
            UpdateUtils.check4Update(this);
        } catch (Exception e) {
            System.out.println("Update exception:" + e.getMessage());
        }
    }

    @Override
    protected void initTopBar() {

    }


    public static boolean isFirstLogin = false;

    protected void setToolbarTitle() {
        mToolbar.setNavigationIcon(null);
        mToolbar.setTitle("");
        tool_bar_frame.setVisibility(View.VISIBLE);
        updateTitle();
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EventUtils.unregisterEventBus(this);
        mServerManager.unRegister();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshAvatars(RefreshAvatarsEvent event) {
        for (ImageView item : selfAvatarsInMomentList) {
            Utils.setUserAvatar(Utils.getCurrentShortUser(), item);
        }
    }

    public void refreshTopAvatar() {
        Utils.setUserAvatar(mCustomLogo);
    }

    public void reSetAvatarList() {
        selfAvatarsInMomentList.clear();
        selfAvatarsInMomentList.add(mCustomLogo);
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


    public void onServerStart(String ip) {
        SELF_WLAN_SERVER_AVALIABLE = true;
        System.out.println("Self WLAN server started on " + ip);
    }

    public void onServerError(String msg) {
        SELF_WLAN_SERVER_AVALIABLE = false;
        System.out.println("Self WLAN server error: " + msg);
        refreshServer();
    }

    public void onServerStop() {
        SELF_WLAN_SERVER_AVALIABLE = false;
        System.out.println("Self WLAN server stopped");
        refreshServer();
    }

    private void refreshServer() {
        if (!SELF_WLAN_SERVER_AVALIABLE) {
            System.out.println("Restart WLAN server...");
            mServerManager.stopService();
            App.getHandler().postDelayed(() -> mServerManager.startService(), 2222);
        }
    }




}
