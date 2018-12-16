package top.wifistar.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.wifistar.R;
import top.wifistar.app.App;
import top.wifistar.view.MultiImageView;
import top.wifistar.utils.DisplayUtils;
import top.wifistar.utils.ImageUtils;


/**
 * Created by yiw on 2016/1/6.
 */
public class ImagePagerActivity extends YWActivity {
    public static final String INTENT_IMGURLS = "imgurls";
    public static final String INTENT_IMAGESIZE = "imagesize";
    public volatile static int current;

    private List<View> guideViewList = new ArrayList<View>();
    private LinearLayout guideGroup;
    public ImageSize imageSize;
    private ArrayList<String> imgUrls;
    private ViewPager viewPager;
    private ImageAdapter mAdapter;
    private int adapterPosition;


    @SuppressLint("RestrictedApi")
    public static void startImagePagerActivity(MultiImageView startView, Context context, List<String> imgUrls, int adapterPosition, int picPosition, ImageSize imageSize) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putStringArrayListExtra(INTENT_IMGURLS, new ArrayList<>(imgUrls));
        intent.putExtra(INTENT_IMAGESIZE, imageSize);

        intent.putExtra("adapter_position", adapterPosition);
        intent.putExtra("current", picPosition);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View view = startView.getSharedViews()[picPosition];
            String str = view.getTransitionName();
            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, view, str).toBundle();
            ((AppCompatActivity) context).startActivityForResult(intent, 0, options);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepager);

        Intent intent = getIntent();
        adapterPosition = intent.getIntExtra("adapter_position", 0);
        current = intent.getIntExtra("current", 0);

        viewPager = (ViewPager) findViewById(R.id.pager);
        guideGroup = (LinearLayout) findViewById(R.id.guideGroup);

        getIntentData();

        mAdapter = new ImageAdapter(this);
        mAdapter.setDatas(imgUrls);
        mAdapter.setImageSize(imageSize);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                View current = getItemViewByTag(viewPager.getCurrentItem());
                if (current != null) {
                    setSharedElementCallback(current.findViewById(R.id.image));
                }
                for (int i = 0; i < guideViewList.size(); i++) {
                    guideViewList.get(i).setSelected(i == position ? true : false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(current);
        addGuideView(guideGroup, current, imgUrls);

    }

    private void getIntentData() {
        imgUrls = getIntent().getStringArrayListExtra(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);
    }

    private void addGuideView(LinearLayout guideGroup, int current, ArrayList<String> imgUrls) {
        if (imgUrls != null && imgUrls.size() > 0) {
            guideViewList.clear();
            for (int i = 0; i < imgUrls.size(); i++) {
                View view = new View(this);
                view.setBackgroundResource(R.drawable.selector_guide_bg);
                view.setSelected(i == current ? true : false);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.gudieview_width), getResources().getDimensionPixelSize(R.dimen.gudieview_heigh));
                layoutParams.setMargins(10, 0, 0, 0);
                guideGroup.addView(view, layoutParams);
                guideViewList.add(view);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStartPostTransition(final View sharedView) {
        sharedView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    // @Override
                    public boolean onPreDraw() {
                        sharedView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return false;
                    }
                });
    }

    private class ImageAdapter extends PagerAdapter {

        private List<String> datas = new ArrayList<String>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;

        public void setDatas(List<String> datas) {
            if (datas != null)
                this.datas = datas;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (datas == null) return 0;
            return datas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            final String imgurl = datas.get(position).split("_wh_")[0];
            final String transitionName = container.getContext()
                    .getString(R.string.transition_name, adapterPosition, position);
            if (view != null) {
                SubsamplingScaleImageView bigImage = view.findViewById(R.id.bigImage);
                PhotoView imageView = view.findViewById(R.id.image);
//                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setTransitionName(transitionName);
                    if (position == current) {
                        setStartPostTransition(imageView);
                    }
                }

                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    smallImageView.setVisibility(View.GONE);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                bigImage.setVisibility(View.GONE);

                Glide.with(context)
                        .load(imgurl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
//                            .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.drawable.loading_failed)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                if (smallImageView != null) {
                                    smallImageView.setVisibility(View.VISIBLE);
                                }
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                DisplayUtils.ScaleInfo info = DisplayUtils.getInitImageScale(ImagePagerActivity.this, ImageUtils.getCacheFile(imgurl).getPath());
                                if (imgurl.contains(".gif") || imgurl.contains(".GIF") || !info.showRaw){
                                    loading.setVisibility(View.VISIBLE);
                                }else{
                                    loading.setVisibility(View.GONE);
                                }
                                if (smallImageView != null) {
                                    smallImageView.setVisibility(View.GONE);
                                }
                                App.getHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading.setVisibility(View.GONE);
                                        if (imgurl.contains(".gif") || imgurl.contains(".GIF")) {
                                            Glide.clear(imageView);
                                            Glide.with(context)
                                                    .load(imgurl)
                                                    .asGif()
                                                    .crossFade()
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存多个尺寸
                                                    .error(R.drawable.loading_failed)
                                                    .into(imageView);
                                        } else if (!info.showRaw) {
                                            //展示大长图的view
                                            bigImage.setVisibility(View.VISIBLE);
                                            bigImage.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                                            bigImage.setMinScale(0.3F);//最小显示比例
                                            bigImage.setMaxScale( 5.0f);//最大显示比例
// 将图片文件给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
// ImageViewState的三个参数为：scale,center,orientation
                                            bigImage.setImage(ImageSource.uri(Uri.fromFile(ImageUtils.getCacheFile(imgurl))),new ImageViewState(info.scale, new PointF(0, 0), 0));

                                        }
                                        imageView.setTag(transitionName);
                                    }
                                }, 333);


//                                App.getHandler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
////                                            Glide.with(context).load(resource).into(imageView);
//
////                                            Glide.clear(imageView);
//                                            Glide.with(context)
//                                                    .load(imgurl)
//                                                    .crossFade()
//                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存多个尺寸
//                                                    .error(R.drawable.loading_failed)
//                                                    .into(imageView);
//                                            imageView.setTag(transitionName);
//                                        }
//                                    }, 444);
//
//
//                                DisplayUtils.ScaleInfo info = DisplayUtils.getInitImageScale(ImagePagerActivity.this, ImageUtils.getCacheFile(imgurl).getPath());
//                                if (imgurl.contains(".gif") || imgurl.contains(".GIF") || info.showRaw) {
//                                    App.getHandler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Glide.clear(imageView);
//                                            Glide.with(context)
//                                                    .load(imgurl)
//                                                    .asGif()
//                                                    .crossFade()
//                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存多个尺寸
//                                                    .error(R.drawable.loading_failed)
//                                                    .into(imageView);
//                                            imageView.setTag(transitionName);
//                                        }
//                                    }, 444);
//                                }else{
//                                    imageView.setTag(transitionName);
//                                    if(!info.showRaw){
//                                        //展示大长图的view
//                                        bigImage.setVisibility(View.VISIBLE);
//
//                                    }
//                                }
                            }
                        });
                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            PhotoView photoView = (PhotoView) view.findViewById(R.id.image);
//            Glide.clear(photoView);
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }

    @Override
    protected void onDestroy() {
        guideViewList.clear();
        super.onDestroy();
    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    View getItemViewByTag(int tag) {
        View view = viewPager.findViewWithTag(tag);
        return view;
    }

    @Override
    public void finishAfterTransition() {
        int pos = viewPager.getCurrentItem();
        Intent intent = new Intent();
        intent.putExtra("exit_position", pos);
        setResult(RESULT_OK, intent);
        if (current != pos) {
            View view = viewPager.findViewWithTag(
                    getString(R.string.transition_name, adapterPosition, pos));
            setSharedElementCallback(view);
        }
        super.finishAfterTransition();
    }

    //只有在ViewPager左右滑动后才需要在关闭时设置callback
    //view 为转场对象 ImageView
    @TargetApi(21)
    private void setSharedElementCallback(final View view) {
        setEnterSharedElementCallback(new SharedElementCallback() {
            //  @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                names.clear();
                sharedElements.clear();
                if (view != null) {
                    names.add(view.getTransitionName());
                    sharedElements.put(view.getTransitionName(), view);
                }
            }
        });
    }
}
