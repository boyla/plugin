package top.wifistar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import top.wifistar.R;
import top.wifistar.utils.Utils;


public class ThemeImageView extends android.support.v7.widget.AppCompatImageView {

    private int fixedColor;
    private TypedArray typedArray;
    private Boolean needTheme = false;
    private Boolean needThemeInNormal = false;

    public ThemeImageView(Context context) {
        super(context);
        initView();
    }

    public ThemeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThemeImageView);
        initView();
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public ThemeImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThemeImageView);
//        initView();
//    }

    public ThemeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThemeImageView);
        initView();
    }

    private void initView() {
        if (typedArray != null) {
            needTheme = typedArray.getBoolean(R.styleable.ThemeImageView_needTheme, false);
            needThemeInNormal = typedArray.getBoolean(R.styleable.ThemeImageView_needThemeInNormal, false);
            fixedColor = typedArray.getColor(R.styleable.ThemeImageView_fixedColor, -1);
            if (fixedColor != -1 && !needTheme) {
                this.setColorFilter(fixedColor, PorterDuff.Mode.SRC_IN);
            }
            if (needThemeInNormal) {
                this.setColorFilter(ContextCompat.getColor(getContext(), Utils.getLevelColorID(getContext())), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (!needTheme) {
            return;
        }
        if (pressed) {
            this.setColorFilter(ContextCompat.getColor(getContext(), Utils.getLevelColorID(getContext())), PorterDuff.Mode.SRC_IN);
        } else if (!this.isSelected() && !needThemeInNormal) {
            this.clearColorFilter();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (!needTheme) {
            return;
        }
        if (selected) {
            this.setColorFilter(ContextCompat.getColor(getContext(), Utils.getLevelColorID(getContext())), PorterDuff.Mode.SRC_IN);
        } else {
            this.setColorFilter(ContextCompat.getColor(getContext(), R.color.bottom_tab_text_color_normal), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!needTheme) {
            return;
        }
        if (!enabled) {
            int color = Utils.getLevelColorID(getContext());
            this.setColorFilter(ContextCompat.getColor(getContext(), color), PorterDuff.Mode.SRC_IN);
        } else if (!needThemeInNormal) {
            this.clearColorFilter();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!needTheme) {
            return;
        }
        if (gainFocus) {
            int color = Utils.getLevelColorID(getContext());
            this.setColorFilter(ContextCompat.getColor(getContext(), color), PorterDuff.Mode.SRC_IN);
        } else if (!needThemeInNormal) {
            this.clearColorFilter();
        }
    }
}

