package top.wifistar.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.greysonparrelli.permiso.Permiso;
import com.jaeger.library.StatusBarUtil;
import com.kongzue.dialog.v2.WaitDialog;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.ruffian.library.RTextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import top.wifistar.R;

import top.wifistar.app.App;
import top.wifistar.bean.LocationBean;
import top.wifistar.bean.CNLocationBean;
import top.wifistar.bean.bmob.BmobUtils;
import top.wifistar.bean.bmob.Follow;
import top.wifistar.bean.bmob.User;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.customview.CircleImageView;
import top.wifistar.customview.ObservableScrollView;
import top.wifistar.event.RefreshAvatarsEvent;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.FollowRealm;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;


/**
 * Created by boyla on 2018/1/10.
 */

public class UserProfileActivity extends AppCompatActivity {

    private User shortUser;
    ImageView ivHead, ivHeadBg, ivRecentPic1, ivRecentPic2, ivRecentPic3, ivRecentPic4;
    private Toolbar mToolbar;
    private View mViewNeedOffset;
    ObservableScrollView scrollview;
    LinearLayout llDetail;
    View toolbar, vSendMail;
    LinearLayout llHeadAndInfo, llInfo, llEditInfo, llMoments;
    View flUp, vSex;
    TextView tvAddFan, tvInfo, tvName, tvSelfIntro, tvStartWord1;

    String follower;
    String followed;
    FollowRealm followRealm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Permiso.getInstance().setActivity(this);
        mViewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTransparentForImageView(this, mViewNeedOffset);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewNeedOffset = findViewById(R.id.view_need_offset);
        scrollview = (ObservableScrollView) findViewById(R.id.scrollview);
        llDetail = (LinearLayout) findViewById(R.id.llDetail);
        toolbar = findViewById(R.id.toolbar);
        llHeadAndInfo = (LinearLayout) findViewById(R.id.llHeadAndInfo);
        llInfo = (LinearLayout) findViewById(R.id.llInfo);
        flUp = findViewById(R.id.flUp);
        vSex = findViewById(R.id.vSex);
        llMoments = (LinearLayout) findViewById(R.id.llMoments);
        tvName = (TextView) findViewById(R.id.tvName);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvSelfIntro = (TextView) findViewById(R.id.tvSelfIntro);
        tvStartWord1 = (TextView) findViewById(R.id.tvStartWord1);
        llEditInfo = (LinearLayout) findViewById(R.id.llEditInfo);
        tvAddFan = (TextView) findViewById(R.id.tvAddFan);
        vSendMail = findViewById(R.id.vSendMail);
        ivHeadBg = (ImageView) findViewById(R.id.ivHeadBg);
        ivRecentPic1 = (ImageView) findViewById(R.id.ivRecentPic1);
        ivRecentPic2 = (ImageView) findViewById(R.id.ivRecentPic2);
        ivRecentPic3 = (ImageView) findViewById(R.id.ivRecentPic3);
        ivRecentPic4 = (ImageView) findViewById(R.id.ivRecentPic4);

        mToolbar.setNavigationIcon(R.drawable.back);
        shortUser = (User) getIntent().getExtras().getSerializable("ShortUser");
        ivHead = (ImageView) findViewById(R.id.ivHead);
        mToolbar.setTitle("");

        showUserInfo(shortUser);
        BmobUtils.querySingleUser(shortUser.getObjectId(),new BmobUtils.BmobDoneListener<User>() {
            @Override
            public void onSuccess(User res) {
                shortUser = res;
                BaseRealmDao.insertOrUpdate(shortUser.toRealmObject());
                showUserInfo(shortUser);
            }

            @Override
            public void onFailure(String msg) {
                Log.d("querySingleBmob failed:",msg);
            }
        });

        if (isSelfProfile()) {
            ivHeadBg.setOnClickListener((v) -> {
                changeHeadImgType = HEAD_IMG_TYPE_BG;
                showChangeHeadImgDialog(changeHeadImgType);
            });
            ivHead.setOnClickListener((v) -> {
                changeHeadImgType = HEAD_IMG_TYPE_AVATAR;
                showChangeHeadImgDialog(changeHeadImgType);
            });
        }
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        scrollview.setOnScrollChangedListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> scrollChangeHeader(scrollY));
        scrollview.post(() -> {
            initSlidingParams();
            setViewHeight();
        });

        /**
         *     先查询 https://freegeoip.net/json/， 获取外网IP和英文地理位置
         *     如果当前手机是中文环境，再根据外网IP查询 http://ip.taobao.com/service/getIpInfo.php?ip=125.69.107.89
         */
        String able = getResources().getConfiguration().locale.getCountry();
        boolean isChinese = able.equals("CN");
        String selfCountry = Utils.getCurrentShortUser().country;
        if (isSelfProfile()) {
            if (TextUtils.isEmpty(shortUser.city)) {
                if (!TextUtils.isEmpty(shortUser.country) && shortUser.country.equals(shortUser.region) || TextUtils.isEmpty(shortUser.region)) {
                    shortUser.loaction = shortUser.country;
                } else {
                    shortUser.loaction = shortUser.country + "丨" + shortUser.region;
                }
            } else {
                shortUser.loaction = shortUser.region + "丨" + shortUser.city;
            }
        } else {
            if (!TextUtils.isEmpty(selfCountry) && selfCountry.equals(shortUser.country)) {
                if (TextUtils.isEmpty(shortUser.city)) {
                    if (TextUtils.isEmpty(shortUser.region)) {
                        shortUser.loaction = shortUser.country;
                    } else {
                        shortUser.loaction = shortUser.country + "丨" + shortUser.region;
                    }
                } else {
                    shortUser.loaction = shortUser.region + "丨" + shortUser.city;
                }
            } else {
                if (TextUtils.isEmpty(shortUser.region)) {
                    shortUser.loaction = shortUser.country;
                } else {
                    shortUser.loaction = shortUser.country + "丨" + shortUser.region;
                }
            }
        }

        if (isSelfProfile()) {
            llEditInfo.setVisibility(View.VISIBLE);
            llInfo.setVisibility(View.GONE);
            llEditInfo.setOnClickListener((v) -> {
                Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
            new Thread(() -> {
                LocationBean locationBean = getLocationByIp(isChinese);
                if (locationBean != null) {
                    shortUser.country = locationBean.getCountry_name();
                    shortUser.region = locationBean.getRegion_name();
                    shortUser.city = locationBean.getCity();
                    if (TextUtils.isEmpty(shortUser.city)) {
                        if (shortUser.country.equals(shortUser.region) || TextUtils.isEmpty(shortUser.region)) {
                            shortUser.loaction = shortUser.country;
                        } else {
                            shortUser.loaction = shortUser.country + "丨" + shortUser.region;
                        }
                    } else {
                        shortUser.loaction = shortUser.region + "丨" + shortUser.city;
                    }
                    tvInfo.post(() -> {
                        tvInfo.setText(getAgeByBirth(shortUser.birth) + ", " + shortUser.loaction);
                        BmobUtils.updateUser(shortUser);
                    });
                }
            }).start();
        } else {
            llEditInfo.setVisibility(View.GONE);
            llInfo.setVisibility(View.VISIBLE);

            initFollow();
            tvAddFan.setOnClickListener((v) -> {
                WaitDialog waitDialog = WaitDialog.show(this,"请稍后...");
                if (followRealm != null && followRealm.isFollowing) {
                    //取消关注
                    Follow follow = followRealm.toBmobObject();
                    follow.isFollowing = false;
                    follow.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            waitDialog.dismiss();
                            if (e == null) {
                                BaseRealmDao.insertOrUpdate(follow.toRealmObject());
                                tvAddFan.setText("＋关注");
                            } else {
                                Utils.makeSysToast(e.getMessage());
                            }
                        }
                    });
                } else {
                    //添加关注
                    if(followRealm==null){
                        Follow follow = new Follow();
                        follow.follower = follower;
                        follow.followed = followed;
                        follow.isFollowing = true;
                        follow.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                waitDialog.dismiss();
                                if (e == null) {
                                    follow.setObjectId(s);
                                    BaseRealmDao.insertOrUpdate(follow.toRealmObject());
                                    tvAddFan.setText("已关注");
                                } else {
                                    Utils.makeSysToast(e.getMessage());
                                }
                            }
                        });
                    }else{
                        Follow follow = followRealm.toBmobObject();
                        follow.isFollowing = true;
                        follow.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                waitDialog.dismiss();
                                if (e == null) {
                                    BaseRealmDao.insertOrUpdate(follow.toRealmObject());
                                    tvAddFan.setText("已关注");
                                } else {
                                    Utils.makeSysToast(e.getMessage());
                                }
                            }
                        });
                    }

                }

            });
            vSendMail.setOnClickListener((v) -> {
                //TODO 私信
            });
        }
    }

    private void showUserInfo(User shortUser) {
        Utils.setUserAvatar(shortUser, ivHead, false);
        if (TextUtils.isEmpty(shortUser.headBgUrl)) {
            ivHeadBg.setImageResource(R.color.darkgray);
        } else {
            ivHeadBg.setImageResource(R.color.darkgray);
            Glide.with(this).load(shortUser.headBgUrl.split("_wh_")[0]).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivHeadBg);
        }
        tvName.setText(shortUser.getName());
        if (shortUser.sex == 1) {
            vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_male));
        } else {
            vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_female));
        }

        tvInfo.setText(getAgeByBirth(shortUser.birth) + (TextUtils.isEmpty(shortUser.loaction) ? ", 中国" : ", " + shortUser.loaction));
        setUserImgsAndInfo();
    }

    private int getAgeByBirth(String birthStr) {
        int age = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate;
        try {
             birthDate = sdf.parse(birthStr);
        } catch (Exception e) {
            e.printStackTrace();
            return age;
        }
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDate);
            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
                    age -= 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return age;
        }
    }

    private void initFollow() {
        follower = Utils.getCurrentShortUser().getObjectId();
        followed = shortUser.getObjectId();
        //from realm
        followRealm = BaseRealmDao.realm.where(FollowRealm.class).equalTo("follower", follower).equalTo("followed", followed).findFirst();
        //from bmob
        if(followRealm==null){
            BmobQuery<Follow> query = new BmobQuery<>();
            query.addWhereEqualTo("follower", follower);
            query.addWhereEqualTo("followed", followed);
            query.findObjects(new FindListener<Follow>() {
                @Override
                public void done(List<Follow> list, BmobException e) {
                    if(e==null && list.size()>0){
                        Follow follow = list.get(0);
                        if(follow.isFollowing){
                            tvAddFan.setText("已关注");
                        }
                        BaseRealmDao.insertOrUpdate(follow.toRealmObject());
                    }
                }
            });
        }else{
            if(followRealm.isFollowing){
                tvAddFan.setText("已关注");
            }
        }
    }

    private static final int HEAD_IMG_TYPE_AVATAR = 1;
    private static final int HEAD_IMG_TYPE_BG = 0;

    public static int changeHeadImgType;

    private void showChangeHeadImgDialog(int type) {
        //type: 0, 相册封面；1， 头像
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        RTextView tv = new RTextView(this);
        if (type == HEAD_IMG_TYPE_BG)
            tv.setText("更换相册封面");
        if (type == HEAD_IMG_TYPE_AVATAR)
            tv.setText("更换头像");
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColorNormal(Color.WHITE);
        tv.setBackgroundColorPressed(Color.LTGRAY);
        tv.setCornerRadius(5);
        tv.setPadding(50, 0, 0, 0);
        tv.setTextSize(16f);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setWidth(Utils.dip2px(this, 260));
        tv.setMinHeight(Utils.dip2px(this, 50));
        builder.setView(tv);
        AlertDialog dialog = builder.show();
        tv.setOnClickListener((v) -> {
            dialog.dismiss();
            Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                @Override
                public void onPermissionResult(Permiso.ResultSet resultSet) {
                    if (resultSet.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        jumpToPicSelectPage();
                    } else {
                        Utils.makeSysToast("打开相册需要文件读取权限，请到应用权限中进行设置");
                    }
                }

                @Override
                public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                    Permiso.getInstance().showRationaleInDialog("需要文件读取权限", "打开相册需要文件读取权限，请到应用权限中进行设置", null, callback);
                }
            }, Manifest.permission.READ_EXTERNAL_STORAGE);
        });
    }

    private static final int REQUEST_CAMERA_CODE = 10;
    private ArrayList<String> imagePaths = new ArrayList<>();

    private void jumpToPicSelectPage() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(1); // 最多选择照片数量，默认为6
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 得到图片路径，根据type显示并上传
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    if (list != null && list.size() > 0) {
                        String delUrl = changeHeadImgType == HEAD_IMG_TYPE_BG ? shortUser.headBgUrl : shortUser.headUrl;
                        if (!TextUtils.isEmpty(delUrl)) {
                            BmobUtils.deleteBmobFile(new String[]{delUrl.split("_wh_")[0]});
                        }
                        imagePaths.clear();
                        imagePaths.add(list.get(0));
                        Glide.with(this).load(list.get(0).split("_wh_")[0]).into(changeHeadImgType == 0 ? ivHeadBg : ivHead);
                        uploadPic(changeHeadImgType);
                    }
                    break;
            }
        }
    }

    //changeHeadImgType 0:相册封面  1:头像
    private void uploadPic(int headIvType) {
        String[] src = new String[imagePaths.size()];
        imagePaths.toArray(src);
        BmobFile.uploadBatch(imagePaths.toArray(src), new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == 1) {//如果数量相等，则代表文件全部上传完成
                    //获取图片宽高，并保存
                    for (int i = 0; i < urls.size(); i++) {
                        urls.set(i, urls.get(i) + Utils.getImageUrlWithWidthHeight(src[i]));
                    }
                    if (headIvType == HEAD_IMG_TYPE_BG) {
                        shortUser.headBgUrl = urls.get(0);
                    } else {
                        shortUser.headUrl = urls.get(0);
                    }
                    BmobUtils.updateUser(shortUser);
                    if (headIvType == HEAD_IMG_TYPE_AVATAR) {
                        EventUtils.post(new RefreshAvatarsEvent());
                    }
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                //ShowToast("错误码"+statuscode +",错误描述："+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

    private LocationBean getLocationByIp(boolean isChinese) {
        LocationBean locationBean = null;
        String urlStr, encodeType;
        if (isChinese) {
            urlStr = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js";
            encodeType = "utf-8";
        } else {
            urlStr = "https://freegeoip.net/json/";
            encodeType = "utf-8";
        }
        URL infoUrl;
        InputStream inStream = null;
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL(urlStr);
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setReadTimeout(2000);
            httpConnection.setConnectTimeout(2000);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, encodeType));
                StringBuilder strber = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                String res = strber.toString();
                if (!isChinese) {
                    locationBean = new Gson().fromJson(res, LocationBean.class);
                } else {
                    String cnStr = convert(res).replace(";", "").split(" = ")[1];
                    CNLocationBean CNLocationBean = new Gson().fromJson(cnStr, CNLocationBean.class);
                    if (CNLocationBean != null) {
                        locationBean = new LocationBean();
                        locationBean.setCountry_name(CNLocationBean.getCountry());
                        locationBean.setRegion_name(CNLocationBean.getProvince());
                        locationBean.setCity(CNLocationBean.getCity());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return locationBean;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityCompat.finishAfterTransition(this);
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    void setViewHeight() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        int toolBarHeight = toolbar.getHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight - toolBarHeight - Utils.getStatusBarHeight(this));
        llDetail.setLayoutParams(params);
    }

    int slidingDistance;

    int currScrollY = 0;

    /**
     * 初始化滑动参数,k值
     */
    private void initSlidingParams() {
        int headerSize = flUp.getHeight();
        int navBarHeight = toolbar.getHeight();
        slidingDistance = headerSize - navBarHeight;
        Log.d("HomeFragment", "slidingDistance" + slidingDistance);
    }

    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        if (scrolledY < slidingDistance) {
            int alpha = scrolledY * 192 / slidingDistance;
            setTopAlpha(alpha, scrolledY);
            currScrollY = scrolledY;
        } else {
            setTopAlpha(192, scrolledY);
            currScrollY = slidingDistance;
        }
    }

    private void setTopAlpha(int alpha, int scrolledY) {
        toolbar.setBackgroundColor(Color.argb(alpha, 0x00, 0x00, 0x00));
        StatusBarUtil.setTranslucentForImageView(this, alpha, mViewNeedOffset);
        setLinearAlpha(llHeadAndInfo, scrolledY);
        setLinearAlpha(llInfo, scrolledY);
    }

    private void setLinearAlpha(LinearLayout llHeadAndInfo, int scrolledY) {
        float percent = scrolledY / (float) slidingDistance * 2;
        if (percent > 1) {
            percent = 1;
        }
        int alph = (int) (255 * (1 - percent));
        int count = llHeadAndInfo.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = llHeadAndInfo.getChildAt(i);
            if (view instanceof LinearLayout) {
                setLinearAlpha(((LinearLayout) view), scrolledY);
            }
            if (view.getBackground() != null) {
                Drawable drawable = view.getBackground();
                drawable.mutate();
                drawable.setAlpha(alph);
            }
            if (view instanceof TextView) {
                if (view.getTag() == null) {
                    int selfAlpha = Color.alpha(((TextView) view).getCurrentTextColor());
                    view.setTag(selfAlpha);
                }
                int selfAlpha = (int) view.getTag();
                int textAlpha = (int) (selfAlpha * (1 - percent));
                ((TextView) view).setTextColor(Color.argb(textAlpha, 0xff, 0xff, 0xff));
            }
            if (view instanceof CircleImageView) {
                Drawable drawable = ((ImageView) view).getDrawable();
                if (drawable != null)
                    drawable.mutate().setAlpha(alph);
                ((CircleImageView) view).setImageDrawable(drawable);
                int rawColor = Utils.getLevelColor(this);
                int borderColor = alph << 24 | Color.red(rawColor) << 16 | Color.green(rawColor) << 8 | Color.blue(rawColor);
                ((CircleImageView) view).setBorderColor(borderColor);
            }
            if (percent < 0.8f) {
                mToolbar.setTitle("");
            } else {
                mToolbar.setTitle(shortUser.getName());
            }
        }
    }

    public String convert(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }
        if (pos < utfString.length()) {
            sb.append(utfString.substring(pos, utfString.length()));
        }
        return sb.toString();
    }

    public boolean isSelfProfile() {
        String curId = Utils.getCurrentShortUser().getObjectId();
        return curId.equals(shortUser.getObjectId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
        if (isSelfProfile()) {
            shortUser = Utils.getCurrentShortUser();
            UserProfile userProfile = App.currentUserProfile;
            tvName.setText(shortUser.getName());
            userProfile.nickName = shortUser.getName();
            userProfile.sex = shortUser.sex;
            if (shortUser.sex == 1) {
                vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_male));
            } else {
                vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_female));
            }
            if (!TextUtils.isEmpty(shortUser.startWord1)) {
                tvStartWord1.setText(shortUser.startWord1);
            }
            tvInfo.setText(getAgeByBirth(shortUser.birth) + (TextUtils.isEmpty(shortUser.loaction) ? ", 中国" : ", " + shortUser.loaction));
            if (!TextUtils.isEmpty(shortUser.selfIntroduce)) {
                tvSelfIntro.setText(shortUser.selfIntroduce);
            }
            userProfile.save();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void setUserImgsAndInfo() {
        if (TextUtils.isEmpty(shortUser.recentImgs)) {
            llMoments.setVisibility(View.GONE);
        } else {
            ImageView[] ivList = new ImageView[]{ivRecentPic1, ivRecentPic2, ivRecentPic3, ivRecentPic4};
            String[] urls = shortUser.recentImgs.split(",");
            for (int i = 0; i < urls.length; i++) {
                if (i == ivList.length) {
                    break;
                }
                Glide.with(this).load(urls[i].split("_wh_")[0]).into(ivList[i]);
            }
        }

        if (!TextUtils.isEmpty(shortUser.selfIntroduce)) {
            tvSelfIntro.setText(shortUser.selfIntroduce);
        }
        if (!TextUtils.isEmpty(shortUser.startWord1)) {
            tvStartWord1.setText(shortUser.startWord1);
        }
    }
}
