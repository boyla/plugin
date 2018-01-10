package top.wifistar.activity;

import android.view.View;
import android.widget.ImageView;

import top.wifistar.R;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.bean.bmob.User;
import top.wifistar.utils.Utils;

/**
 * Created by boyla on 2018/1/10.
 */

public class UserProfileActivity extends ToolbarActivity {

    private User shortUser;
    ImageView ivHead;

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initUI() {
        super.setContentView(R.layout.activity_user_profile);
        ivHead = (ImageView) findViewById(R.id.ivHead);
        setToolbarTitle();
        Utils.setUserAvatar(shortUser,ivHead);
    }

    protected void setToolbarTitle() {
        mToolbar.setNavigationIcon(R.drawable.back);
        tool_bar_frame.setVisibility(View.GONE);
        setTitle("");
        setCenterTitle("title");
        invalidateOptionsMenu();
    }

    @Override
    protected void getExtraData() {
        shortUser = (User) getIntent().getExtras().getSerializable("ShortUser");
    }
}
