package top.wifistar.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import top.wifistar.utils.Utils;

/**
 * Created by boyla on 2017/12/25.
 */

public class OnTouchXRecyclerView extends XRecyclerView {

    public OnTouchListener listener;

    public OnTouchXRecyclerView(Context context) {
        super(context);
    }

    public OnTouchXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public OnTouchXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Utils.closeKeyboard((Activity) getContext());
        if (listener != null)
            listener.onTouch();
        return super.onTouchEvent(ev);
    }

    public interface OnTouchListener {
        void onTouch();
    }
}
