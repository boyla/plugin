package top.wifistar.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.wifistar.R;

/**
 * Created by boyla on 2018/6/28.
 */

public class BottomInputActivity extends ToolbarActivity {

    protected View editTextBodyLl;
    public List<ImageView> selfAvatarsInMomentList = new ArrayList<>();

    @Override
    protected void initTopBar() {

    }

    private View[] sharedViews;
    private int exitPosition;
    private int enterPosition;


    @TargetApi(21)
    private void setCallback(final int enterPosition) {
        this.enterPosition = enterPosition;
        setExitSharedElementCallback(new android.support.v4.app.SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (exitPosition != enterPosition &&
                        names.size() > 0 && exitPosition < sharedViews.length) {
                    names.clear();
                    sharedElements.clear();
                    View view = sharedViews[exitPosition];
                    names.add(view.getTransitionName());
                    sharedElements.put(view.getTransitionName(), view);
                }
                setExitSharedElementCallback((android.support.v4.app.SharedElementCallback) null);
                sharedViews = null;
            }
        });
    }

    private OnSharedViewListener sharedViewListener = new OnSharedViewListener() {
        @Override
        public void onSharedViewListener(View[] views, int enterPosition) {
            sharedViews = views;
            setCallback(enterPosition);
        }
    };

    public EditText getBottomEditText() {
        return (EditText) editTextBodyLl.findViewById(R.id.circleEt);
    }

    public ImageView getBottomImageView() {
        return (ImageView) editTextBodyLl.findViewById(R.id.sendIv);
    }

    public void showBottomInput(int visibility) {
        editTextBodyLl.setVisibility(visibility);
    }

    public OnSharedViewListener getSharedViewListener() {
        return sharedViewListener;
    }

    public interface OnSharedViewListener {
        void onSharedViewListener(View[] views, int enterPosition);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            exitPosition = data.getIntExtra("exit_position", enterPosition);
        }
    }

    @Override
    public void onBackPressed() {
        if(editTextBodyLl!=null && editTextBodyLl.getVisibility() == View.VISIBLE){
            editTextBodyLl.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}
