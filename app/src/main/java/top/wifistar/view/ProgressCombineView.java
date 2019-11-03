package top.wifistar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.wifistar.R;


public class ProgressCombineView extends RelativeLayout {

    private static final String TAG_LOADING = "ProgressActivity.TAG_LOADING";
    private static final String TAG_EMPTY = "ProgressActivity.TAG_EMPTY";
    private static final String TAG_ERROR = "ProgressActivity.TAG_ERROR";
    private static final String TAG_CUSTOM = "ProgressActivity.TAG_CUSTOM";
    private int default_text_color;
    private int default_background_color;

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";
    final String CUSTOM = "type_custom";

    LayoutInflater inflater;
    View view;
    View customStateView;
    int customLayout;
    LayoutParams layoutParams;
    Drawable currentBackground;

    List<View> contentViews = new ArrayList<>();

    RelativeLayout loadingStateRelativeLayout;
    //    DotsTextView dotsTextView;
    ImageView iv_loading;
    TextView textview;
    View emptyRefreshImageView;

    RelativeLayout emptyStateRelativeLayout;
    ImageView emptyStateImageView;
    TextView emptyStateTitleTextView;
    TextView emptyStateContentTextView;

    RelativeLayout errorStateRelativeLayout;
    LinearLayout errorViewLinearLayout;
    ImageView errorStateImageView;
    TextView errorStateTitleTextView;
    TextView errorStateContentTextView;
    ImageView errorStateButton;
    TextView errorNetOffTextView;


    int loadingStateProgressBarWidth;
    int loadingStateProgressBarHeight;
    int loadingStateBackgroundColor;
    String loadingStateText;
    int loadingStateTextColor;
    boolean loadingStateDotPlay;

    int emptyStateImageWidth;
    int emptyStateImageHeight;
    int emptyStateTitleTextSize;
    int emptyStateContentTextSize;
    int emptyStateTitleTextColor;
    int emptyStateContentTextColor;
    int emptyStateBackgroundColor;

    int errorStateImageWidth;
    int errorStateImageHeight;
    int errorStateTitleTextSize;
    int errorStateContentTextSize;
    int errorStateTitleTextColor;
    int errorStateContentTextColor;
    int errorStateButtonTextColor;
    int errorStateBackgroundColor;
    Animation progressRoateAnimation;
    private String state = CONTENT;

    public ProgressCombineView(Context context) {
        super(context);
    }

    public ProgressCombineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ProgressCombineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        default_text_color = getResources().getColor(R.color.progress_error_text);
        default_background_color = getResources().getColor(R.color.container_bg);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.progressCombine);
        //Loading state attrs
        loadingStateProgressBarWidth =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_loadingProgressBarWidth, 158);
        loadingStateProgressBarHeight =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_loadingProgressBarHeight, 158);
        loadingStateBackgroundColor =
                typedArray.getColor(R.styleable.progressCombine_loadingBackgroundColor, Color.TRANSPARENT);
        loadingStateText =
                typedArray.getString(R.styleable.progressCombine_loadingStateText);
        loadingStateTextColor =
                typedArray.getColor(R.styleable.progressCombine_loadingStateTextColor, getResources().getColor(R.color.text_dark));
        loadingStateDotPlay =
                typedArray.getBoolean(R.styleable.progressCombine_loadingStateDotPlay, true);
        //Empty state attrs
        emptyStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_emptyImageWidth, 308);
        emptyStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_emptyImageHeight, 308);
        emptyStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_emptyTitleTextSize, 14);
        emptyStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_emptyContentTextSize, 14);
        emptyStateTitleTextColor =
                typedArray.getColor(R.styleable.progressCombine_emptyTitleTextColor, default_text_color);
        emptyStateContentTextColor =
                typedArray.getColor(R.styleable.progressCombine_emptyContentTextColor, default_text_color);
        emptyStateBackgroundColor =
                typedArray.getColor(R.styleable.progressCombine_emptyBackgroundColor, Color.TRANSPARENT);

        //Error state attrs
        errorStateImageWidth =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_errorImageWidth, 308);
        errorStateImageHeight =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_errorImageHeight, 308);
        errorStateTitleTextSize =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_errorTitleTextSize, 22);
        errorStateContentTextSize =
                typedArray.getDimensionPixelSize(R.styleable.progressCombine_errorContentTextSize, 20);
        errorStateTitleTextColor =
                typedArray.getColor(R.styleable.progressCombine_errorTitleTextColor, default_text_color);
        errorStateContentTextColor =
                typedArray.getColor(R.styleable.progressCombine_errorContentTextColor, default_text_color);
        errorStateButtonTextColor =
                typedArray.getColor(R.styleable.progressCombine_errorButtonTextColor, default_text_color);
        errorStateBackgroundColor =
                typedArray.getColor(R.styleable.progressCombine_errorBackgroundColor, Color.TRANSPARENT);
        //custom view
        customLayout =
                typedArray.getResourceId(R.styleable.progressCombine_customLayout, R.layout.progress_empty_view);
        if (customStateView == null) {
            customStateView = inflater.inflate(customLayout, null);
            customStateView.setTag(TAG_CUSTOM);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            addView(customStateView, layoutParams);
        }
        emptyRefreshImageView = customStateView.findViewById(R.id.emptyStateImageView);
        customStateView.setVisibility(View.GONE);
        typedArray.recycle();
        currentBackground = this.getBackground();
    }

    @Override
    public void addView(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR) && !child.getTag().equals(TAG_CUSTOM))) {

            contentViews.add(child);
        }
    }

    /**
     * Hide all other states and show content
     */
    public void showContent() {
        switchState(CONTENT, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide all other states and show content
     *
     * @param skipIds Ids of views not to show
     */
    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, null, null, null, null, null, skipIds);
    }

    /**
     * Hide content and show the progress bar
     */
    public void showLoading() {
        switchState(LOADING, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Hide content and show the progress bar
     *
     * @param skipIds Ids of views to not hide
     */
    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, null, null, null, null, null, skipIds);
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable Drawable to show
     * @param emptyTextTitle     Title of the empty view to show
     * @param emptyTextContent   Content of the empty view to show
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent) {
        switchState(EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, null, Collections.<Integer>emptyList());
    }

    /**
     * Show empty view when there are not data to show
     *
     * @param emptyImageDrawable Drawable to show
     * @param emptyTextTitle     Title of the empty view to show
     * @param emptyTextContent   Content of the empty view to show
     * @param skipIds            Ids of views to not hide
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextTitle, String emptyTextContent, List<Integer> skipIds) {
        switchState(EMPTY, emptyImageDrawable, emptyTextTitle, emptyTextContent, null, null, skipIds);
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable Drawable to show
     * @param errorTextTitle     Title of the error view to show
     * @param errorTextContent   Content of the error view to show
     * @param errorButtonText    Text on the error view button to show
     * @param onClickListener    Listener of the error view button
     */
    public void showNetworkError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, String errorButtonText, OnClickListener onClickListener) {
        switchState(ERROR, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, onClickListener, Collections.<Integer>emptyList());
    }

    public void showNetworkError(OnClickListener onClickListener) {
        switchState(ERROR, getResources().getDrawable(R.drawable.loading_failed), getResources().getString(R.string.network_failed_title), getResources().getString(R.string.Oops_Loading_failed), null, onClickListener, Collections.<Integer>emptyList());
    }

    public void showNetworkError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, OnClickListener onClickListener) {
        showNetworkError(errorImageDrawable, errorTextTitle, errorTextContent, null, onClickListener);
    }

    public void showCustom() {
        switchState(CUSTOM, null, null, null, null, null, Collections.<Integer>emptyList());
    }

    public void showCustom(OnClickListener onClickListener) {
        switchState(CUSTOM, null, null, null, null, onClickListener, Collections.<Integer>emptyList());
    }

    /**
     * Show error view with a button when something goes wrong and prompting the user to try again
     *
     * @param errorImageDrawable Drawable to show
     * @param errorTextTitle     Title of the error view to show
     * @param errorTextContent   Content of the error view to show
     * @param errorButtonText    Text on the error view button to show
     * @param onClickListener    Listener of the error view button
     * @param skipIds            Ids of views to not hide
     */
    public void showNetworkError(Drawable errorImageDrawable, String errorTextTitle, String errorTextContent, String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        switchState(ERROR, errorImageDrawable, errorTextTitle, errorTextContent, errorButtonText, onClickListener, skipIds);
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public String getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state.equals(CONTENT);
    }

    /**
     * Check if loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state.equals(LOADING);
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state.equals(EMPTY);
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state.equals(ERROR);
    }

    public boolean isCustom() {
        return state.equals(CUSTOM);
    }

    private void switchState(String state, Drawable drawable, String errorText, String errorTextContent,
                             String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        this.state = state;

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                hideLoadingView();
                hideEmptyView();
                hideErrorView();
                hideCustomView();
                setContentVisibility(true, skipIds);
                break;
            case LOADING:
                hideEmptyView();
                hideErrorView();
                hideCustomView();
                setLoadingView();
                setContentVisibility(false, skipIds);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();
                hideCustomView();
                setEmptyView();
                if(drawable!=null){
                    emptyStateImageView.setImageDrawable(drawable);
                    emptyStateImageView.setVisibility(View.VISIBLE);
                }else {
                    emptyStateImageView.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(errorText)){
                    emptyStateTitleTextView.setText(errorText);
                    emptyStateTitleTextView.setVisibility(View.VISIBLE);
                }else {
                    emptyStateTitleTextView.setVisibility(View.GONE);
                }
                if(!TextUtils.isEmpty(errorTextContent)){
                    emptyStateContentTextView.setText(errorTextContent);
                    emptyStateContentTextView.setVisibility(View.VISIBLE);
                }else {
                    emptyStateContentTextView.setVisibility(View.GONE);
                }
                setContentVisibility(false, skipIds);
                break;
            case CUSTOM:
                hideLoadingView();
                hideErrorView();
                hideEmptyView();
                setCustomView();
                if (emptyRefreshImageView != null) {
                    emptyRefreshImageView.setOnClickListener(onClickListener);
                }
                setContentVisibility(false, skipIds);
                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();
                hideCustomView();
                setErrorView();
                errorStateImageView.setImageDrawable(drawable);
                errorStateTitleTextView.setText(errorText);
                errorStateContentTextView.setText(errorTextContent);
                errorViewLinearLayout.setOnClickListener(onClickListener);
                setContentVisibility(false, skipIds);
                break;
        }
    }

    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_loading_view, null);
            loadingStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadingStateRelativeLayout);
            loadingStateRelativeLayout.setTag(TAG_LOADING);
            progressRoateAnimation =
                    AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotation);

            iv_loading = (ImageView) view.findViewById(R.id.iv_loading);

//            textview = (TextView) view.findViewById(R.id.textview);
//            if (loadingStateText != null) {
//                textview.setText(loadingStateText);
//            }
//            textview.setTextColor(loadingStateTextColor);
//            dotsTextView.setTextColor(loadingStateTextColor);
            if (loadingStateDotPlay) {
                iv_loading.setVisibility(View.VISIBLE);
                iv_loading.startAnimation(progressRoateAnimation);
//                dotsTextView.showAndPlay();
            } else {
                iv_loading.setVisibility(View.GONE);
//                dotsTextView.setVisibility(View.GONE);
            }


            //Set background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(loadingStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_empty_view, null);
            emptyStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyStateRelativeLayout);
            emptyStateRelativeLayout.setTag(TAG_EMPTY);

            emptyStateImageView = (ImageView) view.findViewById(R.id.emptyStateImageView);
            emptyStateTitleTextView = (TextView) view.findViewById(R.id.emptyStateTitleTextView);
            emptyStateContentTextView = (TextView) view.findViewById(R.id.emptyStateContentTextView);

            //Set empty state image width and height
           /* emptyStateImageView.getLayoutParams().width = emptyStateImageWidth;
            emptyStateImageView.getLayoutParams().height = emptyStateImageHeight;
            emptyStateImageView.requestLayout();*/

//            emptyStateTitleTextView.setTextSize(emptyStateTitleTextSize);
//            emptyStateContentTextView.setTextSize(emptyStateContentTextSize);
            emptyStateTitleTextView.setTextColor(emptyStateTitleTextColor);
            emptyStateContentTextView.setTextColor(emptyStateContentTextColor);

            //Set background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(emptyStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(emptyStateRelativeLayout, layoutParams);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setCustomView() {
        customStateView.setVisibility(View.VISIBLE);

    }

    public void showOrHideOff(boolean isShow){
        if (errorNetOffTextView != null){
            if (isShow){
                errorNetOffTextView.setVisibility(View.VISIBLE);
            }else {
                errorNetOffTextView.setVisibility(View.GONE);
            }
        }

    }

    private void setErrorView() {
        if (errorStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_error_view, null);
            errorStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.errorStateRelativeLayout);
            errorStateRelativeLayout.setTag(TAG_ERROR);
            errorViewLinearLayout = (LinearLayout) view.findViewById(R.id.errorViewLinearLayout);
            errorStateImageView = (ImageView) view.findViewById(R.id.errorStateImageView);
            errorStateTitleTextView = (TextView) view.findViewById(R.id.errorStateTitleTextView);
            errorStateContentTextView = (TextView) view.findViewById(R.id.errorStateContentTextView);
            errorStateButton = (ImageView) view.findViewById(R.id.errorStateButton);
            errorNetOffTextView = (TextView) view.findViewById(R.id.error_tv_network_offline);

            //Set error state image width and height
            /*errorStateImageView.getLayoutParams().width = errorStateImageWidth;
            errorStateImageView.getLayoutParams().height = errorStateImageHeight;
            errorStateImageView.requestLayout();*/

 //           errorStateTitleTextView.setTextSize(errorStateTitleTextSize);
 //           errorStateContentTextView.setTextSize(errorStateContentTextSize);
            errorStateTitleTextView.setTextColor(errorStateTitleTextColor);
            errorStateContentTextView.setTextColor(errorStateContentTextColor);
          //  errorStateButton.setTextColor(errorStateButtonTextColor);

            //Set background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundColor(errorStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : contentViews) {
            if (!skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (loadingStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }

    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
                ;
            }
        }
    }

    private void hideCustomView() {
        if (customStateView != null) {
            customStateView.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);

            //Restore the background color if not TRANSPARENT
            if (errorStateBackgroundColor != Color.TRANSPARENT) {
                this.setBackgroundDrawable(currentBackground);
            }
        }
    }
}