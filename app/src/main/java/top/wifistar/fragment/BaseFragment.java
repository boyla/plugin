/**
 * Copyright 2015 ZhangQu Li
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.wifistar.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.corepage.CorePageManager;
import top.wifistar.view.BadgeView;
import top.wifistar.utils.Utils;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected static final String TAG = BaseFragment.class.getSimpleName();

    protected Activity mActivity;

    protected Context mContext;

    protected Resources mResources;

    protected View mContentView;

    protected BaseFragment mBaseFragment;

    protected Toolbar mToolbar;

    protected boolean isVisible,isPrepared;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarActivity) {
            mToolbar = ((ToolbarActivity) getActivity()).getToolBar();
        }
    }

    private ScrollView mMyProflieScrollView;

    public final static String RED_CIRCLE_TAB = "litte_red_circle";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(mContentView == null){
            mContentView = createView(inflater, container, savedInstanceState);
        }

        mContext = mActivity = getActivity();

        mResources = getResources();

        mBaseFragment = this;

        isPrepared = true;

        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        removeLitteRedBadgeView();

        viewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        removeLitteRedBadgeView();
    }

    protected void removeLitteRedBadgeView() {
        if (getActivity() instanceof ToolbarActivity) {
            ToolbarActivity activity = (ToolbarActivity) getActivity();
            View redDot = activity.getToolBar().findViewWithTag(RED_CIRCLE_TAB);
            if (redDot != null) {
                ViewGroup vg = (ViewGroup) redDot.getParent();
                vg.removeView(redDot);
            }
        }
    }

    protected BadgeView createLitteBadgeView(Context context, View targetView) {
        BadgeView badgeView = new BadgeView(context, targetView);

        int width = Utils.dip2px(mContext, 14);
        badgeView.setHeight(width);

        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Medium.ttf");
        badgeView.setTypeface(typeFace);

        badgeView.setGravity(Gravity.CENTER);
        badgeView.setTextSize(COMPLEX_UNIT_DIP, 8);
        badgeView.setText("");
        badgeView.setTag(RED_CIRCLE_TAB);
        return badgeView;
    }


    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void viewCreated(View view, Bundle savedInstanceState);

    protected <T extends View> T bindViewById(int resId) {
        return (T) mContentView.findViewById(resId);
    }

    protected <T extends View> T bindViewById(int resId, boolean isSetOnClickListener) {
        T view = (T) mContentView.findViewById(resId);
        if (isSetOnClickListener) {
            view.setOnClickListener(this);
        }
        return view;
    }

    public void openPage(String nameAlias) {
        openPage(nameAlias, null);
    }

    public void openPage(String nameAlias, Bundle bundle) {
        CorePageManager.getInstance().openPage(mContext, nameAlias, bundle);
    }

    public void openPage(String nameAlias, Bundle bundle, int flags) {
        CorePageManager.getInstance().openPage(mContext, nameAlias, bundle, flags);
    }

    protected void openPageForResult(String nameAlias, int requestCode) {
        openPageForResult(nameAlias, null, requestCode);
    }

    protected void openPageForResult(String nameAlias, Bundle bundle, int requestCode) {
        CorePageManager.getInstance().openPageForResult(mActivity, nameAlias, bundle, requestCode);
    }

    public Fragment openPage(int fragmentContainerID, String pageName) {
        return openPage(fragmentContainerID, pageName, null, null, false);
    }

    public Fragment openPage(int fragmentContainerID, String pageName, Bundle bundle, int[] animations, boolean addToBackStack) {
        return CorePageManager.getInstance().openPage(fragmentContainerID, getFragmentManager(), pageName, bundle, animations, addToBackStack);
    }

    public Fragment gotoPage(int fragmentContainerID, String pageName, Bundle bundle, int[] animations) {
        return gotoPage(false, fragmentContainerID, pageName, bundle, animations);
    }

    public Fragment gotoPage(boolean openFragmentInThisFragment, int fragmentContainerID, String pageName, Bundle bundle, int[] animations) {
        return CorePageManager.getInstance().gotoPage(fragmentContainerID, openFragmentInThisFragment ? getChildFragmentManager() : getFragmentManager(), pageName, bundle, animations);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (getActivity() == null) return;
        //getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_no_ani);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (getActivity() == null) return;
       // getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_no_ani);
    }


    public ScrollView getMyProflieScrollView() {
        return mMyProflieScrollView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void invalidate(Bundle arguments) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        if(!isPrepared) {
            return;
        }else{
            lazyLoad();
        }

    }
    protected void lazyLoad() {

        //填充各控件的数据
    }
    protected void onInvisible(){}
}
