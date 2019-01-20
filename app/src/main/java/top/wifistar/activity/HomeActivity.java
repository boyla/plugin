package top.wifistar.activity;



import android.view.View;
import android.widget.ImageView;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.functions.Action1;
import top.wifistar.app.App;
import top.wifistar.app.BottomInputActivity;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.Installation;
import top.wifistar.bean.bmob.User;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.view.BottomMenuView;
import top.wifistar.R;
import top.wifistar.view.TopReminder;
import top.wifistar.event.BottomMenuItemClickEvent;
import top.wifistar.event.RefreshAvatarsEvent;
import top.wifistar.httpserver.NetUtils;
import top.wifistar.httpserver.ServerManager;
import top.wifistar.utils.ACache;
import top.wifistar.utils.EventUtils;
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
//            UpdateUtils.check4Update(this);
        } catch (Exception e) {
            System.out.println("Update exception:" + e.getMessage());
        }
        checkLogin();
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
                ConnectionStatus status = BmobIM.getInstance().getCurrentStatus();
                App.getApp().showReloginDialog("当前IM连接状态",status.getMsg());
                break;
            default:
                title = getResources().getString(R.string.chats);

        }
        setCenterTitle(title);
        lastBottomItem = bottomMenuView.currentItem;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    public void onBackPressed() {
        if(editTextBodyLl!=null && editTextBodyLl.getVisibility() == View.VISIBLE){
            editTextBodyLl.setVisibility(View.GONE);
        }else{
            moveTaskToBack(true);
        }
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

    public void checkLogin(){
        BmobInstallation bi = BmobInstallationManager.getInstance().getCurrentInstallation();
        BmobQuery<Installation> bmobQuery = new BmobQuery<>();
        User currentUser = Utils.getCurrentShortUser();
        if(currentUser==null){
            finish();
            return;
        }
        final String userId = currentUser.getObjectId();
        bmobQuery.addWhereEqualTo("userId", userId);
        bmobQuery.findObjectsObservable(Installation.class)
                .subscribe(new Action1<List<Installation>>() {
                    @Override
                    public void call(List<Installation> installations) {
                        String localId = BmobInstallationManager.getInstallationId();
                        //有用户信息
                        if (installations.size() > 0) {
                            Installation installation = installations.get(0);
                            String currentId = installation.getInstallationId();
                            if(!localId.equals(currentId)){
                                //不同设备登陆，发送PUSH，将上一个设备挤下线
                                App.getApp().pushAndroidMessage("LOGIN ON OTHER DEVICE",currentId);
                                //更新数据
                                installation.userId = "";
                                installation.update();

                                Installation ins = new Installation();
                                ins.setObjectId(bi.getObjectId());
                                ins.setInstallationId(localId);
                                ins.userId = userId;
                                ins.update();
                            }
//                            //删除多余的数据
//                            if (installations.size() > 1){
//                                List<BmobObject> res = new ArrayList<>();
//                                int len = installations.size() - 1;
//                                for(int i = len; i > 0; i--){
//                                    res.add(installations.get(i));
//                                }
//                                new BmobBatch().deleteBatch(res).doBatch(new QueryListListener<BatchResult>() {
//
//                                    @Override
//                                    public void done(List<BatchResult> o, BmobException e) {
//                                        if(e==null){
//                                            for(int i=0;i<o.size();i++){
//                                                BatchResult result = o.get(i);
//                                                BmobException ex =result.getError();
//                                                if(ex==null){
//                                                    System.out.println("第"+i+"个数据批量删除成功");
//                                                }else{
//                                                    System.out.println("第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
//                                                }
//                                            }
//                                        }else{
//                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                                        }
//                                    }
//                                });
//                            }

                        } else {
                            //通过userId没有找到,通过installationId查找，然后更新
                            BmobQuery<Installation> query = new BmobQuery<Installation>();
                            query.addWhereEqualTo("installationId", localId);
                            query.findObjects(new FindListener<Installation>() {

                                @Override
                                public void done(List<Installation> list, BmobException e) {
                                    Installation ins;
                                    if(e!=null || list==null || list.size()==0){
                                        //save data
                                        ins = new Installation();
                                        ins.setInstallationId(localId);
                                        ins.userId = userId;
                                        ins.save();
                                    }else{
                                        //update data
                                        ins = list.get(0);
                                        ins.userId = userId;
                                        ins.update();
                                    }

                                }
                            });
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("查询设备数据失败：" );
                    }
                });
    }


}
