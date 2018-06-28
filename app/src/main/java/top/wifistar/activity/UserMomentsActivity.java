package top.wifistar.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.greysonparrelli.permiso.Permiso;

import top.wifistar.R;
import top.wifistar.app.BottomInputActivity;
import top.wifistar.bean.bmob.User;
import top.wifistar.fragment.MomentsFragment;


public class UserMomentsActivity extends BottomInputActivity {

    MomentsFragment momentsFragment;
    User currentUser;

    @Override
    protected void initTopBar() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Permiso.getInstance().setActivity(this);
    }

    @Override
    protected void initUI() {
        super.setContentView(R.layout.activity_user_moments);
        setToolbarTitle();

        editTextBodyLl = findViewById(R.id.editTextBodyLl);
        //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
        //在系统中原生的Fragment是通过getFragmentManager获得的。
        FragmentManager fm = getSupportFragmentManager();
        //2.开启一个事务，通过调用beginTransaction方法开启。
        FragmentTransaction ft = fm.beginTransaction();
        //把自己创建好的fragment创建一个对象
        momentsFragment = new MomentsFragment();
        momentsFragment.setUser(currentUser);
        //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
        ft.add(R.id.flContent, momentsFragment);
        //提交事务，调用commit方法提交。
        ft.commit();
    }

    @Override
    protected void getExtraData() {
        currentUser = (User) getIntent().getSerializableExtra("user");
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    protected void setToolbarTitle() {
        mToolbar.setNavigationIcon(R.drawable.back);
        setCenterTitle("用户动态");
        tool_bar_frame.setVisibility(View.VISIBLE);
        mCustomLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public EditText getBottomEditText() {
        return (EditText) editTextBodyLl.findViewById(R.id.circleEt);
    }

    public ImageView getBottomImageView() {
        return (ImageView) editTextBodyLl.findViewById(R.id.sendIv);
    }
}
