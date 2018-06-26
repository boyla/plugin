package top.wifistar.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.view.View;
import com.greysonparrelli.permiso.Permiso;
import top.wifistar.R;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.fragment.MomentsFragment;


public class UserMomentsActivity extends ToolbarActivity {

    MomentsFragment momentsFragment;

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
        super.setContentView(R.layout.activity_main);
        setToolbarTitle();
    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    protected void setToolbarTitle() {
        mToolbar.setNavigationIcon(R.drawable.back);
        setCenterTitle("新动态");
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

}
