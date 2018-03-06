package top.wifistar.activity;

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
import com.jaeger.library.StatusBarUtil;
import com.ruffian.library.RTextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import top.wifistar.R;

import top.wifistar.app.App;
import top.wifistar.bean.LocationBean;
import top.wifistar.bean.CNLocationBean;
import top.wifistar.bean.bmob.BmobUtils;
import top.wifistar.bean.bmob.User;
import top.wifistar.customview.CircleImageView;
import top.wifistar.customview.ObservableScrollView;
import top.wifistar.utils.Utils;


/**
 * Created by boyla on 2018/1/10.
 */

public class UserProfileActivity extends AppCompatActivity {

    private User shortUser;
    ImageView ivHead, ivHeadBg;
    private Toolbar mToolbar;
    private View mViewNeedOffset;
    ObservableScrollView scrollview;
    LinearLayout llDetail;
    View toolbar, vSendMail;
    LinearLayout llHeadAndInfo, llInfo, llEditInfo;
    View flUp, vSex;
    TextView tvAddFan, tvInfo, tvName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        tvName = (TextView) findViewById(R.id.tvName);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        llEditInfo = (LinearLayout) findViewById(R.id.llEditInfo);
        tvAddFan = (TextView) findViewById(R.id.tvAddFan);
        vSendMail = findViewById(R.id.vSendMail);
        ivHeadBg = (ImageView) findViewById(R.id.ivHeadBg);

        mToolbar.setNavigationIcon(R.drawable.back);
        shortUser = (User) getIntent().getExtras().getSerializable("ShortUser");
        ivHead = (ImageView) findViewById(R.id.ivHead);
        mToolbar.setTitle("");

        Utils.setUserAvatar(shortUser, ivHead, false);
        if (TextUtils.isEmpty(shortUser.headBgUrl)) {
            ivHeadBg.setImageResource(R.drawable.splash);
        } else {
            ivHeadBg.setImageResource(R.color.darkgray);
            Glide.with(this).load(shortUser.headBgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivHeadBg);
        }
        if(isSelfProfile()){
            ivHeadBg.setOnClickListener((v) -> {
                showChangeHeadImgDialog(0);
            });
            ivHead.setOnClickListener((v) -> {
                showChangeHeadImgDialog(1);
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

        tvName.setText(shortUser.getName());
        if (shortUser.sex == 1) {
            vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_male));
        } else {
            vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_female));
        }
        /**
         *     先查询 https://freegeoip.net/json/， 获取外网IP和英文地理位置
         *     如果当前手机是中文环境，再根据外网IP查询 http://ip.taobao.com/service/getIpInfo.php?ip=125.69.107.89
         */
        String able = getResources().getConfiguration().locale.getCountry();
        boolean isChinese = able.equals("CN");
        String selfCountry = Utils.getCurrentShortUser().country;
        if (!TextUtils.isEmpty(selfCountry) && selfCountry.equals(shortUser.country)) {
            if (TextUtils.isEmpty(shortUser.city)) {
                shortUser.loaction = shortUser.region;
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
        tvInfo.setText(shortUser.age + (TextUtils.isEmpty(shortUser.loaction) ? ", 中国" : ", " + shortUser.loaction));

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
                        shortUser.loaction = shortUser.region;
                    } else {
                        shortUser.loaction = shortUser.region + "丨" + shortUser.city;
                    }
                    tvInfo.post(() -> {
                        tvInfo.setText(shortUser.age + ", " + shortUser.loaction);
                        BmobUtils.updateUser(shortUser);
                    });
                }
            }).start();
        } else {
            llEditInfo.setVisibility(View.GONE);
            llInfo.setVisibility(View.VISIBLE);
            tvAddFan.setOnClickListener((v) -> {
                //TODO 添加关注
            });
            vSendMail.setOnClickListener((v) -> {
                //TODO 私信
            });
        }
    }

    private void showChangeHeadImgDialog(int type) {
        //type: 0, 相册封面；1， 头像
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        RTextView tv = new RTextView(this);
        if (type == 0)
            tv.setText("更换相册封面");
        if (type == 1)
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
        tv.setOnClickListener((v) -> {
            Utils.makeSysToast("选择图片");
            alertDialog.dismiss();
        });
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = tv.getHeight();   //高度设置为屏幕的0.3
//        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
////        alertDialog.getWindow().setAttributes(p);
//        alertDialog.getWindow().setLayout(p.width, p.height);
//        alertDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        builder.show();
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

    public boolean isSelfProfile(){
        String curId = App.currentUserProfile.getObjectId();
        return  curId.equals(shortUser.id);
    }
}
