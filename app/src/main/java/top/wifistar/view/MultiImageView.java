package top.wifistar.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import io.realm.RealmResults;
import top.wifistar.R;
import top.wifistar.app.App;
import top.wifistar.bean.bmob.Photo;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.utils.DensityUtil;
import top.wifistar.utils.DisplayUtils;

/**
 * @author shoyu
 * @ClassName MultiImageView.java
 * @Description: 显示1~N张图片的View
 */

public class MultiImageView extends LinearLayout {
    public static int MAX_WIDTH = 0;
    static int maxH = DisplayUtils.getScreenHeight(App.getApp()) / 2;

    // 照片的Url列表
    private List<Photo> imagesList;

    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = DensityUtil.dp2px(getContext(), 3);// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private OnItemClickListener mOnItemClickListener;
    private ColorFilterImageView[] views;

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public int adapterPosition;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<Photo> lists) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;

        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWandH = MAX_WIDTH * 4 / 5;
            initImageLayoutParams();
        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicPara = new LayoutParams(wrap, wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);

        rowPara = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }

        views = new ColorFilterImageView[imagesList.size()];
        if (imagesList.size() == 1) {
            addView(createImageView(0, false));
        } else {
            int allCount = imagesList.size();
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;
            } else {
                MAX_PER_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_PER_ROW_COUNT
                    + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
                        : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    public ColorFilterImageView[] getSharedViews() {
        return views;
    }

    private ImageView createImageView(int position, final boolean isMultiImage) {
        Photo photo = imagesList.get(position);
        ColorFilterImageView imageView = new ColorFilterImageView(getContext());
        String transitionName = getContext().getString(R.string.transition_name, adapterPosition + 1, position);
        ViewCompat.setTransitionName(imageView, transitionName);

        imageView.url = photo.url;
        imageView.adapterPosition = adapterPosition;
        imageView.picPosition = position;
        views[position] = imageView;
        if (isMultiImage) {
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
        } else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            imageView.setMaxHeight(maxH);

            if (photo.w == 0 || photo.h == 0) {
                //get real w and h of pic
                RealmResults<Photo> dbData = BaseRealmDao.realm.where(Photo.class).equalTo("url", photo.url).findAll();
                if (dbData.isEmpty()) {
                    Glide.with(getContext())
                            .load(photo.url)
                            .asBitmap()//强制Glide返回一个Bitmap对象
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                    photo.w = bitmap.getWidth();
                                    photo.h = bitmap.getHeight();
                                    setWh(photo, imageView);
                                    BaseRealmDao.insertOrUpdate(photo);
                                }
                            });
                } else {
                    photo.w = dbData.first().w;
                    photo.h = dbData.first().h;
                    setWh(photo, imageView);
                }
            } else {
                setWh(photo, imageView);
            }
        }

        imageView.setId(photo.url.hashCode());
        imageView.setOnClickListener(new ImageOnClickListener(position));
        imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
        if (photo.url.contains(".gif") || photo.url.contains(".GIF")) {
            Glide.with(getContext()).load(photo.url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        } else {
            Glide.with(getContext()).load(photo.url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }

        return imageView;
    }

    boolean showPart = false;

    private void setWh(Photo photo, ImageView imageView) {
        int expectW = photo.w;
        int expectH = photo.h;
        if (expectW == 0 || expectH == 0) {
            imageView.setLayoutParams(onePicPara);
        } else {
            int actualW = 0;
            int actualH = 0;
            float scale = ((float) expectH) / ((float) expectW);
            if (expectW > pxOneMaxWandH) {
                actualW = pxOneMaxWandH;
                actualH = (int) (actualW * scale);
            } else if (expectW < pxMoreWandH) {
                actualW = pxMoreWandH;
                actualH = (int) (actualW * scale);
            } else {
                actualW = expectW;
                actualH = expectH;
            }

            if (actualH > maxH) {
                int scaleH = actualH / maxH;
                actualH = maxH;
                actualW = actualW / scaleH;
                if (actualW < pxMoreWandH) {
                    actualW = pxMoreWandH;
                    showPart = true;
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                }
            }

            imageView.setLayoutParams(new LayoutParams(actualW, actualH));
        }
    }

    private class ImageOnClickListener implements OnClickListener {

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int picPosition);
    }
}