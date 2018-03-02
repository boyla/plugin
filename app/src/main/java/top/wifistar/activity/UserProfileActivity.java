package top.wifistar.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ruffian.library.RTextView;

import top.wifistar.R;

import top.wifistar.app.App;
import top.wifistar.bean.bmob.User;
import top.wifistar.customview.CircleImageView;
import top.wifistar.customview.ObservableScrollView;
import top.wifistar.utils.Utils;


/**
 * Created by boyla on 2018/1/10.
 */

public class UserProfileActivity extends AppCompatActivity {

    private User shortUser;
    ImageView ivHead;
    private Toolbar mToolbar;
    private View mViewNeedOffset;
    ObservableScrollView scrollview;
    LinearLayout llDetail;
    View toolbar;
    LinearLayout llHeadAndInfo, llInfo, llEditInfo;
    View flUp,vSex;
    RTextView tvName, tvInfo;


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
        vSex  = findViewById(R.id.vSex);
        tvName = (RTextView) findViewById(R.id.tvName);
        tvInfo = (RTextView) findViewById(R.id.tvInfo);
        llEditInfo = (LinearLayout) findViewById(R.id.llEditInfo);

        mToolbar.setNavigationIcon(R.drawable.back);
        shortUser = (User) getIntent().getExtras().getSerializable("ShortUser");
        ivHead = (ImageView) findViewById(R.id.ivHead);
        mToolbar.setTitle("");

        Utils.setUserAvatar(shortUser, ivHead, false);

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
        if(shortUser.sex == 1){
            vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_male));
        }else{
            vSex.setBackgroundDrawable(getResources().getDrawable(R.drawable.sex_female));
        }
        tvInfo.setText(shortUser.age + (TextUtils.isEmpty(shortUser.loaction) ? "" : ", shortUser.loaction"));

        String curId = App.currentUserProfile.getObjectId();
        if (curId.equals(shortUser.id)) {
            llEditInfo.setVisibility(View.VISIBLE);
            llInfo.setVisibility(View.GONE);
        } else {
            llEditInfo.setVisibility(View.GONE);
            llInfo.setVisibility(View.VISIBLE);
        }
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
            if(view instanceof LinearLayout){
                setLinearAlpha(((LinearLayout) view),scrolledY);
            }
            if (view.getBackground() != null) {
                view.getBackground().setAlpha(alph);
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
}
