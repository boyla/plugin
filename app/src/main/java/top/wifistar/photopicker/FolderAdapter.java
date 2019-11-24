package top.wifistar.photopicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.wifistar.R;
import top.wifistar.imagelib.ImageUrl;

/**
 * 文件夹Adapter
 * Created by Nereo on 2015/4/7.
 */
public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Folder> mFolders = new ArrayList<>();
    Folder allFolder;

    int mImageSize;

    int lastSelected = 0;

    public FolderAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
    }

    /**
     * 设置数据集
     *
     * @param folders
     */
    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
        } else {
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size() + 1;
    }

    @Override
    public Folder getItem(int i) {
        if (i == 0) return null;
        return mFolders.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (i == 0) {
                holder.name.setText(mContext.getResources().getString(R.string.all_image));
                holder.size.setText(getTotalImageSize() + "张");
                if (holder.cover.getDrawable() != null) {
                    if (mFolders.size() > 0) {
                        if (allFolder == null) {
                            allFolder = mFolders.get(0);
                        }
                        Glide.with(mContext)
                                .load(allFolder.cover.path.contains("http") ? allFolder.cover.path : new File(allFolder.cover.path))
                                .error(R.mipmap.default_error)
                                .override(mImageSize, mImageSize)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(holder.cover);
                    }
                }
            } else {
                holder.bindData(getItem(i));
            }
            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (Folder f : mFolders) {
                if (f.images != null) {
                    result += f.images.size();
                }
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            cover.setImageResource(R.mipmap.default_error);
            name.setText(data.name);
            size.setText(data.images.size() + "张");
            // 显示图片
            Glide.with(mContext)
                    .load(data.cover.path.contains("http") ? data.cover.path : new File(data.cover.path))
                    .asBitmap()
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .override(mImageSize, mImageSize)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            cover.setImageBitmap(bitmap);
                            // _wh_2210&1242
                            if (data.cover.path.contains("http")) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                ImageUrl.uploadUrl(data.cover.path + "_wh_" + width + "&" + height);
                            }
                        }
                    });
        }
    }

}
