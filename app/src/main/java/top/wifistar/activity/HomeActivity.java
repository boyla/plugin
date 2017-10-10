package top.wifistar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.flatbuffers.FlatBufferBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

//import io.rong.imkit.RongIM;
//import io.rong.imkit.fragment.ConversationListFragment;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Conversation;
//import io.rong.imlib.model.UserInfo;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import top.wifistar.customview.BottomMenuView;
import top.wifistar.customview.LoadingView;
import top.wifistar.R;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.bean.User;
import top.wifistar.customview.TopReminder;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.fragment.FragmentPageConfig;
import top.wifistar.im.IMUser;
import top.wifistar.service.WiFiNetworkService;
import top.wifistar.utils.ACache;
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
    int lastBottomItem = BottomMenuView.Item_None;
    protected String currentPageStr = null;
    boolean isFirstIn = true;

    @Override
    protected void initUI() {

        super.setContentView(R.layout.activity_main);
        topReminder = (TopReminder) findViewById(R.id.topReminder);
        bottomMenuView = (BottomMenuView) findViewById(R.id.bottomMenuView);
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
        mCustomLogo.setVisibility(View.VISIBLE);
        updateTitle();
        Utils.setUserAvatar(this,mCustomLogo);
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

    public void changePage(){
        if(lastBottomItem == bottomMenuView.currentItem){
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

    public void updateTitle(){
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

    private void refreshPage(){
        if(isFirstIn){
            gotoPage(R.id.flContent, FRAGMENT_PAGE_CHATS, currentPageStr);
            currentPageStr = FRAGMENT_PAGE_CHATS;
            isFirstIn = false;
        }else{
            changePage();
            updateTitle();
        }
    }
}
